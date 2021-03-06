/*
 * This file is part of Flow Engine, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Spout LLC <http://www.spout.org/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.engine.geo.region;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;

import com.flowpowered.api.geo.cuboid.Region;
import com.flowpowered.api.io.bytearrayarray.BAAWrapper;
import com.flowpowered.api.io.bytearrayarray.ByteArrayArray;
import com.flowpowered.api.io.regionfile.SimpleRegionFile;
import com.flowpowered.engine.geo.snapshot.FlowChunkSnapshot;
import org.apache.logging.log4j.Logger;

public class RegionFileManager {
    /**
     * The segment size to use for chunk storage. The actual size is 2^(SEGMENT_SIZE)
     */
    private final int SEGMENT_SIZE = 8;
    /**
     * The timeout for the chunk storage in ms. If the store isn't accessed within that time, it can be automatically shutdown
     */
    public static final int TIMEOUT = 30000;
    private final Path regionDirectory;
    private final ConcurrentHashMap<String, BAAWrapper> cache = new ConcurrentHashMap<>();
    private final TimeoutThread timeoutThread;
    private final Logger logger;

    public RegionFileManager(Path worldDirectory, Logger logger) {
        this(worldDirectory, "region", logger);
    }

    public RegionFileManager(Path worldDirectory, String prefix, Logger logger) {
        this.logger = logger;
        regionDirectory = worldDirectory.resolve(prefix);
        try {
            Files.createDirectories(regionDirectory);
        } catch (IOException ex) {
            throw new RuntimeException("Cannot create region directory", ex);
        }
        timeoutThread = new TimeoutThread(worldDirectory);
        timeoutThread.start();
    }

    public BAAWrapper getBAAWrapper(int rx, int ry, int rz) {
        String filename = getFilename(rx, ry, rz);
        BAAWrapper regionFile = cache.get(filename);
        if (regionFile != null) {
            return regionFile;
        }
        final Path file = regionDirectory.resolve(filename);
        BAAWrapper.BAACreator c = () -> new SimpleRegionFile(file, SEGMENT_SIZE, FlowRegion.CHUNKS.VOLUME, TIMEOUT);
        regionFile = new BAAWrapper(c);
        BAAWrapper oldRegionFile = cache.putIfAbsent(filename, regionFile);
        if (oldRegionFile != null) {
            return oldRegionFile;
        }
        return regionFile;
    }

    /**
     * Gets the DataOutputStream corresponding to a given Chunk Snapshot.<br> <br> WARNING: This block will be locked until the stream is closed
     *
     * @param c the chunk snapshot
     * @return the DataOutputStream
     */
    public OutputStream getChunkOutputStream(FlowChunkSnapshot c) {
        int rx = c.getX() >> Region.CHUNKS.BITS;
        int ry = c.getY() >> Region.CHUNKS.BITS;
        int rz = c.getZ() >> Region.CHUNKS.BITS;
        return getBAAWrapper(rx, ry, rz).getBlockOutputStream(FlowRegion.getChunkKey(c.getX(), c.getY(), c.getZ()));
    }

    public void stopTimeoutThread() {
        timeoutThread.interrupt();
    }

    public void closeAll() {
        timeoutThread.interrupt();
        try {
            timeoutThread.join();
        } catch (InterruptedException ie) {
            logger.info("Interrupted when trying to stop RegionFileManager timeout thread");
        }
        cache.values().stream().filter(regionFile -> !regionFile.attemptClose()).forEach(regionFile -> logger.info("Unable to close region file."));
    }

    private static String getFilename(int rx, int ry, int rz) {
        return "reg" + rx + "_" + ry + "_" + rz + ".spr";
    }

    private class TimeoutThread extends Thread {
        public TimeoutThread(Path worldDirectory) {
            super("Region File Manager Timeout Thread - " + worldDirectory.toString());
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                int files = cache.size();
                if (files <= 0) {
                    try {
                        Thread.sleep(TIMEOUT >> 1);
                    } catch (InterruptedException ie) {
                        return;
                    }
                    continue;
                }
                int cnt = 0;
                long start = System.currentTimeMillis();
                for (BAAWrapper regionFile : cache.values()) {
                    regionFile.timeoutCheck();
                    cnt++;
                    long currentTime = System.currentTimeMillis();
                    long expiredTime = currentTime - start;
                    long idealTime = (cnt * ((long) TIMEOUT)) / files / 2;
                    long excessTime = idealTime - expiredTime;
                    if (excessTime > 0) {
                        try {
                            Thread.sleep(excessTime);
                        } catch (InterruptedException ie) {
                            return;
                        }
                    } else if (isInterrupted()) {
                        return;
                    }
                }
            }
        }
    }
}

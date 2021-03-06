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
package com.flowpowered.engine.util.thread.snapshotable;

import java.util.concurrent.atomic.AtomicBoolean;

import com.flowpowered.api.util.thread.annotation.DelayedWrite;
import com.flowpowered.api.util.thread.annotation.LiveRead;
import com.flowpowered.api.util.thread.annotation.SnapshotRead;

/**
 * A snapshotable object that supports primitive booleans
 */
public class SnapshotableBoolean implements Snapshotable {
    private AtomicBoolean next;
    private boolean snapshot;

    public SnapshotableBoolean(SnapshotManager manager, boolean initial) {
        next = new AtomicBoolean(initial);
        snapshot = initial;
        if (manager != null) {
            manager.add(this);
        }
    }

    /**
     * Sets the next value for the Snapshotable
     */
    @DelayedWrite
    public void set(boolean next) {
        this.next.set(next);
    }

    /**
     * Sets the next value but only if the current next value is the given value
     *
     * @return true on success
     */
    public boolean compareAndSet(boolean expect, boolean next) {
        return this.next.compareAndSet(expect, next);
    }

    /**
     * Gets the snapshot value for
     *
     * @return the stable snapshot value
     */
    @SnapshotRead
    public boolean get() {
        return snapshot;
    }

    /**
     * Gets the live value
     *
     * @return the unstable Live "next" value
     */
    @LiveRead
    public boolean getLive() {
        return next.get();
    }

    public boolean isDirty() {
        return snapshot != next.get();
    }

    /**
     * Copies the next value to the snapshot value
     */
    @Override
    public void copySnapshot() {
        snapshot = next.get();
    }
}

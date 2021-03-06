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
package com.flowpowered.engine.entity;

import java.util.UUID;

import com.flowpowered.api.Engine;
import com.flowpowered.api.component.BaseComponentOwner;
import com.flowpowered.api.component.Component;
import com.flowpowered.api.component.entity.EntityObserver;
import com.flowpowered.api.entity.Entity;
import com.flowpowered.api.entity.EntitySnapshot;
import com.flowpowered.api.entity.Physics;
import com.flowpowered.api.geo.LoadOption;
import com.flowpowered.api.geo.cuboid.Chunk;
import com.flowpowered.api.geo.cuboid.Region;
import com.flowpowered.api.geo.discrete.Transform;
import com.flowpowered.api.geo.reference.WorldReference;
import com.flowpowered.engine.geo.chunk.FlowChunk;
import com.flowpowered.engine.geo.region.FlowRegion;
import com.flowpowered.engine.geo.world.FlowWorld;

public class FlowEntity extends BaseComponentOwner implements Entity {
    private final int id;
    private final FlowPhysics physics;

    private final EntityObserver observer;

    protected FlowEntity(Engine engine, int id, Transform transform) {
        super(engine);
        this.id = id;
        this.physics = new FlowPhysics(this);
        this.physics.setTransform(transform);
        this.physics.copySnapshot();
        this.observer = new EntityObserver(this);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public UUID getUID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRemoved() {
        return false;
    }

    @Override
    public void setSavable(boolean savable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSavable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public FlowChunk getChunk() {
        Chunk chunk = physics.getPosition().getChunk(LoadOption.NO_LOAD, getEngine().getWorldManager());
        return (FlowChunk) chunk;
    }

    @Override
    public FlowRegion getRegion() {
        Region region = physics.getPosition().getRegion(LoadOption.LOAD_GEN, getEngine().getWorldManager());
        return (FlowRegion) region;
    }

    @Override
    public EntitySnapshot snapshot() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void tick(float dt) {
        for (Component c : values()) {
            c.tick(dt/1000);
        }
    }

    @Override
    public WorldReference getWorld() {
        return physics.getPosition().getWorld();
    }

    void finalizeRun() {
        FlowWorld worldLive = (FlowWorld) getWorld().get();
        FlowWorld worldSnapshot = (FlowWorld) physics.getSnapshottedTransform().getPosition().getWorld().get();
        //Move entity from World A to World B
        if (worldSnapshot != worldLive) {
            worldSnapshot.getEntityManager().removeEntity(this);
            //Add entity to World B
            worldLive.getEntityManager().addEntity(this);
            physics.crossInto(worldSnapshot, worldLive);
        }

        observer.update();
    }

    void copySnapshot() {
        physics.copySnapshot();
        observer.copySnapshot();
    }

    @Override
    public Physics getPhysics() {
        return physics;
    }

    @Override
    public EntityObserver getObserver() {
        return observer;
    }
}

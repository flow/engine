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
package com.flowpowered.engine.render.model;

import java.util.concurrent.Future;

import com.flowpowered.engine.scheduler.render.RenderThread;

import org.spout.renderer.api.data.VertexData;
import org.spout.renderer.api.gl.VertexArray;
import org.spout.renderer.api.model.Model;

/**
 * In the case that meshing is occurring and that the chunk is not renderable, a previous model can be rendered instead. To use this feature, simply pass it during construction.
 * This previous model will be used until the mesh becomes available. At this point, the previous model will be destroyed, and the new one rendered. When a model isn't needed anymore,
 * you must call {@link ParallelChunkMesher.ChunkModel#destroy()} to dispose of it completely. This will also cancel the meshing if it's in progress, and destroy the previous model.
 */
public class ChunkModel extends Model {
    private final RenderThread renderer;
    private volatile Future<VertexData> mesh;
    private volatile boolean complete = false;
    private volatile boolean destroyed = false;
    private ChunkModel previous;

    public ChunkModel(RenderThread renderer, Future<VertexData> mesh, ChunkModel previous) {
        this.renderer = renderer;
        this.mesh = mesh;
        this.previous = previous;
    }

    @Override
    public synchronized void render() {
        if (destroyed) {
            throw new IllegalStateException("Destroyed but rendering!");
        }
        // If we have not received the mesh and it's done
        if (!complete && mesh.isDone()) {
            // Get the mesh
            final VertexData vertexData;
            try {
                vertexData = mesh.get();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            // If the chunk mesher returned a mesh. It may not return one if the chunk has no mesh (completely invisible)
            if (vertexData != null) {
                // Create the vertex array from the mesh
                final VertexArray vertexArray = renderer.getRenderer().getContext().newVertexArray();
                vertexArray.create();
                vertexArray.setData(vertexData);
                // Set it for rendering
                setVertexArray(vertexArray);
            }
            // Set the model as complete
            complete = true;
            mesh = null;
            // Destroy and discard the previous model (if any), as it is now obsolete
            if (previous != null) {
                previous.destroy();
                previous = null;
            }
        }
        // If we have a vertex array, we can render
        if (complete) {
            // Only render if the model has a vertex array and we're visible
            if (getVertexArray() != null) {
                if (!renderer.isChunkVisible(getPosition())) {
                    return;
                }
                super.render();
            }
        } else if (previous != null) {
            // Else, fall back on the previous model if we have one and we're visible
            previous.render();
        }
    }

    /**
     * Destroys the models, cancelling the meshing task if in progress, and the previous model (if any).
     */
    public synchronized void destroy() {
        destroyed = true;
        complete = false;
        // If we have a vertex array, destroy it
        if (getVertexArray() != null) {
            getVertexArray().destroy();
        }
        // Else, the mesh is still in progress, cancel that
        if (mesh != null) {
            mesh.cancel(false);
            mesh = null;
        }
        // Also destroy and discard the previous model if we have one
        if (previous != null) {
            previous.destroy();
            previous = null;
        }
    }
}

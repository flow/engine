/*
 * This file is part of Spout.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spout is licensed under the Spout License Version 1.
 *
 * Spout is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * In addition, 180 days after any changes are published, you can use the
 * software, incorporating those changes, under the terms of the MIT license,
 * as described in the Spout License Version 1.
 *
 * Spout is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details.
 *
 * You should have received a copy of the GNU Lesser General Public License,
 * the MIT license and the Spout License Version 1 along with this program.
 * If not, see <http://www.gnu.org/licenses/> for the GNU Lesser General Public
 * License and see <http://spout.in/licensev1> for the full license, including
 * the MIT license.
 */
package com.flowpowered.api.player.reposition;

import com.flowpowered.api.geo.cuboid.Chunk;
import com.flowpowered.api.geo.discrete.Point;
import com.flowpowered.api.geo.discrete.Transform;
import com.flowpowered.math.vector.Vector3f;


public abstract class RepositionManagerImpl implements RepositionManager {
	@Override
	public int convertChunkX(int x) {
		return (convertX(x << Chunk.BLOCKS.BITS)) >> Chunk.BLOCKS.BITS;
	}

	@Override
	public int convertChunkY(int y) {
		return (convertY(y << Chunk.BLOCKS.BITS)) >> Chunk.BLOCKS.BITS;
	}

	@Override
	public int convertChunkZ(int z) {
		return (convertZ(z << Chunk.BLOCKS.BITS)) >> Chunk.BLOCKS.BITS;
	}

	@Override
	public int convertX(int x) {
		return (int) convertX((double) x);
	}

	@Override
	public int convertY(int y) {
		return (int) convertY((double) y);
	}

	@Override
	public int convertZ(int z) {
		return (int) convertZ((double) z);
	}

	@Override
	public float convertX(float x) {
		return (float) convertX((double) x);
	}

	@Override
	public float convertY(float y) {
		return (float) convertY((double) y);
	}

	@Override
	public float convertZ(float z) {
		return (float) convertZ((double) z);
	}

	@Override
	public abstract double convertX(double x);

	@Override
	public abstract double convertY(double y);

	@Override
	public abstract double convertZ(double z);

	@Override
	public Transform convert(Transform t) {
		return new Transform(convert(t.getPosition()), t.getRotation(), t.getScale());
	}

	@Override
	public Point convert(Point p) {
		Point newP = new Point(p.getWorld(), convert(p.getVector()));
		return newP;
	}

	@Override
	public Vector3f convert(Vector3f v) {
		float newX = convertX(v.getX());
		float newY = convertY(v.getY());
		float newZ = convertZ(v.getZ());
		Vector3f newP = new Vector3f(newX, newY, newZ);
		return newP;
	}
}

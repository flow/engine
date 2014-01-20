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
package com.flowpowered.api.generator;

import com.flowpowered.api.geo.World;
import com.flowpowered.api.material.BlockMaterial;
import com.flowpowered.api.util.cuboid.CuboidBlockMaterialBuffer;

/**
 * Generates a flat world of a Spout-colored material
 */
public class FlatWorldGenerator implements WorldGenerator {
	private final BlockMaterial material;

	public FlatWorldGenerator() {
		material = BlockMaterial.SOLID_BLUE;
	}

	public FlatWorldGenerator(BlockMaterial material) {
		this.material = material;
	}

	@Override
	public void generate(CuboidBlockMaterialBuffer blockData, World world) {
        int flooredY = blockData.getBase().getFloorY();
		if (flooredY < 0) {
            blockData.setHorizontalLayer(flooredY, (blockData.getSize().getFloorY() / 2), material);
			blockData.flood(material);
		} else {
            blockData.flood(BlockMaterial.AIR);
        }
	}

	@Override
	public Populator[] getPopulators() {
		return new Populator[0];
	}

	@Override
	public String getName() {
		return "FlatWorld";
    }
}
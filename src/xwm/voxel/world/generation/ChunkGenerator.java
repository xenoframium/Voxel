package xwm.voxel.world.generation;

import xwm.voxel.world.BlockCreation;
import xwm.voxel.world.BlocksComponent;
import xwm.voxel.world.PosInChunk;
import xwm.voxel.Main;
import xenoframium.glmath.linearalgebra.Vec3;

public class ChunkGenerator {

	public static void generateChunk(BlocksComponent c) {
		Vec3 cpos = new Vec3(c.pos.x, c.pos.y, c.pos.z).mult(Main.CHUNK_SIZE);
		for (int x = 0; x < Main.CHUNK_SIZE; x++) {
			for (int z = 0; z < Main.CHUNK_SIZE; z++) {
				int height = GenMain.getHeight(c.pos.x*Main.CHUNK_SIZE + x, c.pos.z*Main.CHUNK_SIZE + z);
				for (int y = 0; y < Main.CHUNK_SIZE; y++) {
					if (c.pos.y*Main.CHUNK_SIZE + y > height) {
						c.blocks.setBlock(new PosInChunk(x,y,z), BlockCreation.blockFromString("air"));
					} else {
						if (c.pos.y*Main.CHUNK_SIZE + y == height) {
							String block = "grass_side";
							int biome = GenMain.getBiome(c.pos.x*Main.CHUNK_SIZE + x, c.pos.z*Main.CHUNK_SIZE + z);
							switch(biome){
							case GenMain.JG:
								block = "jungle_placeholder";
								break;
							case GenMain.RF:
								block = "jungle_placeholder";
								break;
							case GenMain.SF:
								block = "forest_placeholder";
								break;
							case GenMain.DF:
								block = "forest_placeholder";
								break;
							case GenMain.GL:
								block = "grass";
								break;
							case GenMain.SL:
								block = "scrub_placeholder";
								break;
							case GenMain.TA:
								block = "taiga_placeholder";
								break;
							case GenMain.TN:
								block = "snow";
								break;
							case GenMain.SN:
								block = "snow";
								break;
							case GenMain.BR:
								block = "stone";
								break;
							case GenMain.DS:
								block = "dry_dirt";
								break;
							case GenMain.SD:
								block = "sand";
								break;
							case GenMain.SEA:
								block = "air";
								break;
							}
							c.blocks.setBlock(new PosInChunk(x,y,z), BlockCreation.blockFromString(block));
						}else if (c.pos.y*Main.CHUNK_SIZE + y == height - 1) {
							c.blocks.setBlock(new PosInChunk(x,y,z), BlockCreation.blockFromString("dirt"));
						}else{
							c.blocks.setBlock(new PosInChunk(x,y,z), BlockCreation.blockFromString("stone"));
						}
					}
				}
			}
		}
	}
}

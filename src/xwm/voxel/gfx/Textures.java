package xwm.voxel.gfx;

import xwm.voxel.world.BlockCreation;
import xwm.voxel.world.Position;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Textures {

	private static Map<String, Position> texturePositions = new HashMap<>();
	private static Map<Short, UVSet> uvs = new HashMap<>();
	private static Set<String> hasTopTexture = new HashSet<>();
	private static Set<String> hasBottomTexture = new HashSet<>();
	private static final int TEXTURES_PER_ROW = 10;

	public static void initTextures() {
		texturePositions.put("stone", new Position(0,0,0));
		texturePositions.put("dirt", new Position(1,0,0));
		texturePositions.put("grass.bottom", new Position(1,0,0));
		texturePositions.put("grass.top", new Position(2,0,0));
		texturePositions.put("other_store", new Position(3,0,0));
		texturePositions.put("wood", new Position(4,0,0));
		texturePositions.put("leaves", new Position(5,0,0));
		texturePositions.put("dry_dirt", new Position(6,0,0));
		texturePositions.put("sand", new Position(7,0,0));
		texturePositions.put("forest_placeholder", new Position(8,0,0));
		texturePositions.put("jungle_placeholder", new Position(9,0,0));
		texturePositions.put("grass", new Position(0,1,0));
		texturePositions.put("snow", new Position(1,1,0));
		texturePositions.put("wood2", new Position(2,1,0));
		texturePositions.put("moss_wood", new Position(3,1,0));
		texturePositions.put("scrub_placeholder", new Position(4,1,0));
		texturePositions.put("taiga_placeholder", new Position(5,1,0));

		hasTopTexture.add("grass");
		hasBottomTexture.add("grass");
	}

	public static UVSet uvsForTexture(short t) {
		if(!uvs.containsKey(t)){
			String s = BlockCreation.blockFromID(t);
			Position texPosS = texturePositions.get(s);
			Position texPosB;
			Position texPosT;
			if(hasTopTexture.contains(s)){
				texPosT = texturePositions.get(s + ".top");
			}else{
				texPosT = texPosS;
			}
			if(hasBottomTexture.contains(s)){
				texPosB = texturePositions.get(s + ".bottom");
			}else{
				texPosB = texPosS;
			}
			uvs.put(t, new UVSet(// maybe there should be a textures per column
					((float)texPosS.x) / TEXTURES_PER_ROW,
					((float)texPosS.y) / TEXTURES_PER_ROW,
					((float)texPosS.x+1) / TEXTURES_PER_ROW,
					((float)texPosS.y+1) / TEXTURES_PER_ROW,
					((float)texPosT.x) / TEXTURES_PER_ROW,
					((float)texPosT.y) / TEXTURES_PER_ROW,
					((float)texPosT.x+1) / TEXTURES_PER_ROW,
					((float)texPosT.y+1) / TEXTURES_PER_ROW,
					((float)texPosB.x) / TEXTURES_PER_ROW,
					((float)texPosB.y) / TEXTURES_PER_ROW,
					((float)texPosB.x+1) / TEXTURES_PER_ROW,
					((float)texPosB.y+1) / TEXTURES_PER_ROW
			));
		}
		return uvs.get(t);
	}
}

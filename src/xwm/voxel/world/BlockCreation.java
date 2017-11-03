package xwm.voxel.world;

import java.util.HashMap;

public class BlockCreation {

	private static HashMap<Short, String> blockStrings = new HashMap<>();
	private static HashMap<String, Short> blockIds = new HashMap<>();

	private static short id = Short.MIN_VALUE;

	public static String blockFromID(short id){
		return blockStrings.get(id);
	}

	public static short blockFromString(String id){
		return blockIds.get(id);
	}

	public static void addBlockType(String str){ //TODO make this less messy
		if(str == "air"){
			blockIds.put(str, (short)0);
			blockStrings.put((short)0, str);
		} else {
			blockIds.put(str, id);
			blockStrings.put(id, str);
		}
		id++;
		if(id == 0){
			id++;
		}
	}
}

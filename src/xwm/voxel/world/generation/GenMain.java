package xwm.voxel.world.generation;

public class GenMain {

	static final int GL = 0;
	static final int SL = 1;
	static final int DF = 2;
	static final int SF = 3;
	static final int RF = 4;
	static final int JG = 5;
	static final int DS = 6;
	static final int SD = 7;
	static final int BR = 8;
	static final int TA = 9;
	static final int TN = 10;
	static final int SN = 11;
	static final int SEA = 12;

	private static SimplexNoise temperatureNoise;
	private static SimplexNoise moistureNoise;
	private static SimplexNoise heightNoise;

	static int[][] biome = {
		//dry				 wet
/*cold*/{BR, BR, TN, SN, SN, SN},
		{DS, SL, SL, SL, TA, TA},
		{DS, GL, GL, DF, DF, RF},
/*hot*/ {SD, GL, SF, SF, JG, JG},
	};

	public static void initGeneration(int seed) {
		temperatureNoise = new SimplexNoise(15000, 0.6, seed);
		moistureNoise = new SimplexNoise(4000, 0.6, seed * 31 + 1);
	    heightNoise = new SimplexNoise(2000, 0.5, seed * 29 - 1);
	}

	public static int getBiome(int x, int z) {
		int temp = doubleToTemp(temperatureNoise.getNoise(x,z) * 10 - (getHeight(x, z) / 30f));
		int moisture = doubleToMoisture(moistureNoise.getNoise(x,z) * 10);
		return biome[temp][moisture];
	}

	public static int getHeight(int x, int z) {
		double h = heightNoise.getNoise(x,z);
		return (int)((Math.log(1 + Math.exp(3*h-1)) - Math.log(1 + Math.exp(-1))) * 100);// Math.pow(h - 0.1, 2)
	}

	static int doubleToMoisture(double d){
		if(d > 4.5){return 5;}
		if(d > 2){return 4;}
		if(d > 0){return 3;}
		if(d > -2){return 2;}
		if(d > -4.5){return 1;}
		return 0;
	}

	static int doubleToTemp(double d){
		if(d > 3.5){return 3;}
		if(d > -0.5){return 2;}
		if(d > -4.5){return 1;}
		return 0;
	}
}

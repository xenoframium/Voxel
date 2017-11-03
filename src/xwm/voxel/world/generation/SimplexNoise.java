package xwm.voxel.world.generation;

import java.util.Random;

public class SimplexNoise {

	private SimplexNoise_octave[] octaves;
	private double[] frequencies;
	private double[] amplitudes;

	private int largestFeature;
	private double persistence;
	private int seed;

	public SimplexNoise(int largestFeature, double persistence, int seed){
		this.largestFeature = largestFeature;
		this.persistence = persistence;
		this.seed = seed;

		int numberOfOctaves = (int)Math.ceil(Math.log10(largestFeature) / Math.log10(2));

		octaves = new SimplexNoise_octave[numberOfOctaves];
		frequencies = new double[numberOfOctaves];
		amplitudes = new double[numberOfOctaves];

		Random rnd = new Random(seed);

		for(int i = 0; i < numberOfOctaves; i++){
			octaves[i] = new SimplexNoise_octave(rnd.nextInt());

			frequencies[i] = Math.pow(2, i);
			amplitudes[i] = Math.pow(persistence, octaves.length - i);
		}
	}


	public double getNoise(int x, int y){
		double result = 0;

		for(int i = 0; i < octaves.length; i++){
			//double frequency = Math.pow(2,i);
			//double amplitude = Math.pow(persistence,octaves.length-i);

			result = result + octaves[i].noise(x/ frequencies[i], y/ frequencies[i]) * amplitudes[i];
		}

		return result;
	}

	public double getNoise(int x,int y, int z){
		double result = 0;

		for(int i = 0; i < octaves.length; i++){
			double frequency = Math.pow(2, i);
			double amplitude = Math.pow(persistence,octaves.length - i);

			result = result+octaves[i].noise(x/frequency, y/frequency,z/frequency) * amplitude;
		}

		return result;
	}
}
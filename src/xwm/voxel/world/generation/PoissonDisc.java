package xwm.voxel.world.generation;

import xwm.voxel.world.Position;

import java.util.List;

import java.lang.Math;

import java.util.*;

public class PoissonDisc {

	public static final int HEIGHT = 160;
	public static final int WIDTH = 160;
	public static final int SAMPLE = 16;

	static boolean world[][] = new boolean[WIDTH][HEIGHT];

	static Random rand = new Random();

	static List<Position> poisson_disk_sampling(int k, int r, int x, int y) {
		List<Position> samples = new ArrayList<>();
		List<Position> active_list = new ArrayList<>();
		active_list.add(new Position(rand.nextInt(SAMPLE) + x, rand.nextInt(SAMPLE) + y, 0));

		int len;
		while ((len = active_list.size()) > 0) {
			int index = rand.nextInt(len);
			Collections.swap(active_list, len-1, index);
			Position sample = active_list.get(len-1);
			boolean found = false;
			for (int i = 0; i < k; i++) {
				// generates a point uniformly at random in the sample's
				// disk situated at a distance from r to 2*r
				float angle = 2*rand.nextFloat()*(float)(Math.PI);
				float radius = rand.nextInt(r) + r;

				Position s = new Position(fastfloor(radius*Math.cos(angle)) + sample.x, fastfloor(radius*Math.sin(angle)) + sample.y, 0);
				if(s.x < 0 || s.y < 0 || s.x >= WIDTH || s.y >= HEIGHT) {
					continue;
				}
				boolean okay = true;
				for (int x1 = s.x-r; x1 <= s.x+r && !okay; x1++) {
					for (int y1 = s.y-r; y1 <= s.y+r; y1++) {
						if(x1 < 0 || y1 < 0 || x1 >= WIDTH || y1 >= HEIGHT){
							continue;
						}
						if (world[x1][y1] && (s.x - x1) * (s.x - x1) + (s.y - y1) * (s.y - y1) <= r * r) {
							okay = false;
							break;
						}
					}
				}
				if (okay) {
					if (x <= s.x && s.x < SAMPLE + x && y <= s.y && s.y < SAMPLE + y) {
						world[s.x][s.y] = true;
						samples.add(s);
						active_list.add(s);
						len++;
						found = true;
					}
				}
			}
			if (!found) {
				active_list.remove(active_list.size() - 1);
			}
		}

		return samples;
	}

	static void print() {
		for(int y = HEIGHT - 1; y >= 0; y--){
			for(int x = 0; x < WIDTH; x++) {
				if(world[x][y]) {
					System.out.print('o');
				}else{
					System.out.print(' ');
				}
			}
			System.out.println();
		}
		System.out.println("------------------------------------------------");
	}

	private static int fastfloor(double x){
		int xi = (int)x;
		return x < xi ? xi-1 : xi;
	}
}

package xwm.voxel.gfx;

public class UVSet {

	public float startx, starty, endx, endy;
	public float startxTop, startyTop, endxTop, endyTop;
	public float startxBot, startyBot, endxBot, endyBot;

	public UVSet(float x1, float y1, float x2, float y2, float x1t, float y1t, float x2t, float y2t, float x1b, float y1b, float x2b, float y2b) {
		startx = x1;
		starty = y1;
		endx = x2;
		endy = y2;
		startxTop = x1t;
		startyTop = y1t;
		endxTop = x2t;
		endyTop = y2t;
		startxBot = x1b;
		startyBot = y1b;
		endxBot = x2b;
		endyBot = y2b;
	}


}

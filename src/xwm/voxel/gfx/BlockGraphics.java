package xwm.voxel.gfx;

public class BlockGraphics { //TODO maybe merge this somewhere, or move stuff from WorldComponent into here

	public static float[] getCubeUVs(short type) {
		UVSet u = Textures.uvsForTexture(type);
		float u00 = u.startx; //TODO remove these variables
		float u01 = u.starty;
		float u10 = u.endx;
		float u11 = u.endy;
		float t00 = u.startxTop;
		float t01 = u.startyTop;
		float t10 = u.endxTop;
		float t11 = u.endyTop;
		float b00 = u.startxBot;
		float b01 = u.startyBot;
		float b10 = u.endxBot;
		float b11 = u.endyBot;
		return new float[]{
				u10,u01, u10,u11, u00,u11, u10,u01, u00,u11, u00,u01,
				u00,u01, u10,u11, u00,u11, u00,u01, u10,u01, u10,u11,
				u00,u01, u10,u11, u00,u11, u00,u01, u10,u01, u10,u11,
				u10,u01, u10,u11, u00,u11, u10,u01, u00,u11, u00,u01,
				t00,t01, t10,t11, t00,t11, t00,t01, t10,t01, t10,t11,
				b00,b01, b00,b11, b10,b11, b00,b01, b10,b11, b10,b01,
		};
	}


}

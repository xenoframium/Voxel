package xwm.voxel.player;

import xwm.voxel.Main;
import xenoframium.ecs.BaseSystem;
import xenoframium.ecs.Entity;
import xenoframium.ecs.EntityManager;
import xenoframium.ecsrender.*;
import xenoframium.ecsrender.system.*;
import xenoframium.ecsrender.universalcomponents.PositionComponent;
import xenoframium.ecsrender.universalcomponents.PositioningComponent;
import xenoframium.ecsrender.universalcomponents.ScaleComponent;
import xenoframium.glmath.linearalgebra.Vec3;
import xenoframium.glmath.linearalgebra.Vec4;
import xwm.voxel.world.ChunkPos;
import xwm.voxel.world.World;
import xwm.voxel.world.WorldComponent;

import java.io.File;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerInputSystem implements BaseSystem {

	//Stop GC
	private static KeyPressCallback keyCB;
	private static CursorMovementCallback mouseCB;

	public static Input inputCallbackRegistry;
	private static Vec3 pos = new Vec3(0, 10, 0);
	private static float mouseX = 0;
	private static float mouseY = 0;
	private static float yaw = 0;
	private static float pitch = 0;

	private static final Vec3 UP_MOVEMENT = new Vec3(0, 0.2f, 0);

	private static boolean w_down = false;
	private static boolean a_down = false;
	private static boolean s_down = false;
	private static boolean d_down = false;
	private static boolean q_down = false;
	private static boolean e_down = false;

	public static double blockModifyTimer = 0;

	//text
	private static FontInfo fi = new FontInfo(new File("/Library/Fonts/Arial.ttf"), 1024, 1024, ' ', '~', 72);
	private static Entity posText;


	private static boolean firstMouseInput = true;

	public static void init() {
		posText = EntityManager.createEntity();
		posText.addComponents(
				new PositioningComponent(),
				new PositionComponent(new Vec3(15f, 11.5f, 0f)),
				new DepthComponent(0),
				new ScaleComponent(new Vec3(0.08f, 0.08f, 0.08f))
		);

		inputCallbackRegistry = new Input(Main.window);
		inputCallbackRegistry.subscribeKeyPressCallback(keyCB = new KeyPressCallback() {
			@Override
			public void invoke(Window window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_W && action == GLFW_PRESS) {
					w_down = true;
				}
				if (key == GLFW_KEY_S && action == GLFW_PRESS) {
					s_down = true;
				}
				if (key == GLFW_KEY_A && action == GLFW_PRESS) {
					a_down = true;
				}
				if (key == GLFW_KEY_D && action == GLFW_PRESS) {
					d_down = true;
				}
				if (key == GLFW_KEY_Q && action == GLFW_PRESS) {
					q_down = true;
				}
				if (key == GLFW_KEY_E && action == GLFW_PRESS) {
					e_down = true;
				}

				//Test block generation
				if (key == GLFW_KEY_T && action == GLFW_PRESS) {
					WorldComponent world = World.getWorld().getComponent(WorldComponent.class);
					Set<ChunkPos> toUnload = world.getLoadedChunks();
//					toUnload.removeAll(chunks);

					for(ChunkPos p : toUnload){
						world.unloadChunk(p);
					}
//					for (int i = 0; i < 5; i++) {
//						for (int j = 0; j < 5; j++) {
//							for (int k = 2; k < 4; k++) {
//								World.setBlock(new Position(i, k, j), BlockCreation.blockFromString("leaves"));
//							}
//						}
//					}
//					for (int i = 1; i < 4; i++) {
//						for (int j = 1; j < 4; j++) {
//							World.setBlock(new Position(i, 4, j), BlockCreation.blockFromString("leaves"));
//						}
//					}
//					World.setBlock(new Position(2, 2, 2), BlockCreation.blockFromString("wood"));
//					World.setBlock(new Position(2, 1, 2), BlockCreation.blockFromString("wood"));
//					World.setBlock(new Position(2, 0, 2), BlockCreation.blockFromString("wood"));
				}

				if (key == GLFW_KEY_W && action == GLFW_RELEASE) {
					w_down = false;
				}
				if (key == GLFW_KEY_S && action == GLFW_RELEASE) {
					s_down = false;
				}
				if (key == GLFW_KEY_A && action == GLFW_RELEASE) {
					a_down = false;
				}
				if (key == GLFW_KEY_D && action == GLFW_RELEASE) {
					d_down = false;
				}
				if (key == GLFW_KEY_Q && action == GLFW_RELEASE) {
					q_down = false;
				}
				if (key == GLFW_KEY_E && action == GLFW_RELEASE) {
					e_down = false;
				}
			}
		});

		inputCallbackRegistry.subscribeCursorMovementCallback(mouseCB = new CursorMovementCallback() {
			@Override
			public void invoke(Window window, float x, float y) {
				float dx = (mouseX - x);
				float dy = (mouseY - y);
				if(firstMouseInput){
					dy = 0;
					dx = 0;
					firstMouseInput = false;
				}
				mouseX = x;
				mouseY = y;

				yaw -= dx/200;
				pitch += dy/200;
				pitch = Math.min(pitch, (float)Math.PI/2.1f);
				pitch = Math.max(pitch, (float)-Math.PI/2.1f);
				updateCameraPosition();
			}
		});
	}

	private static void updateCameraPosition() {
		Main.mainCamera.reposition(pos, facing().add(pos), UP_MOVEMENT);
	}


	public static Vec3 facing() {
		float fx = (float)Math.cos(yaw);
		float fy = (float)Math.tan(pitch);
		float fz = (float)Math.sin(yaw);
		Vec3 v = new Vec3(fx,0,fz);
		v.y = fy;
		return v;
	}

	public static Vec3 horizontalFacing() {
		Vec3 v = facing();
		return new Vec3(v.x, 0, v.z).normalize();
	}

	private static Vec3 horizontalFacingLeft() {
		Vec3 v = facing();
		return new Vec3(v.z, 0, -v.x).normalize();
	}

	public static Vec3 getPlayerPosition() {
		return new Vec3(pos);
	}

	public static void setPlayerPosition(Vec3 newPos) {
		pos = new Vec3(newPos);
	}

	@Override
	public void notifyEntityAddition(Entity entity) {

	}

	@Override
	public void notifyEntityRemoval(Entity entity) {

	}

	@Override
	public void update(double deltaTime, double l1) {
		TextInfo ti = TextMeshGenerator.assemble(fi, pos.toString(), 50, new Vec4(0.9f, 0.9f, 0.9f, 1), new Vec4(0f, 0f, 0f, 0));
		Renderable2D rd2 = new Renderable2D(ti, 0);
		if(posText.hasComponents(Renderable2D.class)){
			posText.removeComponents(Renderable2D.class);
		}
		posText.addComponents(rd2);

		if(blockModifyTimer > 0) {
			blockModifyTimer -= deltaTime;
		}
		Vec3 horizontalMovement = new Vec3(0,0,0);
		if(w_down && !s_down) {
			horizontalMovement.add(horizontalFacing());
		}else if(s_down && !w_down) {
			horizontalMovement.subt(horizontalFacing());
		}
		if(a_down && !d_down) {
			horizontalMovement.add(horizontalFacingLeft());
		}else if(d_down && !a_down) {
			horizontalMovement.subt(horizontalFacingLeft());
		}

		if(w_down || a_down || s_down || d_down){
			pos.add(horizontalMovement.normalize().mult(0.2f));
		}

		if(q_down && !e_down) {
			pos.add(UP_MOVEMENT);
		}else if(e_down && !q_down) {
			pos.subt(UP_MOVEMENT);
		}

		updateCameraPosition();
	}
}

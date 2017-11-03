package xwm.voxel;


import xwm.voxel.gfx.Textures;
import xwm.voxel.player.PlayerInputComponent;
import xwm.voxel.player.PlayerInputSystem;
import xwm.voxel.world.BlockCreation;
import xwm.voxel.world.ChunkLoaderSystem;
import xwm.voxel.world.World;
import xwm.voxel.world.WorldComponent;
import xwm.voxel.world.generation.GenMain;
import xenoframium.ecs.BaseSystem;
import xenoframium.ecs.EntityManager;
import xenoframium.ecsrender.*;
import xenoframium.ecsrender.gl.Camera;
import xenoframium.ecsrender.gl.Projection;
import xenoframium.ecsrender.system.Window;
import xenoframium.ecsrender.universalcomponents.PositioningComponent;
import xenoframium.ecsrender.universalcomponents.PositioningSystem;
import xenoframium.glmath.linearalgebra.Vec3;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Main {

	public static final int CHUNK_SIZE = 32;

	public static EntityManager em = new EntityManager();

	public static Camera mainCamera;
	private static Projection projection;
	public static Window window;

	public static void main(String[] args) {
		window = GraphicsInitialiser.initGlAndContext("Title", 1200, 900);
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL);

		GenMain.initGeneration(new Random().nextInt());

		Textures.initTextures();
		BlockCreation.addBlockType("air");
		BlockCreation.addBlockType("grass");
		BlockCreation.addBlockType("dirt");
		BlockCreation.addBlockType("stone");
		BlockCreation.addBlockType("wood");
		BlockCreation.addBlockType("leaves");
		BlockCreation.addBlockType("sand");
		BlockCreation.addBlockType("dry_dirt");
		BlockCreation.addBlockType("other_stone");
		BlockCreation.addBlockType("grass_side");
		BlockCreation.addBlockType("snow");
		BlockCreation.addBlockType("wood2");
		BlockCreation.addBlockType("moss_wood");

		BlockCreation.addBlockType("forest_placeholder");
		BlockCreation.addBlockType("jungle_placeholder");
		BlockCreation.addBlockType("scrub_placeholder");
		BlockCreation.addBlockType("taiga_placeholder");

		BaseSystem s = new ChunkLoaderSystem();
		em.subscribeSystem(s, WorldComponent.class);

		projection = Projection.createPerspectiveProjection(90f, window.getAspect(), 0.1f, 500f);

		mainCamera = new Camera(new Vec3(0, 1.75f, 0), new Vec3(0, 1.75f, 1));
		RenderSystem3D renderSys = new RenderSystem3D(projection, mainCamera);
		em.subscribeSystem(renderSys, Renderable3D.class);

		RenderSystem2D renderSys2 = new RenderSystem2D(projection, mainCamera);
		em.subscribeSystem(renderSys2, Renderable2D.class);

		PlayerInputSystem inputSystem = new PlayerInputSystem();
		em.subscribeSystem(inputSystem, PlayerInputComponent.class);
		inputSystem.init();

		PositioningSystem psys = new PositioningSystem();
		Main.em.subscribeSystem(psys, PositioningComponent.class);
		PickingSystem pickSys = new PickingSystem(Main.window, PlayerInputSystem.inputCallbackRegistry, 8, projection, mainCamera);
		Main.em.subscribeSystem(pickSys, PickableComponent.class);
		Main.em.addSystemPredecessors(pickSys, psys);

		World.getWorld();

		window.disableCursor();

		glClearColor(164/255f, 234/255f, 238/255f, 1);

		while (!window.shouldClose()) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			em.updateSystems();
			window.swapBuffers();
			glfwPollEvents();
		}
		window.destroy();
		glfwTerminate();
	}
}
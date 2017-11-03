package xwm.voxel.gfx;

import xwm.voxel.player.PlayerInputSystem;
import org.lwjgl.system.MemoryUtil;
import xenoframium.ecs.Entity;
import xenoframium.ecsrender.PickCallback;
import xenoframium.ecsrender.PickableComponent;
import xenoframium.ecsrender.Renderable3D;
import xenoframium.ecsrender.gl.Texture;
import xenoframium.ecsrender.universalcomponents.PositionComponent;
import xenoframium.ecsrender.universalcomponents.PositioningComponent;
import xenoframium.glmath.linearalgebra.Vec3;
import xwm.voxel.world.*;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static xwm.voxel.Main.CHUNK_SIZE;

public class ChunkMesh {

	//adjacent blocks
	private static int[] xOff = {0, 0, 1, -1, 0, 0};
	private static int[] yOff = {0, 0, 0, 0, 1, -1};
	private static int[] zOff = {1, -1, 0, 0, 0, 0};

	private static Texture texture = new Texture(new File("inputs/textures.png"));

	//Stop GC, not sure if this is required
	private static Map<PickableComponent, PickCallback> pickCallbackReferences = new HashMap<>();

	private static FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE * 108);
	private static FloatBuffer uvBuffer = MemoryUtil.memAllocFloat(CHUNK_SIZE*CHUNK_SIZE*CHUNK_SIZE * 108);

	private static void fillMeshBuffers(BlocksComponent c) {
		vertexBuffer.clear();
		uvBuffer.clear();
		int vertexCount = 0;
		for(int x = 0; x < CHUNK_SIZE; x++) {
			for(int y = 0; y < CHUNK_SIZE; y++) {
				for(int z = 0; z < CHUNK_SIZE; z++) {
					PosInChunk p = new PosInChunk(x, y, z);
					short block = c.blocks.getBlock(p);
					if(block != 0) {
						float[] fullUvs = BlockGraphics.getCubeUVs(block);
						for(int face = 0; face < 6; face++){
							if(World.getBlock(new Position(
									x + c.pos.x*CHUNK_SIZE + xOff[face],
									y + c.pos.y*CHUNK_SIZE + yOff[face],
									z + c.pos.z*CHUNK_SIZE + zOff[face]
							)) == 0) {//TODO change to transparency
								for(int i = 0; i < 6; i++) {
									vertexBuffer.put(vertexCount * 3 + 0, cubeVerts[face*18 + i*3 + 0] + x);
									vertexBuffer.put(vertexCount * 3 + 1, cubeVerts[face*18 + i*3 + 1] + y);
									vertexBuffer.put(vertexCount * 3 + 2, cubeVerts[face*18 + i*3 + 2] + z);
									uvBuffer.put(vertexCount * 2 + 0, fullUvs[face*12 + i*2 + 0]);
									uvBuffer.put(vertexCount * 2 + 1, fullUvs[face*12 + i*2 + 1]);
									vertexCount++;
								}
							}
						}
					}
				}
			}
		}
		vertexBuffer.position(vertexCount*3);
		uvBuffer.position(vertexCount*2);
		vertexBuffer.flip();
		uvBuffer.flip();
	}

	public static void makeRenderable(Entity chunk) {
		BlocksComponent c = chunk.getComponent(BlocksComponent.class);
		Position cPos = c.pos.toPosition();
		PositionComponent positionComponent = new PositionComponent(cPos.toVec3());
		PositioningComponent positioningComponent = new PositioningComponent();
		fillMeshBuffers(c);
		Renderable3D tex3DRenderable = new Renderable3D(vertexBuffer, uvBuffer, GL_TRIANGLES, texture, false);
		PickableComponent pickableComponent = chunkPickable(cPos);
		chunk.addComponents(positionComponent, positioningComponent, tex3DRenderable, pickableComponent);
	}

	public static void makeNonrenderable(Entity chunk) {
		try {
			chunk.getComponent(Renderable3D.class).close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		pickCallbackReferences.remove(chunk.getComponent(PickableComponent.class));
		chunk.removeComponents(PositionComponent.class, Renderable3D.class, PositioningComponent.class, PickableComponent.class);
	}

	private static PickableComponent chunkPickable(Position pos) {
		PickCallback callback = new PickCallback() {
			@Override
			public void onPick(Entity e, Vec3 hit, Vec3 cross) {
				if(PlayerInputSystem.blockModifyTimer <= 0){
					if(PlayerInputSystem.inputCallbackRegistry.getMouseButtonStatus(GLFW_MOUSE_BUTTON_LEFT) == GLFW_PRESS){
						hit.add(pos.toVec3());
						hit.add(new Vec3(0.5f, 0.5f, 0.5f));
						hit.subt(new Vec3(cross).div(2));
						World.setBlock(
								new Position(
										(int)Math.floor(hit.x),
										(int)Math.floor(hit.y),
										(int)Math.floor(hit.z)
								),
								BlockCreation.blockFromString("air")
						);
						PlayerInputSystem.blockModifyTimer = 0.2;
					}
					if(PlayerInputSystem.inputCallbackRegistry.getMouseButtonStatus(GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS){
						hit.add(pos.toVec3());
						hit.add(new Vec3(0.5f, 0.5f, 0.5f));
						hit.subt(new Vec3(cross).div(2));
						cross.normalize();
						hit.add(cross);

						World.setBlock(
								new Position(
										(int)Math.floor(hit.x),
										(int)Math.floor(hit.y),
										(int)Math.floor(hit.z)
								),
								BlockCreation.blockFromString("stone")
						);
						PlayerInputSystem.blockModifyTimer = 0.2;
					}
				}
			}
		};
		PickableComponent component = new PickableComponent(vertexBuffer, GL_TRIANGLES, callback);
		pickCallbackReferences.put(component, callback);
		return component;
	}

	private static float cubeVerts[] = {
			0.5f,-0.5f,0.5f,
			0.5f, 0.5f,0.5f,
			-0.5f, 0.5f,0.5f,

			0.5f,-0.5f,0.5f,
			-0.5f, 0.5f,0.5f,
			-0.5f,-0.5f,0.5f,

			0.5f,-0.5f,-0.5f,
			-0.5f, 0.5f,-0.5f,
			0.5f, 0.5f,-0.5f,

			0.5f,-0.5f,-0.5f,
			-0.5f,-0.5f,-0.5f,
			-0.5f, 0.5f,-0.5f,

			0.5f,-0.5f, 0.5f,
			0.5f, 0.5f,-0.5f,
			0.5f, 0.5f, 0.5f,

			0.5f,-0.5f, 0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f, 0.5f,-0.5f,

			-0.5f,-0.5f, 0.5f,
			-0.5f, 0.5f, 0.5f,
			-0.5f, 0.5f,-0.5f,

			-0.5f,-0.5f, 0.5f,
			-0.5f, 0.5f,-0.5f,
			-0.5f,-0.5f,-0.5f,

			0.5f, 0.5f,-0.5f,
			-0.5f,0.5f, 0.5f,
			0.5f, 0.5f, 0.5f,

			0.5f, 0.5f,-0.5f,
			-0.5f,0.5f,-0.5f,
			-0.5f,0.5f, 0.5f,

			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f, 0.5f,
			-0.5f,-0.5f, 0.5f,

			0.5f,-0.5f,-0.5f,
			-0.5f,-0.5f, 0.5f,
			-0.5f,-0.5f,-0.5f
	};
}

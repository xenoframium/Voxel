package xwm.voxel.world.generation;

import xwm.voxel.world.Position2D;
import xenoframium.ecs.Component;

import java.util.HashSet;
import java.util.Set;

public class TreeData implements Component {

	public Set<Position2D> positions = new HashSet<>();


}

package org.maxgamer.rs.model.map.area;

import java.util.HashSet;

import org.maxgamer.rs.model.map.WorldMap;
import org.maxgamer.rs.structure.areagrid.AreaGrid;
import org.maxgamer.rs.structure.areagrid.MBR;

/**
 * @author netherfoam
 */
public class AreaManager {
	private WorldMap map;
	private AreaGrid<Area> areas;
	
	public AreaManager(WorldMap map) {
		this.map = map;
		this.areas = new AreaGrid<Area>(map.getSizeX(), map.getSizeY(), WorldMap.CHUNK_SIZE);
	}
	
	public HashSet<Area> getAreas(MBR overlap) {
		return areas.get(overlap, 16);
	}
	
	public void add(Area a) {
		if (a.getMap() != this.map) throw new IllegalArgumentException("Cannot add an area from one map to an AreaManager of a different map.");
		this.areas.put(a);
	}
	
	public void remove(Area a) {
		this.areas.remove(a);
	}
}
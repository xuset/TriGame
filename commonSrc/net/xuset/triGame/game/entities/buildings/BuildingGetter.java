package net.xuset.triGame.game.entities.buildings;

import java.util.ArrayList;
import java.util.List;

import net.xuset.triGame.game.entities.buildings.types.HeadQuarters;

public class BuildingGetter {
	private List<Building> list = new ArrayList<Building>();
	
	public List<Building> getList() { return list; }
	
	public BuildingGetter() {
		
	}
	
	public Building getByLocation(double x, double y) {
		for (Building b : list) {
			if ((int) b.getX() == (int) x &&
					(int) b.getY() == (int) y)
				return b;
		}
		return null;
	}
	
	public Building getHQ() {
		for (Building b : list) {
			if (b.info == HeadQuarters.INFO)
				return b;
		}
		return null;
	}
}

package org.cart.igd.game;

import org.cart.igd.entity.Entity;
import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;

public class TerrainEntity extends Entity{
	public int terrainId = -1; 

	public TerrainEntity(Vector3f pos, float fD, float bsr, OBJModel model, int terrainId ) {
		super(pos, fD, bsr, model);
		this.terrainId = terrainId;
	}

}

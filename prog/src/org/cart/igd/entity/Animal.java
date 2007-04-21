package org.cart.igd.entity;

import org.cart.igd.math.Vector3f;
import org.cart.igd.models.obj.OBJModel;

public class Animal extends Entity
{
	float walkNoiseRadius;
	float runNoiseRadius;
	float viewDistance;
	float hearDistance;//show pings of movement on "radar"
	
	public Animal(Vector3f pos, float fD, float bsr,OBJModel model){
		super(pos,fD,bsr,model);
	}
}

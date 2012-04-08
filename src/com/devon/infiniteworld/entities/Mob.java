package com.devon.infiniteworld.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

/**
 * Represents entities that move or attack(enemies, birds, players, etc.)
 * @author Devon Guinane
 *
 */
public class Mob extends Entity 
{	
	public Mob(Vector2f position)
	{
		super(position);
	}
	
	public void update(GameContainer gc, int delta)
	{
		
	}
}

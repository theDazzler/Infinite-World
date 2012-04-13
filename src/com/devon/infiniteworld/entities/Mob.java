package com.devon.infiniteworld.entities;

import java.util.Random;

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
	public final Random random = new Random();
	
	public Mob(Vector2f position)
	{
		super(position);
	}
	
	public void update(GameContainer gc, int delta)
	{
		
	}
}

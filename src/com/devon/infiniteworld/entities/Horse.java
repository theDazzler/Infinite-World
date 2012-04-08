package com.devon.infiniteworld.entities;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Horse extends Mob
{	
	Random random = new Random();
	public float speed = 0.02f;
	
	public Horse(Vector2f position)
	{
		super(position);
		
		try 
		{
			this.image = new Image("assets/images/sprites/painted_horse.png");
		} 
		catch (SlickException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void update(GameContainer gc, int delta)
	{
		/*
		float x = this.position.x;
			
		x -= speed * delta;
		this.position.x = x;
		*/
		
	}

	@Override
	public void draw()
	{
		this.image.draw(this.position.x, this.position.y, 0.5f);
	}
}

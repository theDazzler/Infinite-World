package com.devon.infiniteworld.entities;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.GameSettings;

public class Horse extends Mob
{	
	public final Random random = new Random();
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
		
		if(random.nextInt(4) == 0)
		{
			int x = random.nextInt(3) - 1;
			int y = random.nextInt(3) - 1;
			
			move(x, y, delta);
		}
		
		
		
		/*
		float x = this.position.x;
			
		x -= speed * delta;
		this.position.x = x;
		*/
		
	}

	private void move(int x, int y, int delta)
	{
		this.position.x += x * (speed * delta);
		this.position.y += y * (speed * delta);
		
		this.direction.x = x;
		this.direction.y = y;
		
	}

	@Override
	public void draw()
	{
		this.image.draw(this.position.x, this.position.y, 0.5f);
	}
}

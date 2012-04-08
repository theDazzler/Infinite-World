package com.devon.infiniteworld.entities;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;

public class Entity 
{
	public boolean removed;
	public Image image;
	public Vector2f position;
	
	public Entity(Vector2f position)
	{
		this.position = new Vector2f(position.x, position.y);
	}
	
	public void draw(int x, int y)
	{
		this.image.draw(x, y);
	}
	
	public void draw()
	{
		this.image.draw(this.position.x, this.position.y);
	}
	
	public void remove()
	{
		this.removed = true;
	}

	public void update(GameContainer container, int delta) 
	{
		// TODO Auto-generated method stub
		
	}
}

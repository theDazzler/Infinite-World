package com.devon.infiniteworld.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Cave extends WorldObject
{	
	public static final int rarity = 500; //higher the number, the more rare it is(ex. this means cave will appear 1 in 5000)
	
	public Cave(float xPos, float yPos)
	{
		this.id = WorldObject.caveId;
		this.width = 64;
		this.height = 64;
		this.position = new Vector2f(xPos, yPos);
		this.boundingBox = new Rectangle(xPos, yPos, this.width, this.height);
		
		try 
		{
			this.texture = new Image("assets/images/tiles/cave.png");
		} 
		catch (SlickException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}

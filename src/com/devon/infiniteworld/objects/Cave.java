package com.devon.infiniteworld.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

public class Cave extends WorldObject
{	
	public Cave(int id)
	{
		super(id);
		this.rarity = 50;//higher the number, the more rare it is(ex. this means cave will appear 1 in 5000)
		
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

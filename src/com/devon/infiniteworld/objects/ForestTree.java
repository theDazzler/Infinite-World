package com.devon.infiniteworld.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ForestTree extends OneByOneTree
{
	public ForestTree(int id) 
	{
		super(id);
		
		try 
		{
			this.texture = new Image("assets/images/objects/tree.png");
		} 
		catch (SlickException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isCollidable() 
	{
		// TODO Auto-generated method stub
		return true;
	}
	
}

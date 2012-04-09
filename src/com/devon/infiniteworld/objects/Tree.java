package com.devon.infiniteworld.objects;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Tree extends WorldObject 
{
	public Tree(float xPos, float yPos) 
	{
		this.id = WorldObject.treeId;
		this.width = 64;
		this.height = 64;
		this.position = new Vector2f(xPos, yPos);
		this.boundingBox = new Rectangle(xPos, yPos, this.width, this.height);

		try 
		{
			this.texture = new Image("assets/images/tiles/tree.png");
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

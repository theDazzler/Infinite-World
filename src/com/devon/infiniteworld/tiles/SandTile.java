package com.devon.infiniteworld.tiles;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SandTile extends Tile
{
	public SandTile(int id) 
	{
		super(id);
		try {
			this.texture = new Image("assets/images/tiles/sand.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean isCollidable() 
	{
		// TODO Auto-generated method stub
		return false;
	}

}

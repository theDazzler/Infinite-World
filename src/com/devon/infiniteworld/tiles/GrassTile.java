package com.devon.infiniteworld.tiles;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.GameSettings;

public class GrassTile extends VisibleTile 
{
	public GrassTile(Vector2f position) throws SlickException
	{
		super(position, GameSettings.TILE_WIDTH * 2, GameSettings.TILE_HEIGHT * 2, new Image("assets/images/tiles/grass.png"), false);
	}

}

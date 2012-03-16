package com.devon.infiniteworld.tiles;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.GameSettings;

public class DeepWaterTile extends VisibleTile
{
	public DeepWaterTile(Vector2f position) throws SlickException
	{
		super(position, GameSettings.TILE_WIDTH, GameSettings.TILE_HEIGHT, new Image("assets/images/tiles/deep_water.png"), false);
	}
}

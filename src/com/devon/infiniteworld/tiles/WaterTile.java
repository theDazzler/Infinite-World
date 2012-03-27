package com.devon.infiniteworld.tiles;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.GameSettings;

public class WaterTile extends VisibleTile
{
	Rectangle boundingBox;
	
	public WaterTile(Vector2f position, Image image) throws SlickException
	{
		super(position, GameSettings.TILE_WIDTH, GameSettings.TILE_HEIGHT, image, true);
		
		this.boundingBox = new Rectangle(position.x, position.y, GameSettings.TILE_WIDTH, GameSettings.TILE_HEIGHT);
	}
}

package com.devon.infiniteworld.tiles;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Vector2f;


/**
 * Represents a tile that should be rendered
 * @author Devon GUinane
 *
 */
public abstract class VisibleTile extends Tile implements Renderable
{
	public VisibleTile(Vector2f position, int width, int height, Image texture, boolean isCollidable)
	{
		super(position, width, height, texture, isCollidable);
	}
	
	@Override
	public void draw(float x, float y) 
	{
		this.getTexture().draw(x, y);
	}
	
}

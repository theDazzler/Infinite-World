package com.devon.infiniteworld.test;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

public class LightTile extends Rectangle
{

	public Color color;
	public LightTile(float x, float y, float width, float height, Color color)
	{
		super(x, y, width, height);
		this.color = color;
		// TODO Auto-generated constructor stub
	}

}

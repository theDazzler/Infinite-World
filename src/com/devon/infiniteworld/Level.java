package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.List;

public class Level 
{
	public int w, h;
	
	public byte[] tiles;
	public byte[] data;
    public List<NewEntity> entities = new ArrayList<NewEntity>();
	
	public Level(int w, int h)
	{
		this.w = w;
		this.h = h;
		tiles = new byte[w*h];
		data = new byte[w*h];
		
		for(int i = 0; i < w*h; i++)
		{
			tiles[i] = NewTile.grass.id;
		}
	}
	
	public void render()
	{
		//go thru each element in 2d array and call getTile().render() at each location
	}
	
	private NewTile getTile(int x, int y)
	{
		return NewTile.tiles[tiles[x + y * w]];
	}
	
	public void add(NewEntity entity)
	{
		entities.add(entity);
		entity.init(this);
	}
}

package com.devon.infiniteworld;

import com.devon.infiniteworld.GameSettings;

/** 
 * SUper class that all other tiles inherit from
 * @author Devon Guinane
 *
 */
public abstract class GameScreenType
{
	public static GameScreenType[] tiles = new GameScreenType[256]; //holds all GameScreenTypes
	public static GameScreenType forest = new ForestGameScreenType(1);
	public static GameScreenType water = new WaterGameScreenType(2);
	public static GameScreenType snow = new SnowGameScreenType(3);
	public static GameScreenType volcanic = new VolcanicGameScreenType(4);
	public static GameScreenType plain = new PlainGameScreenType(5);
	public static GameScreenType cityRoad = new CityRoadGameScreenType(6);
	public static GameScreenType cityBuilding = new CityBuildingGameScreenType(7);
	public static GameScreenType cityWater = new CityWaterGameScreenType(8);
	public static GameScreenType cityCoast = new CityCoastGameScreenType(9);
	public static GameScreenType dirt = new DirtGameScreenType(10);
	
	public static final int WIDTH = GameSettings.TILE_WIDTH;
	public static final int HEIGHT = GameSettings.TILE_HEIGHT;
	
	public final int id;
	
	public GameScreenType(int id)
	{
		this.id = id;
		//this.texture = texture;
		//this.boundingBox = new Rectangle(position.x, position.y, width, height);
	}

	
	/*
	public float getX()
	{
		return this.position.getX();
	}
	*/

	/*
	public float getY() 
	{
		return this.position.getY();
	}
	*/

	//get width of screen
	public float getWidth()
	{
		return WIDTH;
	}

	//get height of screen
	public float getHeight()
	{
		return HEIGHT;
	}
	
	/*
	public Vector2f getWorldMapPosition()
	{
		return new Vector2f(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
	}
	*/
}

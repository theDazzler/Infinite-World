package com.devon.infiniteworld;

public class GameSettings 
{
	public static int seed = 175459;
	public static final int SCREEN_WIDTH = 1024; //width of screen in pixels 960
	public static final int SCREEN_HEIGHT = 768; //height of screen in pixels 576
	public static final int TILE_WIDTH = 64; //width of tiles in pixels
	public static final int TILE_HEIGHT = 64; //height of tiles in pixels
	public static final int CHUNK_WIDTH = SCREEN_WIDTH / TILE_WIDTH; //number of horizontal tiles 
	public static final int CHUNK_HEIGHT = SCREEN_HEIGHT / TILE_HEIGHT; //number of vertical tiles
	
	public static final int FBM_OCTAVES = 4;
	public static final double FBM_LACUNARITY = 16.0;
	public static final double FBM_H = 2.0;
	
	public static final float WATER_THRESHOLD = -0.3f; //water is placed on tiles with a value <= the threshold
}

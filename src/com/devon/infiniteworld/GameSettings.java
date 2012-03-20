package com.devon.infiniteworld;

public class GameSettings 
{
	public static int seed = 175359;
	public static final int SCREEN_WIDTH = 1920; //width of screen in pixels 960
	public static final int SCREEN_HEIGHT = 1080; //height of screen in pixels 576
	public static final int TILE_WIDTH = 64; //width of tiles in pixels
	public static final int TILE_HEIGHT = 64; //height of tiles in pixels
	public static final int CHUNK_PIXEL_WIDTH = 1024; //number of horizontal tiles 1024 / 64 = 16
	public static final int CHUNK_PIXEL_HEIGHT = 768; //number of vertical tiles 768 / 64 = 12
	public static final int CHUNK_WIDTH = CHUNK_PIXEL_WIDTH / TILE_WIDTH; //number of horizontal tiles 1024 / 64 = 16
	public static final int CHUNK_HEIGHT = CHUNK_PIXEL_HEIGHT / TILE_HEIGHT; //number of vertical tiles 768 / 64 = 12
	
	
	public static final int FBM_OCTAVES = 6;
	public static final double FBM_LACUNARITY = 16.0;
	public static final double FBM_H = 8.0;
	
	public static final float WATER_THRESHOLD = -0.3f; //water is placed on tiles with a value <= the threshold
}

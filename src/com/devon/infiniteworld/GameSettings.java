package com.devon.infiniteworld;

import java.util.Random;

public class GameSettings 
{
	private static Random rand = new Random();
	public static int seed = 434555;
	public static final int SCREEN_WIDTH = 1920; //width of screen in pixels 960
	public static final int SCREEN_HEIGHT = 1080; //height of screen in pixels 576
	public static final int TILE_WIDTH = 64; //width of tiles in pixels
	public static final int TILE_HEIGHT = 64; //height of tiles in pixels
	public static final int CHUNK_PIXEL_WIDTH = 1024; //width of a GameScreenChunk in pixels
	public static final int CHUNK_PIXEL_HEIGHT = 768; //height of a GameScreenChunk in pixels
	public static final int CHUNK_WIDTH = 16; //number of tiles wide a GameScreenChunk is (1024 / 64 = 16)
	public static final int CHUNK_HEIGHT = 12; //number of tiles high a GameScreenChunk is
	public static final int WORLDMAP_CHUNK_WIDTH = 16; //number of tiles wide a WorldMapChunk is (1024 / 64 = 16)
	public static final int WORLDMAP_CHUNK_HEIGHT = 16; //number of tiles high a WorldMapCHunk is
	
	
	public static final int FBM_OCTAVES = 6;
	public static final double FBM_LACUNARITY = 16.0;
	public static final double FBM_H = 8.0;
	
	public static final float WATER_THRESHOLD = -0.2f; //water is placed on tiles with a value <= the threshold
}

package com.devon.infiniteworld;

import java.util.Random;

import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.TileType;

/**
 * Modifies GameScreenChunks
 * ex. Adds water tiles to land chunks so that diagonal oceans can connect to each other
 * @author Devon
 *
 */
public class GameScreenChunkModifier implements Runnable
{
	GameScreenChunk chunk;

	public GameScreenChunkModifier(GameScreenChunk chunk)
	{
		this.chunk = chunk;
	}

	@Override
	public void run() 
	{
		connectDiagonalOceans();
	}

	/**
	 * connects diagonal oceans by placing water tiles on surrounding land tiles
	 */
	private void connectDiagonalOceans() 
	{
	
		//if ocean is to the right of the chunk
		if(ChunkManager.getBiomeValueRightOf(this.chunk) == BiomeType.OCEAN)
		{
			//if ocean is below the chunk
			if(ChunkManager.getBiomeValueBelow(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = GameSettings.CHUNK_HEIGHT - 1;
				int startY = GameSettings.CHUNK_WIDTH - 1;
				
				int stopY = rand.nextInt(14); //2 column minimum
				int stopX = GameSettings.CHUNK_HEIGHT - (GameSettings.CHUNK_WIDTH - stopY); //makes water tiles connect Ocean biomes diagonally rather than squares
				if(stopX < 0)
					stopX = 0;
				
				//spread some water tiles into the land chunks
				for(int i = startX; i >= stopX; i--)
				{
					for(int j = startY; j >= stopY; j--)
					{
						this.chunk.tileLayer[i][j] = TileType.WATER;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY++;
				}
			}
			
			//if ocean is above the chunk
			if(ChunkManager.getBiomeValueAbove(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = 0;
				int startY = GameSettings.CHUNK_WIDTH - 1;
				
				int stopY = rand.nextInt(14); //2 column minimum
				int stopX = (GameSettings.CHUNK_WIDTH - stopY); //makes water tiles connect Ocean biomes diagonally rather than squares
				
				
				//spread some water tiles into the land chunks
				for(int i = startX; i <= stopX; i++)
				{
					for(int j = startY; j >= stopY; j--)
					{
						this.chunk.tileLayer[i][j] = TileType.WATER;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY++;
				}
			}
		}
		
		//if ocean is to the left of the chunk
		if(ChunkManager.getBiomeValueLeftOf(this.chunk) == BiomeType.OCEAN)
		{
			
			//if ocean is below the chunk
			if(ChunkManager.getBiomeValueBelow(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = GameSettings.CHUNK_HEIGHT - 1;
				int startY = 0;
				
				int stopY = rand.nextInt(14) + 2; //2 column minimum
				int stopX = GameSettings.CHUNK_HEIGHT - (GameSettings.CHUNK_WIDTH - stopY); //makes water tiles connect Ocean biomes diagonally rather than squares
				if(stopX < 0)
					stopX = 0;
				
				//spread some water tiles into the land chunks
				for(int i = startX; i >= stopX; i--)
				{
					for(int j = startY; j <= stopY; j++)
					{
						this.chunk.tileLayer[i][j] = TileType.WATER;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY--;
				}
			}
			
			
			//if ocean is above the chunk
			if(ChunkManager.getBiomeValueAbove(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = 0;
				int startY = 0;
				
				int stopY = rand.nextInt(14) + 2; //2 column minimum
				int stopX = stopY; //makes water tiles connect Ocean biomes diagonally rather than squares
				
				
				//spread some water tiles into the land chunks
				for(int i = startX; i <= stopX; i++)
				{
					for(int j = startY; j <= stopY; j++)
					{
						this.chunk.tileLayer[i][j] = TileType.WATER;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY--;
				}
			}
		}

	}

}

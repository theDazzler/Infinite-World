package com.devon.infiniteworld;

import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.Tile;
import com.devon.infiniteworld.tiles.WaterTile;

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
		if(WorldManager.currentEnvironment.chunkManager.getBiomeValueRightOf(this.chunk) == BiomeType.OCEAN)
		{
			//if ocean is below the chunk
			if(WorldManager.currentEnvironment.chunkManager.getBiomeValueBelow(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = GameSettings.CHUNK_HEIGHT - 1;
				int startY = GameSettings.CHUNK_WIDTH - 1;
				
				int stopY = rand.nextInt(GameSettings.CHUNK_WIDTH - 2); //2 column minimum
				int stopX = GameSettings.CHUNK_HEIGHT - (GameSettings.CHUNK_WIDTH - stopY); //makes water tiles connect Ocean biomes diagonally rather than squares
				if(stopX < 0)
					stopX = 0;
				
				//spread some water tiles into the land chunks
				for(int i = startX; i >= stopX; i--)
				{
					for(int j = startY; j >= stopY; j--)
					{
						this.chunk.tileLayer[i][j] = Tile.water.id;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY++;
				}
			}
			
			//if ocean is above the chunk
			if(WorldManager.currentEnvironment.chunkManager.getBiomeValueAbove(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = 0;
				int startY = GameSettings.CHUNK_WIDTH - 1;
				
				int stopY = rand.nextInt(GameSettings.CHUNK_WIDTH - 2); //2 column minimum
				int stopX = (GameSettings.CHUNK_HEIGHT - stopY); //makes water tiles connect Ocean biomes diagonally rather than squares
				
				
				//spread some water tiles into the land chunks
				for(int i = startX; i < stopX; i++)
				{
					for(int j = startY; j >= stopY; j--)
					{
						System.out.println("IJIJIJI: " + i + " " +  j);
						System.out.println("STOPX: " + stopX);
						this.chunk.tileLayer[i][j] = Tile.water.id;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY++;
				}
			}
		}
		
		//if ocean is to the left of the chunk
		if(WorldManager.currentEnvironment.chunkManager.getBiomeValueLeftOf(this.chunk) == BiomeType.OCEAN)
		{
			
			//if ocean is below the chunk
			if(WorldManager.currentEnvironment.chunkManager.getBiomeValueBelow(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = GameSettings.CHUNK_HEIGHT - 1;
				int startY = 0;
				
				int stopY = rand.nextInt(GameSettings.CHUNK_WIDTH - 2) + 2; //2 column minimum
				int stopX = GameSettings.CHUNK_HEIGHT - (GameSettings.CHUNK_WIDTH - stopY); //makes water tiles connect Ocean biomes diagonally rather than squares
				if(stopX < 0)
					stopX = 0;
				
				//spread some water tiles into the land chunks
				for(int i = startX; i >= stopX; i--)
				{
					for(int j = startY; j <= stopY; j++)
					{
						this.chunk.tileLayer[i][j] = Tile.water.id;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY--;
				}
			}
			
			
			//if ocean is above the chunk
			if(WorldManager.currentEnvironment.chunkManager.getBiomeValueAbove(this.chunk) == BiomeType.OCEAN)
			{
				Random rand = new Random((long) (GameSettings.seed + ((this.chunk.getX() + this.chunk.getY()) / 100)));
				int startX = 0;
				int startY = 0;
				
				int stopY = rand.nextInt(GameSettings.CHUNK_WIDTH - 2) + 2; //2 column minimum
				int stopX = stopY; //makes water tiles connect Ocean biomes diagonally rather than squares
				
				if(stopX > GameSettings.CHUNK_HEIGHT)
					stopX = GameSettings.CHUNK_HEIGHT;
				
				
				//spread some water tiles into the land chunks
				for(int i = startX; i < stopX; i++)
				{
					for(int j = startY; j <= stopY; j++)
					{
						this.chunk.tileLayer[i][j] = Tile.water.id;
					}

					//make water tiles connect Ocean biomes diagonally rather than squares
					stopY--;
				}
			}
		}

	}

}

package com.devon.infiniteworld;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.BiomeType;

public class Player
{
	Image image;
	private double speed = 0.02;
	public Rectangle boundingBox;
	private Vector2f worldMapPosition; //position of player on worldMap(15 pixels of horizontal movement in GameScreenChunk = 1 pixel on worldMap, player must move 960 pixels in GameScreenChunk to move 64 pixels on worldMap)
									   //9 pixels of vertical movement in GameScreenChunk = 1 pixel on worldMap
	public Vector2f direction;
	public Vector2f position; //actual position of player inside the world
	public Vector2f currentGameScreenChunkPosition; //top left coordinates of the current GameScreenChunk the player is in
	public Vector2f worldMapChunkPosition; //position of WorldMapChunk player is currently on
	
	public Player(float x, float y, float width, float height) throws SlickException 
	{
		this.boundingBox = new Rectangle(x, y, width, height);
		this.position = new Vector2f(x, y);
		this.worldMapPosition = new Vector2f(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		this.worldMapChunkPosition = this.getWorldMapChunkPosition();

		this.image = new Image("assets/images/sprites/player.png");
		this.direction = new Vector2f(0f, 0f);

	}
	
	//return position coordinates of player on world map(15 pixels of horizontal movement in game = 1 pixel of movement in world map. 9 pixels vertical = 1 pixel in world map)
	public Vector2f getWorldMapPosition()
	{
		return this.worldMapPosition;
	}
	
	//returns top left coordinates of the WorldMapChunk the player is on(each WorldMapChunk is 64x64 on the WorldMap), if player is at 32,32 on WorldMap, then coordinates 0,0 will be returned
	public Vector2f getWorldMapChunkPosition()
	{
		Vector2f coordinates = new Vector2f();
		float x = 0;
		float y = 0;
		
		x = (float) (Math.floor(this.getWorldMapPosition().x / GameSettings.CHUNK_PIXEL_WIDTH) * GameSettings.CHUNK_PIXEL_WIDTH);
		
		y = (float) (Math.floor(this.getWorldMapPosition().y / GameSettings.CHUNK_PIXEL_HEIGHT) * GameSettings.CHUNK_PIXEL_HEIGHT);
		
		coordinates.set(x, y);
		
		return coordinates;
	}
	
	public int getCurrentBiomeType()
	{
		String key = "x" + Integer.toString((int)this.getWorldMapChunkPosition().x) + "y" + Integer.toString((int)this.getWorldMapChunkPosition().y);
		
		int biomeType = WorldMap.map.get(key).biomeTypes[(int)this.getWorldMapTerrainIndices().x][(int)this.getWorldMapTerrainIndices().y];
		
		/**
		String result = "";
		
		switch(biomeType)
		{
			case BiomeType.FOREST:
				result =  "Forest";
				break;
			case BiomeType.OCEAN:
				result = "Ocean";
				break;
			case BiomeType.PLAIN:
				result = "Plain";
				break;
			case BiomeType.SNOW:
				result = "Snow";
				break;
			case BiomeType.VOLCANIC:
				result = "Volcanic";
				break;
		}
		**/
		
		return biomeType;
	}
	
	/**this method returns the array indices of the terrain the player is currently in 
	 * 3x3 WorldMap with 3x3 terrain array(each large square section is a WorldMapChunk containing a terrain array[][])
	 * Map is actually made up of 15x9 terrain arrays rather than 3x3(3x3 is for Demo purposes only)
	 * W = Water
	 * L = Land
	 * * = player
	 * $ = player
	 * [L] and [W] = values from terrain array that are used to create GameScreenChunks filled with either water tiles or land tiles(trees, grass, etc.)
	 * 
	 * In the case(*), it would return (1,1)
	 * In the case($), it would also return (1,1) because each WorldMapChunk has its own array containing terrain data
	 * 
	 * 			    ---------      ---------	  ---------		
	 * 			   |[W][W][W]|    |[W][L][L]|    |[W][W][W]|
	 * 			   |[W][*][L]|	  |[L][$][L]|	 |[W][W][W]|
	 *             |[L][L][L]|	  |[L][L][L]|    |[W][W][W]|
	 *              ---------      ---------      ---------
	 *              
	 * 			    ---------      ---------	  ---------		
	 * 			   |[W][L][W]|    |[W][L][W]|    |[L][W][W]|
	 * 			   |[W][L][L]|	  |[W][L][W]|	 |[W][L][W]|
	 *             |[L][L][L]|	  |[L][W][L]|    |[L][W][W]|
	 *              ---------      ---------      ---------
	 *    
	 * 			    ---------      ---------	  ---------		
	 * 			   |[W][W][W]|    |[W][W][L]|    |[W][W][L]|
	 * 			   |[W][L][L]|	  |[L][W][L]|	 |[W][W][L]|
	 *             |[L][L][W]|	  |[L][W][L]|    |[W][W][L]|
	 *              ---------      ---------      ---------
	 */
	public Vector2f getWorldMapTerrainIndices()
	{
		float x = Math.abs((float)(Math.floor(this.getWorldMapPosition().y / GameSettings.TILE_HEIGHT)));
		float y = Math.abs((float)(Math.floor(this.getWorldMapPosition().x / GameSettings.TILE_WIDTH)));
		
		return new Vector2f(x, y);
	}
	
	//get player's x position
	public float getX()
	{
		return this.position.getX();
	}
	
	//get player's y position
	public float getY()
	{
		return this.position.getY();
	}
	
	//return the top left coordinates of the GameScreenChunk the player is in
	public Vector2f getCurrentGameScreenChunkTopLeftPosition()
	{
		Vector2f coordinates = new Vector2f();
		
		float x = (float) (Math.floor(this.getWorldMapPosition().x / GameSettings.TILE_WIDTH) * GameSettings.CHUNK_PIXEL_WIDTH);
		float y = (float) (Math.floor(this.getWorldMapPosition().y / GameSettings.TILE_WIDTH) * GameSettings.CHUNK_PIXEL_HEIGHT);
		
		coordinates.set(x, y);
		
		return coordinates;
	}
	
	public void update(GameContainer gc, int delta) throws SlickException
	{
		//handle player input
		handleInput(gc, delta);
		
		//manage GameScreenChunks around player
		manageGameScreenChunks();
		
	}	
	
	//manage GameScreenChunks to render surrounding the player
	private void manageGameScreenChunks() 
	{
		//if player moves to the left of the center GameScreenChunk
		if(this.position.x < this.currentGameScreenChunkPosition.x)
		{
			//add column of GameScreenChunks to the left of player's new GameScreenChunk position to get rendered
			ChunkManager.addRenderColumn("left", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}
		
		//if player moves to the right of the center GameScreenChunk(currently the player's position is the top left corner of the bounding box, so when moving right the player's whole body must pass the center chunk for this method to be called)
		if(this.position.x > this.currentGameScreenChunkPosition.x + GameSettings.CHUNK_PIXEL_WIDTH)
		{
			//add column of GameScreenChunks to the left of player's new GameScreenChunk position to get rendered
			ChunkManager.addRenderColumn("right", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}
		
		//if player moves above center GameScreenChunk, add top row to be rendered
		if(this.position.y < this.currentGameScreenChunkPosition.y)
		{
			//add row of GameScreenChunks above the player's new GameScreenChunk position
			ChunkManager.addRenderRow("top", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}
		
		//if player moves below center GameScreenChunk, add new bottom row to be rendered
		if(this.position.y > this.currentGameScreenChunkPosition.y + GameSettings.CHUNK_PIXEL_HEIGHT)
		{
			//add row of GameScreenChunks below the player's new GameScreenChunk position
			ChunkManager.addRenderRow("bottom", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}	
		
		checkWorldChunkUpdate();
		
	}
	
	//handle player's input
	private void handleInput(GameContainer gc, int delta) 
	{
		float x = this.boundingBox.getX();
		float y = this.boundingBox.getY();
		
		Input input = gc.getInput();
		
		if(input.isKeyDown(Input.KEY_ESCAPE))
		{
			gc.exit();
		}
		
		//move left
		if(input.isKeyDown(Input.KEY_A))
		{
			moveLeft(delta);
			
		}
		
		//move right
		if(input.isKeyDown(Input.KEY_D))
		{
			moveRight(delta);
			
		}
		
		//move down
		if(input.isKeyDown(Input.KEY_S))
		{
			moveDown(delta);
			
		}
		
		//move up
		if(input.isKeyDown(Input.KEY_W))
		{
			moveUp(delta);
			
		}
		
		if(!(input.isKeyDown(Input.KEY_A)) && !(input.isKeyDown(Input.KEY_D)))
		{
			this.direction.x = 0f;
		}
		
		if(!(input.isKeyDown(Input.KEY_S)) && !(input.isKeyDown(Input.KEY_W)))
		{
			this.direction.y = 0f;
		}
	}
	
	public void setPosition(Vector2f position)
	{
		this.boundingBox.setX(position.x);
		this.boundingBox.setY(position.y);
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
	}

	public void moveDown(int delta)
	{
		float y = this.boundingBox.getY();
		
		y += speed * delta;
		this.boundingBox.setY(y);
		this.position.y = y;
		this.direction.y = 1.0f;
		//update player's WorldMap position
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		
	}

	public void moveUp(int delta) 
	{
		float y = this.boundingBox.getY();
		
		y -= speed * delta;
		this.boundingBox.setY(y);
		this.position.y = y;
		this.direction.y = -1.0f;			
		//update player's WorldMap position
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		
	}

	public void moveRight(int delta) 
	{
		float x = this.boundingBox.getX();
		
		x += speed * delta;
		this.boundingBox.setX(x);
		this.position.x = x;
		this.direction.x = 1.0f;
		//update player's WorldMap position
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		
	}

	public void moveLeft(int delta) 
	{
		float x = this.boundingBox.getX();
		
		x -= speed * delta;
		this.boundingBox.setX(x);
		this.position.x = x;
		this.direction.x = -1.0f;
		//update player's WorldMap position
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		
	}

	//check to see if more WorldMapChunks need to be generated
	private void checkWorldChunkUpdate()
	{
		//if player has moved above the center WorldMapChunk, generate more WorldMapChunks above the player
		if(this.worldMapPosition.y < this.worldMapChunkPosition.y)
		{
			ChunkManager.addWorldChunkRow("top", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
		
		//if player has moved below the center WorldMapChunk, generate more WorldMapChunks below the player
		if(this.worldMapPosition.y > this.worldMapChunkPosition.y + GameSettings.CHUNK_PIXEL_HEIGHT)
		{
			ChunkManager.addWorldChunkRow("bottom", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
		
		//if player has moved to the right of the center WorldMapChunk, generate more WorldMapChunks to the right of player
		if(this.worldMapPosition.x > this.worldMapChunkPosition.x + GameSettings.CHUNK_PIXEL_WIDTH)
		{
			ChunkManager.addWorldChunkColumn("right", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
		
		//if player has moved to the left of the center WorldMapChunk, generate more WorldMapChunks to the left of player
		if(this.worldMapPosition.x < this.worldMapChunkPosition.x)
		{
			ChunkManager.addWorldChunkColumn("left", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
	}
}

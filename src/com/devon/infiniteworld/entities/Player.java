package com.devon.infiniteworld.entities;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

import com.devon.infiniteworld.Attack;
import com.devon.infiniteworld.BasicSwordSlashAttack;
import com.devon.infiniteworld.ChunkManager;
import com.devon.infiniteworld.CollisionManager;
import com.devon.infiniteworld.Environment;
import com.devon.infiniteworld.GameScreenChunk;
import com.devon.infiniteworld.GameSettings;
import com.devon.infiniteworld.WorldManager;
import com.devon.infiniteworld.WorldMap;
import com.devon.infiniteworld.objects.WorldObject;
import com.devon.infiniteworld.particles.SnowEmitter;
import com.devon.infiniteworld.tiles.BiomeType;
import com.devon.infiniteworld.tiles.Tile;

public class Player extends Mob implements Renderable
{
	Image armImage;
	Image movementCollision;
	
	Animation idleAnimation;
	Animation walkAnimation;
	Animation rollAnimation;
	Animation currentAnimation;
	
	Rectangle arm;
	private double speed = 0.02;
	//public Rectangle boundingBox; //collision box
	private Vector2f worldMapPosition; //position of player on worldMap(15 pixels of horizontal movement in GameScreenChunk = 1 pixel on worldMap, player must move 960 pixels in GameScreenChunk to move 64 pixels on worldMap)
									   //9 pixels of vertical movement in GameScreenChunk = 1 pixel on worldMap
	public Vector2f direction; //direction(velocity) vector ex. 1,1 = moving right and down
	public Vector2f currentGameScreenChunkPosition; //top left coordinates of the current GameScreenChunk the player is in
	public Vector2f worldMapChunkPosition; //position of WorldMapChunk player is currently on
	
	public float width;
	public float height;
	
	private boolean isAttacking = false;
	private Attack currentAttack;
	private boolean attackEnabled = true;
	public boolean environmentChanged = false;
	
	ParticleSystem pSystem;
	SnowEmitter snowEmitter;
	
	public Player(Vector2f position, float width, float height) throws SlickException 
	{
		//this.boundingBox = new Rectangle(x, y, width, height);
		super(position);
		this.width = width;
		this.height = height;
		
		this.walkAnimation = new Animation(new SpriteSheet(new Image("assets/images/sprites/ryu_sheet.png"), 53, 120), 3, 0, 6, 0, false, 3000, false);
		this.walkAnimation.setLooping(true);
		
		this.idleAnimation = new Animation(new SpriteSheet(new Image("assets/images/sprites/ryu_sheet.png"), 53, 120), 3, 0, 6, 0, false, 3000, false);
		this.idleAnimation.setLooping(true);
		
		//set starting animation to idle
		this.currentAnimation = this.idleAnimation;
		
		this.worldMapPosition = new Vector2f(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		this.worldMapChunkPosition = this.getWorldMapChunkPosition();

		this.image = new Image("assets/images/sprites/ryu_stance.png", new Color(34, 177, 76));
		this.movementCollision = new Image("assets/images/tiles/dirt.png");
		this.armImage = new Image("assets/images/sprites/arm.png");
		this.direction = new Vector2f(0f, 0f);
		//Image fireImage = new Image("assets/images/particles/snow.png");
		//fireImage = fireImage.getScaledCopy(0.5f);
		//pSystem = new ParticleSystem(fireImage, 500);
		//snowEmitter = new SnowEmitter();
		//pSystem.addEmitter(snowEmitter);

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
	
	public GameScreenChunk getCurrentGameScreenChunk()
	{
		Vector2f currentGameScreenChunkPos = this.getCurrentGameScreenChunkTopLeftPosition();
		String key = "x" + Integer.toString((int)currentGameScreenChunkPos.x) + "y" + Integer.toString((int)currentGameScreenChunkPos.y);
		return WorldManager.currentEnvironment.chunkManager.visibleChunks.get(key);
	}
	
	public void update(GameContainer gc, int delta)
	{
		//handle player input
		handleInput(gc, delta);
		
		//play idle animation
		if(this.direction.x == 0 && this.direction.y == 0 && !this.isAttacking)
			this.currentAnimation = this.idleAnimation;
		
		//play walking animation
		else if(!this.isAttacking)
		{
			this.currentAnimation = this.walkAnimation;
		}
		checkCollisions(delta);
		updateAnimation(delta);
		
		//manage GameScreenChunks around player
		manageGameScreenChunks();
		
		//this.pSystem.setPosition(this.getX() - (GameSettings.SCREEN_WIDTH / 2), this.getY() - (GameSettings.SCREEN_HEIGHT / 2));
		//snowEmitter.update(pSystem, delta);
		
	}	
	

	private void updateAnimation(int delta) 
	{
		this.currentAnimation.update(delta);
		
		//this.currentAttack.animation.update(delta);
		if(this.isAttacking)
		{
			if(this.currentAnimation.isStopped())
			{
				this.currentAnimation.stop();
				this.isAttacking = false;
				this.attackEnabled = true;
			}
		}
		
		/**
		//if attack is done with its execution frames
		if(this.currentAttack.isExecutionDone())
		{
			//if attack is in "active" phase
			if(this.currentAttack.animation.getFrame() < (this.currentAttack.phase.frameAmount - this.currentAttack.phase.recovery.length))
			{
				//create hit boxes
				this.currentAttack.createHitBoxes();
				this.currentAttack.state = "active";
			}	
			
			else
				this.currentAttack.state = "recovery";
			
			
			if(this.currentAttack.animation.getFrame() == this.currentAttack.animation.getFrameCount() - 1)
			{
				this.currentAttack.animation.stop();
				this.isAttacking = false;
			}
				
		}	
		**/
	}

	private void checkCollisions(int delta)
	{
		//checkObjectCollisions(delta);
		/*
		//check tile collisions
		for (Tile tile : CollisionManager.collidableTiles.values()) 
		{
			if(this.movementBox().intersects(tile.getBoundingBox()))
			{
				if(this.movementBox().intersects(tile.getBoundingBox()))
				{
					float x, y;		
					//System.out.println("COLLIDE");
					
					
					//player moving right
					if(this.movementBox().getX() + this.movementBox().getWidth() > tile.getX() && this.movementBox().getCenterX() < tile.getBoundingBox().getCenterX() && this.direction.x == 1 && this.direction.y == 0)
					{
						
						while(this.movementBox().intersects(tile.getBoundingBox()))
						{
							this.moveLeft(delta);
						}
							
			
					}
					
					//player moving left
					if(this.movementBox().getX() < tile.getX() + tile.getWidth() && this.movementBox().getCenterX() > tile.getBoundingBox().getCenterX() && this.direction.x == -1 && this.direction.y == 0)
					{
						while(this.movementBox().intersects(tile.getBoundingBox()))
						{
							this.moveRight(delta);
						}
					}
					
					//player moving up
					if(this.movementBox().getY() < tile.getY() + tile.getHeight() && this.movementBox().getCenterY() > tile.getBoundingBox().getCenterY() && this.direction.y == -1 && this.direction.x == 0)
					{
						if(this.direction.x != 1)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveDown(delta);
							}
						}
					}
					
					//player moving down
					if(this.movementBox().getY() + this.movementBox().getHeight() > tile.getY() && this.movementBox().getCenterY() < tile.getBoundingBox().getCenterY() && this.direction.y == 1 && this.direction.x == 0)
					{
						while(this.movementBox().intersects(tile.getBoundingBox()))
						{
							this.moveUp(delta);
						}
					}
					
					//if player moving northeast
					if(this.direction.x == 1 && this.direction.y == -1)
					{
						float playerTopRightCornerX = this.movementBox().getX() + this.movementBox().getWidth();
						float objectBottomLeftCornerX = tile.getBoundingBox().getX();
						
						float playerTopRightCornerY = this.movementBox().getY();
						float objectBottomLeftCornerY = tile.getBoundingBox().getY() + tile.getBoundingBox().getHeight();
						
						float xDif = Math.abs(playerTopRightCornerX - objectBottomLeftCornerX);
						float yDif = Math.abs(playerTopRightCornerY - objectBottomLeftCornerY);
						
						//collided with bottom
						if(xDif > yDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveDown(delta);
							}
						}
						
						//collided with left side of object
						else if(yDif > xDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveLeft(delta);
							}
						}
					}
					
					//if player moving northwest
					if(this.direction.x == -1 && this.direction.y == -1)
					{
						float playerTopLeftCornerX = this.movementBox().getX();
						float objectBottomRightCornerX = tile.getBoundingBox().getX() + tile.getBoundingBox().getWidth();
						
						float playerTopLeftCornerY = this.movementBox().getY();
						float objectBottomRightCornerY = tile.getBoundingBox().getY() + tile.getBoundingBox().getHeight();
						
						float xDif = Math.abs(playerTopLeftCornerX - objectBottomRightCornerX);
						float yDif = Math.abs(playerTopLeftCornerY - objectBottomRightCornerY);
						
						//collided with bottom
						if(xDif > yDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveDown(delta);
							}
						}
						
						//collided with right side of object
						else if(yDif > xDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveRight(delta);
							}
						}
					}	
					
					//if player moving southeast
					if(this.direction.x == 1 && this.direction.y == 1)
					{
						float playerBottomRightCornerX = this.movementBox().getX() + this.movementBox().getWidth();
						float objectTopLeftCornerX = tile.getBoundingBox().getX();
						
						float playerBottomRightCornerY = this.movementBox().getY() + this.movementBox().getHeight();
						float objectTopLeftCornerY = tile.getBoundingBox().getY();
						
						float xDif = Math.abs(playerBottomRightCornerX - objectTopLeftCornerX);
						float yDif = Math.abs(playerBottomRightCornerY - objectTopLeftCornerY);
						
						//collided with top of object
						if(xDif > yDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveUp(delta);
							}
						}
						
						//collided with left side of object
						else if(yDif > xDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveLeft(delta);
							}
						}
					}	
					
					//if player moving southwest
					if(this.direction.x == -1 && this.direction.y == 1)
					{
						float playerBottomLeftCornerX = this.movementBox().getX();
						float objectTopRightCornerX = tile.getBoundingBox().getX() + tile.getBoundingBox().getWidth();
						
						float playerBottomLeftCornerY = this.movementBox().getY() + this.movementBox().getHeight();
						float objectTopRightCornerY = tile.getBoundingBox().getY();
						
						float xDif = Math.abs(playerBottomLeftCornerX - objectTopRightCornerX);
						float yDif = Math.abs(playerBottomLeftCornerY - objectTopRightCornerY);
						
						//collided with top of object
						if(xDif > yDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveUp(delta);
							}
						}
						
						//collided with right side of object
						else if(yDif > xDif)
						{
							while(this.movementBox().intersects(tile.getBoundingBox()))
							{
								this.moveRight(delta);
							}
						}
					}
				}
			}
		}
		*/
	}
	

	private void checkObjectCollisions(int delta) 
	{
		ArrayList<WorldObject> collidableObjects = this.getCurrentGameScreenChunk().worldObjects;
		for(int i =0; i < collidableObjects.size(); i++)
		{
			WorldObject obj = collidableObjects.get(i);
			if(this.boundingBox().intersects(obj.boundingBox))
			{
				this.environmentChanged = true;
			}
		}
		
	}

	//manage GameScreenChunks to render surrounding the player
	private void manageGameScreenChunks() 
	{
		//if player moves to the left of the center GameScreenChunk
		if(this.position.x < this.currentGameScreenChunkPosition.x)
		{
			//add column of GameScreenChunks to the left of player's new GameScreenChunk position to get rendered
			WorldManager.currentEnvironment.chunkManager.addRenderColumn("left", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}
		
		//if player moves to the right of the center GameScreenChunk(currently the player's position is the top left corner of the bounding box, so when moving right the player's whole body must pass the center chunk for this method to be called)
		if(this.position.x > this.currentGameScreenChunkPosition.x + GameSettings.CHUNK_PIXEL_WIDTH)
		{
			//add column of GameScreenChunks to the left of player's new GameScreenChunk position to get rendered
			WorldManager.currentEnvironment.chunkManager.addRenderColumn("right", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}
		
		//if player moves above center GameScreenChunk, add top row to be rendered
		if(this.position.y < this.currentGameScreenChunkPosition.y)
		{
			//add row of GameScreenChunks above the player's new GameScreenChunk position
			WorldManager.currentEnvironment.chunkManager.addRenderRow("top", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}
		
		//if player moves below center GameScreenChunk, add new bottom row to be rendered
		if(this.position.y > this.currentGameScreenChunkPosition.y + GameSettings.CHUNK_PIXEL_HEIGHT)
		{
			//add row of GameScreenChunks below the player's new GameScreenChunk position
			WorldManager.currentEnvironment.chunkManager.addRenderRow("bottom", this, this.currentGameScreenChunkPosition);
			
			//update currentGameScreenChunkPosition
			this.currentGameScreenChunkPosition = this.getCurrentGameScreenChunkTopLeftPosition();
		}	
		
		checkWorldChunkUpdate();
		
	}
	
	//handle player's input
	private void handleInput(GameContainer gc, int delta) 
	{
		Input input = gc.getInput();
		
		if(input.isKeyDown(Input.KEY_ESCAPE))
		{
			gc.exit();
		}
				
		//space bar
		if(input.isKeyDown(Input.KEY_SPACE) && this.attackEnabled)
		{
			this.attackEnabled = false;
			//this.setPosition(new Vector2f(this.position.x + 50, this.position.y));
			punch();
		}
		
		//if not punching, set to false
		
		
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

		
		//if not moving left or right, set x direction to 0
		if(!(input.isKeyDown(Input.KEY_A)) && !(input.isKeyDown(Input.KEY_D)))
		{
			this.direction.x = 0f;
		}
		
		//if not moving up or down, set y direction to 0
		if(!(input.isKeyDown(Input.KEY_S)) && !(input.isKeyDown(Input.KEY_W)))
		{
			this.direction.y = 0f;
		}
	}
	
	private void punch()
	{
		
		//arm = new Rectangle(this.getX() + this.width, this.getY() + this.height / 3, 64, 32);
		
		
		this.currentAttack = new BasicSwordSlashAttack(this);
		this.currentAnimation = this.currentAttack.animation;
		this.isAttacking = true;
		//arm.draw(rect.getX(), rect.getY());
	}

	public void setPosition(Vector2f position)
	{
		this.position = position;
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
	}

	public void moveDown(int delta)
	{
		float y = this.getY();
		
		y += speed * delta;
		this.position.y = y;
		this.direction.y = 1.0f;
		//update player's WorldMap position
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		
	}

	public void moveUp(int delta) 
	{
		float y = this.getY();
		
		y -= speed * delta;
		this.position.y = y;
		this.direction.y = -1.0f;			
		//update player's WorldMap position
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		
	}

	public void moveRight(int delta) 
	{
		float x = this.getX();
		
		x += speed * delta;
		this.position.x = x;
		this.direction.x = 1.0f;
		//update player's WorldMap position
		this.worldMapPosition.set(this.getX() / (GameSettings.CHUNK_PIXEL_WIDTH / GameSettings.TILE_WIDTH), this.getY() / (GameSettings.CHUNK_PIXEL_HEIGHT / GameSettings.TILE_HEIGHT));
		
	}

	public void moveLeft(int delta) 
	{
		float x = this.getX();
		
		x -= speed * delta;
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
			WorldManager.currentEnvironment.chunkManager.addWorldChunkRow("top", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
		
		//if player has moved below the center WorldMapChunk, generate more WorldMapChunks below the player
		if(this.worldMapPosition.y > this.worldMapChunkPosition.y + GameSettings.CHUNK_PIXEL_HEIGHT)
		{
			WorldManager.currentEnvironment.chunkManager.addWorldChunkRow("bottom", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
		
		//if player has moved to the right of the center WorldMapChunk, generate more WorldMapChunks to the right of player
		if(this.worldMapPosition.x > this.worldMapChunkPosition.x + GameSettings.CHUNK_PIXEL_WIDTH)
		{
			WorldManager.currentEnvironment.chunkManager.addWorldChunkColumn("right", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
		
		//if player has moved to the left of the center WorldMapChunk, generate more WorldMapChunks to the left of player
		if(this.worldMapPosition.x < this.worldMapChunkPosition.x)
		{
			WorldManager.currentEnvironment.chunkManager.addWorldChunkColumn("left", this, this.getWorldMapChunkPosition());
			
			//update worldMapChunkPosition
			this.worldMapChunkPosition = this.getWorldMapChunkPosition();
		}
	}
	
	//box around player used for fighting collision
	public Rectangle boundingBox()
	{
		return new Rectangle(this.getX(), this.getY(), this.width, this.height);
	}
	
	//collision box at player's feet used for movement collision
	public Rectangle movementBox()
	{
		return new Rectangle(this.getX(), this.getY() + (this.height / 2), this.width, this.height / 2);
	}

	@Override
	public void draw(float x, float y) 
	{
		//draw player
		//this.image.draw(x, y);
		this.currentAnimation.draw(x, y);
		//this.movementCollision.draw(movementBox().getX(), movementBox().getY(), 0.5f);
		
	}
	
	@Override
	public void draw() 
	{
		//draw player
		//this.image.draw(x, y);
		this.currentAnimation.draw(this.getX(), this.getY());
		//this.movementCollision.draw(movementBox().getX(), movementBox().getY(), 0.5f);
		
	}
}

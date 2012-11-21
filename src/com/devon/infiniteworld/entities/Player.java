package com.devon.infiniteworld.entities;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.Chunk;
import com.devon.infiniteworld.Game;
import com.devon.infiniteworld.Level;
import com.devon.infiniteworld.MiniMap;
import com.devon.infiniteworld.tiles.Tile;

public class Player extends Mob
{
	private double speed;
	Image weapon;
	boolean isAttacking = false;
	int attackDelay = 100;
	boolean testing = false;
	
	public Player(Vector2f position, float width, float height, Image texture)
	{
		super(position, width, height, texture);
		this.speed = 0.35;
		try {
			this.weapon = new Image("assets/images/weapons/sword.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	public void update(GameContainer gc, int delta, Level level, MiniMap miniMap) 
	{
		handleInput(gc, delta, level, miniMap);
		checkCollisions(gc, delta, level);
		if(this.attackDelay == 0)
		{
			this.isAttacking = false;
			this.attackDelay = 100;
		}
		if(this.isAttacking)
			this.attackDelay--;
		
		if(this.getX() < level.getX() + 200)
		{
			
			if(!testing)
			{
				level.addChunk(new Chunk((int)level.getX() - Chunk.SIZE * Tile.WIDTH, (int)level.getY(), level.tiles));
				System.out.println(level.getChunks().size());
				this.testing = true;
			}
			
		}
			
	}

	private void checkCollisions(GameContainer gc, int delta, Level level) 
	{
		
			
			
		
	}

	@Override
	public void draw(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(Graphics g) 
	{
		this.texture.draw(this.getX(), this.getY());
		g.drawRect(this.boundingBox().getX(), this.boundingBox().getY(), this.boundingBox().getWidth(), this.boundingBox().getHeight());
		if(this.isAttacking)
			this.weapon.draw(this.getX() - (this.getWidth() / 2) - 20, this.getY() + this.getHeight() / 2);
		
	}
	
	/**
	 * ArrayList containing the coordinates the player can see, which are also the tiles that get rendered
	 * [0] = topLeftX coordinate
	 * [1] = topLeftY coordinate
	 * [2] = bottomRightX coordinate
	 * [3] = bottomRightY coordinate
	 * Tiles inside this rectangle(frustrum) are the ones that get rendered
	 * @return
	 */
	protected ArrayList<Float> getViewFrustram()
	{
		ArrayList<Float> frustram = new ArrayList<Float>();
		
		float topLeftX = this.getX() - (this.getWidth() / 2) - (Game.SCREEN_WIDTH / 2);
		float topLeftY = this.getY() - (this.getHeight() / 2) - (Game.SCREEN_HEIGHT / 2);
		float bottomRightX = this.getX() + (this.getWidth() / 2) + (Game.SCREEN_WIDTH / 2);
		float bottomRightY = this.getY() + (this.getHeight()) + (Game.SCREEN_HEIGHT / 2);
		
		frustram.add(topLeftX);
		frustram.add(topLeftY);
		frustram.add(bottomRightX);
		frustram.add(bottomRightY);
		
		return frustram;
	}

	/**
	 * Checks to see if coordinates are in player's view frustrum
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @return
	 */
	public boolean inFrustram(float x, float y)
	{
		ArrayList<Float> frustram = getViewFrustram();
		if(x >= frustram.get(0) && x <= frustram.get(2) && y >= frustram.get(1) && y <= frustram.get(3))
			return true;
		
		return false;
	}
	
	//handle player's input
	private void handleInput(GameContainer gc, int delta, Level level, MiniMap miniMap) 
	{
		Input input = gc.getInput();
		
		if(input.isKeyDown(Input.KEY_ESCAPE))
		{
			gc.exit();
		}
				
		//space bar
		if(input.isKeyDown(Input.KEY_SPACE))
		{
			if(!this.isAttacking)
				this.isAttacking = true;
		}
		
		//tab to toggle minimap
		if(input.isKeyDown(Input.KEY_TAB))
		{
			if(miniMap.nextDisplay == 0)
			{
				if(miniMap.isVisible)
					miniMap.isVisible = false;
				else
					miniMap.isVisible = true;
				
				miniMap.nextDisplay = 20;
			}
			
		}
		
		//move left
		if(input.isKeyDown(Input.KEY_A))
		{
			float x = this.boundingBox().getX();
			float yTop = this.boundingBox().getY();
			float yBottom = this.boundingBox().getY() + this.boundingBox().getHeight();
			
			x -= 1 * (speed * delta);
						
			int xTileIndexOne = (int) (yTop / Tile.HEIGHT);
			int xTileIndexTwo = (int) (yBottom / Tile.HEIGHT);
			int yTileIndex = (int) (x / Tile.WIDTH);
			
			Tile tileTop = level.getTile(xTileIndexOne, yTileIndex);
			Tile tileBottom = level.getTile(xTileIndexTwo, yTileIndex);
			
			if(!tileTop.isCollidable() && !tileBottom.isCollidable())
				move(-1, 0, delta);
		}
		
		//move right
		if(input.isKeyDown(Input.KEY_D))
		{
			float x = this.boundingBox().getX() + this.boundingBox().getWidth();
			float yTop = this.boundingBox().getY();
			float yBottom = this.boundingBox().getY() + this.boundingBox().getHeight();
			
			x += 1 * (speed * delta);
						
			int xTileIndexOne = (int) (yTop / Tile.HEIGHT);
			int xTileIndexTwo = (int) (yBottom / Tile.HEIGHT);
			int yTileIndex = (int) (x / Tile.WIDTH);
			
			Tile tileTop = level.getTile(xTileIndexOne, yTileIndex);
			Tile tileBottom = level.getTile(xTileIndexTwo, yTileIndex);
			
			if(!tileTop.isCollidable() && !tileBottom.isCollidable())
				move(1, 0, delta);
		}
		
		//move down
		if(input.isKeyDown(Input.KEY_S))
		{
			float y = this.boundingBox().getY() + this.boundingBox().getHeight();
			float xLeft = this.boundingBox().getX();
			float xRight = this.boundingBox().getX() + this.boundingBox().getWidth();
			
			y += 1 * (speed * delta);
						
			int yTileIndexOne = (int) (xLeft / Tile.WIDTH);
			int yTileIndexTwo = (int) (xRight / Tile.WIDTH);
			int xTileIndex = (int) (y / Tile.HEIGHT);
			
			Tile tileLeft = level.getTile(xTileIndex, yTileIndexOne);
			Tile tileRight = level.getTile(xTileIndex, yTileIndexTwo);
			
			if(!tileLeft.isCollidable() && !tileRight.isCollidable())
				move(0, 1, delta);
			
		}
		
		//move up
		if(input.isKeyDown(Input.KEY_W))
		{
			float y = this.boundingBox().getY();
			float xLeft = this.boundingBox().getX();
			float xRight = this.boundingBox().getX() + this.boundingBox().getWidth();
			
			y -= 1 * (speed * delta);
						
			int yTileIndexOne = (int) (xLeft / Tile.WIDTH);
			int yTileIndexTwo = (int) (xRight / Tile.WIDTH);
			int xTileIndex = (int) (y / Tile.HEIGHT);
			
			Tile tileLeft = level.getTile(xTileIndex, yTileIndexOne);
			Tile tileRight = level.getTile(xTileIndex, yTileIndexTwo);
			if(!tileLeft.isCollidable() && !tileRight.isCollidable())
				move(0, -1, delta);
			
		}

		//if not moving left or right, set x velocity to 0
		if(!(input.isKeyDown(Input.KEY_A)) && !(input.isKeyDown(Input.KEY_D)))
		{
			this.velocity.x = 0f;
		}
		
		//if not moving up or down, set y velocity to 0
		if(!(input.isKeyDown(Input.KEY_S)) && !(input.isKeyDown(Input.KEY_W)))
		{
			this.velocity.y = 0f;
		}
	}
	
	//box around Mob used for collision
	public Rectangle boundingBox()
	{
		return new Rectangle(this.getX(), this.getY() + (this.height / 2), this.width, this.height / 2);
	}
	
	public void move(int x, int y, int delta)
	{
		this.position.x += x * (speed * delta);
		this.position.y += y * (speed * delta);
		
		this.velocity.x = x;
		this.velocity.y = y;
	}

	public void findStartPos(Level currentLevel) 
	{
		
		Random rand = new Random();
		
		int xIndex;
		int yIndex;
		
		do
		{
			xIndex = rand.nextInt(currentLevel.getWidth());
			yIndex = rand.nextInt(currentLevel.getHeight());
		}
		
		//don't allow player to spawn in water or on a collidable tile
		while(currentLevel.getTile(xIndex, yIndex) == Tile.water || currentLevel.getTile(xIndex, yIndex).isCollidable());
		
		this.position = new Vector2f(currentLevel.getX() + (yIndex * Tile.WIDTH), currentLevel.getY() + (xIndex * Tile.HEIGHT));
		//this.position = new Vector2f(0, 0);
	}

	

}

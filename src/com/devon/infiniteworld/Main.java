package com.devon.infiniteworld;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.tiles.WaterTile;

public class Main extends BasicGame 
{
	ChunkGenerator chunkGenerator;
	WorldMap worldMap;
	MiniMap miniMap;
	WaterTile waterTile;
	
	Player player;
	
	public Main(String title) 
	{
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{		
		//center player and follow player around
		g.translate((GameSettings.SCREEN_WIDTH / 2) - (player.boundingBox.getX() + (player.boundingBox.getWidth() / 2)), (GameSettings.SCREEN_HEIGHT / 2) - (player.boundingBox.getY() + (player.boundingBox.getHeight() / 2)));

		drawGameScreenChunks();
		//drawMiniMap();
		drawDebugInformation(g);
		//waterTile.draw(400, 300);
		drawPlayer();	
	}

	private void drawMiniMap() 
	{
		miniMap.draw(300f, 50f);
		
	}

	//draw player
	private void drawPlayer()
	{
		//display player
		player.image.draw(player.boundingBox.getX(), player.boundingBox.getY());
		
	}
	
	//draw position information
	private void drawDebugInformation(Graphics g)
	{
		g.drawString("WorldMap X:" + player.getWorldMapPosition().x + "Y:" + player.getWorldMapPosition().y, player.getX() - 380, player.getY() - 342);
		g.drawString("Position X:" + player.getX() + "Y:" + player.getY(), player.getX() - 380, player.getY() - 322);
		g.drawString("Screen X:" + player.getCurrentGameScreenChunkTopLeftPosition().x + "Y:" + player.getCurrentGameScreenChunkTopLeftPosition().y, player.getX() - 380, player.getY() - 302);
		g.drawString("Chunk X:" + player.getWorldMapChunkPosition().x+ "Y:" + player.getWorldMapChunkPosition().y, player.getX() - 380, player.getY() - 282);
		//g.drawString("Biome:" + player.getCurrentBiomeType(), player.getX() - 380, player.getY() - 262);
		
		
	}

	//render GameScreenChunks
	private void drawGameScreenChunks() 
	{
		//render 3x3 GameScreenChunks
		for (GameScreenChunk chunk : ChunkManager.visibleChunks.values()) 
		{
			chunk.draw(chunk.getX(), chunk.getY());
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException 
	{		
		player = new Player(512, 384, 64, 64);
		worldMap = WorldMap.getWorldMap(player);
		//waterTile = new WaterTile(new Vector2f(400f, 300f));
		
		chunkGenerator = new ChunkGenerator(player);
		chunkGenerator.generateGameScreenChunks();
		//miniMap = new MiniMap(300, 50);
		
		//String key = "x" + Integer.toString((int)player.getWorldMapChunkPosition().x) + "y" + Integer.toString((int)player.getWorldMapChunkPosition().y);
		//WorldChunkWriter.writeWorldChunk(WorldMap.map.get(key), key);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException 
	{
		for(int i = delta; i > 0; i--)
		{
			player.update(container, delta);
			//checkCollision(delta);
		}
	}
	
	private void checkCollision(int delta)
	{
		if(player.boundingBox.intersects(waterTile.getBoundingBox()))
		{
			float x, y;		
			
			//player moving right
			if(player.boundingBox.getX() + player.boundingBox.getWidth() > waterTile.getX() && player.boundingBox.getCenterX() < waterTile.getBoundingBox().getCenterX() && player.direction.x > 0)
			{
					while(player.boundingBox.intersects(waterTile.getBoundingBox()))
					{
						player.moveLeft(delta);
					}
	
			}
			
			//player moving left
			if(player.boundingBox.getX() < waterTile.getX() + waterTile.getWidth() && player.boundingBox.getCenterX() > waterTile.getBoundingBox().getCenterX() && player.direction.x < 0)
			{
				while(player.boundingBox.intersects(waterTile.getBoundingBox()))
				{
					player.moveRight(delta);
				}
			}
			
			//player moving up
			if(player.boundingBox.getY() < waterTile.getY() + waterTile.getHeight() && player.boundingBox.getCenterY() > waterTile.getBoundingBox().getCenterY() && player.direction.y < 0)
			{
				if(player.direction.x != 1)
				{
					while(player.boundingBox.intersects(waterTile.getBoundingBox()))
					{
						player.moveDown(delta);
					}
				}
			}
			
			//player moving down
			if(player.boundingBox.getY() + player.boundingBox.getHeight()< waterTile.getY() + waterTile.getHeight() && player.boundingBox.getCenterY() > waterTile.getBoundingBox().getCenterY() && player.direction.y < 0)
			{
				while(player.boundingBox.intersects(waterTile.getBoundingBox()))
				{
					player.moveDown(delta);
				}
			}
		}
		
	}

	public static void main(String[] args)
	{
        try
        {
            AppGameContainer app = new AppGameContainer(new ScalableGame(new Main("Custom"), GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT, true));
            app.setDisplayMode(GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT, false);
            app.setTargetFrameRate(60);
            app.start();
        } 
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }
}

package com.devon.infiniteworld;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.ScalableGame;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

import com.devon.infiniteworld.entities.Player;
import com.devon.infiniteworld.tiles.WaterTile;

public class Main extends BasicGame 
{
	ChunkGenerator chunkGenerator;
	WorldMap worldMap;
	MiniMap miniMap;
	WaterTile waterTile;
	Sound bg;
	Image enemy;
	
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
		g.translate((GameSettings.SCREEN_WIDTH / 2) - (player.boundingBox().getX() + (player.boundingBox().getWidth() / 2)), (GameSettings.SCREEN_HEIGHT / 2) - (player.boundingBox().getY() + (player.boundingBox().getHeight() / 2)));

		drawGameScreenChunks();
		//drawMiniMap();
		drawDebugInformation(g);
		drawPlayer();
		
		//draw entities
		for(int i = 0; i < WorldManager.entities.size(); i++)
		{
			WorldManager.entities.get(i).draw();
		}
		
		//enemy.draw(player.getX() + 100, player.getY());
		
		
		//player.pSystem.render();
		
	}

	private void drawMiniMap() 
	{
		miniMap.draw(1300f, 300f);
		
	}

	//draw player
	private void drawPlayer()
	{
		//display player
		//player.image.draw(player.getX(), player.getY());
		//player.arm.draw(player.getX() + 100, player.getY());
		player.draw(player.getX(), player.getY());
		
	}
	
	//draw position information
	private void drawDebugInformation(Graphics g)
	{
		g.drawString("WorldMap X:" + player.getWorldMapPosition().x + " Y:" + player.getWorldMapPosition().y, player.getX() - 380, player.getY() - 342);
		g.drawString("Position X:" + player.getX() + " Y:" + player.getY(), player.getX() - 380, player.getY() - 322);
		g.drawString("Screen X:" + player.getCurrentGameScreenChunkTopLeftPosition().x + " Y:" + player.getCurrentGameScreenChunkTopLeftPosition().y, player.getX() - 380, player.getY() - 302);
		g.drawString("Chunk X:" + player.getWorldMapChunkPosition().x+ " Y:" + player.getWorldMapChunkPosition().y, player.getX() - 380, player.getY() - 282);
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
		player = new Player(new Vector2f(1300f, 384f), 64, 128);
		worldMap = WorldMap.getWorldMap(player);

		//enemy = new Image("assets/images/sprites/muscle_enemy_normal.png");
		//bg = new Sound("assets/sounds/bg_music/test.ogg");
		
		chunkGenerator = new ChunkGenerator(player);
		chunkGenerator.generateGameScreenChunks();

		
		//bg.play();

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
		}
		
		
		//update entities
		for(int i = 0; i < WorldManager.entities.size(); i++)
		{
			WorldManager.entities.get(i).update(container, delta);
		}
		
		
		//player.pSystem.update(delta);
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

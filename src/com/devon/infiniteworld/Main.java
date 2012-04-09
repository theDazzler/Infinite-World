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
	WorldManager worldManager;
	
	WaterTile waterTile;
	Sound bg;
	
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
		
		this.worldManager.currentEnvironment.render(gc, g);
		
		//player.pSystem.render();
		
	}

	private void drawMiniMap() 
	{
		this.worldManager.currentEnvironment.miniMap.draw(1300f, 300f);
		
	}

	@Override
	public void init(GameContainer container) throws SlickException 
	{		
		player = new Player(new Vector2f(1300f, 384f), 64, 128);
		worldManager = new WorldManager();
		worldManager.setCurrentEnvironment(new NormalOutdoorEnvironment(player));
		

		//bg = new Sound("assets/sounds/bg_music/test.ogg");
				
		//bg.play();

		//miniMap = new MiniMap(300, 50);
		
		//String key = "x" + Integer.toString((int)player.getWorldMapChunkPosition().x) + "y" + Integer.toString((int)player.getWorldMapChunkPosition().y);
		//WorldChunkWriter.writeWorldChunk(WorldMap.map.get(key), key);
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException 
	{
		/*
		if(this.player.environmentChanged)
		{
			
			this.worldManager.setCurrentEnvironment(new CaveEnvironment(player, player.getCurrentGameScreenChunkTopLeftPosition().x, player.getCurrentGameScreenChunk().position.y));
			this.player.environmentChanged = false;
			//ChunkManager.releaseAll();
			
		}
		*/
		this.worldManager.currentEnvironment.update(container, delta);
		
		
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

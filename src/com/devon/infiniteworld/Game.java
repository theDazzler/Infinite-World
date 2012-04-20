package com.devon.infiniteworld;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import com.devon.infiniteworld.entities.Player;

public class Game extends BasicGame
{
	Level currentLevel;
	MiniMap miniMap;
	Player player;
	
	public static final int SCREEN_WIDTH = 1720;
	public static final int SCREEN_HEIGHT = 880;
	

	public Game(String title) 
	{
		super(title);
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		//center player and follow player around
		g.translate((Game.SCREEN_WIDTH / 2) - (player.boundingBox().getX() + (player.boundingBox().getWidth() / 2)), (Game.SCREEN_HEIGHT / 2) - (player.boundingBox().getY() + (player.boundingBox().getHeight() / 2)));
		
		this.currentLevel.draw(this.currentLevel.getX(), this.currentLevel.getY(), player);
		if(this.miniMap.isVisible)
			this.miniMap.draw(this.miniMap.xPos, this.miniMap.yPos, this.currentLevel, this.player);
		this.player.draw(g);

		
	}

	@Override
	public void init(GameContainer gc) throws SlickException 
	{
		this.player = new Player(new Vector2f(0, 0), 64, 128, new Image("assets/images/sprites/player.png", new Color(34, 177, 76)));
		this.currentLevel = new OutdoorLevel(0, 0, 128, 128);
		this.miniMap = new MiniMap(player.getX(), player.getY());
		
		player.findStartPos(this.currentLevel);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException 
	{
		this.player.update(gc, delta, this.currentLevel, this.miniMap);
		this.miniMap.update(delta, player);
	}

}

package com.devon.infiniteworld.test;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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

public class Lighting extends BasicGame 
{
	LightingTile l;
	
	public Lighting(String title) 
	{
		super(title);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{		
		
		l.draw(g);
	}

	@Override
	public void init(GameContainer container) throws SlickException 
	{		
		
		 l = new LightingTile();
	}

	@Override
	public void update(GameContainer container, int delta) throws SlickException 
	{
		l.update(container, delta);
	}
	
	

	public static void main(String[] args)
	{
        try
        {
            AppGameContainer app = new AppGameContainer(new ScalableGame(new Lighting("Lighting"), 1024, 768, true));
            app.setDisplayMode(1024, 768, false);
            app.setTargetFrameRate(60);
            app.start();
        } 
        catch (SlickException e)
        {
            e.printStackTrace();
        }
    }
}

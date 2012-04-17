package com.devon.infiniteworld.test;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class LightingTile
{
	Shape s;
	float speed = 0.2f;
	LightTile[][] tiles = new LightTile[14][14];
	Image lightMap;
	
	public LightingTile()
	{
		this.s = new Rectangle(50, 30, 700, 700);
		try {
			lightMap = new Image("assets/images/alpha_map.png");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initTiles();
	}
	
	private void initTiles()
	{
		int startX = 50;
		int startY = 30;
		int rectSize = 50;
		
		int R = 0;
		int G = 78;
		int B = 0;
		float A = .5f;
		
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				tiles[i][j] = new LightTile(startX + (j * rectSize), startY + (i * rectSize), rectSize, rectSize, new Color(R, G, B, A));
			}
		}
	}
	
	public void draw(Graphics g)
	{
		//g.setDrawMode(Graphics.MODE_ALPHA_MAP);
		//g.setColor(new Color(0, 0, 0));
		
		
		g.setDrawMode(Graphics.MODE_NORMAL);
		
		
		//g.setDrawMode(Graphics.MODE_ALPHA_BLEND);
		
		
		for(int i = 0; i < tiles.length; i++)
		{
			for(int j = 0; j < tiles[i].length; j++)
			{
				LightTile rect = tiles[i][j];
				g.setColor(rect.color);
				g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
			}
		}
		g.setColor(new Color(0, 100, 0));
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		g.setDrawMode(Graphics.MODE_ALPHA_BLEND);

		lightMap.draw(this.s.getX(), this.s.getY(), 4.0f);
		
				
		g.setDrawMode(Graphics.MODE_NORMAL);
		g.setColor(new Color(200, 0, 0));
		g.drawRect(s.getX(), s.getY(), s.getWidth(), s.getHeight());
	}
	
	public void update(GameContainer gc, int delta)
	{
		Input input = gc.getInput();
		
		if(input.isKeyDown(Input.KEY_ESCAPE))
		{
			gc.exit();
		}
				
		//space bar
		if(input.isKeyDown(Input.KEY_SPACE))
		{
			
		}
		
		//move left
		if(input.isKeyDown(Input.KEY_A))
		{
			float x = s.getX();
			x -= speed * delta;
			s.setX(x);
		}
		
		//move right
		if(input.isKeyDown(Input.KEY_D))
		{
			float x = s.getX();
			x += speed * delta;
			s.setX(x);
		}
		
		//move down
		if(input.isKeyDown(Input.KEY_S))
		{
			float y = s.getY();
			y += speed * delta;
			s.setY(y);
			
		}
		
		//move up
		if(input.isKeyDown(Input.KEY_W))
		{
			float y = s.getY();
			y -= speed * delta;
			s.setY(y);
			
		}
		
		//checkCollisions();
	}

	private void checkCollisions()
	{
		Random rand = new Random();
		for(int i = 0; i < this.tiles.length; i++)
		{
			for(int j = 0; j < this.tiles[i].length; j++)
			{
				LightTile tile = tiles[i][j];
				//if alpha map intersects with tiles
				if(this.s.intersects(tile))
				{
					float alphaMapCenterX = this.s.getCenterX();
					float alphaMapCenterY = this.s.getCenterY();
					
					float xDistanceToTile = Math.abs(alphaMapCenterX - tile.getX());
					float yDistanceToTile = Math.abs(alphaMapCenterY - tile.getY());
					
					float distance = (float) Math.sqrt((xDistanceToTile * xDistanceToTile) + (yDistanceToTile * yDistanceToTile));
					
					distance /= 10;
					System.out.println(distance);
					
					if(distance < 20)
					{
						tile.color.g = 33;
					}	
				}
				
				else
				{
					tile.color.g = 50;
				}
				
			}
		}
		
	}
}

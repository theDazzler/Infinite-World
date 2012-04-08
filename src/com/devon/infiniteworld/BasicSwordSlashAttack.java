package com.devon.infiniteworld;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

import com.devon.infiniteworld.entities.Player;

public class BasicSwordSlashAttack extends Attack 
{
	Player player;

	public BasicSwordSlashAttack(Player player)
	{
		this.player = player;
		this.damage = 1.0f;
		this.hitBoxes = new Rectangle[1];
		createHitBoxes();
		this.phase = new AttackPhase(11, 3, 12);
		
		try {
			//Image [] one = {new Image("assets/images/sprites/ryu_03.png", new Color(34, 177, 76)), new Image("assets/images/sprites/ryu_04.png", new Color(34, 177, 76)), new Image("assets/images/sprites/ryu_05.png", new Color(34, 177, 76))};

			//int [] duration = {2000, 2000, 2000};

			SpriteSheet sheet = new SpriteSheet(new Image("assets/images/sprites/ryu_sheet.png"), 62, 120);
			
			this.animation = new Animation(sheet, 7, 1, 9, 1, false, 2000, false);
			this.animation.setLooping(false);
			/*
			int x = 1;
			int y = 7;
			for(int i = 0; i < 3; i++)
			{
				
				this.animation.addFrame(sheet.getSprite(y, x), 2000);
				y++;
				
			}
			*/
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void createHitBoxes() 
	{
		this.hitBoxes[0] = new Rectangle(this.player.getX() + this.player.width, this.player.getY() + this.player.height / 3, 64, 32);
		
	}
		
	
}

package com.devon.infiniteworld.particles;

import java.util.Random;



import org.newdawn.slick.Image;
import org.newdawn.slick.particles.Particle;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;

import com.devon.infiniteworld.GameSettings;

public class SnowEmitter implements ParticleEmitter
{
	private float size = 8.5f; //base size for the snow
	private float maxYVelocity = 17; //max Y speed (fall speed)
	private float minYVelocity = 7; //min Y speed (fall speed)
	private int particleLifetime = 12000; //lifetime of a particle in milliseconds
	private int timer;
	private int interval = 3000; //time between emissions in milliseconds
	
	Random rand = new Random();
	
	public SnowEmitter()
	{
		
	}
	
	@Override
	public boolean completed() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Image getImage()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isOriented() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetState()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setEnabled(boolean arg0) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(ParticleSystem system, int delta) 
	{
		
			timer -= delta;
	        if (timer <= 0) 
	        {
	            timer = interval;
	            
				for(int i = 0; i < 5; i++)
				{
					double a = Math.PI * 2 * Math.random();
		            float parX = (float) (1.5 * Math.cos(a));
		            //float parY = (float) (3 * a);
		            Particle p = system.getNewParticle(this, 1000);
		            p.setColor(1, 1, 1, 0.5f);
		            p.setPosition(rand.nextInt(GameSettings.SCREEN_WIDTH), 0);
		            p.setLife(particleLifetime);
		            p.setSize(size);
		            p.setVelocity(parX, rand.nextInt((int)(this.maxYVelocity - this.minYVelocity)) + this.minYVelocity, 0.005f);
				}
	        }
		
	}

	@Override
	public void updateParticle(Particle particle, int delta) 
	{
		
		if (particle.getLife() < 3000) 
		{
            
            float c = 0.002f * delta;
            particle.adjustColor(c, c, c, -c / 8);
            
            if(particle.getLife() < 1000)
            {
            	particle.adjustSize(-0.01f * delta);
            }
        } 

		/**
		else 
		{
            particle.adjustSize(-0.04f * delta * (size / 40.0f));
        }
        **/
		
        
		
	}

	@Override
	public boolean useAdditive() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean usePoints(ParticleSystem arg0) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void wrapUp()
	{
		// TODO Auto-generated method stub
		
	}

}

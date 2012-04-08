package com.devon.infiniteworld;

import org.newdawn.slick.Animation;
import org.newdawn.slick.geom.Rectangle;

/**
 * Represents an attack
 * @author Devon Guinane
 *
 */
public abstract class Attack 
{
	float damage;
	public Animation animation; //animation played during attack
	AttackPhase phase; //attack frame data (execution, active, recovery)
	Rectangle[] hitBoxes; //holds hitboxes for each attack
	String state; //"execution", "active", or "recovery"
	
	public abstract void createHitBoxes();
	
	/**
	 * determines if execution frames are finished
	 * @return
	 */
	public boolean isExecutionDone()
	{
		if(this.animation.getFrame() > this.phase.execution.length - 1)
			return true;
		
		return false;
	}
	
	public String getState()
	{
		return this.state;
	}
	
	
}

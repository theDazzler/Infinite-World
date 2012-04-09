package com.devon.infiniteworld;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;

import com.devon.infiniteworld.entities.Entity;
import com.devon.infiniteworld.entities.Player;

public class WorldManager
{
	
	public static Environment currentEnvironment;
	public Player player;
	
	 
	public WorldManager()
	{	
		
	}
	 
	public static void setCurrentEnvironment(Environment environment)
	{
		currentEnvironment = environment;
		
	}
}

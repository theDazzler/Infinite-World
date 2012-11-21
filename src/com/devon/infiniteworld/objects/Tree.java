package com.devon.infiniteworld.objects;


public abstract class Tree extends WorldObject
{
	public static Tree basicTree = new ForestTree(128);
	
	public Tree(int id)
	{
		super(id);
	}

}

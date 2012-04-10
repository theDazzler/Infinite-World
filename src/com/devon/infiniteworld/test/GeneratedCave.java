package com.devon.infiniteworld.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import javax.swing.JPanel;

public class GeneratedCave extends JPanel
{

	public static final int MAX_WIDTH = 1000;
	public static final int MAX_HEIGHT = 700;
	public static final int TILE_WIDTH = 8;
	public static final int TILE_HEIGHT= 8;

	
	public static final int FLOOR = 0;
	public static final int WALL = 1;
	
	private int[][] map;
	private int numCols;
	private int numRows;
	
	
	
	
	private static final long serialVersionUID = 8111554517572474109L;
	
	public GeneratedCave()
	{
		this.numCols = MAX_HEIGHT / TILE_HEIGHT;
		this.numRows = MAX_WIDTH / TILE_WIDTH;
		this.map = new int[this.numRows][this.numCols];
		this.createCave();
		this.generateWalls();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{
				if(this.map[i][j] == FLOOR)
				{
					g.setColor(Color.GRAY);
					g.fillRect(i * TILE_WIDTH, j * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
					
				}
				else
				{
					g.setColor(Color.BLACK);
					g.fillRect(i * TILE_WIDTH, j * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
				}
				//g.fillRect(10, 10, i * 10, j * 10);
			}
		}
	}
	
	public void createCave()
	{
		Random rand = new Random();
		
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{
				//40% floor fill rate to begin with
				if(rand.nextInt(10) < 4)
				{
					this.map[i][j] = WALL;
				}
				else
				{
					this.map[i][j] = FLOOR;
				}
			}
		}	
		
		
	}

	private int[][] populateNextGrid(int r1Cutoff, int r2Cutoff) 
	{
		int[][] tempGrid = new int[this.numRows][this.numCols];
		
		for(int i = 0; i < this.numRows; i++)
		{
			for(int j = 0; j < this.numCols; j++)
			{
				int numCells = checkSurroundingsOneStep(i, j);
				
				if(numCells >= r1Cutoff)
				{
					tempGrid[i][j] = WALL;
				}
				
				int numCellsTwo = checkSurroundingsTwoSteps(i, j);
				
				if(numCellsTwo <= r2Cutoff)
				{
					tempGrid[i][j] = WALL;
				}	
			}
		}
		
		return tempGrid;
	}

	private void generateWalls() 
	{
		int[][] nextGrid;
		int oneR1 = 7;
		int oneR2 = 4;
		int twoR1 = 6;
		int twoR2 = -1;
		
		for(int i = 0; i < 4; i++)
		{
			//6 3
			nextGrid = populateNextGrid(oneR1, oneR2);
			this.map = nextGrid;
		}
		
		for(int i = 0; i < 3; i++)
		{
			// 6 -1
			nextGrid = populateNextGrid(twoR1, twoR2);
			this.map = nextGrid;
		}
	}
		

	//counts number of surrounding walls in neighboring cells
	private int checkSurroundingsOneStep(int xIndex, int yIndex) 
	{
		int count = 0;
		count += checkNorth(xIndex, yIndex);
		count += checkNorthEast(xIndex, yIndex);
		count += checkEast(xIndex, yIndex);
		count += checkSouthEast(xIndex, yIndex);
		count += checkSouth(xIndex, yIndex);
		count += checkSouthWest(xIndex, yIndex);
		count += checkWest(xIndex, yIndex);
		count += checkNorthWest(xIndex, yIndex);
		count += this.map[xIndex][yIndex];
		
		return count;
	}
	
	//counts number of surrounding walls two steps away
	private int checkSurroundingsTwoSteps(int xIndex, int yIndex) 
	{
		int count = 0;
		count += checkNorthTwo(xIndex, yIndex);
		count += checkNorthEastTwo(xIndex, yIndex);
		count += checkEastTwo(xIndex, yIndex);
		count += checkSouthEastTwo(xIndex, yIndex);
		count += checkSouthTwo(xIndex, yIndex);
		count += checkSouthWestTwo(xIndex, yIndex);
		count += checkWestTwo(xIndex, yIndex);
		count += checkNorthWestTwo(xIndex, yIndex);
		
		return count;
	}

	private int checkNorthWestTwo(int xIndex, int yIndex)
	{
		if(xIndex <= 1 || yIndex <= 1)
			return 0;
		return this.map[xIndex - 2][yIndex - 2];
	}

	private int checkWestTwo(int xIndex, int yIndex) 
	{
		if(yIndex <= 1)
			return 0;
		return this.map[xIndex][yIndex - 2];
	}

	private int checkSouthWestTwo(int xIndex, int yIndex) 
	{
		if(xIndex >= this.numRows - 2 || yIndex <= 1)
			return 0;
		return this.map[xIndex + 2][yIndex - 2];
	}

	private int checkSouthTwo(int xIndex, int yIndex) 
	{
		if(xIndex >= this.numRows - 2)
			return 0;
		return this.map[xIndex + 2][yIndex];
	}

	private int checkSouthEastTwo(int xIndex, int yIndex) 
	{
		if(xIndex >= this.numRows - 2 || yIndex >= this.numCols - 2)
			return 0;
		return this.map[xIndex + 2][yIndex + 2];
	}

	private int checkEastTwo(int xIndex, int yIndex) 
	{
		if(yIndex >= this.numCols - 2)
			return 0;
		return this.map[xIndex][yIndex + 2];
	}

	private int checkNorthEastTwo(int xIndex, int yIndex)
	{
		if(xIndex <= 1 || yIndex >= this.numCols - 2)
			return 0;
		return this.map[xIndex - 2][yIndex + 2];
	}

	private int checkNorthTwo(int xIndex, int yIndex) 
	{
		if(xIndex <= 1)
			return 0;
		return this.map[xIndex - 2][yIndex];
	}

	private int checkNorthWest(int xIndex, int yIndex)
	{
		if(xIndex == 0 || yIndex == 0)
			return 0;
		return this.map[xIndex - 1][yIndex - 1];
	}

	private int checkWest(int xIndex, int yIndex) 
	{
		if(yIndex == 0)
			return 0;
		return this.map[xIndex][yIndex - 1];
	}

	private int checkSouthWest(int xIndex, int yIndex) 
	{
		if(xIndex == this.numRows - 1 || yIndex == 0)
			return 0;
		return this.map[xIndex + 1][yIndex - 1];
	}

	private int checkSouth(int xIndex, int yIndex) 
	{
		if(xIndex == this.numRows - 1)
			return 0;
		return this.map[xIndex + 1][yIndex];
	}

	private int checkSouthEast(int xIndex, int yIndex)
	{
		if(xIndex == this.numRows - 1 || yIndex == this.numCols - 1)
			return 0;
		return this.map[xIndex + 1][yIndex + 1];
	}

	private int checkEast(int xIndex, int yIndex) 
	{
		if(yIndex == this.numCols - 1)
			return 0;
		return this.map[xIndex][yIndex + 1];
	}

	private int checkNorthEast(int xIndex, int yIndex) 
	{
		if(xIndex == 0 || yIndex == this.numCols - 1)
			return 0;
		return this.map[xIndex - 1][yIndex + 1];
	}

	private int checkNorth(int xIndex, int yIndex) 
	{
		if(xIndex == 0)
			return 0;
		return this.map[xIndex - 1][yIndex];
	}
}

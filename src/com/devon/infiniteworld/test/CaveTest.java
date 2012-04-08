package com.devon.infiniteworld.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class CaveTest extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204918799602570148L;
	public static Cave currentCave;
	public static CaveTest testFrame;

	public CaveTest()
	{
		currentCave = new Cave();
		addMouseListener(new MouseClickHandler());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(currentCave, BorderLayout.CENTER);
		
		setSize(1200, 900);
		setVisible(true);
	}
	
	public static void main(String args[])
	{
		testFrame = new CaveTest();
		
		
	}
	
	private class MouseClickHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent event)
		{
			testFrame.remove(currentCave);
			currentCave = new Cave();
			add(currentCave,BorderLayout.CENTER);
			currentCave.revalidate();
			testFrame.repaint();
			System.out.println("SS");
		}
	}
}

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
	public static GeneratedCave genCave;
	public static CaveTest testFrame;

	public CaveTest()
	{
		genCave = new GeneratedCave();
		addMouseListener(new MouseClickHandler());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(genCave, BorderLayout.CENTER);
		
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
			testFrame.remove(genCave);
			genCave = new GeneratedCave();
			add(genCave,BorderLayout.CENTER);
			genCave.revalidate();
			testFrame.repaint();
			System.out.println("SS");
		}
	}
}

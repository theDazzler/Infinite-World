package com.devon.infiniteworld.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

public class CityTest extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204918799602570148L;
	public static GeneratedCity genCity;
	public static CityTest testFrame;

	public CityTest()
	{
		genCity = new GeneratedCity();
		addMouseListener(new MouseClickHandler());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(genCity, BorderLayout.CENTER);
		
		setSize(1200, 900);
		setVisible(true);
	}
	
	public static void main(String args[])
	{
		testFrame = new CityTest();
		
		
	}
	
	private class MouseClickHandler extends MouseAdapter
	{
		public void mouseClicked(MouseEvent event)
		{
			testFrame.remove(genCity);
			genCity = new GeneratedCity();
			add(genCity, BorderLayout.CENTER);
			genCity.revalidate();
			testFrame.repaint();
			System.out.println("SS");
		}
	}
}

package com.devon.infiniteworld;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;

public class CityTest extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204918799602570148L;
	public static City genCity;
	public static CityTest testFrame;

	public CityTest()
	{
		genCity = new City(37);
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
			genCity = new City(37);
			add(genCity, BorderLayout.CENTER);
			genCity.revalidate();
			testFrame.repaint();
			System.out.println("SS");
		}
	}
}
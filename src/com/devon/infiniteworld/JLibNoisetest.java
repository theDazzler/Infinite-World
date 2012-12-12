package com.devon.infiniteworld;


import java.awt.Canvas;
import java.io.IOException;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.devon.infiniteworld.jlibnoise.Abs;
import com.devon.infiniteworld.jlibnoise.Add;
import com.devon.infiniteworld.jlibnoise.Billow;
import com.devon.infiniteworld.jlibnoise.Blend;
import com.devon.infiniteworld.jlibnoise.Color;
import com.devon.infiniteworld.jlibnoise.Cylinders;
import com.devon.infiniteworld.jlibnoise.Image;
import com.devon.infiniteworld.jlibnoise.Module;
import com.devon.infiniteworld.jlibnoise.NoiseMap;
import com.devon.infiniteworld.jlibnoise.NoiseMapBuilderPlane;
import com.devon.infiniteworld.jlibnoise.NoiseMapBuilderSphere;
import com.devon.infiniteworld.jlibnoise.Perlin;
import com.devon.infiniteworld.jlibnoise.RendererImage;
import com.devon.infiniteworld.jlibnoise.RidgedMulti;
import com.devon.infiniteworld.jlibnoise.ScaleBias;
import com.devon.infiniteworld.jlibnoise.Select;
import com.devon.infiniteworld.jlibnoise.Spheres;
import com.devon.infiniteworld.jlibnoise.Turbulence;
import com.devon.infiniteworld.jlibnoise.Utils;
import com.devon.infiniteworld.jlibnoise.Plane;
import com.devon.infiniteworld.jlibnoise.Voronoi;
import com.devon.infiniteworld.jlibnoise.WriterBMP;

public class JLibNoisetest 
{


	public static void main(String[] args) 
	{
		int seed = 32537;
		Random rand = new Random(seed);
		//int seed = rand.nextInt();
		
		//Create mountain range type heightmap
		//RidgedMulti mountainTerrain = new RidgedMulti();
		Perlin mountainTerrain = new Perlin();
		Billow baseFlatTerrain = new Billow();
		baseFlatTerrain.setFrequency(1.0);
		
		ScaleBias flatTerrain = new ScaleBias();
		flatTerrain.SetSourceModule(0, baseFlatTerrain);
		flatTerrain.setScale(0.125);
		flatTerrain.setBias(-0.90);
		
		Perlin terrainType = new Perlin();
		terrainType.SetFrequency(0.5);
		terrainType.SetPersistence(0.25);
		
		Select terrainSelector = new Select();
		terrainSelector.SetSourceModule(0, flatTerrain);
		terrainSelector.SetSourceModule(1, mountainTerrain);
		terrainSelector.setControlModule(terrainType);
		terrainSelector.setBounds(1000.0, 0);
		terrainSelector.setEdgeFalloff(0.125);
		
		//Turbulence finalTerrain = new Turbulence();
		//finalTerrain.SetSourceModule(0, terrainSelector);
		//finalTerrain.setFrequency(4.0);
		//finalTerrain.setPower(0.125);
	
		
		NoiseMap heigthMap = new NoiseMap();
		NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
		heightMapBuilder.SetSourceModule(terrainSelector);
		heightMapBuilder.SetDestNoiseMap(heigthMap);
		heightMapBuilder.SetDestSize(512, 512);
		heightMapBuilder.SetBounds(6.0, 10.0, 1.0, 5.0);
		heightMapBuilder.Build();
		
		RendererImage renderer = new RendererImage();
		Image image = new Image();
		renderer.SetSourceNoiseMap(heigthMap);
		renderer.SetDestImage(image);
		renderer.ClearGradient();
		renderer.AddGradientPoint (-1.00, new Color ( 32, 160,   0, 255)); // grass
		renderer.AddGradientPoint (-0.25, new Color (224, 224,   0, 255)); // dirt
		renderer.AddGradientPoint ( 0.25, new Color (128, 128, 128, 255)); // rock
		renderer.AddGradientPoint ( 1.00, new Color (255, 255, 255, 255)); // snow		renderer.EnableLight();
		renderer.BuildGrayscaleGradient();
		renderer.EnableLight();
		renderer.SetLightContrast(3.0);
		renderer.SetLightBrightness(2.0);
		renderer.Render();
		
		WriterBMP writer = new WriterBMP();
		writer.SetSourceImage(image);
		writer.SetDestFilename("tutorial.png");
		try {
			writer.WriteDestFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		/**Create Earth Sphere map
		Perlin myModule = new Perlin();
		
		NoiseMap heightMap = new NoiseMap();
		NoiseMapBuilderSphere heightMapBuilder = new NoiseMapBuilderSphere();
		heightMapBuilder.SetSourceModule(myModule);
		heightMapBuilder.SetDestNoiseMap(heightMap);
		heightMapBuilder.SetDestSize(512, 256);
		heightMapBuilder.SetBounds(-90.0, 90.0, -180.0, 180.0);
		heightMapBuilder.Build();
		
		
		RendererImage renderer = new RendererImage();
		Image image = new Image();
		renderer.SetSourceNoiseMap(heightMap);
		renderer.SetDestImage(image);
		renderer.ClearGradient();
		renderer.AddGradientPoint (-1.0000, new Color (  0,   0, 128, 255)); // deeps
		renderer.AddGradientPoint (-0.2500, new Color (  0,   0, 255, 255)); // shallow
		renderer.AddGradientPoint ( 0.0000, new Color (  0, 128, 255, 255)); // shore
		renderer.AddGradientPoint ( 0.0625, new Color (240, 240,  64, 255)); // sand
		renderer.AddGradientPoint ( 0.1250, new Color ( 32, 160,   0, 255)); // grass
		renderer.AddGradientPoint ( 0.3750, new Color (224, 224,   0, 255)); // dirt
		renderer.AddGradientPoint ( 0.7500, new Color (128, 128, 128, 255)); // rock
		renderer.AddGradientPoint ( 1.0000, new Color (255, 255, 255, 255)); // snow
		renderer.EnableLight ();
		renderer.SetLightContrast (3.0);
		renderer.SetLightBrightness (2.0);
		renderer.Render();
		
		WriterBMP writer = new WriterBMP();
		writer.SetSourceImage(image);
		writer.SetDestFilename("earth.jpg");
		try {
			writer.WriteDestFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
		
		/*country map
		
		Perlin terrainType = new Perlin();
		terrainType.SetSeed(seed);
		
		
		NoiseMap heightMap2 = new NoiseMap();
		NoiseMapBuilderPlane heightMapBuilder2 = new NoiseMapBuilderPlane();
		heightMapBuilder2.SetSourceModule(terrainType);
		heightMapBuilder2.SetDestNoiseMap(heightMap2);
		heightMapBuilder2.SetDestSize(512, 512);
		heightMapBuilder2.SetBounds(6.0, 10.0, 1.0, 5.0);
		heightMapBuilder2.Build();
		
		RendererImage renderer2 = new RendererImage();
		Image image2 = new Image();
		renderer2.SetSourceNoiseMap(heightMap2);
		renderer2.SetDestImage(image2);
				
		//renderer2.BuildGrayscaleGradient();
		
		renderer2.Render();
		
		WriterBMP writer2 = new WriterBMP();
		writer2.SetSourceImage(image2);
		writer2.SetDestFilename("perlin.png");
		try {
			writer2.WriteDestFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Voronoi myModule = new Voronoi();
		myModule.setFrequency(2.9);
		myModule.setEnableDistance(false);
		myModule.setSeed(seed);
		
		//make all voronoi be land
		Abs voronoiToLand = new Abs();
		voronoiToLand.SetSourceModule(0, myModule);	
		
		Select terrainSelector = new Select();
		terrainSelector.SetSourceModule(0, terrainType);
		terrainSelector.SetSourceModule(1, voronoiToLand);
		terrainSelector.setControlModule(terrainType);
		terrainSelector.setBounds(1000.0, 0);
		//terrainSelector.setEdgeFalloff(0.125);
		
		
		//output voronoi countries image
		
		
		
		//add elevation data to voronoi
		Blend blender = new Blend();
		blender.SetSourceModule(0, terrainSelector);
		blender.SetSourceModule(1, terrainType);
		blender.setControlModule(terrainSelector);
		
		
		Turbulence finalTerrain = new Turbulence();
		finalTerrain.SetSourceModule(0, blender);
		finalTerrain.setFrequency(3.0);
		finalTerrain.setPower(0.125);
		finalTerrain.setRoughness(5);
		
		
		
		NoiseMap heightMap = new NoiseMap();
		NoiseMapBuilderSphere heightMapBuilder = new NoiseMapBuilderSphere();
		heightMapBuilder.SetSourceModule(finalTerrain);
		heightMapBuilder.SetDestNoiseMap(heightMap);
		heightMapBuilder.SetDestSize(1024, 512);
		heightMapBuilder.SetBounds(-90.0, 90.0, -180.0, 180.0);
		heightMapBuilder.Build();
		
		
		RendererImage renderer = new RendererImage();
		Image image = new Image();
		renderer.SetSourceNoiseMap(heightMap);
		renderer.SetDestImage(image);
		renderer.ClearGradient();
		renderer.AddGradientPoint (-1.0000, new Color (  0,   0, 128, 255)); // deeps
		renderer.AddGradientPoint (-0.2500, new Color (  0,   0, 255, 255)); // shallow
		renderer.AddGradientPoint ( 0.0000, new Color (  0, 128, 255, 255)); // shore
		renderer.AddGradientPoint ( 0.0625, new Color (240, 240,  64, 255)); // sand
		renderer.AddGradientPoint ( 0.1250, new Color ( 32, 160,   0, 255)); // grass
		renderer.AddGradientPoint ( 0.3750, new Color (224, 224,   0, 255)); // dirt
		renderer.AddGradientPoint ( 0.7500, new Color (128, 128, 128, 255)); // rock
		renderer.AddGradientPoint ( 1.0000, new Color (255, 255, 255, 255)); // snow
		renderer.BuildTerrainGradient();
		renderer.EnableLight ();
		renderer.SetLightContrast (3.0);
		renderer.SetLightBrightness (2.0);
		
		renderer.Render();
		
		WriterBMP writer = new WriterBMP();
		writer.SetSourceImage(image);
		writer.SetDestFilename("earth.jpg");
		try {
			writer.WriteDestFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		

		

		//JOptionPane.showMessageDialog(null, null, "Another", JOptionPane.YES_NO_OPTION, new ImageIcon());
		

	}

}

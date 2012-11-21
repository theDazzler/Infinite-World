package com.devon.infiniteworld;


import java.io.IOException;

import com.devon.infiniteworld.jlibnoise.Billow;
import com.devon.infiniteworld.jlibnoise.Color;
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
import com.devon.infiniteworld.jlibnoise.Turbulence;
import com.devon.infiniteworld.jlibnoise.Utils;
import com.devon.infiniteworld.jlibnoise.Plane;
import com.devon.infiniteworld.jlibnoise.WriterBMP;

public class JLibNoisetest 
{


	public static void main(String[] args) 
	{
		/**Create mountain range type heightmap
		RidgedMulti mountainTerrain = new RidgedMulti();
		Billow baseFlatTerrain = new Billow();
		baseFlatTerrain.setFrequency(2.0);
		
		ScaleBias flatTerrain = new ScaleBias();
		flatTerrain.SetSourceModule(0, baseFlatTerrain);
		flatTerrain.setScale(0.125);
		flatTerrain.setBias(-0.75);
		
		Perlin terrainType = new Perlin();
		terrainType.SetFrequency(0.5);
		terrainType.SetPersistence(0.25);
		
		Select terrainSelector = new Select();
		terrainSelector.SetSourceModule(0, flatTerrain);
		terrainSelector.SetSourceModule(1, mountainTerrain);
		terrainSelector.setControlModule(terrainType);
		terrainSelector.setBounds(1000.0, 0);
		terrainSelector.setEdgeFalloff(0.125);
		
		Turbulence finalTerrain = new Turbulence();
		finalTerrain.SetSourceModule(0, terrainSelector);
		finalTerrain.setFrequency(4.0);
		finalTerrain.setPower(0.125);
		
		NoiseMap heigthMap = new NoiseMap();
		NoiseMapBuilderPlane heightMapBuilder = new NoiseMapBuilderPlane();
		heightMapBuilder.SetSourceModule(finalTerrain);
		heightMapBuilder.SetDestNoiseMap(heigthMap);
		heightMapBuilder.SetDestSize(256, 256);
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
		renderer.EnableLight();
		renderer.SetLightContrast(3.0);
		renderer.SetLightBrightness(2.0);
		renderer.Render();
		
		WriterBMP writer = new WriterBMP();
		writer.SetSourceImage(image);
		writer.SetDestFilename("tutorial.jpg");
		try {
			writer.WriteDestFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
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

		
		
		
		

	}

}

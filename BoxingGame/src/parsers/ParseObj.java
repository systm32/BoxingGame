package parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import objects.Vertex;

public class ParseObj {
	
	private float transX,transY,transZ;
	private int scale;
	HashMap<String,Vertex> hmap;
	private ArrayList<Integer> Vertex,Normal,Texture;
	private ArrayList<Vertex> vertices,normals,textures;
	private ArrayList<Vertex> outVertex,outNormal,outTexture,outColor;
	private String file_url;
	
	public ParseObj(String url,int scale,float transx,float transy,float transz)
	{
		file_url = url;
		
		this.scale = scale;
		this.transX = 0.0f;
		this.transY = 0.0f;
		this.transZ = 0.0f;
		
		vertices = new ArrayList<Vertex>();
		normals = new ArrayList<Vertex>();
		textures = new ArrayList<Vertex>();
		hmap = new HashMap<String,Vertex>();
		
		outVertex = new ArrayList<Vertex>();
		outNormal = new ArrayList<Vertex>();
		outTexture = new ArrayList<Vertex>();
		outColor = new ArrayList<Vertex>();
		
		Vertex = new ArrayList<Integer>();
		Normal = new ArrayList<Integer>();
		Texture = new ArrayList<Integer>();	
		
	}
	
	public void fillHmap(String url1)
	{
		File file = new File(url1);
		//Read file
		
		try {
			FileInputStream fis = new FileInputStream(file);
			
			//Construct Buffered Reader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			
			String line = null;
			String key = null;
			while((line = br.readLine()) != null){
				System.out.println(line.trim());
				String[] arr = line.split("\\ ");
				if(arr[0].contentEquals("newmtl")){
					key = arr[1];
				}else if(arr[0].contentEquals("Kd"))
				{
					System.out.println(hmap.size());
					hmap.put(key,new Vertex(Float.parseFloat(arr[1]),Float.parseFloat(arr[2]),Float.parseFloat(arr[3])));
					System.out.println(key+" "+hmap.get(key).getX() +" "+arr[2]+" "+arr[3]);
				}
			}
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public ArrayList<Vertex> getVertices()
	{
		return outVertex;
	}
	
	public ArrayList<Vertex> getNormals()
	{
		return outNormal;
	}
	
	public ArrayList<Vertex> getTextures()
	{
		return outTexture;
	}
	
	public ArrayList<Vertex> getColor()
	{
		for(int i=0;i<outColor.size();i++)
			System.out.println(outColor.get(i).getX()+" "+outColor.get(i).getY()+" "+outColor.get(i).getZ());
		return outColor;
	}
	
	public void ParseIt()
	{
		
		File file = new File(file_url);
		//Read file
		
		try {
			FileInputStream fis = new FileInputStream(file);
			
			//Construct Buffered Reader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			
			String line = null;
			Vertex color = new Vertex(0.0f,0.0f,0.0f);
			while((line = br.readLine()) != null){
				System.out.println(line.trim());
				String[] arr = line.split("\\ ");
				for(int i=0;i<arr.length;i++)
				{
					arr[i].trim();
				}
				if(arr[0].contentEquals("v"))
				{
					vertices.add(new Vertex(scale*(Float.parseFloat(arr[1])+transX),scale*(Float.parseFloat(arr[2])+transY),scale*(Float.parseFloat(arr[3])+transZ)));
				}else if(arr[0].contentEquals("vt"))
				{
					textures.add(new Vertex(Float.parseFloat(arr[1]),Float.parseFloat(arr[2]),Float.parseFloat("0.0")));
				}else if(arr[0].contentEquals("vn")){
					normals.add(new Vertex(Float.parseFloat(arr[1]),Float.parseFloat(arr[2]),Float.parseFloat(arr[3])));
				}else if(arr[0].contentEquals("f")){
					System.out.println("Added " +color.getX()+" "+color.getY()+" "+color.getZ());
					outColor.add(new Vertex(color.getX(),color.getY(),color.getZ()));
					for(int i=1;i<4;i++)
					{
						String part = arr[i];
						String[] arr1 = part.split("\\/");
						outVertex.add(vertices.get(Integer.parseInt(arr1[0])-1));
						outNormal.add(normals.get(Integer.parseInt(arr1[2])-1));
						outTexture.add(textures.get(Integer.parseInt(arr1[1])-1));
					}
				}else if(arr[0].contentEquals("usemtl")){
					//System.out.println("hey" + hmap.get(arr[1]).getX()+" "+hmap.get(arr[1]).getY()+" "+hmap.get(arr[1]).getZ());
					color.setX(hmap.get(arr[1]).getX());
					color.setY(hmap.get(arr[1]).getY());
					color.setZ(hmap.get(arr[1]).getZ());
				}
			}			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}

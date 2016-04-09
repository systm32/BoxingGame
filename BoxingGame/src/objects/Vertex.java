package objects;

public class Vertex {
	public float x;
	public float y;
	public float z;
	
	public Vertex(float x,float y,float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float getX()
	{
		return this.x;
	}
	
	public float getY()
	{
		return this.y;
	}
	
	public float getZ()
	{
		return this.z;
	}
	
	public void setX(float x)
	{
		this.x = x;
	}
	
	public void setY(float y)
	{
		this.y = y;
	}
	
	public void setZ(float z)
	{
		this.z = z;
	}
}

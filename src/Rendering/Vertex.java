package Rendering;

/**
 * A class that hold the x,y,z coordiantes for a vertex of a 3D object
 * @author Ashton Davenport
 *
 */
public class Vertex {
	
	private float x;
	private float y;
	private float z;
	
	public Vertex(float x, float y, float z){
		this.x = x;
		this.y =y;
		this.z = z;
	}
	
	public float getX(){
		return this.x;
	}
	
	public float getY(){
		return this.y;
	}
	
	public float getZ(){
		return this.z;
	}
	
}

package Rendering;

/**
 * A class which represents a face of a 3D object
 * @author Ashton Davenport
 *
 */

public class Face {
	
	private int first;
	private int second;
	private int third;
	
	public Face(int first, int second, int third){
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public int getFirst(){
		return this.first;
	}
	
	public int getSecond(){
		return this.second;
	}
	
	public int getThird(){
		return this.third;
	}
}

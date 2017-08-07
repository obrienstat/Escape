package Rendering;

/**
 * This class represents a texture Coordinate for a 3D object
 * @author Ashton Davenport
 *
 */

public class TextureCoordinate {
	
	private double first;
	private double second;
	
	public TextureCoordinate(double first, double second){
		this.first = first;
		this.second = second;
	}
	
	public double getFirst(){
		return this.first;
	}
	
	public double getSecond(){
		return this.second;
	}
}

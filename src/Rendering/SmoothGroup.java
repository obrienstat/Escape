package Rendering;

import java.util.ArrayList;

/**
 * A class that holds a group of faces that make up a smooth group
 * @author Ashton Davenport
 *
 */
public class SmoothGroup {

	private ArrayList<Face>faces;
	
	public SmoothGroup(ArrayList<Face> faces){
		this.faces = faces;
	}
	
	public ArrayList<Face> getFaces(){
		return this.faces;
	}
}

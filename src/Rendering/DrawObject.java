package Rendering;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.TriangleMesh;

/**
 * An object which hold the vertices, texture coordinates, faces and smoothing groups for a 3D object to be drawn by the renderer
 * @author Ashton Davenport
 *
 */
public class DrawObject {

	private ArrayList<Vertex> vertices;
	private ArrayList<TextureCoordinate>tc;
	private ArrayList<Face>faces;
	private ArrayList<SmoothGroup> sg;

	public DrawObject(ArrayList<Vertex>vertices, ArrayList<TextureCoordinate> tc, ArrayList<Face>faces, ArrayList<SmoothGroup>sg){
		this.vertices = vertices;
		this.tc = tc;
		this.faces = faces;
		this.sg = sg;
	}

	/**
	 * Returns the Vertices for this DrawObject
	 * @return the Vertices for this DrawObject
	 */
	public ArrayList<Vertex> getVertices(){
		return this.vertices;
	}

	/**
	 * Returns the TextureCoordinates for this DrawObject
	 * @return the TextureCoordinates for this DrawObject
	 */
	public List<TextureCoordinate> getTextureCoordinates(){
		return this.tc;
	}

	/**
	 * Returns the faces for this DrawObject
	 * @return the faces for this DrawObject
	 */
	public List<Face> getFaces(){
		return this.faces;
	}

	/**
	 * Returns the smoothGroup for this DrawObject
	 * @return the smoothGroup for this DrawObject
	 */
	public List<SmoothGroup> getSmoothGroups(){
		return this.sg;
	}

	/**
	 * Returns a new Triangle mesh using the information stored in this drawObject
	 * @return a new TriangleMesh that represents the information held in this DrawObject
	 */
	public TriangleMesh getMesh(){
		TriangleMesh mesh = new TriangleMesh();
		mesh.getTexCoords().addAll(0,0); // add texture coordinates, we don't need any so we add 0,0

		// get the vertices and add them to the mesh
		float[] verts = new float[vertices.size()*3];
		for(int i = 0; i < vertices.size(); i++){
			verts[i*3] = vertices.get(i).getX();
			verts[(i*3)+1] = vertices.get(i).getY();
			verts[(i*3)+2] = vertices.get(i).getZ();
		}
		mesh.getPoints().addAll(verts, 0, verts.length);

		// get the faces and add them to the mesh
		for(Face f: faces){
			mesh.getFaces().addAll(f.getFirst(),0, f.getSecond(),0, f.getThird(),0);
		}
		return mesh;
	}
}

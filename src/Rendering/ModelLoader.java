package Rendering;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * This class reads in information from a .obj file and creates a drawObject
 * @author Ashton Davenport
 *
 */
public class ModelLoader implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parses an .obj file into a DrawObject
	 * @param fileName the directory of the .obj file, must be a .obj file with triangulated faces to work correctly
	 * @return a draw object that represents the .obj file
	 */
	public DrawObject loadModel(String fileName){

		// create the ArrayLists to add the data to
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		ArrayList<TextureCoordinate> tc = new ArrayList<TextureCoordinate>();
		ArrayList<Face> faces = new ArrayList<Face>();
		ArrayList<SmoothGroup> sg = new ArrayList<SmoothGroup>();
		ArrayList<Face> temp = new ArrayList<Face>(); // temp ArrayList used for creating the smoothgroups

		try {

			BufferedReader br = new BufferedReader(new FileReader(fileName));
			String currentLine = " ";

			// read in the file line by line
			while((currentLine = br.readLine()) != null){

				String[] line = currentLine.split("\\s+"); //remove spaces
				String firstLine = " ";
				if(line.length > 0) {
					firstLine = line[0];

				}


				// use a switch statement to parse the info based on the start of the line read in
				switch(firstLine){
				case "v": // it is a vector to be parsed
					float x = Float.parseFloat(line[1]);
					float y = Float.parseFloat(line[2]);
					float z = Float.parseFloat(line[3]);
					Vertex v = new Vertex(x, y, z);
					vertices.add(v);
					break;
				case "vt": // it is a texture coordinate to be parsed
					double first = Double.parseDouble(line[1]);
					double second = Double.parseDouble(line[2]);
					TextureCoordinate tc1 = new TextureCoordinate(first, second);
					tc.add(tc1);
					break;
				case "s": // it is a smooth group to be parsed
					if(line[1] != "1"){
						SmoothGroup smg = new SmoothGroup(temp);
						sg.add(smg);
						temp = new ArrayList<Face>();
					}
					break;
				case "f": // it is a face to be parsed
					String[] s1 = line[1].split("/"); // removes backslashes
					String[] s2 = line[2].split("/"); // removes backslashes
					String[] s3 = line[3].split("/"); // removes backslashes
					int v1 = Integer.parseInt(s1[0]);
					int v2 = Integer.parseInt(s2[0]);
					int v3 = Integer.parseInt(s3[0]);
					Face f = new Face(v1-1, v2-1, v3-1);
					faces.add(f);
					temp.add(f);
					break;
				}
			}
		} catch (IOException e) {
			System.out.println("The file was not found");
			e.printStackTrace();
		}

		SmoothGroup smg = new SmoothGroup(temp);
		sg.add(smg);


		DrawObject drawO = new DrawObject(vertices, tc, faces, sg);
		return drawO;
	}



}

package world;

import javafx.scene.paint.Color;
/**
 * Clues give the players hints to assist them in finding items and progress through the game.s
 * @author Ashton A
 *
 */
public class Clue extends Portable{

	private String clue;

	public Clue(String name, String description, int id, coordinate location, String image, int rotation, String clue, String bumpMap,
			String diffuseMap, String objFile, Color color) {
		super(name, description, id, location, image,rotation, bumpMap, diffuseMap,objFile,color);
	}




	public String getClue(){
		return clue;
	}

	public void setClue(String clue){
		this.clue=clue;
	}



	public String read(){
		return clue;
	}

}
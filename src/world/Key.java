package world;

import javafx.scene.paint.Color;
/**
 * Item used to unlock Doors in game. TO Unlock a door, the Code field for both the door and the key must match.
 * @author Ashton A
 *
 */
public class Key extends Portable{

	private int code;//unique code to unlock door

	public Key(String name, String description, int id, coordinate location, String image, int rotation, int code, String bumpMap,
			   String diffuseMap, String objFile, Color color) {

		super(name, description, id, location, image, rotation,bumpMap, diffuseMap,objFile,color);
		this.code = code;
	}

	public int getCode(){
		return code;
	}


	public void setCode(int new_code){
		code=new_code;
	}




}

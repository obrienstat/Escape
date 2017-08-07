package world;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Sprite extends Portable {

	private coordinate coord;
	private Room.Direction direction;


	public Sprite(String name, int id, String description, coordinate location, String image, int rotation,
			String bumpMap, String diffuseMap, String objFile, Color color) {
		super(name, description, id, location, image, rotation,bumpMap, diffuseMap,objFile,color);
	}



	public void setDirection(Room.Direction direction){
		this.direction = direction;
	}

	@Override
	public void setLocation(coordinate c){
		this.coord = c;
	}

	@Override
	public coordinate getLocation(){
		switch (direction){
		case EAST:
			return new coordinate(0, 0, -300);
		case NORTH:
			return new coordinate(-300, 0, 0);
		case SOUTH:
			return new coordinate(300, 0, 0);
		case WEST:
			return new coordinate(0, 0, 300);
		default:
			return null; // dead code

		}
	}

	@Override
	public void rightClick(MouseEvent event){

	}


	@Override
	public void leftClick() {
		// TODO Auto-generated method stub

	}

}

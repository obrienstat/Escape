package world;

import java.io.IOException;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import userInterface.AppWindow;

/**
 * Mobile is an item that cannot be carried in the inventory but can be moved in the world. Movement is only "on rails" , 
 * the item traveling between two pre determined coordinates.
 * @author Ashton A
 *
 */
public class Mobile extends Item{

	private coordinate destination;
	private coordinate location;

	public Mobile(String name, String description, int id, coordinate location, coordinate destination, String image, int rotation, String bumpMap,
			String diffuseMap, String objFile, Color color) {
		super(name, id, description, location, image,rotation,bumpMap, diffuseMap,objFile,color);
		this.destination= destination;
		this.location = new coordinate(super.getLocation().getValues()); // storing location here as it is used alot locally
	}

	/**
	 * Shifts item to to another part of the world to reveal a hidden item
	 * or trigger an interaction. Movement can only be between two predetermined points.
	 */
	public void move(){
		coordinate temp = new coordinate(this.location.getValues());
		this.setLocation(new coordinate(destination.getValues()));
		this.setDestination(new coordinate(temp.getValues()));

	}

	public void setDestination(coordinate destination){
		this.destination=destination;
	}

	public coordinate getDestination(){
		return destination;
	}

	@Override
	public void leftClick(){};

	@Override
	public void rightClick(MouseEvent event) {
		// this was used for testing, so don't remove
		ContextMenu context = getPopUpMenu();
		MenuItem pickup = new MenuItem("Pick Up");
		pickup.setOnAction(e -> {
			try {
				AppWindow.out.writeUTF("PICK_UP");
				AppWindow.out.writeInt(getId());
			}catch (IOException ex){
				ex.printStackTrace();
			}
		});
		context.getItems().add(pickup);
		context.show(AppWindow.window, event.getScreenX(), event.getSceneY());
	}



}

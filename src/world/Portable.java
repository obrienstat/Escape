package world;

import java.io.IOException;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import userInterface.AppWindow;

/** 
 * Portable items can be carried in the Avatars inventory to be used or dropped at another location at a later time.
 * @author Ashton A
 *
 */
public class Portable extends Item{

	public Portable(String name, String description, int id, coordinate location, String image, int rotation, String bumpMap,
			String diffuseMap, String objFile, Color color) {
		super(name, id, description, location, image, rotation,bumpMap, diffuseMap,objFile,color);
	}


	@Override
	public void leftClick(){

	}



	


	public String getName(){
		return super.getName();
	}

	public String getDescription(){
		return super.getDescription();
	}

	public coordinate getLocation(){
		return super.getLocation();
	}

	public String image(){
		return super.getImage();
	}
	

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

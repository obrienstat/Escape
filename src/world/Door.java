package world;
import java.io.IOException;
import java.util.*;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import userInterface.AppWindow;
/**
 * Used as transporters between rooms. Must be unlocked for passage.
 * @author Ashton
 *
 */
public class Door extends Immobile{

	private boolean locked;
	private int code;

	private Room roomTo;

	public Door(String name, String description, int id, coordinate location, String image, int rotation,boolean locked, int code, Room roomTo
			, String bumpMap, String diffuseMap, String objFile, Color color){

		super(name, description, id, location, image,rotation,bumpMap, diffuseMap,objFile,color);
		this.locked=locked;
		this.code=code;


		this.roomTo = roomTo;
	}

	@Override
	public void rightClick(MouseEvent event) {
		ContextMenu cm = getPopUpMenu();
		MenuItem mi = new MenuItem("Enter");
		mi.setOnAction(e -> {
			try{
				AppWindow.out.writeUTF("ENTER");
				AppWindow.out.writeInt(getId());
			}catch(IOException ex){
				ex.printStackTrace();
			}
		});
		cm.getItems().add(mi);
		cm.show(AppWindow.window, event.getScreenX(), event.getScreenY());
	}




	/**
	 * Return whether door is locked or not
	 * @return
	 */
	public boolean isLocked(){
		return locked;
	}

	public void setLocked(boolean locked){
		this.locked=locked;
	}

	public int getCode(){
		return code;
	}

	public void setCode(int code){
		this.code=code;
	}

	public Room getRoomTo(){
		return roomTo;
	}

	public void setRoomTo(Room roomTo){
		this.roomTo=roomTo;
	}


	/**
	 * Pass a key to unlock the door. Matching key will have same code as door.
	 * Only matching key will unlock the door.
	 * @param key
	 * @return
	 */
	public boolean unlock(List<Item> inventory){
		for(Item item : inventory){
			if(item instanceof Key){
				if(((Key)item).getCode() == this.code){
					this.setLocked(false);					
					return true;
				}
			}
		}
				return false;
	}

}

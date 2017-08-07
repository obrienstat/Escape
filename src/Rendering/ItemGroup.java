package Rendering;

import javafx.scene.Group;
/**
 * This is designed to hold items, its purpose is so we can find the item group in the root node of the renderer and remove it using instanceof
 * @author Ashton Davenport
 *
 */
public class ItemGroup extends Group{

	public ItemGroup(){
		super();
		
		setOnMouseClicked(event -> {
			
		});
	}

}

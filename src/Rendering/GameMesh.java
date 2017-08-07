package Rendering;

import java.io.IOException;
import java.io.Serializable;

import javafx.application.Platform;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import userInterface.AppWindow;
import world.Item;

/**
 * This class is a drawable mesh to the renderer and holds the item the mesh is modeling
 * @author Ashton Davenport
 *
 */
public class GameMesh extends MeshView implements Serializable {

	private static final long serialVersionUID = -7272955162701378258L;

	private Item item;

	public GameMesh(Item item, Mesh mesh){
		super(mesh);
		this.item = item;

		setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.PRIMARY){
				
			}
			else if(e.getButton() == MouseButton.SECONDARY){
				item.rightClick(e);
			}
		});
	}

//	public ContextMenu rightClick(){
//		final ContextMenu contextMenu = new ContextMenu();
//		MenuItem pickup = new MenuItem("Pick Up");
//		MenuItem description = new MenuItem("Description");
//		contextMenu.getItems().addAll(pickup, description);
//		pickup.setOnAction(e -> {
//			try {
//				AppWindow.out.writeUTF("PICK_UP");
//				AppWindow.out.writeUTF(item.getName());
//			} catch (Exception e1) {
//				e1.printStackTrace();
//			}
//		});
//
//		description.setOnAction(e -> {
//			System.out.println(item.getDescription());
//
//		});
//
//		return contextMenu;
//	}

	public Item getItem(){
		return item;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Item){
			Item other = (Item)o;
			if(other.getName().equals(item.getName())) return true;
		}
		return false;
	}

	public String toString(){
		return "Item name: "+item.getName();
	}
}

package userInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



import Rendering.Renderer;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import world.Avatar;
import world.Door;
import world.Item;
import world.Key;

/**
* The inventory class represents the area where players inventory is placed. This class is used by the
* Dashboard to show the players inventory. It is able to hold a maximum of six items.
* @author barnettayl
*
*/
public class Inventory extends HBox {

	GridPane gridPane;
	private Avatar previousAvatar;
	private AppWindow app;


	public Inventory()	{
		setId("inventory");
		setPrefSize(660, 110);
	}

	/**
	 * get all the items from the players inventory and addeds them to the inventory box
	 * @param player
	 */
	public void appendItems(AppWindow app, Avatar player){
		this.app = app;

		// checks if we really need to render these objects again
		if (this.previousAvatar != null)
			if (player.getInventory().equals(this.previousAvatar.getInventory()))
				return;

		// clears all the items in the inventory box
		getChildren().clear();


		for (Item i: player.getInventory()){
			System.out.println(i.getName());
			Image Image = new Image("file:" + i.getImage());
			Label label = new Label();
			label.setGraphic(new ImageView(Image));

			label.setOnMouseClicked(event -> {
				ContextMenu popup = i.getPopUpMenu();

				// adds a drop option to the popup
				MenuItem drop = new MenuItem("Drop");
				drop.setOnAction(dropEvent -> {

					// we send the drop option to the server
					try {
						AppWindow.out.writeUTF("DROP");
						AppWindow.out.writeInt(i.getId());
					} catch (IOException e) {
						e.printStackTrace();
					}

				});
				popup.getItems().add(drop);

				if (i.getName().equals("Map")){
					MenuItem mapMenu = mapMenu(i);
					popup.getItems().add(mapMenu);
				}

				// now show the popup menu
				popup.show(AppWindow.window, event.getScreenX(), event.getScreenY());
			});

			getChildren().add(label);

		}
		previousAvatar = player;
	}

	private MenuItem mapMenu(Item mapItem){

		MenuItem item = new MenuItem("Show Map");
		item.setOnAction(event -> {
			if(containsCompass()){
				app.fadeTransitionCompassMap();
			} else {
				app.fadeTransitionMap();
			}
		});

		return item;
	}

	private boolean containsCompass(){
		for (Item i: previousAvatar.getInventory()){
			if (i.getName().equals("Compass")){
				return true;
			}
		}
		return false;
	}



}




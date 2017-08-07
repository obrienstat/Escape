package userInterface;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
* This class creates the bottom dashboard. The Dashboard holds the players inventory
* and also the chat.
* It extends HBox and is added to the GameWindow in the AppWindow.
* @author barnettayl
*
*/
public class Dashboard extends HBox {

	Inventory inventory;
	//RotateControl controls;

	Chat chatWindow;
	Separator s1;
	Stage window;

	public Dashboard(Stage window, AppWindow app) {

		this.window = window;
		setId("dashboard");
		setPadding(new Insets(10, 10, 10, 10));
		setSpacing(10);
		setPrefHeight(120);
		//setStyle("-fx-background-color: #336699;");

		inventory = new Inventory();

		s1 = new Separator();
		s1.setOrientation(Orientation.VERTICAL);

		chatWindow = new Chat();

		TextArea chat = new TextArea();
		TextField input = new TextField();
		inventory.setAlignment(Pos.CENTER_LEFT);
		chatWindow.setAlignment(Pos.CENTER_RIGHT);

		getChildren().addAll(inventory, s1, chatWindow);
	}

	public Chat getChatWindow(){
		return this.chatWindow;
	}

	public Inventory getInventory(){
		return this.inventory;
	}
}

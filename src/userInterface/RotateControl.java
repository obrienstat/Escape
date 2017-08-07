package userInterface;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

import Rendering.Renderer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import world.Avatar;
import world.Room;

/**
* This class creates a pane that holds the rotate control arrows
* @author barnettayl
*
*/
public class RotateControl extends FlowPane {

	Label left;
	Label right;

	public RotateControl(Renderer render, AppWindow app) {

		setHgap(5);
		setPrefWrapLength(95);
		FileInputStream leftButton = null;
		try {
			leftButton = new FileInputStream("rotateButtons/leftArrow.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//Rectangle leftRect = new Rectangle(80, 80);
		Image imageLeft = new Image(leftButton,100,100,true,true);
		left = new Label();
		left.setGraphic(new ImageView(imageLeft));

		left.setOnMouseClicked(e -> {
			try {
				AppWindow.out.writeUTF("MOVE");
				AppWindow.out.write(0);
			} catch (IOException e1) {
				// append a message to the client chat
				app.getChatWindow().appendMessage("[SERVER]: Move command not sent!");
			}
		});

		FileInputStream rightButton = null;
		try {
			rightButton = new FileInputStream("rotateButtons/rightArrow.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Image imageRight = new Image(rightButton,100,100,true,true);
		right = new Label();
		right.setGraphic(new ImageView(imageRight));
		right.setOnMouseClicked(e -> {
			try {
				AppWindow.out.writeUTF("MOVE");
				AppWindow.out.write(1);

			} catch (IOException e1) {
				// append a message to the client chat
				app.getChatWindow().appendMessage("[SERVER]: Move command not sent!");
			}
		});

		//getChildren().addAll(left, right);

	}

	public Label getLeft(){
		return left;
	}

	public Label getRight(){
		return right;
	}


}

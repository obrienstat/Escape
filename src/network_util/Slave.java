package network_util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Rendering.ItemGroup;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import logic.Game;
import userInterface.AppWindow;
import world.Avatar;
import world.Item;
import world.Room;
import world.Room.Direction;



/**
 * A slave receives updates from the server and relays this new information into
 * the application.
 *
 * The slave constructs a new game and uses this input to update the state of
 * the application
 * @author Jack
 *
 */
public class Slave extends Thread {

	private DataOutputStream out;
	private DataInputStream in;
	private Socket sock;
	private AppWindow app;

	public Slave(Socket socket, AppWindow app){
		this.sock = socket;
		this.app = app;
		//setDaemon(true);
	}

	public void run(){
		Avatar player = null;
		try {
			out = new DataOutputStream(sock.getOutputStream());
			in = new DataInputStream(sock.getInputStream());

			// retrieve the player from the server
			int amount = in.readInt();
			byte[] byteArray = new byte[amount];
			in.readFully(byteArray);
			player = (Avatar)Game.deserialize(byteArray);



			// now update the renderer to reflect this players position
			receiveUpdatedPlayer(player);

			boolean exit = false;
			while (!exit) {

				// we listen for communication
				if (in.available() != 0){
					String data = in.readUTF();

					switch (data){
					case "WARNING":
						alertOnWarning(in.readUTF());
						break;

					case "MSG":
						receiveInstantMessage(in.readUTF());
						break;
					case "AVATAR_UPDATE":
						amount = in.readInt();
						byteArray = new byte[amount];
						in.readFully(byteArray);
						player = (Avatar)Game.deserialize(byteArray);
						receiveUpdatedPlayer(player);

						//System.out.println("Should have updated [CLIENT] rooms left: "+player.getLocation().getItems().size());
					}
				}

			}
			sock.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void alertOnWarning(String message){
		Platform.runLater(() -> {
			ContextMenu warning = new ContextMenu();
			MenuItem descrip = new MenuItem(message);
			warning.getItems().add(descrip);
			warning.show(AppWindow.window);
		});
	}


	/**
	 * Updates the game state of the board and relays this information to the renderer
	 * @param player
	 */
	private void receiveUpdatedPlayer(Avatar player){
		// append the newly updated avatar to the renderer
		Platform.runLater(() -> {
			app.getDashboard().getInventory().appendItems(app, player);;
			app.getRenderer().update(player);
		});
	}

	/**
	 * Receives an Instant message from other player
	 * @param message
	 */
	private void receiveInstantMessage(String message){
		Platform.runLater(() -> {
			app.getChatWindow().appendMessage(message);
		});
	}

}

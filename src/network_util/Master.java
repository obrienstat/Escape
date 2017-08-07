package network_util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import logic.Game;
import userInterface.AppWindow;
import world.Avatar;
import world.Item;
import world.Room;
import world.Room.Direction;

public class Master extends Thread {

	private Socket sock;
	private Game game;
	private int playerUid;


	public Master(Socket sock, Game game){
		this.sock = sock;
		this.game = game;
		//setDaemon(true);
	}

	public void run(){

		try {
			DataOutputStream out = new DataOutputStream(sock.getOutputStream());
			DataInputStream in = new DataInputStream(sock.getInputStream());

			// create an Avatar for the current player
			Avatar player = game.getPlayer();
			playerUid = player.getUid();
			//System.out.println(in.readUTF());
			player.setName(in.readUTF());

			while (!game.isConnected()){
				// waiting for other players to join
				Thread.sleep(1000);
			}

			// send all the information of the newly updated Avatar to the client
			byte[] byteArray = Game.serialize(player);
			out.writeInt(byteArray.length);
			out.write(byteArray);
			out.flush();

			boolean exit = false;
			while (!exit) {
				try {

					// during sending and receiving of events, there will be times when
					// special events will be triggered. We need to catch these special events and
					// then proceed to update the game state accordingly
					if (in.available() != 0){
						// once we receive a special state, we acknowledge appropriately

						String data = in.readUTF();
						switch (data){
							case "MSG":
								// Relay this message to all connected clients
								//System.out.println("received message [SERVER]");
								sendInstantMessage(player, in.readUTF());
								break;

							case "ENTER":
								int doorId = in.readInt();
								if (game.changeRoom(player, doorId)){
									//System.out.println("Change rooms");
									break;
								}else {
									out.writeUTF("WARNING");
									out.writeUTF("The door is locked!");
									break;
								}

							case "MOVE":
								//System.out.println("[SERVER]: Moving Player");
								int dir = in.read();
								movePlayer(player, dir, playerUid);
								break;

							case "PICK_UP":

								if (player.getInventory().size() == 6){
									out.writeUTF("WARNING");
									out.writeUTF("You inventory is full");
									break;
								}

								int pickItem = in.readInt();
								//System.out.println("Player: "+player.getName()+" is picking up an item: "+pickItem);
								game.pickUpItem(player, pickItem);
								break;

							case "DROP":

								int itemId = in.readInt();
								//System.out.println("Player: "+player.getName()+" is dropping an item: "+itemId);
								game.drop(player, itemId);
								break;

							case "SAVE":

								//System.out.println("Saving game state");
								game.saveGameToState();
								break;

							case "LOAD":

								//System.out.println("Loading game from state");
								game.loadSavedGame();
								break;

							case "END":
								// the client wishes to disconnect
								sock.close();
								return;
						}
					}

					// doing this forces us to send the game state every 250ms
					sendUpdate(player, out);
					out.flush();
					Thread.sleep(50);

				} catch (Exception e){
					e.printStackTrace();
				}
			}
		} catch (IOException io){
			io.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Sends the current and other player, their updated game states after each update received from client
	 * @param player
	 * @param out
	 * @throws IOException
	 */
	private void sendUpdate(Avatar player, DataOutputStream out) throws IOException {
		out.writeUTF("AVATAR_UPDATE");
		byte[] playerState = Game.serialize(player);
		out.writeInt(playerState.length);
		out.write(playerState);
		out.flush();
		//System.out.println("[SERVER]: Sent state to the client");
	}

	/**
	 * Changes the players position.
	 * @param player
	 * @param out
	 * @throws IOException
	 */
	private void movePlayer(Avatar player, int dir, int playerUid) throws IOException {

		// set our player in the correct direction
		if (dir == 1)
			game.turnRight(player, playerUid);
		else
			game.turnLeft(player, playerUid);
	}

	private void sendInstantMessage(Avatar player, String message) throws IOException{
		for (Socket s: AppWindow.connections){
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF("MSG");
			out.writeUTF(player.getName()+": "+message);
		}
		//System.out.println("[SERVER]: Sent instant message to all players");
	}

	private void sendGameState() throws IOException{

		// send game state to all players
		for (Socket s: AppWindow.connections){
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeUTF("STATE");
			// send byte object
		}
		//System.out.println("[SERVER]: Sent game state to all players");
	}

}

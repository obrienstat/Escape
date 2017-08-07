package dataStorage;
import org.w3c.dom.*;

import world.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the loading of all game data
 *
 * @author Tom Kennedy 300326440
 *
 */
public class Load {

	//For testing only
	public static void main(String[] args){
		loadGame("save");
	}

	/**
	 * Load the GameState
	 * If any errors occur a new game is loaded instead of the old game
	 * @param fileName
	 * @return new loaded GameState
	 */
	public static GameState loadGame(String fileName){
		GameState game = new GameState("","");
		GameState loadedGame = game;

		Avatar player1 = new Avatar(null,4431,null,null);
		Avatar player2 = new Avatar(null,4413,null,null);

		try{
			//Create the document to parse
			File saveFile = new File(fileName + ".xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document saveDoc = builder.parse(saveFile);
			saveDoc.getDocumentElement().normalize();

			//Load all rooms
			NodeList roomsNode = saveDoc.getElementsByTagName("room");
			loadedGame.rooms = loadRooms(roomsNode,game);

			//Load both players
			NodeList playerNodes = saveDoc.getElementsByTagName("player");
			loadedGame.player1 = loadPlayer(playerNodes.item(0),player1,game);
			loadedGame.player2 = loadPlayer(playerNodes.item(1),player2,game);

			//Save the new state to ensure it's the same
			Save.saveGame("loadedSave", loadedGame);
			return loadedGame;

		}
		catch(Exception e){
			e.printStackTrace();
			return game;
		}



	}

	/**
	 * Load an Avatar object from a player node
	 * @param playerNode
	 * @param player
	 * @param game
	 * @return
	 */
	private static Avatar loadPlayer(Node playerNode, Avatar player, GameState game){
		NamedNodeMap playerAttributes = playerNode.getAttributes();
		player.setName(playerAttributes.getNamedItem("playerName").getNodeValue());
		player.setLocation(getRoom(playerAttributes.getNamedItem("playerRoom").getNodeValue(),game.rooms));
		player.setDirection(Room.Direction.valueOf(playerAttributes.getNamedItem("playerDir").getNodeValue()));

		player.setInventory(loadItems(playerNode.getFirstChild(),game));

		return player;
	}

	/**
	 * Load a list of rooms from a list of roomNodes
	 * @param roomsNode
	 * @param game
	 * @return
	 */
	private static List<Room> loadRooms(NodeList roomsNode, GameState game){
		List<Room> updatedRooms = new ArrayList<Room>();

		for(int i = 0; i < roomsNode.getLength(); i++){
			Node roomNode = roomsNode.item(i);
			Room room = getRoom(roomNode.getAttributes().getNamedItem("roomName").getNodeValue(),game.rooms);

			ArrayList<Item> roomItems = new ArrayList<Item>();
			roomItems = loadItems(roomNode.getFirstChild(),game);
			room.setItems(roomItems);

			updatedRooms.add(room);

		}
		assert game.rooms.size() == updatedRooms.size();
		return updatedRooms;
	}

	/**
	 * Get the Room object the player is in from the string in the player node
	 * @param roomName from the player node
	 * @param list of all rooms in the game
	 * @return
	 */
	private static Room getRoom(String roomName, List<Room> rooms){
		for(Room r : rooms){
			if(r.getName().equals(roomName)){
				return r;
			}
		}

		return null;
	}

	/**
	 * Load a list of items from a list of itemNodes
	 * @param itemListNode
	 * @param game
	 * @return
	 */
	private static ArrayList<Item> loadItems(Node itemListNode, GameState game){

		ArrayList<Item> itemsInNode = new ArrayList<Item>();
		//Create a list of all Item Nodes
		NodeList itemNodes = itemListNode.getChildNodes();
		//Iterate across each item
		for(int i = 0; i < itemNodes.getLength(); i++){
			Node itemNode = itemNodes.item(i);

			//Find the item object that the node is referring to in order to get the unchangeable data
			for(Item gameItem : game.items){
				if(Integer.parseInt(itemNode.getAttributes().getNamedItem("itemID").getNodeValue()) == (gameItem.getId())){
					String coordStr = itemNode.getAttributes().getNamedItem("itemLoc").getNodeValue();
					gameItem.setLocation(loadCoordinate(coordStr));

					//If it's a door see if it's locked or not
					if(gameItem.getClass() == Door.class){
						Door door = (Door) gameItem;
						if(itemNode.getAttributes().getNamedItem("doorLocked").getNodeValue().equals("true")){
							door.setLocked(true);
						}else{
							door.setLocked(false);
						}
						gameItem = door;
					}
					//If it's mobile set it's destination in case its changed
					else if(gameItem.getClass() == Mobile.class){
						Mobile mobile = (Mobile) gameItem;
						mobile.setDestination(loadCoordinate(itemNode.getAttributes().getNamedItem("mobileDest").getNodeValue()));
						gameItem = mobile;
					}
					//If it's a container load all its items
					else if(gameItem.getClass() == Container.class){
						Container cont = (Container) gameItem;
						cont.setInventory(loadItems(itemNode.getFirstChild(),game));
						gameItem = cont;
					}
					itemsInNode.add(gameItem);
				}
			}
		}
		return itemsInNode;
	}

	/**
	 * Create a coordinate object from a string in the format xx,yy,zz
	 * @param coordStr
	 * @return
	 */
	private static coordinate loadCoordinate(String coordStr){
		//I know this is terrible
		//Sorry
		String xStr;
		String yStr;
		String zStr;
		xStr = coordStr.substring(0, coordStr.indexOf(','));
		yStr = coordStr.substring(xStr.length()+1, coordStr.indexOf(',', xStr.length()+1));
		zStr = coordStr.substring(xStr.length()+yStr.length()+2);

		return new coordinate(Integer.parseInt(xStr),Integer.parseInt(yStr),Integer.parseInt(zStr));
	}


}

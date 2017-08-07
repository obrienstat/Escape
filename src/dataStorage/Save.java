package dataStorage;
import org.w3c.dom.*;

import world.*;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;


/**
 * Class that handles the saving of all game data
 *
 * @author Tom Kennedy 300326440
 */
public class Save {

	//For testing only
	public static void main(String[] args){
		saveGame("save", new GameState("TestOne","TestTwo"));
	}

	/**
	 * Saves the GameState in XML format
	 * @author Tom Kennedy
	 * @param fileName
	 * @param game
	 * @return true if saved without errors, otherwise false
	 */
	public static boolean saveGame(String fileName, GameState game){

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document saveFile = dBuilder.newDocument();

			//Root node
			Element root = saveFile.createElement("game");
			saveFile.appendChild(root);

			//Player data
			savePlayer(saveFile, root, game.player1);
			savePlayer(saveFile, root, game.player2);

			//Room data
			Element rooms = saveFile.createElement("rooms");
			for(Room r : game.rooms){
				saveRoom(saveFile, rooms, r);
			}
			root.appendChild(rooms);

			//Write content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(saveFile);
			StreamResult result = new StreamResult(new File(fileName + ".xml"));
			transformer.transform(source, result);

			//StreamResult consoleResult = new StreamResult(System.out);
	        //transformer.transform(source, consoleResult);

	        //Return true if it reaches this far without errors
			return true;

		} catch (Exception e){
			e.printStackTrace();
			//Something has gone wrong, return false
			return false;
		}
	}

	/**
	 * Save the player object
	 * @param saveFile
	 * @param root
	 * @param player
	 */
	private static void savePlayer(Document saveFile, Element root, Avatar player){
		Element elemPlayer = saveFile.createElement("player");

		Attr attrPlayerName = saveFile.createAttribute("playerName");
		attrPlayerName.setValue(player.getName());
		elemPlayer.setAttributeNode(attrPlayerName);

		Attr attrPlayerRoom = saveFile.createAttribute("playerRoom");
		attrPlayerRoom.setValue(player.getLocation().getName());
		elemPlayer.setAttributeNode(attrPlayerRoom);

		Attr attrPlayerDir = saveFile.createAttribute("playerDir");
		attrPlayerDir.setValue(player.getDirection().toString());
		elemPlayer.setAttributeNode(attrPlayerDir);

		Element elemInventory = saveFile.createElement("inventory");
		for (Item item : player.getInventory()){
			saveItem(saveFile,elemInventory,item);
		}
		elemPlayer.appendChild(elemInventory);

		root.appendChild(elemPlayer);
	}

	/**
	 * Save an item to the parent node
	 * @param saveFile
	 * @param parentNode
	 * @param item
	 */
	private static void saveItem(Document saveFile, Element parentNode, Item item){
		Element elemItem = saveFile.createElement("item");

		//Name so the save file is readable
		Attr attrItemName = saveFile.createAttribute("itemName");
		attrItemName.setValue(item.getName());
		elemItem.setAttributeNode(attrItemName);

		//ID for loading items safely
		Attr attrItemId = saveFile.createAttribute("itemID");
		attrItemId.setValue(String.valueOf(item.getId()));
		elemItem.setAttributeNode(attrItemId);

		//Save the location
		Attr attrItemLoc = saveFile.createAttribute("itemLoc");
		attrItemLoc.setValue(item.getLocation().toString());
		elemItem.setAttributeNode(attrItemLoc);

		//If it's a door save its locked status
		if(item.getClass() == Door.class){
			Door door = (Door) item;
			Attr attrDoorLocked = saveFile.createAttribute("doorLocked");
			if(door.isLocked()){
				attrDoorLocked.setValue("true");
			}else{
				attrDoorLocked.setValue("false");
			}
			elemItem.setAttributeNode(attrDoorLocked);
		}
		//If it's mobile save the destination
		else if(item.getClass() == Mobile.class){
			Mobile mobile = (Mobile) item;
			Attr attrMobileDest = saveFile.createAttribute("mobileDest");
			attrMobileDest.setValue(mobile.getDestination().toString());
			elemItem.setAttributeNode(attrMobileDest);
		}
		//If it's a container save the items it contains
		else if(item.getClass() == Container.class){
			Container cont = (Container) item;
			Element elemContItems = saveFile.createElement("contItems");
			for(Item it : cont.getInventory()){
				saveItem(saveFile,elemContItems,it);
			}
			elemItem.appendChild(elemContItems);
		}

		parentNode.appendChild(elemItem);

	}

	/**
	 * Save a room
	 * @param saveFile
	 * @param parentNode
	 * @param room
	 */
	private static void saveRoom(Document saveFile, Element parentNode, Room room){
		Element elemRoom = saveFile.createElement("room");

		Attr attrRoomName = saveFile.createAttribute("roomName");
		attrRoomName.setValue(room.getName());
		elemRoom.setAttributeNode(attrRoomName);

		Element elemRoomItems = saveFile.createElement("roomItems");
		for(Item it : room.getItems()){
			saveItem(saveFile,elemRoomItems,it);
		}
		elemRoom.appendChild(elemRoomItems);

		parentNode.appendChild(elemRoom);
	}


}

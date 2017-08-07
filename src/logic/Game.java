package logic;
import world.*;
import world.Room.Direction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import dataStorage.Load;
import dataStorage.Save;
/**
 *Logic for game, controls interactions between the Player and the Game World.
 * @author Ashton A
 *
 */
public class Game {

	private final int PLAYER_UID_START = 36482;

	private List<Avatar> avatar = new ArrayList<Avatar>();
	private List<Room> rooms;
	private int index = 0;

	private int playerUid = PLAYER_UID_START;

	// the players information
	private Avatar playerOne;
	private Avatar playerTwo;
	private Item playerOneSprite;
	private Item playerTwoSprite;

	private GameState gameState;


	/**
	 * Create a new instance of game taking info from gameState.
	 */
	public Game(){
		this.gameState = new GameState("","");
		this.rooms = gameState.rooms;

		this.avatar.add(gameState.player1);
		this.avatar.add(gameState.player2);
//		playerOne = gameState.player1;
//		playerTwo = gameState.player2;

		// now create

	}

	/**
	 * Creates the game from the game state class, is flexible with number of players.
	 */
	public Game( int players){
		this.gameState = new GameState("","");
		this.rooms = gameState.rooms;
		this.avatar.add(gameState.player1);
		if(players>1)this.avatar.add(gameState.player2);
		this.avatar.add(gameState.player2);
	}




	/**
	 * Player walks through the current door to the next room
	 */
	public synchronized boolean changeRoom(Avatar player, int id){

		//Locate the door
		Door door =	(Door) findItemRoom(player,id);

		if (door == null) return false;

		// check if door is already unlocked
		if (!door.isLocked()){
			// feel like the way doors work could be improved but its not critical to fix.
			player.getLocation().getAvatars().remove(player);
			player.setLocation(door.getRoomTo());
			return true;
		}
		// attempt to unlock the door
		if(door.unlock(player.getInventory())){
			player.getLocation().getAvatars().remove(player);
			player.setLocation(door.getRoomTo());
			return true;
		}
		//print failure message if its not working
		return false;
	}

	public int getPlayerNumber(){
		int num = playerUid;
		playerUid++;
		return num;
	}

	private Avatar getOtherAvatar(int playerUid){
		return this.playerUid == playerUid ? playerOne : playerTwo;
	}

	/**
	 * Get the other players avatar
	 * @param a
	 */
	public synchronized Avatar getPlayer(){
		if (index >= this.avatar.size())
			return null;
		return this.avatar.get(index++);
	}

	public boolean isConnected(){
		return this.index == 2;
	}

	/**
	 * Turn Avatar 90 degrees to the right in game.
	 * @param player
	 */

	public synchronized void turnRight(Avatar player, int playerUid){
		player.right();
	}

	/**
	 * Turn Avatar 90 degrees to the left in game
	 * @param player
	 * @param playerUid2
	 */

	public synchronized void turnLeft(Avatar player, int playerUid2){
		player.left();
	}




	/**
	 * Adds current item to the inventory list
	 * @param a
	 * @param itemName
	 */
	public synchronized void pickUpItem(Avatar a, int id){
		if (a.getInventory().size() == 6)
			return;

		Item item = findItemRoom(a,id);
		if(item != null){

			// it was still in the room
			if(item instanceof Portable || item instanceof Key){
			a.addItem(item);
			a.getLocation().removeItem(item);
			}
		}
	}

	/**
	 * Remove item from Avatars' inventory if it belongs to that Avatar
	 * @param a
	 * @param image
	 */
	public synchronized void drop(Avatar a,	int id){
		// remove item from the inventory
		Item item = findItemInventory(a, id);
		a.removeItem(item);

		//add item to the room
		a.getLocation().addItem(item);
		//place item in middle of room
		item.setLocation(new coordinate(0,0,0));
	}

	/**
	 * Saves the current State of the game to a file
	 */
	public void saveGameToState(){
		gameState.player1 = this.avatar.get(0);
		gameState.player2 = this.avatar.get(1);
		gameState.rooms = this.rooms;
		Save.saveGame("stateSaving", gameState);
	}

	/**
	 * Loads a previous state of the game from a file
	 */
	public void loadSavedGame(){

		gameState = Load.loadGame("stateSaving");
		this.rooms.clear();
		this.avatar.clear();
		this.index = 0;
		this.rooms = gameState.rooms;
		this.avatar.add(gameState.player1);
		this.avatar.add(gameState.player2);

	}




	// Container specific methods
	/**
	 * Move item to a new location at fixed predetermined coordinate
	 * @param i
	 */
	public void move(Avatar a, int id){
		Item i = findItemRoom(a,id);
		if(i instanceof Mobile){
			((Mobile) i).move();
			//update coordinates
		}


	}
	/**
	 *Remove every item from the Container and place them in the Avatar inventory.
	 *Avatar must have inventory space otherwise items will not be taken.
	 * @param a
	 * @param id
	 */
	public void takeAll(Avatar a, int id){
		//id = id of container
		Item box = findItemRoom(a, id);

		List<Item> items = ((Container) box).getInventory();
		if(a.addItemAll(items))((Container) box).removeAll();
	}





	public void throughWinDoor(Avatar a, WinDoor door){
	if(door.isLocked()){
		if(door.unlock(a.getInventory())){
			a.setLocation(door.getRoomTo());
			
			}
		}
	}

	/**
	 * Move Avatar through a door to the adjacent room on the other side.
	 * @param avatar
	 * @param (door) item
	 */
	public void throughDoor(Avatar a, Door door){
		if(door.isLocked()){// do not combine the conditionals otherwise it will always print the messages in door.unlock()
			if(door.unlock(a.getInventory())){
				a.setLocation(door.getRoomTo());
				return;
			}
		}
	}



	public List<Room> getRooms(){
		return rooms;
	}

	public void setRooms(List<Room> rooms){
		this.rooms=rooms;
	}



	public List<Avatar> getAvatars(){
		return avatar;
	}


	public void setAvatar(List<Avatar> avatar){
		this.avatar=avatar;
	}



	public static byte[] serialize(Object obj) throws IOException {

		try (ByteArrayOutputStream bout = new ByteArrayOutputStream()){
			try (ObjectOutputStream out = new ObjectOutputStream(bout)){
				out.writeObject(obj);
			}
			return bout.toByteArray();
		}

	}

	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try(ByteArrayInputStream bin = new ByteArrayInputStream(bytes)){
            try(ObjectInputStream in = new ObjectInputStream(bin)){
                return in.readObject();
            }
        }
    }


	/**
	 * Search Avatars inventory for the item with a matching id
	 * @param a
	 * @param id
	 * @return
	 */
	private Item findItemInventory(Avatar a, int id){
		for(Item item : a.getInventory()){
			if(item.getId()== id)return item;
		}
		return null;
	}


	/**
	 * Search the Avatars Location for the Item with a matching id.
	 * @param a
	 * @param id
	 * @return
	 */
private Item findItemRoom(Avatar a, int id){
		for(Item item : a.getLocation().getItems()){
			if(item.getId() == id) return item;
		}
		return null;
	}





}


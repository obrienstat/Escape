package world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;
import world.Room.Direction;


/**
 * Class that holds all the game data
 *
 * @author Tom Kennedy 300326440
 *
 */
public class GameState {
	public Avatar player1;
	public Avatar player2;
	public List<Room> rooms;
	//All items and doors created have to be added to items as well as wherever they belong
	public List<Item> items;

	public GameState(String player1Name, String player2Name){
		//Initialise fields
		items = new ArrayList<Item>();
		rooms = new ArrayList<Room>();
		int id = 1000;

		//Create rooms
		Room spawnRoom = new Room("spawn");
		Room nRoom = new Room("nRoom");
		Room wRoom = new Room("wRoom");
		Room eRoom = new Room("eRoom");
		Room sRoom = new Room("sRoom");
		Room nwRoom = new Room("nwRoom");
		Room neRoom = new Room("neRoom");
		Room swRoom = new Room("swRoom");
		Room seRoom = new Room("seRoom");

		//Create all items
		//Spawn Room Items
		Item bookShelf1 = new Immobile("Bookshelf1","A bookshelf", id, new coordinate(380,180,450),null,180,"src/files/wood1.jpg","src/files/wood.jpg","src/files/Bookshelf.obj",Color.BURLYWOOD);
		spawnRoom.addItem(bookShelf1);
		id++;

		Item bookShelf3 = new Immobile("Bookshelf3","A bookshelf", id, new coordinate(380,180,-450),null,0,"src/files/wood1.jpg","src/files/wood.jpg","src/files/Bookshelf.obj",Color.BURLYWOOD);
		spawnRoom.addItem(bookShelf3);
		id++;

		Item map = new Portable("Map","A map of the area", id, new coordinate(380,90,450),"src/files/map_inventory2.gif",90,"src/files/wood1.jpg","src/files/wood.jpg","src/files/map.obj",Color.AZURE);
		spawnRoom.addItem(map);
		id++;

		//West Room Items
		Item bookShelf2 = new Immobile("Bookshelf2","A bookshelf", id, new coordinate(-380,180,450),null,180,"src/files/wood1.jpg","src/files/wood.jpg","src/files/Bookshelf.obj",Color.BURLYWOOD);
		wRoom.addItem(bookShelf2);
		id++;

		Item key2 = new Key("KeyTwo","Another key!", id, new coordinate(-380,88,450),"src/files/key_inventory.gif",0,2,"src/files/wood1.jpg","src/files/wood1.jpg","src/files/key.obj",Color.YELLOW);
		wRoom.addItem(key2);
		id++;



		//North Room Items
		Item coin3 = new Coin("Coin3",id, new coordinate(-350,290,-445),0,"scr/files/wood1.jpg","src/files/wood.jpg","src/files/coin.obj", Color.GOLD);
		nRoom.addItem(coin3);
		id++;

		Item table = new Immobile("Table1", "A square Table", id, new coordinate(-320,120,-320),null, 0, "scr/files/wood1.jpg", "src/files/wood", "src/files/table.obj", Color.CHOCOLATE);
		nRoom.addItem(table);
		id++;

		Item chair = new Immobile("Chair1", "A chair", id, new coordinate(-300,100,-320),null, 0, "scr/files/wood1.jpg", "src/files/wood", "src/files/chair.obj", Color.CHOCOLATE);
		nRoom.addItem(chair);
		id++;

		Item compass = new Portable("Compass","A handy compass", id, new coordinate(-280,100,-280),"src/files/compass_inventory.gif",90,"src/files/wood1.jpg","src/files/wood.jpg","src/files/compass.obj",Color.AZURE);
		nRoom.addItem(compass);
		id++;

		//NorthEast Room Items
		Item key3 = new Key("KeyThree","And another key!", id, new coordinate(-250,285,445),"src/files/key_inventory.gif",110,3,"src/files/wood1.jpg","src/files/wood1.jpg","src/files/key.obj",Color.YELLOW);
		neRoom.addItem(key3);
		id++;

		Item pictureFrame2 = new Immobile("pictureFrame2", "I can't make out the details", id, new coordinate(450,0,0),null, 270, "scr/files/wood1.jpg", "src/files/wood.jpg", "src/files/picture_frame.obj", Color.CHOCOLATE);
		neRoom.addItem(pictureFrame2);
		id++;

		Item pictureFrame3 = new Immobile("pictureFrame3", "I can't make out the details", id, new coordinate(0,0,450),null, 180, "scr/files/wood1.jpg", "src/files/wood.jpg", "src/files/picture_frame.obj", Color.CHOCOLATE);
		neRoom.addItem(pictureFrame3);
		id++;

		Item bookShelf4 = new Immobile("Bookshelf4","A bookshelf", id, new coordinate(-380,180,450),null,180,"src/files/wood1.jpg","src/files/wood.jpg","src/files/Bookshelf.obj",Color.BURLYWOOD);
		neRoom.addItem(bookShelf4);
		id++;

		//South Room Items
		Item couch1 = new Immobile("Couch1", "Looks comfy", id, new coordinate(0,120,0),null, 180, "scr/files/fabric.jpg", "src/files/fabric1.jpg", "src/files/couch.obj", Color.CHOCOLATE);
		sRoom.addItem(couch1);
		id++;

		//East Room Items
		Item pictureFrame1 = new Immobile("pictureFrame1", "I can't make out the details", id, new coordinate(450,0,0),null, 270, "scr/files/wood1.jpg", "src/files/wood.jpg", "src/files/picture_frame.obj", Color.CHOCOLATE);
		eRoom.addItem(pictureFrame1);
		id++;

		//SouthEast Room Items

		Item coin2 = new Coin("Coin2",id, new coordinate(-350,290,445),0,"scr/files/wood1.jpg","src/files/wood.jpg","src/files/coin.obj", Color.GOLD);
		seRoom.addItem(coin2);
		id++;

		Item key1 = new Key("KeyOne","Looks like it would fit a door", id, new coordinate(-10,170,-370),"src/files/key_inventory.gif",0,1,"src/files/wood1.jpg","src/files/wood1.jpg","src/files/key.obj",Color.YELLOW);
		seRoom.addItem(key1);
		id++;

		Item chair2 = new Immobile("Chair2", "A chair", id, new coordinate(-300,100,-550),null, 180, "scr/files/wood1.jpg", "src/files/wood.jpg", "src/files/chair.obj", Color.CHOCOLATE);
		seRoom.addItem(chair2);
		id++;

		Item chair3 = new Immobile("Chair3", "A chair", id, new coordinate(0,100,-550),null, 180, "scr/files/wood1.jpg", "src/files/wood.jpg", "src/files/chair.obj", Color.CHOCOLATE);
		seRoom.addItem(chair3);
		id++;

		Item chair4 = new Immobile("Chair4", "A chair", id, new coordinate(300,100,-550),null, 180, "scr/files/wood1.jpg", "src/files/wood.jpg", "src/files/chair.obj", Color.CHOCOLATE);
		seRoom.addItem(chair4);
		id++;

		//SouthWest Room Items
		Item coin1 = new Coin("Coin1",id, new coordinate(-350,290,445),0,"scr/files/wood1.jpg","src/files/wood.jpg","src/files/coin.obj", Color.GOLD);
		swRoom.addItem(coin1);
		id++;

		//Create doors
		//Spawn Room Doors
		coordinate coOrd = new coordinate(0,-50,450);
		Item spwnToN = new Door("spwnToN", "The North Door", id, coOrd, null, 180, false, 0, nRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		spawnRoom.addItem(spwnToN);
		id++;

		coOrd = new coordinate(0,-50,-450);
		Item spwnToS = new Door("spwnToS", "The South Door", id, coOrd, null, 0, false, 0, sRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		spawnRoom.addItem(spwnToS);
		id++;

		coOrd = new coordinate(450,-50,0);
		Item spwnToE = new Door("spwnToE", "The East Door", id, coOrd, null, 270, false, 0, eRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		spawnRoom.addItem(spwnToE);
		id++;

		//North Room Doors
		coOrd = new coordinate(0,-50,-450);
		Item nToSpwn = new Door("nToSpwn", "The South Door", id, coOrd, null, 0, false, 0, spawnRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		nRoom.addItem(nToSpwn);
		id++;

		coOrd = new coordinate(450,-50,0);
		Item nToNE = new Door("nToNE", "The East Door", id, coOrd, null, 270, true, 2, neRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		nRoom.addItem(nToNE);
		id++;

		//East Room Doors
		coOrd = new coordinate(-450,-50,0);
		Item eToSpwn = new Door("eToSpwn", "The West Door", id, coOrd, null, 90, false, 0, spawnRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		eRoom.addItem(eToSpwn);
		id++;

		coOrd = new coordinate(0,-50,-450);
		Item eToSE = new Door("eToSE", "The South Door", id, coOrd, null, 0, false, 0, seRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		eRoom.addItem(eToSE);
		id++;

		//West Room Doors
		coOrd = new coordinate(0,-50,-450);
		Item wToSW = new Door("wToSW", "The South Door", id, coOrd, null, 0, true, 1, swRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		wRoom.addItem(wToSW);
		id++;

		coOrd = new coordinate(0,-50,450);
		Item wToNW = new Door("wToNW", "The North Door", id, coOrd, null, 180, true, 3, nwRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		wRoom.addItem(wToNW);
		id++;

		//South Room Doors
		coOrd = new coordinate(0,-50,450);
		Item sToSpwn = new Door("sToSpwn", "The North Door", id, coOrd, null, 180, false, 0, spawnRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		sRoom.addItem(sToSpwn);
		id++;

		coOrd = new coordinate(-450,-50,0);
		Item sToSW = new Door("sToSW", "The West Door", id, coOrd, null, 90, false, 0, swRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		sRoom.addItem(sToSW);
		id++;

		coOrd = new coordinate(450,-50,0);
		Item sToSE = new Door("sToSE", "The East Door", id, coOrd, null, 270, false, 0, seRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		sRoom.addItem(sToSE);
		id++;

		//NorthEast Room Doors
		coOrd = new coordinate(-450,-50,0);
		Item neToN = new Door("neToN", "The West Door", id, coOrd, null, 90, true, 2, nRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		neRoom.addItem(neToN);
		id++;

		//SouthEast Room Doors
		coOrd = new coordinate(0,-50,450);
		Item seToE = new Door("seToE", "The North Door", id, coOrd, null, 180, false, 0, eRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		seRoom.addItem(seToE);
		id++;

		coOrd = new coordinate(-450,-50,0);
		Item seToS = new Door("seToS", "The West Door", id, coOrd, null, 90, false, 0, sRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		seRoom.addItem(seToS);
		id++;

		//SouthWest Room Doors
		coOrd = new coordinate(450,-50,0);
		Item swToS = new Door("swToSE", "The East Door", id, coOrd, null, 270, false, 0, sRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		swRoom.addItem(swToS);
		id++;

		coOrd = new coordinate(0,-50,450);
		Item swToW = new Door("swToW", "The North Door", id, coOrd, null, 180, true, 1, wRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.BROWN);
		swRoom.addItem(swToW);
		id++;

		//NorthWest Room Doors
		coOrd = new coordinate(0,-50,-450);
		Item nwToW = new WinDoor("nwToW", "The South Door", id, coOrd, null, 0, 3, wRoom, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.GOLD, 3);
		nwRoom.addItem(nwToW);
		id++;

		//Add rooms to room list
		rooms.add(spawnRoom);
		rooms.add(nRoom);
		rooms.add(wRoom);
		rooms.add(eRoom);
		rooms.add(sRoom);
		rooms.add(neRoom);
		rooms.add(nwRoom);
		rooms.add(swRoom);
		rooms.add(seRoom);

		//Add all items in rooms to items list
		for(Room r : rooms){
			items.addAll(r.getItems());
		}

		//Create players
		player1 = new Avatar(player1Name,4431,spawnRoom,Direction.NORTH);
		player2 = new Avatar(player2Name,4413,spawnRoom,Direction.NORTH);

		//Place players in rooms
		spawnRoom.addAvatar(player1);
		spawnRoom.addAvatar(player2);

		//Add the player inventory and keys to items
		items.addAll(player1.getInventory());
		items.addAll(player2.getInventory());

	}

}

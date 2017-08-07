package junit_testing;
import world.*;

import world.Room.Direction;
import logic.Game;

import java.util.*;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import dataStorage.Load;
import dataStorage.Save;
import javafx.scene.paint.Color;

public class Tests {

	@Test
	public void test_00_add_Item_Room(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Item pencil = new Portable("Pencil", "A small HB pencil", 500, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(pencil);
		//does room contain item?
		List<Item> items = r.getItems();
		assert(items.get(items.size()-1).equals(pencil));
	}



	@Test
	public void test_01_pickUp_pencil(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		Item pencil = new Portable("Pencil", "A small HB pencil", 500, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(pencil);

		game.pickUpItem(tester, 500);
		//does avatar have item?
		assert(tester.getInventory().get(0).equals(pencil));
		//is room empty now?
		List<Item> items = r.getItems();
		assert(!items.get(items.size()-1).equals(pencil));

	}

	//pickup multiple items
	@Test
	public void test_02_pickUp_Multiple(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		Item pencil = new Portable("Pencil", "A small HB pencil", 500, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		Item book = new Portable("Book", "A big book", 501, new coordinate(0,0,0)," book",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(pencil);
		r.addItem(book);

		game.pickUpItem(tester, 500);
		game.pickUpItem(tester, 501);
		//are items in his inventory
		assert(tester.getInventory().containsAll(Arrays.asList(new Item[]{pencil,book})));
		//are items gone from the room
		assert(!r.getItems().containsAll(Arrays.asList(new Item[]{pencil,book})));
	}

	@Test
	public void test_03_inventory_limit(){
		Game game= new Game();

		Avatar tester = game.getAvatars().get(0);
		List<Item> items  = new ArrayList<Item>();
		//add 6 items to the game
		items.add(new Portable("Pencil", "A small HB pencil", 500, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		items.add(new Portable("Pencil", "A small HB pencil", 501, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		items.add(new Portable("Pencil", "A small HB pencil", 502, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		items.add(new Portable("Pencil", "A small HB pencil", 503, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		items.add(new Portable("Pencil", "A small HB pencil", 504, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		items.add(new Portable("Pencil", "A small HB pencil", 505, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		tester.addItemAll(items);
		//attempt to add item over inventory limit
		tester.addItem(new Portable("Pencil", "A small HB pencil", 506, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		boolean exception=false;
		try{
			//should throw out of bounds exception
			tester.getInventory().get(6);
		}catch(Exception e){
			//correct exception thrown
			if(e instanceof IndexOutOfBoundsException)exception=true;
		}
		assert(exception);


	}
	@Test
	public void test_04_unlockDoor(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		tester.addItem(new Key("key", "key", 500, new coordinate(0,0,0), null,0, 5,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		tester.addItem(new Key("key", "key", 501, new coordinate(0,0,0), null,0, 4,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		Door d = new Door("door", "door", 502, new coordinate(0,0,0), null,0,true, 5,r,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(d);

		game.throughDoor(tester, d);
		assert(tester.getLocation().equals(d.getRoomTo()));
	}

	@Test
	public void test_05_unlockDoor_fail(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		tester.addItem(new Key("key", "key", 500, new coordinate(0,0,0), null,0, 5,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		tester.addItem(new Key("key", "key", 501, new coordinate(0,0,0), null,0, 4,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		Door d = new Door("door", "door", 502, new coordinate(0,0,0), null,0,true, 6,r,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(d);

		game.throughDoor(tester, d);
		assert(tester.getLocation().equals(r));

	}


	@Test
	public void test_06_pickUp_fail(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		Item brick = new Immobile("Pencil", "A small HB pencil", 500, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(brick);
		game.pickUpItem(tester, 500);
		List<Item> items = r.getItems();
		//does Avatar have item?
		try{
			tester.getInventory().get(0);
			assert(false);
		}catch(Exception e){
			assert(true);
			assert(items.get(items.size()-1).equals(brick));
		}


	}


	@Test
	public void test_08_drop(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		Item pencil =new Portable("Pencil", "A small HB pencil", 506, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		tester.addItem(pencil);
		game.drop(tester, 506);

		assert(tester.getInventory().isEmpty());
		assert(r.getItems().contains(pencil));
	}

	@Test
	public void test_10_move_latch(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);

		Item latch = new Mobile("latch", "a steel handle",0, new coordinate(0,0,0) ,new coordinate(0,0,1), "latch",180,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(latch);
		game.move(tester, 0);

		assert(latch.getLocation().getZ()==1);
		assert(((Mobile) latch).getDestination().getZ()==0);

	}

	@Test
	public void test_11_take_all_container(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);

		Container box = new Container("box", "Carxboard", 0, new coordinate(0,0,0), "b", 180, new ArrayList<Item>(),2,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		box.insert(new Key("key", "key", 500, new coordinate(0,0,0), null,0, 5,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		box.insert(new Key("key", "key", 501, new coordinate(0,0,0), null,0, 5,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		r.addItem(box);
		game.takeAll(tester, 0);
		assert(box.getInventory().isEmpty());
		assert(tester.getInventory().get(0).getId()==500);
		/*
		Container box = new Container("Box", "Cardboard Box", new coordinate(0,0,0), "box", 180,new ArrayList<Item>(), 2,"x","x","x", null);
		box.insert(new Portable("Pencil", "", null, "pencil",180,"x","x","x", null));
		box.insert(new Portable("Pen", "", null, "pen",180,"x","x","x", null));
		r.addItem(box, Direction.NORTH);
		game.takeAll(tester,"box");
		assert(box.getInventory().isEmpty());
		Item i = tester.getInventory().get(0);
		assert(i.getName().equals("Pencil"));
		*/
	}


	//Cannot add items to box over its capacity
	@Test
	public void test_11a_box_capacity(){
		Container box = new Container("box", "Carxboard", 0, new coordinate(0,0,0), "b", 180, new ArrayList<Item>(),0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		box.insert(new Key("key", "key", 500, new coordinate(0,0,0), null,0, 5,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		assert(box.getInventory().isEmpty());
	}

	//Failed attempt to pay NPC
	@Test
	public void test_12_insufficient_funds_NPC(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);

		NPC man = new NPC("Man", "", 0, new coordinate(0,0,0), null, 0, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED, 2, null);
		r.addItem(man);
		man.pay(tester.getInventory());
		assert(man.getFee()==2);
	}


	//NPC is payed correct amount
	@Test
	public void test_13_pay_NPC(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		tester.addItem(new Coin("Coin", 500, new coordinate(0,0,0), 0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));

		NPC man = new NPC("Man", "", 0, new coordinate(0,0,0), null, 0, "src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED, 1, null);

		r.addItem(man);

		man.pay(tester.getInventory());
		assert(man.getFee()==0);
	}


	//conversation between characters return right results
	@Test
	public void test_15_dialog(){

	}


	//one player drops it, other player picks it up.
	@Test
	public void test_16_trade(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester_a = game.getAvatars().get(0);
		Avatar tester_b = game.getAvatars().get(1);

		r.addItem(new Portable("Pencil", "A small HB pencil", 506, new coordinate(0,0,0), "pencil_1",0,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));

		game.pickUpItem(tester_a, 506);
		game.drop(tester_a, 506);
		game.pickUpItem(tester_b, 506);
		assert(tester_b.getInventory().get(0).getId()==506);
	}

	@Test
	public void test_17_discard_used_key(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		tester.addItem(new Key("key", "key", 500, new coordinate(0,0,0), null,0, 5,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));

		Door d = new Door("door", "door", 502, new coordinate(0,0,0), null,0,true, 5,r,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED);
		r.addItem(d);

		game.throughDoor(tester, d);
		assert(tester.getLocation().equals(d.getRoomTo()));
		assert(!tester.getInventory().isEmpty());
	}

	@Test
	public void test_18_WinDoor_success(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		WinDoor door = new WinDoor("door", "door", 502, new coordinate(0,0,0), null,0,5,r,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED, 1);
		r.addItem(door);
		tester.addItem(new Coin("coin", 0,new coordinate(0,0,0) ,180,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED));
		assert(door.unlock(null,tester.getInventory()));
		
	}

	@Test
	public void test_19_WinDoor_fail(){
		Game game= new Game();
		Room r = game.getRooms().get(0);
		Avatar tester = game.getAvatars().get(0);
		WinDoor door = new WinDoor("door", "door", 502, new coordinate(0,0,0), null,0,5,r,"src/files/wood.jpg", "src/files/wood.jpg",
				"src/files/door.obj", Color.RED, 1);
		r.addItem(door);
		assert(!door.unlock(tester.getInventory()));
	}

	@Test
	public void test_20_load_failsafe(){
		//When a load doesn't work it should instead return a new game
		//Create new game for comparison
		GameState newGame = new GameState("","");
		//Load a game from a broken xml file
		GameState brokenLoad = Load.loadGame("brokenSave");
		assert newGame.player1.getName().equals(brokenLoad.player1.getName());
		assert newGame.player2.getName().equals(brokenLoad.player2.getName());

	}

	@Test
	public void test_save_and_load_no_changes(){
		//Create a new GameState and save it and load it to check there are no differences
		GameState newGame = new GameState("One","Two");
		Save.saveGame("forTestingOnly", newGame);
		GameState loadedGame = Load.loadGame("forTestingOnly");
		assert loadedGame.player1.getName().equals("One");
		assert loadedGame.player2.getName().equals("Two");
		assert newGame.player1.getName().equals(loadedGame.player1.getName());
		assert newGame.player2.getDirection().equals(loadedGame.player2.getDirection());
		assert newGame.rooms.size()==loadedGame.rooms.size();
		for (int i = 0; i < newGame.rooms.size();i++){
			assert newGame.rooms.get(i).getItems().size() == loadedGame.rooms.get(i).getItems().size();
			for (int x = 0;x < newGame.rooms.get(i).getItems().size(); x++){
				Item newItem = newGame.rooms.get(i).getItems().get(x);
				Item loadedItem = loadedGame.rooms.get(i).getItems().get(x);
				assert newItem.getId()==loadedItem.getId();
				assert newItem.getName().equals(loadedItem.getName());
				assert newItem.getLocation().toString().equals(loadedItem.getLocation().toString());
			}
		}
		assert newGame.items.size()==loadedGame.items.size();
	}

	@Test
	public void test_load_with_changes(){
		//Create a new GameState and save it, then make changes to it and load it
		GameState newGame = new GameState("One","Two");
		//Save the game in its original state
		Save.saveGame("forTestingOnly", newGame);
		//Make changes to the game to test that load correctly reverts
		newGame.player1.setName("NotOne");
		newGame.player2.setDirection(Direction.EAST);
		newGame.player2.setLocation(newGame.rooms.get(5));
		newGame.rooms.get(0).setItems(new ArrayList<Item>());
		//Load the old state
		newGame = Load.loadGame("forTestingOnly");

		assert !newGame.player1.getName().equals("NotOne");
		assert !newGame.player2.getDirection().equals(Direction.EAST);
		assert !newGame.player2.getLocation().getName().equals(newGame.rooms.get(5).getName());
		assert !newGame.rooms.get(0).getItems().isEmpty();
	}

}

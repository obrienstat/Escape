package world;
import java.io.Serializable;
import java.util.*;

import javafx.scene.paint.Color;
import world.Room.Direction;
/**
 * Representation of player as an element of the game world
 * Contains their inventory, score and location.
 * @author Ashton
 *
 */
public class Avatar implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7412385362710563140L;

	private String name;
	private int uId;
	private Room location;
	private Room.Direction direction;
	private List<Item> inventory;

	// files for meshes
	public final String objFile = "src/files/man.obj";
	//public final Color color = Color.GREENYELLOW;


	public Avatar(String name, int uId,  Room spawn, Room.Direction direction){
		this.name=name;
		this.location=spawn;
		this.direction=direction;
		inventory = new ArrayList<Item>();
		this.uId = uId;
	}

	public int getUid(){
		return this.uId;
	}

	/**
	 * Pick up item and add to the appropriate inventory
	 * @param item
	 */
	public boolean addItem(Item item){
		if(inventory.size()>5)return false;
		this.inventory.add(item);
		return true;
	}

	public boolean addItemAll(List<Item> item){
		if(inventory.size()+item.size()>6)return false;
		this.inventory.addAll(item);
		return true;
	}

	public coordinate getCoordinate(){
		switch (direction){
		case NORTH:
			return new coordinate(0, -20, -350);
		case EAST:
			return new coordinate(-350, -20, 0);
		case SOUTH:
			return new coordinate(0, -20, 350);
		case WEST:
			return new coordinate(-350, -20, 0);
		default:
			return null; // dead code

		}
	}


	/**
	 * Returns the element at the specified position in the inventory
	 * @param item
	 */
	public void removeItem(Item item){
		inventory.remove(item);
	}

	/**
	 * Returns the List of Portable.
	 * @return
	 */
	public List<Item> getInventory(){
		return inventory;
	}

	/**
	 * Remove all Elements from inventory
	 */
	public void emptyInventory(){
		this.setInventory(new ArrayList<Item>());
	}

	/**
	 * Replace current inventory with new list.
	 * @param inventory
	 */
	public void setInventory (List<Item> inventory){
		this.inventory=inventory;
	}

	/**
	 * Return comma separated string of avatar name and names of items in inventory
	 * "name,item,item,item,.....item"
	 */
	@Override
	public String toString(){

		String avatar = name;
		for(Item i : inventory){
			avatar=avatar+","+i.getName();
		}
		return avatar;
	}

	/**
	 * Return the Avatars name.
	 * @return
	 */
	public String getName(){
		return name;
	}

	/**
	 * Change the Avatars' name
	 * @param new_name
	 */
	public void setName(String new_name){
		this.name = new_name;
	}

	/**
	 * Return the Room currently containing the Avatar.
	 * @return
	 */

	public Room getLocation(){
		return location;
	}

	/**
	 *
	 * @param new_location
	 */
	public void setLocation(Room new_location){
		location=new_location;
		this.location.getAvatars().add(this);
	}

	/**
 	* Set Avatars current direction 90 degrees to the left
 	*/
	public void left(){

			if(direction.equals(Direction.NORTH)) direction = Direction.WEST;
			else if(direction.equals(Direction.EAST))direction = Direction.NORTH;
			else if(direction.equals(Direction.SOUTH))direction = Direction.EAST;
			else if(direction.equals(Direction.WEST))direction = Direction.SOUTH;
	}

	/**
	 * Set Avatars current direction 90 degrees to the right.
	 */
	public void right(){
		if(direction.equals(Direction.NORTH))direction = Direction.EAST;
		else if(direction.equals(Direction.EAST))direction = Direction.SOUTH;
		else if(direction.equals(Direction.SOUTH))direction = Direction.WEST;
		else if(direction.equals(Direction.WEST))direction = Direction.NORTH;
	}

	/**
	 * return Avatars current direction.
	 * @return
	 */
	public Room.Direction getDirection(){
		return this.direction;
	}
	/**
	 * Set Avatars current direction.
	 * @param new_direction
	 */
	public void setDirection(Room.Direction new_direction){
		direction=new_direction;
	}


}
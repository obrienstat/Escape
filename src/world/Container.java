package world;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;



public class Container extends Immobile{

	private List<Item> inventory; 
	private int capacity = 1; //base capacity is 1

	
	/**
	 * Constuctor creates Container with a given capacity.
	 * @param name
	 * @param description
	 * @param id
	 * @param location
	 * @param image
	 * @param rotation
	 * @param inventory
	 * @param capacity
	 * @param bumpMap
	 * @param diffuseMap
	 * @param objFile
	 * @param color
	 */
	public Container(String name, String description, int id, coordinate location, String image,int rotation, ArrayList<Item> inventory, int capacity
			, String bumpMap, 	String diffuseMap, String objFile, Color color) {
		// TODO Auto-generated constructor stub
		super(name, description, id, location, image,rotation,bumpMap, diffuseMap,objFile,color);
		this.inventory=inventory;
		this.capacity=capacity;
	}
	
	
	/**
	 * Constructor that creates a container with the default capacity
	 * @param name
	 * @param description
	 * @param id
	 * @param location
	 * @param image
	 * @param rotation
	 * @param inventory
	 * @param bumpMap
	 * @param diffuseMap
	 * @param objFile
	 * @param color
	 */
	public Container(String name, String description, int id, coordinate location, String image,int rotation, ArrayList<Item> inventory
			, String bumpMap, 	String diffuseMap, String objFile, Color color) {
		
		super(name, description, id, location, image,rotation,bumpMap, diffuseMap,objFile,color);
		this.inventory=inventory;
		
	};

	/**
	 * return list of items held by container to game so player can see what is stored
	 * @return
	 */
	public List<Item> getInventory(){
		return inventory;
	}

	public void setInventory(List<Item> inventory){
		this.inventory=inventory;
	}

	public int getCapacity(){
		return capacity;
	}

	public void setCapacity(int capacity){
		this.capacity=capacity;
	}

	/**
	 * Add item to container
	 * @param item
	 * @return
	 */
	public boolean insert(Item item){
		if(inventory.size()<capacity){
			inventory.add(item);
			return true;
		}
		return false;
	}

	/**
	 * Remove an item from the container.
	 * @param i
	 */
	public void remove(Item i){
		inventory.remove(i);
	}
	/**
	 * Remove all items from the container.
	 */
	public void removeAll(){
		inventory = new ArrayList<Item>();

	}
	
}

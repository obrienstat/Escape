package world;
import java.io.Serializable;
import java.util.*;

/**
 *Building blocks of the Game world.Rooms hold items and puzzles and players. Movement between rooms are facilitated by Door objects/
 * @author Ashton
 *
 */
public class Room implements Serializable{

/**
	 *
	 */
	private static final long serialVersionUID = -2253026587039633634L;


	public enum Direction { NORTH, EAST, SOUTH, WEST};

	private List<Item> items = new ArrayList<Item>();
	private List<Avatar> avatars;
	String name;




	public Room(String name, List<Item> items){
				this.name = name;
				this.avatars=new ArrayList<Avatar>();
				this.items=items;
			}

	public Room(String name){
		this.name=name;
		this.avatars= new ArrayList<Avatar>();

	}

	public void setName(String name){
		this.name=name;
	}

	public String getName(){
		return name;
	}


	



	public void removeItem(Item i){
		items.remove(i);
	}

	public void addItem(Item i){
		items.add(i);
	}



	public List<Avatar> getAvatars(){
		return this.avatars;
	}

	public void setAvatars(List<Avatar> avatars){
		this.avatars=avatars;
	}

	public void addAvatar(Avatar a){
		avatars.add(a);
	}

	public void removeAvatar(Avatar a){
		avatars.remove(a);
	}

	public void removeAvatar(int a){
		avatars.remove(a);
	}

	public List<Item> getItems(){
		return this.items;
	}

	public void setItems(List<Item> items){
		this.items = items;
	}

	@Override
	public boolean equals(Object o){
		if(o instanceof Room){
			Room other = (Room)o;
			if (((Room) o).getName().equals(name)) return true;
		}
		return false;
	}

}

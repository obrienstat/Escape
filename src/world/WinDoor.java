package world;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import userInterface.AppWindow;
/**
 * Final in game item, unlocking this door signals the end of the game and triggers the win condition.
 * @author Ashton A
 *
 */
public class WinDoor extends Door{

	private AppWindow app;
	private int fee;

	public WinDoor(String name, String description, int id, coordinate location, String image, int rotation, int code, Room roomTo
			, String bumpMap, String diffuseMap, String objFile, Color color, int fee){
		super(name, description,id,location,image,rotation,true, code,roomTo,bumpMap,diffuseMap,objFile,color);
		this.fee=fee;
	}


	public int getFee(){
		return fee;
	}

	public void setFee(int fee){
		this.fee=fee;
	}

	/**
	 * Avatar attempts to pay required amount to unlock door and end the game.
	 * If payment contains an amount of coins greater than or equal to the fee , the remaining fee is set to zero and the coins are removed from Avatar.
	 * If not, there is no change to either NPC nor player.
	 * @param payment
	 */
	public boolean unlock(AppWindow app, List<Item> inventory){
		this.app = app;
		List<Item> payment = new ArrayList<Item>();
		for(Item i : inventory){
			if(i instanceof Coin)payment.add(i);
		}

		//if payment can match the fee
		if(payment.size()>=fee){
			//remove the payment
			for(int i=0; i<fee; i++){
				inventory.remove(payment.get(i));
				payment.remove(i);
			}
			//fee has been paid
			setFee(0);
			this.setLocked(false);
			app.fadeTransitionFinished();
			return true;
		}
		return false;
	}
}

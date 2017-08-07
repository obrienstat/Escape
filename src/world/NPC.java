package world;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import userInterface.AppWindow;

import java.util.*;

/**
 * NPC (Non Player Characters) are characters that have extended interactions with players. NPC's act as guardians of the content, setting
 * the player a challenge that they must complete successfully to progress through the game.
 * This challenge can be in the form of payment in coins, answering a series of questions.
 * @author abouanasht
 *
 */
public class NPC extends Immobile{

	private int fee;
	private Message dialog; // if player returns to interaction after leaving, it will resume where he left off



	public NPC(String name, String description, int id, coordinate location, String image, int rotation, String bumpMap,String diffuseMap,String objFile, Color color, int fee, Message dialog) {
		super(name, description, id, location,image,rotation,bumpMap,diffuseMap,objFile,color);
		this.fee=fee;
		this.dialog=dialog;

	}

	@Override
	public void rightClick(MouseEvent event) {
		getPopUpMenu().show(AppWindow.window, event.getScreenX(), event.getScreenY());
	};


	/**
	 * Message class allows for the easy implementation of small scale dialog trees in game. An instance of method is essentially a
	 * node in a linked list with references to multiple child nodes(responses). When a response is selected/returned to the NPC
	 * the next node is retrieved and the conversation flows.
	 * @author Ashton A
	 *
	 */
	public class Message{
		private String message;
		private List<Message> responses;

		public Message(String message, List<Message> responses){
			this.message=message;
			this.responses=responses;
		}


		public String getMessage(){
			return message;
		}

		public void setMessage(String message){
			this.message=message;
		}

		public List<Message> getResponses(){
			return this.responses;
		}

		public void setResponses(List<Message> responses){
			this.responses=responses;
		}
	}
	
	


	public int getFee(){
		return fee;
	}

	public void setFee(int fee){
		this.fee=fee;
	}
	
	/**
	 * Avatar attempts to pay required amount to satisfy NPC's constraints and proceed to next piece of game content.
	 * If payment contains an amount of coins greater than or equal to the fee , the remaining fee is set to zero and the coins are removed from Avatar.
	 * If not, there is no change to either NPC nor player.
	 * @param payment
	 */
	public boolean pay(List<Item> inventory){
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
			return true;
		}
		return false;
	}


	
	/**
	 * Player has chosen their reply, NPC then responds with appropriate message.
	 * @param message
	 * @return
	 */
	public Message response(Message message){
		//set the dialog to reply
		this.dialog=message.responses.get(0);
		//send the reply
		return dialog;
	}


	public Message getDialog(){
		return dialog;
	}



}

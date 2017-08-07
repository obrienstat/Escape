package world;

import javafx.scene.paint.Color;

/**
 * Coins are a currency collected for the sole purpose of unlocking new content. Puzzles and doors keep players from progressing
 * through the game,on top of those challenges some content is gated behind a pay wall. Players must find and collect Coin objects hidden through out
 * the game, when their collection is of large they can deposit the coins to unlock the next stage of content.
 * @author abouanasht
 *
 */
public class Coin extends Portable{




	public Coin(String name, int id, coordinate location,int rotation, String bumpMap, String diffuseMap, String objFile, Color color) {
		super(name, "A small gold coin", id, location, "src/files/coin_inventory.gif",rotation,  bumpMap, diffuseMap, objFile, color);

	}

	public Coin(String name,int id,coordinate location,int rotation, String bumpMap, String diffuseMap, String objFile, Color color,int value){
		super(name, "A small gold coin", id, location, "remove image", rotation, bumpMap,diffuseMap, objFile,color);

	}


}

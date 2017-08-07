package world;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Optional item that doesnt contribute to solving puzzles but reveals easter egg
 * or provides points
 */
public class Special extends Item{

	private int reward;

	public Special(String name, String description, int id, coordinate location, String image,int rotation, int reward, String bumpMap,
			String diffuseMap, String objFile, Color color) {
		super(name, id, description, location, image,rotation,bumpMap, diffuseMap,objFile,color);
		this.reward=reward;
	}


	@Override
	public void leftClick(){
		activate();
	}


	public int activate(){
		return reward;
	}

	public void setReward(int reward){
		this.reward=reward;
	}


	@Override
	public void rightClick(MouseEvent event) {
		// TODO Auto-generated method stub

	}

}
package world;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import userInterface.AppWindow;

public class Immobile extends Item{

	public Immobile(String name, String description, int id, coordinate location, String image, int rotation, String bumpMap,
			String diffuseMap, String objFile, Color color) {
		super(name, id, description,location,image,rotation, bumpMap, diffuseMap,objFile,color);
		// TODO Auto-generated constructor stub
	}

 

	public void leftClick(){}



	@Override
	public void rightClick(MouseEvent event) {
		getPopUpMenu().show(AppWindow.window, event.getScreenX(), event.getScreenY());
	};



}

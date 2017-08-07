package world;

import java.io.IOException;
import java.io.Serializable;

import Rendering.DrawObject;
import Rendering.GameMesh;
import Rendering.ModelLoader;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import userInterface.AppWindow;

public abstract class Item implements Serializable{



	/**
	 *
	 */
	private static final long serialVersionUID = -8562129539811113636L;

	private String name;
	private String description;
	private coordinate location;
	private String image;  //filename of the items image/graphic
	private int rotation;
	private String bumpMap;
	private String diffuseMap;
	private String objFile;
	private int id;
	private double[] color = new double[4];
	private GameMesh mesh; // the mesh that will get created for this object

	public Item(String name, int id, String description, coordinate location, String image, int rotation, String bumpMap,
			String diffuseMap, String objFile, Color color){

		this.name=name;
		this.id = id;
		this.description=description;
		this.location=location;
		this.image=image;
		this.rotation = rotation;
		this.bumpMap = bumpMap;
		this.diffuseMap = diffuseMap;
		this.objFile = objFile;


		this.color[0] = color.getRed();
		this.color[1] = color.getGreen();
		this.color[2] = color.getBlue();
		this.color[3] = color.getOpacity();
//		this.mesh = loadMesh(objFile, bumpMap, diffuseMap, location, rotation, color);
	}

	public ContextMenu getPopUpMenu(){
		final ContextMenu contextMenu = new ContextMenu();
		MenuItem descrip = new MenuItem(description);
		contextMenu.getItems().add(descrip);
		return contextMenu;
	}



	public abstract void leftClick();
	/**
	 * Open up an in game pop-up displaying item information 
	 * @param event
	 */
	public abstract void rightClick(MouseEvent event); 
	

	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}

	public void setName(String name){
		this.name=name;
	}

	public String getDescription(){
		return description;
	}

	public void setDescription(String description){
		this.description=description;
	}

	public coordinate getLocation(){
		return location;
	}

	public void setLocation(coordinate location){
		this.location=location;
	}

	public String getImage(){
		return image;
	}

	public void setImage(String image){
		this.image=image;
	}

	public int getRotation(){
		return rotation;
	}

	public void setRotation(int rotation){
		this.rotation=rotation;
	}

	public String getBumpMap(){
		return bumpMap;
	}

	public String getDiffuseMap(){
		return diffuseMap;
	}

	public String getObjFile(){
		return objFile;
	}

	public void setMesh(){
		this.mesh = loadMesh(objFile, bumpMap, diffuseMap, location, rotation, getColor());
	}
	public GameMesh getMesh(){
		return mesh;
	}


	public Color getColor(){
		return new Color(this.color[0], this.color[1], this.color[2], this.color[3]);
	}

	@Override
	/**
	 * returns banana
	 */
	public boolean equals(Object o){
		if(o instanceof Item){
			Item item = (Item)o;
			if (this.id == item.id)return true;
		}
		return false;
		}

	@Override
	public int hashCode(){
		return id;
	}

	/**
	 * Creates the GameMesh that represents this object in the game
	 * @param objFile the file that contains information for creating the mesh
	 * @param bumpMap the image which the bump map is created from
	 * @param diffuseMap the image which the diffuse map is created from
	 * @param loc the location of the item in the room
	 * @param rotation the number of degrees the item is rotated so that it is facing the correct direction
	 * @param color the color of the item
	 * @return The GameMesh object that will be used to draw the item in game
	 */
	private GameMesh loadMesh(String objFile, String bumpMap, String diffuseMap, coordinate loc, int rotation,  Color color){

		//loads in the model and creates traingle mesh
		ModelLoader ml = new ModelLoader();
		DrawObject dO = ml.loadModel(objFile);
		TriangleMesh tm = dO.getMesh();

		//creates the GameMesh and sets its location
		GameMesh gm = new GameMesh(this, tm);
		gm.setTranslateX(loc.getX());
		gm.setTranslateY(loc.getY());
		gm.setTranslateZ(loc.getZ());
		gm.setRotationAxis(Rotate.Y_AXIS);
		gm.setRotate(rotation);

		// creates the material and adds it to the mesh
		PhongMaterial mat = new PhongMaterial(color);
		Image bm = new Image("file:" + bumpMap);
		Image dm = new Image("file:" + diffuseMap);
		mat.setBumpMap(bm);
		mat.setDiffuseMap(dm);
		gm.setMaterial(mat);

		//sets the draw mode
		gm.setDrawMode(DrawMode.FILL);

		// sets up the on click method for the item
//		gm.setOnMouseClicked(new EventHandler<MouseEvent>() {
//			@Override
//			public void handle(MouseEvent event) {
//				if(event.getButton() == MouseButton.PRIMARY){
//					try {
//						AppWindow.out.writeUTF("PICK_UP");
//						AppWindow.out.writeUTF(getName());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				else if(event.getButton() == MouseButton.SECONDARY){
//					gm.getItem().rightClick();
//				}
//			}
//		});
		return gm;
	}
}

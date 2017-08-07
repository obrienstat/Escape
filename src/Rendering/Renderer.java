package Rendering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import userInterface.AppWindow;
import world.Avatar;
import world.Container;
import world.Door;
import world.Item;
import world.Room;
import world.Room.Direction;
import world.Sprite;
import world.coordinate;

/**
 * This Class is responsible for rendering the game, it is passed an avatar and updates the view for the given avatar
 * @author Ashton Davenport
 *
 */
public class Renderer {



	final Group root = new Group();
	final PerspectiveCamera camera = new PerspectiveCamera(true);

	private static final double CAMERA_NEAR_CLIP = 0.1;
	private static final double CAMERA_FAR_CLIP = 100000.0;

	private Avatar previousAvatar;
	private Avatar otherPreviousPlayer;
	private Room currentRoom;
	private Map<Item, GameMesh> itemMeshes = new HashMap<>();
	private MeshView avatarMesh;

	public enum WallDirection {
		NORTH, EAST, SOUTH, WEST, CEIL, FLOOR
	};

	public Renderer(){
		//this.avatar = avatar;
	}

	public Group getRoot(){
		return root;
	}

	/**
	 * This method is called when the renderer is first used, it sets up the camera, walls, and lights for the scene
	 * @return a SubScene of the game world that can be placed in the AppWindow
	 */
	public SubScene start(){

		//create the walls
		Box northWall = createWall(WallDirection.NORTH);
		Box eastWall = createWall(WallDirection.EAST);
		Box southWall = createWall(WallDirection.SOUTH);
		Box westWall = createWall(WallDirection.WEST);
		Box ceil = createWall(WallDirection.CEIL);
		Box floor = createWall(WallDirection.FLOOR);

		//add the walls to the root node
		root.getChildren().add(floor);
		root.getChildren().add(northWall);
		root.getChildren().add(eastWall);
		root.getChildren().add(southWall);
		root.getChildren().add(westWall);
		root.getChildren().add(ceil);

		SubScene scene = new SubScene(root, 1280, 720, true, SceneAntialiasing.BALANCED); // sets the window size

		// setup the camera and add to scene
		buildCamera();
		scene.setCamera(camera);
		camera.setTranslateZ(-300);
		root.setDepthTest(DepthTest.ENABLE);
		Group cameraGroup = new Group();
		cameraGroup.getChildren().add(camera);
		root.getChildren().add(cameraGroup);

//		// draws the bookcase
//		coordinate coOrd1 = new coordinate(380,180,450);
//		Container bookCase = new Container("Bookcase", "A Bookcase", coOrd1, null, 180, new ArrayList<Item>(), 2, "src/files/wood.jpg",
//				"src/files/wood.jpg", "src/files/Bookshelf.obj", Color.BROWN);
//		GameMesh bc = bookCase.getMesh();
//
//		//draws the door
//		coordinate coOrd = new coordinate(0,-50,450);
//		Door door = new Door("Door", "A Door", coOrd, null, 180, false, 1, new ArrayList<Room>(), "src/files/wood.jpg", "src/files/wood.jpg",
//				"src/files/door.obj", Color.RED);
//		GameMesh dr = getMesh("src/files/door.obj", door);



//
//		root.getChildren().add(bc);
//		//root.getChildren().add(dr);

		//final setup of scene
		scene.setFill(Color.BLACK);
		addLights();
		return scene;
	}

	/**
	 * Updates the render window based on the avatar that is passed in
	 * @param a the avatar that the view is to be rendered for
	 */
	public void update(Avatar a){

		Avatar otherPlayer = null;
		for (Avatar other: a.getLocation().getAvatars()){
			if (a.getUid() != other.getUid())
				otherPlayer = other;
		}

		// if I have already been in this room then
		if (a.getLocation().equals(currentRoom)){
			if (a.getLocation().getItems().equals(previousAvatar.getLocation().getItems())
					&& a.getDirection() == previousAvatar.getDirection()){

				if (a.getLocation().getAvatars().size() == previousAvatar.getLocation().getAvatars().size()){
					if (otherPlayer != null){
						if (otherPlayer.getDirection() != otherPreviousPlayer.getDirection()){

						}else
							return;
						// we have to draw him
					}else {
						return;
					}
				}
			}



			previousAvatar = a;
			otherPreviousPlayer = otherPlayer;
			setCameraPos(a);

			// remove the ItemGroup from the root
			ItemGroup removeGroup = null;
			for (Node n: root.getChildren()){
				if (n instanceof ItemGroup){
					removeGroup = (ItemGroup)n;
				}
			}
			if (removeGroup != null)
				root.getChildren().remove(removeGroup);

			// add all the meshes to the itemgroup
			ItemGroup itemGroup = new ItemGroup();
			for (Item i: a.getLocation().getItems()){

				if (itemMeshes.containsKey(i) && !(i instanceof Sprite))
					itemGroup.getChildren().add(itemMeshes.get(i));
				else {
					// i need to redraw the meshes
					itemGroup.getChildren().add(getMesh(i.getObjFile(), i));
				}
			}


			for (Avatar ava: a.getLocation().getAvatars()){

				if (ava.getUid() != a.getUid()){
					itemGroup.getChildren().add(getAvatarMesh(ava.objFile, ava));

				}
			}

			// add the item group to the root
			root.getChildren().add(itemGroup);
			return;
		}

		// This part down here is for when we have not been in this room before

		// update my current position
		previousAvatar = a;
		otherPreviousPlayer = otherPlayer;
		currentRoom = a.getLocation();


		ItemGroup itemsToAdd = new ItemGroup();
		// adds all items from the game
		for (Item i: a.getLocation().getItems()){
			itemsToAdd.getChildren().add(getMesh(i.getObjFile(), i));
		}

		for (Avatar ava: a.getLocation().getAvatars()){
			if (ava.getUid() != a.getUid()){
				itemsToAdd.getChildren().add(getAvatarMesh(ava.objFile, ava));
			}
		}

		setCameraPos(a);


		// removes all items from the game
		ItemGroup itemsToRemove = new ItemGroup();
		for (Node n: root.getChildren()){
			if (n instanceof ItemGroup)
				itemsToRemove = (ItemGroup)n;
		}

		root.getChildren().remove(itemsToRemove);


		root.getChildren().add(itemsToAdd);



	}
	/**
	 * Returns the Mesh for the given avatar
	 * @param fileName the file that the avatar is laded in from
	 * @param player the avatar that we are getting the mesh for
	 * @return a MeshView to draw a representation of the avatar in the scene
	 */
	public MeshView getAvatarMesh(String fileName, Avatar player){

		ModelLoader ml = new ModelLoader();
		DrawObject dO = ml.loadModel(fileName); // loads the object from an .obj file
		TriangleMesh tm = dO.getMesh(); // creates the triangle mesh using read in info
		MeshView bc = new MeshView( tm); // creates the viewable mesh
		bc.setDrawMode(DrawMode.FILL);

		// next lines put the item in the correct position in the room
		bc.setRotationAxis(Rotate.Y_AXIS);

		if (player.getDirection() == Direction.NORTH){
			bc.setRotate(180);
		}
		else if(player.getDirection() == Direction.EAST){
			bc.setRotate(270);
		}
		else if(player.getDirection() == Direction.SOUTH){
			bc.setRotate(0);
		}
		else if (player.getDirection() == Direction.WEST){
			bc.setRotate(90);
		}
		bc.setTranslateX(player.getCoordinate().getX());
		bc.setTranslateY(player.getCoordinate().getY());
		bc.setTranslateZ(player.getCoordinate().getZ());

		bc.setOnMouseClicked(event -> {
			ContextMenu warning = new ContextMenu();
			MenuItem descrip = new MenuItem(player.getName());
			warning.getItems().add(descrip);
			warning.show(AppWindow.window);
		});


		//assign the avatar its colour
		PhongMaterial mat = new PhongMaterial(Color.GREENYELLOW);
		bc.setMaterial(mat);

		avatarMesh = bc;

		return bc;
	}

	/**
	 * Takes a the name of a file and returns the model that the file creates
	 * @param fileName the directory of the .obj file you wish to create a mesh with
	 * @return a MeshView which can be drawn in the scene
	 */
	public GameMesh getMesh(String fileName, Item item){

		ModelLoader ml = new ModelLoader();
		DrawObject dO = ml.loadModel(fileName); // loads the object from an .obj file
		TriangleMesh tm = dO.getMesh(); // creates the triangle mesh using read in info
		GameMesh bc = new GameMesh(item, tm); // creates the viewable mesh
		bc.setDrawMode(DrawMode.FILL);

		// next lines put the item in the correct position in the room
		bc.setRotationAxis(Rotate.Y_AXIS);
		bc.setRotate(item .getRotation());
		bc.setTranslateX(item.getLocation().getX());
		bc.setTranslateY(item.getLocation().getY());
		bc.setTranslateZ(item.getLocation().getZ());

		// loads in the textures for the mesh
		Image bumpMap = new Image("file:" + item.getBumpMap());
		Image diffuseMap = new Image("file:" + item.getDiffuseMap());
		PhongMaterial mat = new PhongMaterial(item.getColor());
		mat.setBumpMap(bumpMap);
		mat.setDiffuseMap(diffuseMap);
		bc.setMaterial(mat);


		itemMeshes.put(item, bc);
		return bc;

	}

	/**
	 * Sets up the camera with the correct settings in-order to display the scene correctly
	 */
	private void buildCamera() {
		camera.setFieldOfView(60);
		camera.setNearClip(CAMERA_NEAR_CLIP);
		camera.setFarClip(CAMERA_FAR_CLIP);
	}

	/**
	 * Sets up the lights and adds them to the scene
	 */
	private void addLights(){
		PointLight light1 = new PointLight();
		PointLight light2= new PointLight();
		PointLight light3 = new PointLight();
		PointLight light4 = new PointLight();

		light1.setTranslateX(-250);
		light1.setTranslateY(250);
		light1.setTranslateZ(-250);
		light2.setTranslateX(-250);
		light2.setTranslateY(250);
		light2.setTranslateZ(250);
		light3.setTranslateX(250);
		light3.setTranslateY(250);
		light3.setTranslateZ(-250);
		light4.setTranslateX(250);
		light4.setTranslateY(250);
		light4.setTranslateZ(250);

		root.getChildren().add(light1);
		root.getChildren().add(light2);
		root.getChildren().add(light3);
		root.getChildren().add(light4);
	}

	/**
	 * Creates the a wall given a WallDirection
	 * @param dir the Wall you see when you are looking in a the given direction
	 * @return the wall as a box shape object
	 */
	public Box createWall(WallDirection dir){
		Box box = new Box(1000,600,20);

		Image diffuseMap = new Image("file:src/files/bricks5.jpg");
		Image normalMap = new Image("file:src/files/bricks6.jpg");


		PhongMaterial m = new PhongMaterial();
		if(dir == WallDirection.NORTH){
			m = new PhongMaterial(Color.GREY);
			box.setMaterial(m);
			box.setTranslateZ(500);
		}
		else if(dir == WallDirection.EAST){
			box = new Box(20,600,1000);
			m = new PhongMaterial(Color.GREY);
			box.setMaterial(m);
			box.setTranslateX(500);
		}
		else if(dir == WallDirection.SOUTH){
			m = new PhongMaterial(Color.GREY);
			box.setMaterial(m);
			box.setTranslateZ(-500);
		}
		else if(dir == WallDirection.WEST){
			box = new Box(20,600,1000);
			m = new PhongMaterial(Color.YELLOW);
			m = new PhongMaterial(Color.GREY);
			box.setMaterial(m);
			box.setTranslateX(-500);
		}
		else if (dir == WallDirection.CEIL){
			box = new Box(1000,20,1000);
			box.setMaterial(m);
			box.setTranslateY(300);
		}
		else if(dir == WallDirection.FLOOR){
			box = new Box(1000,20,1000);
			box.setMaterial(m);
			box.setTranslateY(-300);
		}

		// set the textures for the wall
		m.setDiffuseMap(diffuseMap);
		m.setBumpMap(normalMap);
		return box;
	}

	/**
	 * Sets the camera to the current view of the player based on the direction they are facing
	 * @param a the avatar the camera is to be set for
	 */
	public void setCameraPos(Avatar a){

		// we only need to translate if the current Avatars direction is different to our previous Avatar

		camera.setRotationAxis(Rotate.Y_AXIS); // set the camera to rotate arround the Y axis

		if(a.getDirection() == Room.Direction.NORTH){
			camera.setTranslateZ(-300);
			camera.setTranslateX(0);
			camera.setRotate(0);
		}
		else if(a.getDirection() == Room.Direction.EAST){
			camera.setTranslateX(-300);
			camera.setTranslateZ(0);
			camera.setRotate(90);
		}
		else if(a.getDirection() == Room.Direction.SOUTH){
			camera.setTranslateZ(300);
			camera.setTranslateX(0);
			camera.setRotate(180);
		}
		else if(a.getDirection() == Room.Direction.WEST){
			camera.setTranslateX(300);
			camera.setTranslateZ(0);
			camera.setRotate(270);
		}
	}

}

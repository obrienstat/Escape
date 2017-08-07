package userInterface;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import Rendering.Renderer;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.Game;
import network_util.Master;
import network_util.Slave;


/**
 * The AppWindow is responsible for starting the game. It holds all the ui for the
 * game, including the renderer.
 * The AppWindow consists of two scenes. (start scene and game scene)
 * The start scene holds all the different types of game menus that are can be
 * switched between depending on what buttons are clicked.
 * The game scene holds the renderer, menu bar and Dashboard.
 *
 * @author barnettayl
 *
 */
public class AppWindow extends Application {

	public static Stage window;
	private BorderPane gamePane;
	Canvas canvas;
	private GraphicsContext gc;
	private Dashboard dashboard;
	private RotateControl controls;
	boolean serverDone = false;
	boolean pause = false;
	private Renderer r;

	private Label left;
	private Label right;
	public PauseMenu pauseMenu;
	public FinishedMenu finishedMenu;
	public ShowMap map;
	public ShowCompassMap compassMap;
	public String serverIP;

	public boolean hasCompass = false;
	public boolean hasMap = false;

	public static Socket sock;
	public static DataOutputStream out;
	private boolean isServer = false;

	/**
	 *
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;
		window.setTitle("ESCAPE");
		window.setResizable(false);

		// if user clicks x
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});

		window.setScene(startScene());
		window.show();
	}

	/**
	 * returns the renderer so that it can
	 * be accessed by the server
	 * @return
	 */
	public Renderer getRenderer(){
		return r;
	}

	/**
	 *	returns the chat window from the dashboard
	 * @return
	 */
	public Chat getChatWindow()	{
		return dashboard.getChatWindow();
	}

	/**
	 *	returns the dashboard
	 * @return
	 */
	public Dashboard getDashboard(){
		return this.dashboard;
	}

	/**
	 * creates and returns opening scene (Game Menu)
	 * @return
	 */
	public Scene startScene() {

		// creating main startpane
		Pane bp = new Pane();

		GameMenu gm = new GameMenu();

		bp.getChildren().add(gm);
		bp.setId("root");

		// creates start scene
		Scene startScene = new Scene(bp, 1290, 870);

		startScene.getStylesheets().add(getClass().getClassLoader().getResource("files/start.css").toExternalForm());

		return startScene;

	}

	/**
	 * creates and returns main game scene
	 * with all its nodes added
	 * @return
	 */
	public Scene gameScene() {

		Pane pane = new Pane();

		// creates rotate control
		controls = new RotateControl(getRenderer(), this);

		// creates the two arrows
		left = controls.getLeft();
		right = controls.getRight();

		// translates the arrows from the top left corner
		left.setTranslateY(260);
		right.setTranslateX(1180);
		right.setTranslateY(260);

		// creates the pause menu and sets visible to false
		pauseMenu = new PauseMenu();
		pauseMenu.setVisible(false);

		// creates the finished menu and sets visible to false
		finishedMenu = new FinishedMenu();
		finishedMenu.setVisible(false);

		// creates the normal map and sets visible to false
		map = new ShowMap();
		map.setVisible(false);

		// creates the compass map and sets visible to false
		compassMap = new ShowCompassMap();
		compassMap.setVisible(false);

		// adds all the above nodes to the pane.
		// the pane also includes the gameWindow
		pane.getChildren().addAll(gameWindow(), left, right, pauseMenu, finishedMenu, map, compassMap);

		// creates the game scene
		Scene gameScene = new Scene(pane, 1290, 870);


		// handles keys
		gameScene .setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
            	fadeTransitionPause();
            }
        });

		gameScene.setOnKeyPressed(event -> {
			if (event.getCode() == KeyCode.M){
				System.out.println(hasCompass);
				if(hasCompass){
					fadeTransitionCompassMap();

				} else if (hasMap){
					fadeTransitionMap();
				}
			}
		});


		// sets the css style sheet for the gameScene
		gameScene.getStylesheets().add(getClass().getClassLoader().getResource("files/game.css").toExternalForm());

		return gameScene;

	}

	/**
	 * sets up layout for main game scene
	 * adds everything needed in the game scene
	 * to the border pane
	 * returns a border pane
	 * @return
	 */
	public BorderPane gameWindow()	{

		gamePane = new BorderPane();

		// sets menu bar at the top
		gamePane.setTop(menuBar());

		// create rendering pane
		r  = new Renderer();
		gamePane.setCenter(r.start());

		// create dashboard
		dashboard = new Dashboard(this.window, this);
		gamePane.setBottom(dashboard);
		gamePane.setId("root");

		return gamePane;
	}

	/**
	 * creates and returns a menubar, to be used
	 * in the main game scene
	 * @return
	 */
	public MenuBar menuBar() {

		// File menu
		Menu fileMenu = new Menu("File");
		MenuItem pauseGame, newFile, loadFile, saveFile, settings, exit;
		SeparatorMenuItem s = new SeparatorMenuItem();

		// pause
		pauseGame = new MenuItem("Pause...");
		pauseGame.setOnAction(e -> {
			fadeTransitionFinished();
		});

		// save
		saveFile = new MenuItem("Save...");
		saveFile.setOnAction(e -> {
			try {
				out.writeUTF("SAVE");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// load
		loadFile = new MenuItem("Load...");
		loadFile.setOnAction(e -> {
			try {
				out.writeUTF("LOAD");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// settings (has no functionality)
		settings = new MenuItem("Settings...");
		// settings.setOnAction(e -> );

		// exit
		exit = new MenuItem("Exit...");
		exit.setOnAction(e -> closeProgram());

		// adds all nodes to the file menu
		fileMenu.getItems().addAll(pauseGame, saveFile, loadFile, s, settings, exit);

		// Edit menu
		Menu editMenu = new Menu("_Edit");
		CheckMenuItem autoSave = new CheckMenuItem("Enable Autosave");
		autoSave.setSelected(true);

		editMenu.getItems().add(autoSave);

		// Help menu
		Menu helpMenu = new Menu("Help");
		MenuItem showRules= new MenuItem("Show Rules");
		showRules.setOnAction(e -> {
			rulesDialog();
		});

		// adds to the help menu
		helpMenu.getItems().add(showRules);

		// Main menu bar
		MenuBar menuBar = new MenuBar();
		// adds the menus to the menu bar
		menuBar.getMenus().addAll(fileMenu, editMenu, helpMenu);

		return menuBar;
	}

	/**
	 * creates and returns a alert dialog box containing game rules
	 * @return
	 */
	public Alert rulesDialog() {
		// creates the alert box
		Alert rules = new Alert(AlertType.INFORMATION);

		// sets its content
		rules.setTitle("How To Play");
		rules.setHeaderText("Look, Some Rules");
		rules.setContentText("rules should go here");
		// shows it
		rules.showAndWait();

		return rules;
	}

	/**
	 * method for closing the program
	 */
	public void closeProgram() {
		// creates close alert box
		Alert onClose = new Alert(AlertType.CONFIRMATION);
		onClose.setTitle("Exit Game");
		onClose.setHeaderText(null);
		onClose.setContentText("Are you ok with this?");

		// creates buttons
		ButtonType buttonTypeYes = new ButtonType("Yes");
		ButtonType buttonTypeNo = new ButtonType("No");

		onClose.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

		Optional<ButtonType> result = onClose.showAndWait();
		// button results
		if (result.get() == buttonTypeYes) {
			try {
				if (sock != null){
					if (!isServer)
						out.writeUTF("END");
					sock.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			window.close();

		} else {
			onClose.close();
		}
	}

	/**
	 * this method fades in what is in the show map
	 * class.
	 */
	public void fadeTransitionMap(){
		// if the map is visibile
		if (!map.isVisible()) {
			// creates the fade transition with the number of seconds it takes to fade
	        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), map);
	        ft.setFromValue(0);
	        ft.setToValue(1);

	        map.setVisible(true);
	        // plays the fade
	        ft.play();
	    }
	    else {
	    	// if it is already visible it will set the values around the other way
	        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), map);
	        ft.setFromValue(1);
	        ft.setToValue(0);
	        ft.setOnFinished(evt -> map.setVisible(false));
	        ft.play();
	    }
	}

	/**
	 * this method fades in the compass map
	 * class.
	 */
	public void fadeTransitionCompassMap(){
		if (!compassMap.isVisible()) {
			// creates the fade transition with the number of seconds it takes to fade
	        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), compassMap);
	        ft.setFromValue(0);
	        ft.setToValue(1);

	        compassMap.setVisible(true);
	        ft.play();
	    }
	    else {
	    	// if it is already visible it will set the values around the other way
	        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), compassMap);
	        ft.setFromValue(1);
	        ft.setToValue(0);
	        ft.setOnFinished(evt -> compassMap.setVisible(false));
	        ft.play();
	    }
	}

	/**
	 * this method fades in the finished menu (YOU WON MENU)
	 * class.
	 */
	public void fadeTransitionFinished(){
		if (!finishedMenu.isVisible()) {
			// creates the fade transition with the number of seconds it takes to fade
	        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), finishedMenu);
	        ft.setFromValue(0);
	        ft.setToValue(1);

	        finishedMenu.setVisible(true);
	        ft.play();
	    }
	    else {
	    	// if it is already visible it will set the values around the other way
	        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), finishedMenu);
	        ft.setFromValue(1);
	        ft.setToValue(0);
	        ft.setOnFinished(evt -> finishedMenu.setVisible(false));
	        ft.play();
	    }
	}

	/**
	 * This method fades in the Pause menu
	 */
	public void fadeTransitionPause()	{
		if (!pauseMenu.isVisible()) {
			// creates the fade transition with the number of seconds it takes to fade
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), pauseMenu);
            ft.setFromValue(0);
            ft.setToValue(1);

            pauseMenu.setVisible(true);
            ft.play();
        }
        else {
        	// if it is already visible it will set the values around the other way
            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), pauseMenu);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(evt -> pauseMenu.setVisible(false));
            ft.play();
        }
	}

	/**
	 * This class is responsible for the game menu that is present at the very start
	 * of the game.
	 * It contains six menus that are switched (added and removed by translating)
	 * depending on what buttons you click on the menus.
	 * Menus are switched using translations for animation.
	 * @author barnettayl
	 *
	 */
	private class GameMenu extends Parent {

		String serverPort;
		TextField serverPortInput;

        public GameMenu() {

        	// creates box for title
        	HBox titleBox = new HBox();

        	// creates all boxes for each menu
            VBox menu0 = new VBox(10);
            VBox menu1 = new VBox(10);
            VBox menu2 = new VBox(10);
            VBox menu3 = new VBox(10);
            VBox menu4 = new VBox(10);
            VBox menu5 = new VBox(10);
            VBox menu6 = new VBox(10);

            // translates title box from top left corner
            titleBox.setTranslateX(100);
            titleBox.setTranslateY(100);

            // translates each menu on screen from top left corner
            menu0.setTranslateX(100);
            menu0.setTranslateY(200);

            menu1.setTranslateX(100);
            menu1.setTranslateY(200);

            menu2.setTranslateX(100);
            menu2.setTranslateY(200);

            menu3.setTranslateX(100);
            menu3.setTranslateY(200);

            menu4.setTranslateX(100);
            menu4.setTranslateY(200);

            menu5.setTranslateX(100);
            menu5.setTranslateY(200);

            menu6.setTranslateX(100);
            menu6.setTranslateY(200);

            final int offset = 400;

            menu1.setTranslateX(offset);
            menu2.setTranslateX(offset);
            menu3.setTranslateX(offset);
            menu4.setTranslateX(offset);
            menu5.setTranslateX(offset);
            menu6.setTranslateX(offset);

            // CREATING ESCAPE TITLE
            Image image = new Image(getClass().getClassLoader().getResource("files/escape.png").toExternalForm());
            Label title = new Label();
            title.setGraphic(new ImageView(image));

            titleBox.setSpacing(10);
            titleBox.getChildren().add(title);

        	// CREATING MAIN START PANE (MENU0)
    		GridPane startPane = new GridPane();

    		// setting pane layout
    		startPane.setPadding(new Insets(10, 10, 10, 10));
    		startPane.setVgap(8);
    		startPane.setHgap(10);

    		// creates and sets up new game button
    		MenuButton newGame = new MenuButton("NEW GAME");
    		GridPane.setConstraints(newGame, 0, 0);
    		newGame.setOnMouseClicked(e -> {
    			translateMenusIn(menu2, menu0);
            });

    		// creates and sets up how to play button
    		MenuButton rules = new MenuButton("HOW TO PLAY");
    		GridPane.setConstraints(rules, 0, 1);
    		rules.setOnMouseClicked(e -> {
    			 translateMenusIn(menu6, menu0);
    		});

    		 // creates and sets up options button
    		 MenuButton options = new MenuButton("OPTIONS");
    		 GridPane.setConstraints(options, 0, 2);
             options.setOnMouseClicked(event -> {
            	 translateMenusIn(menu1, menu0);
             });

            // creates and sets up exit button
    		MenuButton exit = new MenuButton("EXIT");
    		GridPane.setConstraints(exit, 0, 3);
    		exit.setOnMouseClicked(e -> closeProgram());

    		// adding all the menu buttons to the pane
    		startPane.getChildren().addAll(newGame, options, rules, exit);

    		// CREATING OPTIONS PANE (MENU1)
            GridPane optionsPane = new GridPane();

            // setting pane layout
        	optionsPane.setPadding(new Insets(10, 10, 10, 10));
    		optionsPane.setVgap(8);
    		optionsPane.setHgap(10);


            MenuButton back = new MenuButton("BACK");
            GridPane.setConstraints(back, 0, 0);
            back.setOnMouseClicked(event -> {
            	translateMenusBack(menu0, menu1);
            });

            MenuButton sound = new MenuButton("SOUND");
            GridPane.setConstraints(sound, 0, 1);

            MenuButton video = new MenuButton("VIDEO");
            GridPane.setConstraints(video, 0, 2);

            // adding all the menu buttons to the options pane
            optionsPane.getChildren().addAll(back, sound, video);


            // CREATING SETUP OPTION PANE (MENU2)
            GridPane setupOptionPane = new GridPane();

            setupOptionPane.setPadding(new Insets(10, 10, 10, 10));
    		setupOptionPane.setVgap(8);
    		setupOptionPane.setHgap(10);

    		MenuButton backbtn = new MenuButton("BACK");
            GridPane.setConstraints(backbtn, 0, 0);
            backbtn.setOnMouseClicked(event -> {
            	translateMenusBack(menu0, menu2);
            });

            MenuButton server = new MenuButton("SERVER");
            GridPane.setConstraints(server, 0, 1);
            server.setOnMouseClicked(event -> {
            	translateMenusIn(menu3, menu2);
            });

            // creates and sets up client button
            MenuButton client = new MenuButton("CLIENT");
            GridPane.setConstraints(client, 0, 2);
            client.setOnMouseClicked(event -> {
            	translateMenusIn(menu4, menu2);
            });

            // adding all the menu buttons to the setup option pane
            setupOptionPane.getChildren().addAll(backbtn, server, client);


            // CREATING SERVER SETUP PANE (MENU3)
            GridPane setupServerPane = new GridPane();

            // setting pane layout
            setupServerPane.setPadding(new Insets(10, 10, 10, 10));
    		setupServerPane.setVgap(8);
    		setupServerPane.setHgap(10);

    		// creates and sets up back button
    		MenuButton back1 = new MenuButton("BACK");
            back1.setOnMouseClicked(event -> {
            	translateMenusBack(menu2, menu3);
            });

            // text
            Text serverSet = new Text("SETUP SERVER");
            serverSet.setFont(serverSet.getFont().font(20));
            serverSet.setFill(Color.WHITE);


            GridPane serverPane = new GridPane();
            // setting pane layout
            serverPane.setPadding(new Insets(20,20,20,20));
            serverPane.setHgap(5);
            serverPane.setVgap(5);

    		Label serverPortName = new Label("Port");
    		serverPortName.setFont(serverPortName.getFont().font(15));
    		serverPortName.setTextFill(Color.WHITE);

    		// new text field
    		serverPortInput = new TextField();

    		// mouse listener
    		serverPortInput.setOnMouseClicked(e -> {
    			serverPortInput.clear();
    		});

    		// sets up enter button
    		MenuButton serverEnterbtn = new MenuButton("Enter");
    		serverEnterbtn.setOnMouseClicked(e -> {
    			setServer(menu5, menu3);
    		});

    		menu3.setOnKeyPressed(e -> {
    			if(e.getCode() == KeyCode.ENTER){
    				serverEnterbtn.fire();
    				setServer(menu5, menu3);
    			}
    		});

    		// add nodes to serverPane layout
    		serverPane.add(back1, 0, 0);
    		serverPane.add(serverSet, 0, 4);
    		serverPane.add(serverPortName, 0, 5);
    		serverPane.add(serverPortInput, 1, 5);
    		serverPane.add(serverEnterbtn, 1, 6);

    		// adds the serverpane to the setup
            setupServerPane.getChildren().addAll(serverPane);


            // CREATING CLIENT SETUP PANE (MENU4)
            GridPane setupClientPane = new GridPane();

            // setting pane layout
            setupClientPane.setPadding(new Insets(10, 10, 10, 10));
            setupClientPane.setVgap(8);
            setupClientPane.setHgap(10);

            MenuButton back2 = new MenuButton("BACK");

            back2.setOnMouseClicked(event -> {
            	translateMenusBack(menu2, menu4);
            });

            Text clientSet = new Text("SETUP CLIENT");
            clientSet.setFont(clientSet.getFont().font(20));
            clientSet.setFill(Color.WHITE);
            //GridPane.setConstraints(clientSet, 1 , 0);

            GridPane clientPane = new GridPane();
            // setting pane layout
            clientPane.setPadding(new Insets(20,20,20,20));
            clientPane.setHgap(5);
            clientPane.setVgap(5);

            Label ipName = new Label("IP");
            ipName.setFont(ipName.getFont().font(15));
    		ipName.setTextFill(Color.WHITE);


    		// new text field and listeners
    		TextField ipInput = new TextField();
    		ipInput.setTooltip(new Tooltip("The ip address of the server..."));
    		ipInput.setOnMouseClicked( e -> {
    			ipInput.clear();
    		});

    		Label clientPortName = new Label("Port");
    		clientPortName.setFont(clientPortName.getFont().font(15));
    		clientPortName.setTextFill(Color.WHITE);

    		// new text field and listeners
    		TextField clientPortInput = new TextField();
    		clientPortInput.setTooltip(new Tooltip("Requires ports above 5000"));
    		clientPortInput.setOnMouseClicked( e -> {
    			clientPortInput.clear();
    		});

    		Label clientName = new Label("Name");
    		clientName.setFont(clientName.getFont().font(15));
    		clientName.setTextFill(Color.WHITE);

    		// new text field and listeners
    		TextField clientNameInput = new TextField();
    		clientNameInput.setTooltip(new Tooltip("Requires a name"));
    		clientNameInput.setOnMouseClicked( e -> {
    			clientNameInput.clear();
    		});


    		// set up login button for client connection
    		MenuButton clientEnterbtn = new MenuButton("Connect");
    		clientEnterbtn.setOnMouseClicked(e -> {
    			if (!checkValidInput(ipInput, clientPortInput, clientNameInput)) return;
    			try {
    				String ip = validateIp(ipInput);
    				int port = validatePort(clientPortInput);
    				String name = clientNameInput.getText();
    				runClient(ip, port, name);
    			} catch (Exception exception){
    				ipInput.getStyleClass().add("error");
    				clientPortInput.getStyleClass().add("error");
    			}

    		});

    		// set up enter key for client connection
    		menu4.setOnKeyPressed(e -> {
    			if(e.getCode() == KeyCode.ENTER){
    				clientEnterbtn.fire();
    				if (!checkValidInput(ipInput, clientPortInput, clientNameInput)) return;
        			try {
        				String ip = validateIp(ipInput);
        				int port = validatePort(clientPortInput);
        				String name = clientNameInput.getText();
        				runClient(ip, port, name);
        			} catch (Exception exception){
        				ipInput.getStyleClass().add("error");
        				clientPortInput.getStyleClass().add("error");
        			}
    			}
    		});

            // add nodes to gridPane layout
    		clientPane.add(back2, 0, 0);
    		clientPane.add(clientSet, 0, 4);
    		clientPane.add(ipName, 0, 5);
    		clientPane.add(ipInput, 1, 5);
    		clientPane.add(clientPortName, 0 , 6);
    		clientPane.add(clientPortInput, 1, 6);
    		clientPane.add(clientName, 0, 7);
    		clientPane.add(clientNameInput, 1, 7);
    		clientPane.add(clientEnterbtn, 1, 8);

    		// adds the server pane to the setup
            setupClientPane.getChildren().addAll(clientPane);

    		// CREATING RULES PANE (MENU6)
    		GridPane rulesPane = new GridPane();

    		// setting pane layout
    		rulesPane.setPadding(new Insets(20,20,20,20));
    		rulesPane.setHgap(5);
    		rulesPane.setVgap(5);

    		MenuButton back3 = new MenuButton("BACK");
            back3.setOnMouseClicked(event -> {
            	translateMenusBack(menu0, menu6);
             });

    		Text howPlay = new Text("HOW TO PLAY");
            howPlay.setFont(howPlay.getFont().font(25));
            howPlay.setFill(Color.WHITE);

            TextArea rulesArea = new TextArea();
            rulesArea.setEditable(false);
            rulesArea.setText("The object of the game is to escape. It isn't a race, you can work together.\n"
            		+ "To turn your character, click the arrows at the left or right side of the screen. To go through doors,\n"
            		+ "right-click on them and select Enter. Some doors may be locked to you at the beginning.\n"
            		+ "Explore the rooms and find items to help you escape. There are several useful items to find including:\n  "
            		+ "1.The Map\n"
            		+ "The Map is a vital tool to get you out of there. When opened from the inventory it will show you all the rooms\n"
            		+ "making it much easier to find your way around. But be warned: the map is randomly rotated until you find:\n  "
            		+ "2.The Compass\n"
            		+ "The Compass isn't a huge help on its own, but when paired with the map it tells you exactly which way is which,\n"
            		+ "so getting out will be a breeze. The compass is used automatically when you open the map while holding\n    "
            		+ "the compass\n"
            		+ "\nKeys allow you to get through locked doors. Once you unlock a door it will always be unlocked, so your friend\n"
            		+ "can follow behind on your heels. If you look hard you might also find coins scattered around. These might\n"
            		+ "come in handy once you've almost found your way out.\n"
            		+ "\nHow to save and load in this masterful game:\n"
            		+ "Saving is easy. At any point in your game, select File -> Save. This will save you game in an XML file.\n"
            		+ "If you want to open the file, we reccomend using a web browser such as Chrome, or Internet Explorer.\n"
            		+ "Don't edit the file, that would be cheating!\n"
            		+ "Once you've made a save, at any time while playing the game either player can select File -> Load.\n"
            		+ "This will load the save you previously made like magic. It's not magic, it was a lot of hard work.");

            rulesArea.setId("rules");

            rulesPane.add(back3, 0, 0);
            rulesPane.add(howPlay, 0, 4);
            rulesPane.add(rulesArea, 0, 5);


            menu0.getChildren().add(startPane);
            menu1.getChildren().add(optionsPane);
            menu2.getChildren().add(setupOptionPane);
            menu3.getChildren().add(setupServerPane);
            menu4.getChildren().add(setupClientPane);
            menu6.getChildren().add(rulesPane);

            getChildren().addAll(titleBox, menu0);
        }

        /**
         *	This method is for translating the menus in and out.
         *	This method is specifically for moving forward
         *  in the menus.
         * @param menuIn
         * @param menuOut
         */
        public void translateMenusIn(VBox menuIn, VBox menuOut)	{

        	int offset = 400;

   		 	getChildren().add(menuIn);

            // translates out old menu
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menuOut);
            tt.setToX(menuOut.getTranslateX() - offset);

            // translates in new menu
            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menuIn);
            tt1.setToX(menuOut.getTranslateX());

            // plays the translations
            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
            	// removes old menu
            	getChildren().remove(menuOut);
            });
        }

        /**
         *  This method is for translating the menus in and out.
         *	This method is specifically for moving back
         *  in the menus.
         * @param menuIn
         * @param menuOut
         */
        public void translateMenusBack(VBox menuIn, VBox menuOut)	{

        	int offset = 400;

   		 	getChildren().add(menuIn);

            // translates out old menu
            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menuOut);
            tt.setToX(menuOut.getTranslateX() + offset);

            // translates in new menu
            TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menuIn);
            tt1.setToX(menuOut.getTranslateX());

            // plays the translations
            tt.play();
            tt1.play();

            tt.setOnFinished(evt -> {
            	// removes old menu
            	getChildren().remove(menuOut);
            });
        }

        /**
         * this method is the layout for setting up the
         * server
         * @param In
         * @param Out
         */
        public void setServer(VBox In, VBox Out){
        	serverPort = serverPortInput.getText();
			System.out.println(serverPortInput.getText());
			try {
				int port = validatePort(serverPortInput);
				runServer(port);
			}catch (Exception ex){
				serverPortInput.getStyleClass().add("error");
			}

			if(serverDone){

				 // CREATING SERVER AWAITING CONNECTION PANE (MENU5)
	    		GridPane serverAwaitPane = new GridPane();

	    		// setting pane layout
	    		serverAwaitPane.setPadding(new Insets(20,20,20,20));
	    		serverAwaitPane.setHgap(5);
	    		serverAwaitPane.setVgap(5);

	    		Text awaiting = new Text("SERVER AWATING CONNECTIONS ..");
	            awaiting.setFont(awaiting.getFont().font(40));
	            awaiting.setFill(Color.WHITE);

				Text serverip = new Text("SERVER IP: " + serverIP);
				serverip.setFont(serverip.getFont().font(40));
	            serverip.setFill(Color.WHITE);

				Text port = new Text("SERVER PORT: " + serverPort);
	            port.setFont(port.getFont().font(40));
	            port.setFill(Color.WHITE);

	    		serverAwaitPane.add(awaiting, 0, 0);
	    		serverAwaitPane.add(serverip, 0, 1);
	    		serverAwaitPane.add(port, 0, 2);

	    		In.getChildren().add(serverAwaitPane);

	    		translateMenusIn(In, Out);
			}
        }
    }

	/**
	 * This class is responsible for handling the pause menu.
	 * @author barnettayl
	 *
	 */
	private class PauseMenu extends Parent {

        public PauseMenu() {

        	HBox titleBox = new HBox();

            VBox menu0 = new VBox(10);
            VBox menu1 = new VBox(10);

            titleBox.setTranslateX(100);
            titleBox.setTranslateY(100);

            // changes x and y of menu
            menu0.setTranslateX(100);
            menu0.setTranslateY(200);
            // changes x and y of menu
            menu1.setTranslateX(100);
            menu1.setTranslateY(200);

            final int moveBy = 400;

            menu1.setTranslateX(moveBy);

            Image image = new Image(getClass().getClassLoader().getResource("files/paused.png").toExternalForm());
            Label title = new Label();
            title.setGraphic(new ImageView(image));

            titleBox.getChildren().add(title);

        	// creating pane for menu buttons
    		GridPane pausePane = new GridPane();

    		pausePane.setPadding(new Insets(10, 10, 10, 10));
    		pausePane.setVgap(8);
    		pausePane.setHgap(10);

    		 MenuButton resume = new MenuButton("RESUME");
    		 GridPane.setConstraints(resume, 0, 0);
             resume.setOnMouseClicked(event -> {
                 FadeTransition ft = new FadeTransition(Duration.seconds(0.5), this);
                 ft.setFromValue(1);
                 ft.setToValue(0);
                 ft.setOnFinished(evt -> setVisible(false));
                 ft.play();
             });


             MenuButton options = new MenuButton("OPTIONS");
             GridPane.setConstraints(options, 0, 1);
             options.setOnMouseClicked(event -> {
                 getChildren().add(menu1);

                 TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu0);
                 tt.setToX(menu0.getTranslateX() - moveBy);

                 TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu1);
                 tt1.setToX(menu0.getTranslateX());

                 tt.play();
                 tt1.play();

                 tt.setOnFinished(evt -> {
                     getChildren().remove(menu0);
                 });
             });

             MenuButton exit = new MenuButton("EXIT");
             GridPane.setConstraints(exit, 0, 2);
             exit.setOnMouseClicked(event -> {
                 closeProgram();
             });

            pausePane.getChildren().addAll(resume, options, exit);

            GridPane optionsPane = new GridPane();

            optionsPane.setPadding(new Insets(10, 10, 10, 10));
    		optionsPane.setVgap(8);
    		optionsPane.setHgap(10);

            MenuButton back = new MenuButton("BACK");
            GridPane.setConstraints(back, 0, 0);
            back.setOnMouseClicked(event -> {
                getChildren().add(menu0);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(0.25), menu1);
                tt.setToX(menu1.getTranslateX() + moveBy);

                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), menu0);
                tt1.setToX(menu1.getTranslateX());

                tt.play();
                tt1.play();

                tt.setOnFinished(evt -> {
                    getChildren().remove(menu1);
                });
            });

            MenuButton sound = new MenuButton("SOUND");
            GridPane.setConstraints(sound, 0, 1);

            MenuButton video = new MenuButton("VIDEO");
            GridPane.setConstraints(video, 0, 2);

            optionsPane.getChildren().addAll(back,sound,video);

            menu0.getChildren().add(pausePane);
            menu1.getChildren().add(optionsPane);

            Rectangle bg = new Rectangle(1290, 870);
            bg.setFill(Color.GREY);
            bg.setOpacity(0.5);

            getChildren().addAll(bg, titleBox, menu0);
        }
    }

	/**
	 * This class creates a Finished Menu that will be called
	 * and faded in when the player finishes the game.
	 * @author barnettayl
	 *
	 */
	private class FinishedMenu extends Parent {

       public FinishedMenu() {

           VBox menu0 = new VBox(10);

           menu0.setTranslateX(300);
           menu0.setTranslateY(250);

       	// creating pane
   		GridPane finishedPane = new GridPane();

   		finishedPane.setPadding(new Insets(10, 10, 10, 10));
   		finishedPane.setVgap(8);
   		finishedPane.setHgap(10);

   		Image imageC = new Image(getClass().getClassLoader().getResource("files/congrats.png").toExternalForm());
        Label congrats = new Label();
        congrats.setGraphic(new ImageView(imageC));

        Image imageEscaped = new Image(getClass().getClassLoader().getResource("files/escaped.png").toExternalForm());
        Label escaped = new Label();
        escaped.setGraphic(new ImageView(imageEscaped));


        MenuButton exit = new MenuButton("EXIT");
        GridPane.setConstraints(exit, 0, 3);
        exit.setOnMouseClicked(event -> {
               closeProgram();
            });

        finishedPane.add(congrats, 0, 0);
        finishedPane.add(escaped, 0, 2);
        finishedPane.add(exit, 0, 4);

        menu0.getChildren().add(finishedPane);

        Rectangle bg = new Rectangle(1290, 870);
        bg.setFill(Color.GREY);
        bg.setOpacity(0.5);

        getChildren().addAll(bg, menu0);
       }
    }

	/**
	 * This class is responsible for showing the map
	 * @author barnettayl
	 *
	 */
	public class ShowMap extends Parent {

		public ShowMap(){

			VBox mapNormal = new VBox();

			mapNormal.setTranslateX(300);
			mapNormal.setTranslateY(75);

			Image mapNormalImage = new Image("file:src/files/Map.jpg");
			Image exitImage = new Image("file:src/files/exit.png");

			Label mapNormalLabel = new Label();
			Label exit = new Label();
			exit.setGraphic(new ImageView(exitImage));
			exit.setOnMouseClicked(e -> {
				fadeTransitionMap();
			});

			mapNormalLabel.setGraphic(new ImageView(mapNormalImage));

			mapNormal.getChildren().addAll(mapNormalLabel, exit);

			Rectangle bg = new Rectangle(1290, 870);
			bg.setFill(Color.GRAY);
			bg.setOpacity(0.5);

			getChildren().addAll(bg, mapNormal);
		}
	}

	/**
	 * This class is responsible for showing the map
	 * @author barnettayl
	 *
	 */
	public class ShowCompassMap extends Parent {

		public ShowCompassMap(){

			VBox mapCompass = new VBox();

			mapCompass.setTranslateX(300);
			mapCompass.setTranslateY(75);

			Image mapCompassImage = new Image("file:src/files/Map_Compass.jpg");
			Image exitImage = new Image("file:src/files/exit.png");
			Label mapCompassLabel = new Label();

			Label exit = new Label();
			exit.setGraphic(new ImageView(exitImage));
			exit.setOnMouseClicked(e -> {
				fadeTransitionCompassMap();
			});

			mapCompassLabel.setGraphic(new ImageView(mapCompassImage));

			mapCompass.getChildren().addAll(mapCompassLabel, exit);

			Rectangle bg = new Rectangle(1290, 870);
			bg.setFill(Color.GRAY);
			bg.setOpacity(0.5);

			getChildren().addAll(bg, mapCompass);
		}
	}




	/**
	 * This class is a customised button class.
	 * @author barnettayl
	 *
	 */
	 private static class MenuButton extends StackPane {

	        private Text text;
	        private DropShadow drop;

	        public MenuButton(String name) {
	        	// customises text
	            text = new Text(name);
	            text.setFont(text.getFont().font(20));
	            text.setFill(Color.WHITE);

	            // creates and customises rectangle
	            Rectangle bg = new Rectangle(250, 30);
	            bg.setOpacity(0.6);
	            bg.setFill(Color.BLACK);
	            bg.setEffect(new GaussianBlur(3.5));


	            setAlignment(Pos.CENTER_LEFT);
	            setRotate(-0.5);
	            getChildren().addAll(bg, text);

	            setOnMouseEntered(event -> {
	                bg.setTranslateX(10);
	                text.setTranslateX(10);
	                bg.setFill(Color.WHITE);
	                text.setFill(Color.BLACK);
	            });

	            setOnMouseExited(event -> {
	                bg.setTranslateX(0);
	                text.setTranslateX(0);
	                bg.setFill(Color.BLACK);
	                text.setFill(Color.WHITE);
	            });

	            drop = new DropShadow(50, Color.WHITE);
	            drop.setInput(new Glow());

	            setOnMousePressed(event -> setEffect(drop));
	            setOnMouseReleased(event -> setEffect(null));
	        }

	        /**
	         * This method sets the effect of button and is
	         * used when using key clicks instead of clicking
	         * the button.
	         */
			public void fire() {
				setEffect(drop);
			}

	    }

	 /**
		 * Starts the client by connecting to the socket, then creates a slave connection
		 * to service the user, then launches the Game Application
		 * @param address - ip address
		 * @param port - must be the same as servers
		 */
		private void runClient(String address, int port, String name){
			try {
				// connect to the server
				sock = new Socket(address, port);
				out = new DataOutputStream(sock.getOutputStream());
				out.writeUTF(name); // send clients name to the server

				// create a new slave and lauch the main window
				//AppWindow application = new AppWindow(socket);
				new Slave(sock, this).start();

				// run until the program closes
				window.setScene(gameScene());

			}catch (Exception e){
				alertOnServiceError(false);
			}
		}

		/**
		 * Starts the server and awaits for maximum connections before exiting. The server
		 * is run locally on the machine that instantiated the server
		 *
		 * @param port - Any number specified
		 */
		private void runServer(int port){
			try{
				this.isServer = true;
				ServerSocket server = new ServerSocket(port);
				serverIP = InetAddress.getLocalHost().getHostAddress();

				Game game = new Game();
				System.out.println("SERVER AWAITING CONNECTIONS");

				Thread background = new Thread(new Runnable() {

					@Override
					public void run() {
						serverDone = true;
						int count = 0;
						while (count < 2){
							try {
								connections[count++] = server.accept();
							} catch (IOException e) {
								e.printStackTrace();
							}
							new Master(connections[count-1], game).start();

						}
						System.out.println("ALL CLIENTS CONNECTED");
					}
				});
				background.start();

				while (true){
					int counter = 0;
					for (Socket s: connections){
						if (s.isInputShutdown()){
							counter++;
							s.close();
						}
					}

					if (counter == connections.length)
						break;
				}
				System.out.println("Server Exited!");

			}catch (IOException e){
				alertOnServiceError(true);
			}
		}

		/**
		 *
		 * @param isServer
		 */
		private void alertOnServiceError(boolean isServer){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			ButtonType close = new ButtonType("Close");
			alert.getButtonTypes().clear();
			alert.getButtonTypes().add(close);
			if (isServer){
				alert.setTitle("Server Error");
				alert.setHeaderText("A server has already been launched on this port");
				alert.setContentText("Please go back and setup client");
				alert.showAndWait();
				if (alert.getResult() == close){
					alert.close();
				}
			} else{
				alert.setTitle("Client Error");
				alert.setHeaderText("You must launch a server in order to play multiplayer");
				alert.setContentText("Please go back and setup server");
				alert.showAndWait();
				if (alert.getResult() == close){
					alert.close();;
				}
			}
		}

		/**
		 *
		 * @param ip
		 * @param port
		 * @param name
		 * @return
		 */
		private boolean checkValidInput(TextField ip, TextField port, TextField name){
			if (ip.getText().equals("") || port.getText().equals("") || name.getText().equals("")){
				return false;
			}
			return true;
		}

		/**
		 *
		 * @param input
		 * @return
		 * @throws Exception
		 */
		private String validateIp(TextField input) throws Exception {
				String ipAddress = InetAddress.getByName(input.getText()).getHostName();
				return ipAddress;
		}

		/**
		 *
		 * @param input
		 * @return
		 * @throws Exception
		 */
		private int validatePort(TextField input) throws Exception {
				int port = Integer.parseInt(input.getText());
				return port;
		}

		// this is static as we want each thread to be able to access it
		public static Socket[] connections = new Socket[2];

	public static void main(String[] args) {
		launch(args);
	}

}

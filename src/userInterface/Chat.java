package userInterface;

import java.io.DataOutputStream;
import java.io.IOException;

import javafx.geometry.Insets;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * This class creates the design for the chat. It also handles sending messages to the
 * server
 * @author barnettayl
 *
 */
public class Chat extends VBox {

	TextArea chatArea;
	TextField input;

	public Chat(){

		setId("chat");
		setPrefSize(560,110);
		chatArea = new TextArea();
		TextField input = new TextField();
		chatArea.setPrefSize(225, 80);
		chatArea.setEditable(false);
		input.setPrefSize(225, 30);
		input.setOnAction(event -> {
			// we will get our output stream

			String message = input.getText();
			input.clear();

			// send message to the server
			try {
				DataOutputStream out = new DataOutputStream(AppWindow.sock.getOutputStream());
				out.writeUTF("MSG");
				out.writeUTF(message);
				//System.out.println("sent to the server from: "+sock.getInetAddress());
			}catch (IOException e){
				e.printStackTrace();
			}
		});

		getChildren().addAll(chatArea, input);

	}

	public void appendMessage(String message){
		chatArea.appendText(message+"\n");
	}



}

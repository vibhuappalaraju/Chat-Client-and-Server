

package chatclient;


import javafx.event.ActionEvent;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.scene.Scene;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
//import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import day23network.observer.ChatClient.IncomingReader;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClientMain extends Application {
	Button Login;
	Button Register;
	Button sendButton;
	Button createid;
	Button signin;
	Button Back;
	Button setNickname;
	TextField Nickname;
	String therealusername;
	String fromperson;
	String toperson;
	boolean waiting = false;
	boolean success= false;
	String trouble = "!"; // the exclamation or other errors 
	String serioustrouble = "#"; // this will be for something not sure yet
	private TextField usernametext = new TextField();
	private PasswordField passwordtext = new PasswordField();
	private TextArea incoming= new TextArea();
	private TextField outgoing = new TextField();
	private BufferedReader reader;
	private PrintWriter writer;
	private Text errorsign = new Text();
	Stage primarystage;
	Label username;
	Label password;
	Stage something;
	Button logoff;
	String originalName;
	
	/// new stuff here 
//	static ArrayList<People> listofpeople = new ArrayList<People>();
//	static ArrayList<String> listofusers = new ArrayList<String>();
	
	public static void main (String [] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
		createwindow(primaryStage);
	}

	public void createwindow(Stage primary){
		//primarystage=primary;
		
		final Stage secondaryStage = new Stage();
		Pane layout = new Pane();
		layout.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
		username = new Label("Username:");
		username.setLayoutX(50);
		username.setLayoutY(20);
		username.setVisible(false);
		usernametext.setLayoutX(150);
		usernametext.setLayoutY(20);
		usernametext.setPromptText("Username");
		usernametext.setVisible(false);
		
		password = new Label("Password:");
		password.setLayoutX(50);
		password.setLayoutY(55);
		password.setVisible(false);
		passwordtext.setLayoutX(150);
		passwordtext.setLayoutY(55);
		passwordtext.setPromptText("Password");
		passwordtext.setVisible(false);
		
		Register = new Button("Register");
		Register.setLayoutX(200);
		Register.setLayoutY(150);
		Register.setVisible(false);
		Register.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String nom=usernametext.getText();
				String pass=passwordtext.getText();
				if(nom.length()>6 && pass.length()>6 ){
					// Register the user
					writer.println(trouble+"R" + nom + " " +pass);
					writer.flush();
					// put message you have registered successfully! 
					// block out register, username and password
				}
				else if(nom.length()<=6){
					errorsign.setText("Username is too short! ");
					errorsign.setFont(Font.font ("Verdana", 15));
					errorsign.setFill(Color.RED);
					errorsign.setLayoutX(150);
					errorsign.setLayoutY(100);
					errorsign.setVisible(true);
					playErrorSound();
					//put error message here saying username is too short
				}
				else if(pass.length()<=6){
					errorsign.setText("Password is too short! ");
					errorsign.setFont(Font.font ("Verdana", 15));
					errorsign.setFill(Color.RED);
					errorsign.setLayoutX(150);
					errorsign.setLayoutY(100);
					errorsign.setVisible(true);
					playErrorSound();
					// put error message saying password is too short
				}
//				else if (listofusers.contains(nom)){
//					errorsign.setText("This User already exists!  ");
//					errorsign.setFont(Font.font ("Verdana", 10));
//					errorsign.setFill(Color.RED);
//					errorsign.setLayoutX(150);
//					errorsign.setLayoutY(90);
//					errorsign.setVisible(true);
//					// pur error message saying user already exists! 					
//				}

			}
		});
		
		Login = new Button("Login");
		Login.setLayoutX(200);
		Login.setLayoutY(150);
		Login.setVisible(false);
		
		Login.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String key = usernametext.getText();
				String lock = passwordtext.getText();
				if(key.length()<=6){
					errorsign.setText("Sorry invalid Username!");
					errorsign.setFont(Font.font ("Verdana", 15));
					errorsign.setFill(Color.RED);
					errorsign.setLayoutX(150);
					errorsign.setLayoutY(100); 
					errorsign.setVisible(true);					
					playErrorSound();
				}
				else if(lock.length()<=6){
					errorsign.setText("Sorry invalid Password!");
					errorsign.setFont(Font.font ("Verdana", 15));
					errorsign.setFill(Color.RED);
					errorsign.setLayoutX(150);
					errorsign.setLayoutY(100); 
					errorsign.setVisible(true);
					playErrorSound();
				}
				else {
					waiting = true;
					writer.println(trouble+"L" + key + " " +lock);
					writer.flush();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(success){
						primarystage.show();
						secondaryStage.hide();
						success=false;
					}
					
				}
//				else if(!listofusers.contains(key)){
//					errorsign.setText("Sorry this User does not exist!  ");
//					errorsign.setFont(Font.font ("Verdana", 10));
//					errorsign.setFill(Color.RED);
//					errorsign.setLayoutX(150);
//					errorsign.setLayoutY(90); 
//					errorsign.setVisible(true);
//				}
//				else if(listofusers.contains(key)){
//					
//					for (People x:listofpeople){
//						if(x.getOnlinestatus()==1){
//							errorsign.setText("Sorry this User is already online!  ");
//							errorsign.setFont(Font.font ("Verdana", 10));
//							errorsign.setFill(Color.RED);
//							errorsign.setLayoutX(150);
//							errorsign.setLayoutY(90); 
//							errorsign.setVisible(true);
//							
//						}
//						else if(x.getUsername().equals(key) &&x.getPassword().equals(lock)){
//							x.onlinestatuson();
//							primary.show();
//						}
//					}
//				}
			}
		});
		
		Back = new Button("Back");
		Back.setLayoutX(20);
		Back.setLayoutY(150);
		Back.setVisible(false);
		Back.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				username.setVisible(false);
				usernametext.setVisible(false);
				password.setVisible(false);
				passwordtext.setVisible(false);
				createid.setVisible(true);
				signin.setVisible(true);
				Register.setVisible(false);
				Back.setVisible(false);
				Login.setVisible(false);
				errorsign.setVisible(false);
				passwordtext.clear();
				usernametext.clear();
				// set the password and stuff to visible
				//have a create button
				// back button
			}
		});
		createid = new Button("Signup");
		createid.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				username.setVisible(true);
				usernametext.setVisible(true);
				password.setVisible(true);
				passwordtext.setVisible(true);
				createid.setVisible(false);
				signin.setVisible(false);
				Register.setVisible(true);
				Back.setVisible(true);
				// set the password and stuff to visible
				//have a create button
				// back button
				
			}
		});
		createid.setLayoutX(160);
		createid.setLayoutY(80);
		signin = new Button("Sign in");
		signin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// make password and stuff visible
				// login button
				username.setVisible(true);
				usernametext.setVisible(true);
				password.setVisible(true);
				passwordtext.setVisible(true);
				createid.setVisible(false);
				signin.setVisible(false);
				Login.setVisible(true);
				Back.setVisible(true);
				
			}
		});
		signin.setLayoutX(160);
		signin.setLayoutY(130);
		
		layout.getChildren().addAll(username, usernametext,password,passwordtext,signin,createid, Login, Register, Back, errorsign);
		Scene scene2 = new Scene(layout, 400, 350, Color.ORANGE);
		//scene2.setFill(Color.ORANGE);
		secondaryStage.setScene(scene2);
		
		primary.setTitle("Simple Chat Client");
		Pane mainPanel = new Pane();
		mainPanel.setBackground(new Background(new BackgroundFill(Color.LIGHTSTEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		logoff = new Button("Logoff");
		logoff.setLayoutX(500);
		logoff.setLayoutY(350);
		logoff.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				writer.println(originalName + " (" + therealusername + ") " + " has left the server.");
				writer.flush();
				writer.println(trouble + "O" + originalName); // O  IS FOR logOUT ! 
				writer.flush();
				username.setVisible(false);
				usernametext.setVisible(false);
				password.setVisible(false);
				passwordtext.setVisible(false);
				createid.setVisible(true);
				signin.setVisible(true);
				Register.setVisible(false);
				Back.setVisible(false);
				Login.setVisible(false);
				errorsign.setVisible(false);
				passwordtext.clear();
				usernametext.clear();
				primarystage.hide();
				secondaryStage.show();
			// switchs screens
				//leave chatroom name
				//changeonline status
			}
		});
		Button setNickname = new Button("Set Nickname");
		setNickname.setLayoutX(250);
		setNickname.setLayoutY(350);
		Nickname=new TextField();
		Nickname.setPromptText("Enter new name here");
		Nickname.setLayoutX(50);
		Nickname.setLayoutY(350);
		setNickname.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Nicknameset();
				playNameSound();
			}
		});
		
		incoming.setLayoutX(20);
		incoming.setLayoutY(50);
		incoming.setEditable(false);
		
		outgoing.setLayoutX(50);
		outgoing.setLayoutY(290);
		sendButton = new Button();
		sendButton.setText("Send");
		sendButton.setLayoutX(565);
		sendButton.setLayoutY(290);
		sendButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(outgoing.getText().charAt(1)=='X' &&outgoing.getText().charAt(0)=='!' ){
					Serverchanges(outgoing.getText());
				} 
				else{//PRIVATE is X, for example !X Vibhu hey what's up
					originalName = therealusername;
					writer.println(therealusername+": "+outgoing.getText());
					writer.flush();
					outgoing.clear();}
					//outgoing.setText("");
					//outgoing.requestFocus();
				}
		});
		
		mainPanel.getChildren().addAll(outgoing,sendButton,incoming,setNickname, Nickname, logoff);
		//mainPanel.getChildren().add();
		Scene scene1 = new Scene(mainPanel, 650, 400);
		//frame.getChildren().add(BorderLayout.CENTER, mainPanel);
		//something = new Stage();
		primary.setScene(scene1);
		primarystage=primary;
		secondaryStage.show();
		//primary.show();
	}
	
	public void showprimary(){
		primarystage.show();
	}
	
	class IncomingReader implements Runnable{
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					if(message.length() > 0 && (message.charAt(0) == serioustrouble.charAt(0) || message.charAt(0) == trouble.charAt(0))){
						Serverchanges(message);
					}	
					else{
						incoming.appendText(message + "\n");
						playMessageRecievedSound();}
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	//sound method, plays a sound for specifically receiving a sound
	public void playMessageRecievedSound() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./KH_Menu_Message_Recieved.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	
	//sound method that plays a sound for specifically having an error
	public void playErrorSound() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./KH_Menu_Error.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	//sound method that plays a sound for specifically having an error
	public void playNameSound() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./KH_Menu_Nickname.wav").getAbsoluteFile());
	        Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	private void Nicknameset(){
		writer.println(therealusername + " changed their nickname to " + Nickname.getText());
		writer.flush();
		therealusername = Nickname.getText();
		Nickname.clear();
		playNameSound();
	}
	
	private void Serverchanges(String message){
		if(message.charAt(0)=='#'){//serious trouble
			if(message.charAt(1)=='A'){ // A for already online
				errorsign.setText("Sorry this User is already online!");
				errorsign.setFont(Font.font ("Verdana", 15));
				errorsign.setFill(Color.RED);
				errorsign.setLayoutX(150);
				errorsign.setLayoutY(100); 
				errorsign.setVisible(true);	
				playErrorSound();
			}
			else if(message.charAt(1)=='P'){// P  for wrong password
				errorsign.setText("Sorry wrong Password!");
				errorsign.setFont(Font.font ("Verdana", 15));
				errorsign.setFill(Color.RED);
				errorsign.setLayoutX(150);
				errorsign.setLayoutY(100); 
				errorsign.setVisible(true);
				playErrorSound();
			}
			else if(message.charAt(1)=='D'){//// D for the user does not exist
				errorsign.setText("That user does not exist!");
				errorsign.setFont(Font.font ("Verdana", 15));
				errorsign.setFill(Color.RED);
				errorsign.setLayoutX(150);
				errorsign.setLayoutY(100); 
				errorsign.setVisible(true);
				playErrorSound();
			}
			else if(message.charAt(1)=='E'){//error means user already exists
				errorsign.setText("That user exists!");
				errorsign.setFont(Font.font ("Verdana", 15));
				errorsign.setFill(Color.RED);
				errorsign.setLayoutX(150);
				errorsign.setLayoutY(100); 
				errorsign.setVisible(true);
				playErrorSound();
			}
			else if(message.charAt(1)=='R'){
				passwordtext.clear();
				Register.setVisible(false);
				username.setVisible(false);
				password.setVisible(false);
				usernametext.setVisible(false);
				passwordtext.setVisible(false);
				errorsign.setText("You Have Registered Successfully!");
				errorsign.setFont(Font.font ("Verdana", 20));
				errorsign.setFill(Color.BLACK);
				errorsign.setLayoutX(25);
				errorsign.setLayoutY(100);
				errorsign.setVisible(true);	
				playNameSound();
			}
		}
		else if(message.charAt(0)=='!'){//trouble
			if(message.charAt(1)=='L'){ // LOGIN
				therealusername = message.substring(message.indexOf('L') + 1, message.length());
				success=true;
			}
			else if(message.charAt(1)=='X'){//// X is for private
				 //PRIVATE is X, for example !X Vibhu Joseph hey what's up
				writer.println(message);
				writer.flush();
			}
		}
		waiting=false;
	}
	
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader);
		writer = new PrintWriter(sock.getOutputStream());
		System.out.println("networking established");
		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();
	}
}

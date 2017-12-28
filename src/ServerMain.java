

package chatclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;



import java.util.*;

public class ServerMain extends Observable {
	
	private ArrayList<People> ListofPeople = new ArrayList<People>();
	private ArrayList<Socket> SocketList = new ArrayList<Socket>();
	private String trouble = "!"; // the exclamation or other errors 
	private String serioustrouble = "#"; // this will be for something not sure yet
	public static void main(String[] args) {
		try {
			new ServerMain().setUpNetworking();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource")
		ServerSocket serverSock = new ServerSocket(4242);
		while (true) {
			Socket clientSocket = serverSock.accept();
			SocketList.add(clientSocket); //LALALA
			ClientObserver writer = new ClientObserver(clientSocket.getOutputStream());
			Thread t = new Thread(new ClientHandler(clientSocket));
			t.start();
			this.addObserver(writer);
			System.out.println("got a connection");
		}
	}
	class ClientHandler implements Runnable {
		private BufferedReader reader;
		private Socket sock;
		private PrintWriter sockWrite;
		private String loginname;

		public ClientHandler(Socket clientSocket) {
			sock = clientSocket;
			try {
				reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				sockWrite = new PrintWriter(sock.getOutputStream()); 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					
					if (message.length() > 0 && message.charAt(0) == trouble.charAt(0)) {
						syntax(message);
					} 
					else{
					System.out.println("server read "+message);
					setChanged();
					notifyObservers(message);}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			catch(NullPointerException e){
				message = "";
				notifyObservers(message);
			}
		}
		
		public void syntax(String message){
			if(message.charAt(1)=='R'){
				String userneme = message.substring(2, message.indexOf(" ") );
				String passwerd = message.substring(message.indexOf(" ")+1,message.length());
				People noob =new People();
				noob.setUsername(userneme);
				noob.setPassword(passwerd);
				for(People p:ListofPeople){
					if(p.getUsername().equals(userneme)){// error means user already exists
						sockWrite.println(serioustrouble + "E");// E for Exists
						sockWrite.flush();
						return;
					}
				}
				ListofPeople.add(noob);
				sockWrite.println(serioustrouble + "R");// R for registered
				sockWrite.flush();
			}
			else if((message.charAt(1)=='L')){
				String userneme = message.substring(2, message.indexOf(" ") );
				loginname = userneme;
				String passwerd = message.substring(message.indexOf(" ")+1,message.length());
				for(People p:ListofPeople){
					if(p.getUsername().equals(userneme) && p.getPassword().equals(passwerd) ){
						// LOGIN AND SHIT!!
						if(p.getOnlinestatus()==0){
							p.onlinestatuson();
							//make the javafx change and shit
							
							p.setPrintwriter(sockWrite);
							p.setPortnumber(sock.getPort());
							//p.onlinestatuson();
							p.setId(sock);
							p.getPrintwriter().println(trouble + "L" + userneme);
							p.getPrintwriter().flush();
							for(People x:ListofPeople){
								x.getPrintwriter().println(p.getUsername() + " joined the chat");							
								x.getPrintwriter().flush();}
						}
						else{
							// THIS MOTHERF is already online!!!
							sockWrite.println(serioustrouble + "A"); // A for Already online!
							sockWrite.flush();
						}
						
					}
					else if(p.getUsername().equals(userneme)){
						if(!(p.getPassword().equals(passwerd))){
							// SORRY WRONG PASSSWORD! 
							sockWrite.println(serioustrouble + "P"); // P  for wrong password
							sockWrite.flush();
						}
					}
					else{
						// THIS USER DOES NOT EXIST! 
						sockWrite.println(serioustrouble + "D"); // D for  does not exist
						sockWrite.flush();
					} 
				}

			}
			else if(message.charAt(1)=='X'){//for example !X Vibhu Joseph hey what's up -> !X,Vibhu,Joseph,...
				boolean found = false; // change name to user exists or online
				String[] communicate = message.split(" ");
				String fromperson = communicate[1];
				String command = message.replace("!X " + fromperson + " ", ""); // this becomes !X Joseph
				for(People p : ListofPeople){
					if(p.getUsername().equals(communicate[1])){
						if(p.getOnlinestatus()==1){
							try {
								PrintWriter PQ = new PrintWriter(p.getId().getOutputStream());
								PQ.println(loginname + "(Direct): " + command);
								PQ.flush();
								sockWrite.println(loginname + "(Direct): " + command);
								sockWrite.flush();
								found = true;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else{
							sockWrite.println("User not online.");
							sockWrite.flush();
							found = true;
						}
					}
				}
				if(!found){
					sockWrite.println("User not found.");
					sockWrite.flush();
				}
			}
			else if(message.charAt(1)=='O'){
				String userneme = message.substring(2, message.length());
				for(People p:ListofPeople){
					if(p.getUsername().equals(userneme)){
						p.setOnlinestatus(0);	
					}
				}
				
			}
		}
	}
}

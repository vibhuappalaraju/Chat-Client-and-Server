/* ChatRoom <People.java>
 * EE422C Project 7 submission by
 * Replace <...> with your actual data.
 * Vibhu Appalaraju
 * vka249
 * 16235
 * Joseph Bae
 * jb65632
 * 16235
 * Slip days used: <1>
 * Git URL: https://github.com/josephbae96/EE422C-Project7
 * Spring 2017
 */

package chatclient;

import java.io.PrintWriter;
import java.net.Socket;

public class People {
	private Socket id;
	private String username;
	private String password;
	private int onlinestatus=0;
	private int portnumber;
	private PrintWriter printwriter;
	
	public int getPortnumber() {
		return portnumber;
	}

	public void setPortnumber(int portnumber) {
		this.portnumber = portnumber;
	}

	public PrintWriter getPrintwriter() {
		return printwriter;
	}

	public void setPrintwriter(PrintWriter printwriter) {
		this.printwriter = printwriter;
	}
	
	public int getOnlinestatus() {
		return onlinestatus;
	}
	
	public void setOnlinestatus(int num){
		this.onlinestatus=num;
	}
	
	public void onlinestatuson(){
		this.onlinestatus=1;
	}
	
	public Socket getId() {
		return id;
	}
	
	public void setId(Socket id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

}

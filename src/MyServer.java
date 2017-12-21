import java.io.*;
import java.net.*;

public class MyServer {
	public static void main(String[] args) {
		try {
			// Starts the server
			ServerSocket ss = new ServerSocket(6666);
			System.out.println("Server running");
			
			// Confirms connection
			Socket s = ss.accept();	
			System.out.println("Client accepted");
			
			//Outputs to the client
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.writeUTF("Welcome to the server! Type 'stop' to close the connection at any time.");
			dout.flush();
			
			//Handles input from client
			DataInputStream dis = new DataInputStream(s.getInputStream());
			String str = "";
			
			//Waits for client input
			while(!str.equals("stop")) {
				str = (String) dis.readUTF();
				System.out.println("Client says " + str);
			}
			
			ss.close();
			System.out.println("Server closed");

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

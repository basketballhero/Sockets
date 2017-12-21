import java.io.*;
import java.net.*;

public class MyServer {
	public static void main(String[] args) {
		
		// Initializes variables
		ServerSocket ss = null;
		int clientCounter = 0;
		Socket s = null;
		clientThread[] threads = new clientThread[10];
		
		// Starts the server
		try {
			ss = new ServerSocket();
			ss.bind(new InetSocketAddress("10.30.136.238", 6666));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Server running");		
		
		// Creates a new socket whenever a client joins and then starts a thread to communicate with said client
		while(clientCounter < 10) {
			try {
				s = ss.accept();
				System.out.println("Client accepted");
				
				for(int i = 0; i < threads.length; i++) {
					if(threads[i] == null) {
						(threads[i] = new clientThread(s, threads)).start();
						break;
					}
				}
				
				clientCounter++;
				
			} catch (IOException e) {
				e.printStackTrace();
			}	
		}
		
		//Outputs to the client
		//DataOutputStream dout = new DataOutputStream(s.getOutputStream());
		//dout.writeUTF("Welcome to the server! Type 'stop' to close the connection at any time.");
		//dout.flush();
		
		
		//Closes server automatically
		try {
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Server closed");

	}
	
	static class clientThread extends Thread {
		
		Socket clientSocket;
		clientThread[] threads;
		DataOutputStream dout;
		DataInputStream din;
		String name;
		
		public clientThread(Socket clientSocket, clientThread[] threads) {
			this.clientSocket = clientSocket;
			this.threads = threads;
			
			try {
				this.dout = new DataOutputStream(clientSocket.getOutputStream());
				this.din = new DataInputStream(clientSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

		public void run() {
			//Executed as soon as the client connects: tells each client that a new client has joined
			try {
				dout.writeUTF("What is your name?");
				name = din.readUTF();
				dout.flush();
				
				dout.writeUTF("Welcome, " + name + " to the server!");
				
				for(int i = 0; i < threads.length; i++) {
					if(threads[i] != null && threads[i] != this) {
						threads[i].dout.writeUTF(name + " has joined the server!");
						threads[i].dout.flush();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			//Main loop that echoes input coming from this client to all other clients
			String message = "";
			try {
				while(true) {
					message = din.readUTF();
					if(message.equals("exit")) {
						break;
					}
					for(int i = 0; i < threads.length; i++) {
						if(threads[i] != null && threads[i] != this) {
							threads[i].dout.writeUTF("<" + name + "> " + message);
							threads[i].dout.flush();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//When this client types "exit", the loop is terminated and the following exit code is executed
			
			//Says goodbye to the client
			try {
				dout.writeUTF("*** Bye " + name + " ***");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			//Tells everyone else that this client just left
			for(int i = 0; i < threads.length; i++) {
				if(threads[i] != null) {
					try {
						threads[i].dout.writeUTF("A client has left");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			//Declares itself to be null to make space for more clients
			for(int i = 0; i < threads.length; i++) {
				if(threads[i] == this) {
					threads[i] = null;
				}
			}
			
			//Closes the readers
			try {
				din.close();
				dout.close();
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}

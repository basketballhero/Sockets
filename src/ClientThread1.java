import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread1 extends Thread {
	private Socket socket;
	PrintStream pw;
	DataOutputStream dout;
	ClientThread1[] threads;
	
	public ClientThread1(Socket clientSocket, ClientThread1 threads[]) throws IOException {
		this.socket = clientSocket;
		this.threads = threads;
		pw = new PrintStream(socket.getOutputStream(), true);
	}
	
	public String toString() {
		return "I am a thread";
	}
	
	public Socket getSocket() throws Exception {
		if(socket == null) {
			System.out.println("SOCKET IS NULL");
			throw new Exception();
			
		}
		return socket;
	}
	
	//Run method is what gets executed when the thread gets called
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String message;
		DataOutputStream dout;
		
		try {
			dout = new DataOutputStream(socket.getOutputStream());			
		} catch (IOException e) {
			e.printStackTrace();
			scanner.close();
			return;
		}
		
		for(int i = 0; i < threads.length; i++) {
			if(threads[i] != null && threads[i] != this) {
				threads[i].pw.println("Client entered");
				try {
					dout.writeUTF("Client entered");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Sending message to client at index i = " + i);
			}
		}
		
		//Sends input from client to server
		while(true) {
			try {
				message = scanner.nextLine();
				if(message.equalsIgnoreCase("QUIT") || message == null) {
					scanner.close();
					return;
				}
				dout.writeUTF(message);
				dout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

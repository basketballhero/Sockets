import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyClient implements Runnable {
	
	static Socket socket;
	static DataOutputStream dout;
	static DataInputStream din;
	static boolean closed;
	
	public static void main(String[] args) {
		//Initializes socket and dout
		socket = null;
		dout = null;
		try {
			socket = new Socket("localhost", 6666);
			dout = new DataOutputStream(socket.getOutputStream());
			din = new DataInputStream(socket.getInputStream());
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Scanner scanner = new Scanner(System.in);
		
		String message = "";
		new Thread(new MyClient()).start();
		while((message = scanner.nextLine()) != "exit") {
			try {
				dout.writeUTF(message);
				dout.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		closed = true;
		scanner.close();
		try {
			socket.close();
			dout.close();
			din.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Second thread that prints whatever comes in from the server (basically the other clients)
	@Override
	public void run() {
		String input = "";
		while(!closed) {
			try {
				input = din.readUTF();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(input);
		}
	}
	
	
}

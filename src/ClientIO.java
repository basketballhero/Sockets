import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientIO extends Thread {
	Socket socket;
	DataInputStream dis;
	
	public ClientIO (Socket clientSocket) {
		this.socket = clientSocket;
		try {
			dis = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		System.out.println("Listening");
		String message = "";
		while(true) {
			try {
				message = dis.readUTF();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Server says " + message);
		}
	}
}

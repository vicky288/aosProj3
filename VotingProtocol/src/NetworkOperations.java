

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkOperations {
	private static NetworkOperations instance = null;

	private ServerSocket serverSocket;
	private Socket clientSocket;
	private NetworkOperations() {

	}
	//Singleton Class // Easy to use the same object throughout
	public static NetworkOperations getNetworkOperations() {
		if(instance == null) {
			//System.out.println("NetworkOperations Instance is null");
			instance = new NetworkOperations();
		}
		return instance;
	}
	
	
	
	
	
	
	//Methods For Sending Message
	public void initializeClientToSend(String serverName, int portNumber){
		//System.out.println("Client initializing");
		try {
			clientSocket = new Socket(serverName,portNumber);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(MessageFormat message) {
		//System.out.println("sending 1 message");
		try {
			OutputStream os= clientSocket.getOutputStream();
			ObjectOutputStream objos = new ObjectOutputStream(os);
			objos.writeObject(message);
		} catch (Exception ex) {
			
		}

	}

	public void sendToNode(String server, int port, MessageFormat message) throws Exception {
		//System.out.println("--Sending to Node--");
		initializeClientToSend(server,port);
		send(message);
//		closeClientConnection();
		//System.out.println("--Message Sent to Node--");
	}
	public void closeClientConnection() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Socket startClient(String serverName, int portNumber){
		try {
			clientSocket = new Socket(serverName,portNumber);
			return clientSocket;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean closeSocket(){
		try {
			clientSocket.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ServerSocket startServer(int port){
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverSocket;
	}
	
	public boolean closeServer(ServerSocket serverSocket){
		try {
			serverSocket.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void sendMessage(MessageFormat message, Socket clientSocket){
		
		try {
			OutputStream outputStream= clientSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MessageFormat receiveMessage(){
		try {
			Socket connectionSocket = serverSocket.accept();
			InputStream is = connectionSocket.getInputStream();
			ObjectInputStream retrieveStream = new ObjectInputStream(is);
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("In receiveMessage of NetworkOperations.java");
				e.printStackTrace();
			}
			MessageFormat messageReceived = (MessageFormat) retrieveStream.readObject();
			return messageReceived;
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

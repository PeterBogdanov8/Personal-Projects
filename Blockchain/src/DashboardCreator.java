import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

/**
 * The class assembles and adds functionality to the components of the GUI for the dash board
 * @author PO Bogdanov 218029215
 *
 */
public class DashboardCreator extends GridPane {
	/**
	 * Connects the user to the host 
	 */
	private Button connectButton;
	/**
	 * Displays the standings on the submissionArea 
	 */
	private Button standingsButton;
	/**
	 * The host's responses and standings will be displayed here
	 */
	private TextArea submissionArea;
	/**
	 * The socket that will be used to connect to the host
	 */
	private DatagramSocket senderSocket;
	/**
	 * The instance of the client that connected to the host
	 */
	private ClientHandler clientHandler;
	
	/**
	 * Constructor
	 */
	public DashboardCreator() {
		assembleDashboard();
		
		connectButton.setOnAction(e->{
			try {
				sendMessage("HI");
				String rcvString=receiveMessage();
				submissionArea.setText(rcvString +"\n");
				
				if (rcvString.equals("HELO FROM HOST")) {
					Object object=receiveObject();
					if (object instanceof ClientHandler) {
						clientHandler=(ClientHandler) object;
						submissionArea.appendText("Connected successfully \n");
						submissionArea.appendText("Information: \n");
						submissionArea.appendText("Graph State: \n" +clientHandler.getClients().toString() +"\n");
						submissionArea.appendText("Blockchain State: \n" +clientHandler.getBlockChain().toString());
					}
				}
			} catch (SocketException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} finally {
				senderSocket.close();
			}
		});
		
		standingsButton.setOnAction(e->{
			clientHandler.getBlockChain().standings(submissionArea);
		});
	}
	
	/**
	 * Assembles the GUI
	 */
	private void assembleDashboard() {
		connectButton=new Button("Connect");
		standingsButton=new Button("Standings");
		submissionArea=new TextArea();
		add(submissionArea, 0, 0, 2, 1);
		add(connectButton, 0, 1);
		add(standingsButton, 1, 1);
	}
	
	/**
	 * Sends a message to the host
	 * @param message The message that will be sent to the host
	 * @throws IOException
	 */
	private void sendMessage(String message) throws IOException {
		senderSocket=new DatagramSocket();
		System.out.println("Sending...");
		InetAddress hostAddress=InetAddress.getLocalHost();
		byte[] sndBytes=message.getBytes();
		DatagramPacket sndPacket=new DatagramPacket(sndBytes, sndBytes.length, hostAddress, 1236);
		senderSocket.send(sndPacket);
	}
	
	/**
	 * Receives a message from the host
	 * @return The message received from the host
	 * @throws IOException
	 */
	private String receiveMessage() throws IOException {
		byte[] rcvBytes=new byte[6000];
		System.out.println("Waiting for Packet...");
		DatagramPacket rcvPacket=new DatagramPacket(rcvBytes, rcvBytes.length);
		senderSocket.receive(rcvPacket);
		String rcvString=new String(rcvPacket.getData()).trim();
		return rcvString;
	}
	
	/**
	 * Receives an object from the host
	 * @return The object received from the host
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object receiveObject() throws IOException, ClassNotFoundException {
		byte[] rcvBytes2=new byte[60000];
		DatagramPacket rcvPacket2=new DatagramPacket(rcvBytes2, rcvBytes2.length);
		senderSocket.receive(rcvPacket2);
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(rcvBytes2);
		ObjectInputStream objectInputStream=new ObjectInputStream(new BufferedInputStream(byteArrayInputStream));
		Object object=objectInputStream.readObject();
		return object;
	}
}

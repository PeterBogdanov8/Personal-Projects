
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * The class assembles and adds functionality to the components of the political party adder
 * @author PO Bogdanov 218029215
 *
 */
public class PartyAdderCreator extends GridPane{
	/**
	 * Label for the political party
	 */
	private Label partyLabel;
	/**
	 * Text field for entering the political party that will participate in the elections
	 */
	private TextField partyField;
	/**
	 * Connects the user to the host
	 */
	private Button connectButton;
	/**
	 * Add political parties to the ArrayList politicalParties
	 */
	private Button addButton;
	/**
	 * Submits the political parties that will participate in the election
	 */
	private Button submitButton;
	/**
	 * The socket that will be used to connect to the host
	 */
	private DatagramSocket senderSocket;
	/**
	 * The instance of the client that connected to the host
	 */
	@SuppressWarnings("unused")
	private ClientHandler clientHandler;
	/**
	 * Stores all political parties that will participate in the election
	 */
	private ArrayList<PoliticalParty> politicalParties;
	
	/**
	 * Constructor
	 */
	public PartyAdderCreator() {
		politicalParties=new ArrayList<>();
		assembleAdder();
		connectButton.setOnAction(e->{
			try {
				sendMessage("HI");
				String rcvString=receiveMessage();
				//submissionArea.setText(rcvString +"\n");
				
				if (rcvString.equals("HELO FROM HOST")) {
					Object object=receiveObject();
					if (object instanceof ClientHandler) {
						clientHandler=(ClientHandler) object;
						//submissionArea.appendText("Connected successfully \n");
						//submissionArea.appendText("Information: \n");
						//submissionArea.appendText("Graph State: \n" +clientHandler.getClients().toString() +"\n");
						//submissionArea.appendText("Blockchain State: \n" +clientHandler.getBlockChain().toString());
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
		
		addButton.setOnAction(e->{
			PoliticalParty politicalParty=new PoliticalParty(partyField.getText(), 0);
			boolean isAdd=false;
			for (PoliticalParty party : politicalParties) {
				if (party.equals(politicalParty)) {
					isAdd=true;
				}
			}
			if (!isAdd) {
				politicalParties.add(politicalParty);
			}
		});
		
		submitButton.setOnAction(e->{
			try {
				sendMessage("ADD PARTIES");
				sendObject(politicalParties);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}
	
	/**
	 * Constructs the GUI that will be used by the election official to add the political parties participating in the election
	 */
	private void assembleAdder() {
		partyLabel=new Label("Political Party: ");
		partyField=new TextField();
		connectButton=new Button("Connect");
		addButton=new Button("Add");
		submitButton=new Button("Submit");
		add(partyLabel, 0, 0);
		add(partyField, 1, 0 , 2, 1);
		add(connectButton,0, 1);
		add(addButton, 1, 1);
		add(submitButton, 2, 1);
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
	 * Sends an object to the host
	 * @param object The object being sent
	 * @throws IOException
	 */
	private void sendObject(Object object) throws IOException {
		InetAddress hostAddress=InetAddress.getLocalHost();
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(5000);
		ObjectOutputStream objectOutputStream=new ObjectOutputStream(new BufferedOutputStream(byteArrayOutputStream));
		objectOutputStream.flush();
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		byte[] sndBytes=byteArrayOutputStream.toByteArray();
		DatagramPacket sndPacket=new DatagramPacket(sndBytes, sndBytes.length, hostAddress, 1236);
		senderSocket.send(sndPacket);
		System.out.println("Packet sent");
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

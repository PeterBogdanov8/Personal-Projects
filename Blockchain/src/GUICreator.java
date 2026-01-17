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

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * The class assembles and adds functionality to the components of the GUI that the voters will use
 * @author PO Bogdanov 218029215
 *
 */
public class GUICreator extends GridPane implements java.io.Serializable {
	/**
	 * Serial ID of the GUICreartor
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Label for the identity number
	 */
	private Label idLabel;
	/**
	 * Label for the age
	 */
	private Label ageLabel;
	/**
	 * Label for the political party
	 */
	private Label partyLabel;
	/**
	 * Text field for entering the voter's identity number
	 */
	private TextField idField;
	/**
	 * Text field for entering the voter's age
	 */
	private TextField ageField;
	/**
	 * Text field for entering the political party the voter wants to vote for
	 */
	private TextField partyField;
	/**
	 * Allows the user to be the host
	 */
	private Button hostButton;
	/**
	 * Connects the user to the host
	 */
	private Button connectButton;
	/**
	 * Submits the vote to the host
	 */
	private Button submitButton;
	/**
	 * Shows the election standings on the submissionArea
	 */
	private Button standingsButton;
	/**
	 * The host's responses, information, and standings will be displayed here
	 */
	private TextArea submissionArea;
	/**
	 * The socket connects the user to the host
	 */
	private DatagramSocket senderSocket;
	/**
	 * The instance of the GUICreator's client that connected to the host
	 */
	private ClientHandler clientHandler;
	
	/**
	 * Constructor
	 */
	public GUICreator() { 
		assembleGUI();
		clientHandler=new ClientHandler(1236);
		
		hostButton.setOnAction(e->{
			clientHandler.createClient(submissionArea);
		});	
		
		connectButton.setOnAction(e->{
			try {
				//Send a message
				sendMessage("HI");
				String rcvString=receiveMessage();
				submissionArea.setText(rcvString +"\n");
				//Receive the response
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
		
		submitButton.setOnAction(e->{
			
			String id=idField.getText();
			String party=partyField.getText();
			int age=Integer.parseInt(ageField.getText());
			char[] idChar=id.toCharArray();
			//InetAddress hostAddress=null;
			
			if (age<18) {
				submissionArea.setText("Citizens under the age of 18 cannot vote.");
			} else if (idChar.length!=13) {
				submissionArea.setText("Invalid ID. ID numbers have 13 digits.");
			} else {
				try {
					senderSocket=new DatagramSocket();
					//hostAddress=InetAddress.getLocalHost();
					
					sendMessage("ADD");
					
					Vote vote=new Vote(id, age, party);
					Record block=null;
					
					if (clientHandler.getBlockChain().size()==0) {
						block=new Record(clientHandler.getBlockChain().size(), null, vote);
						block.mine();
					} else {
						block=new Record(clientHandler.getBlockChain().size(),clientHandler.getBlockChain().getLastRecord().getCurHash(),vote);
						block.mine();
					}
					//Submit the vote
					sendObject(block);
					//Receive the response
					String rcvString=receiveMessage();
					submissionArea.setText(rcvString +"\n");
					
					if (rcvString.equals("RECORD VERIFIED BY  HOST") || rcvString.equals("RECORD NOT VERIFIED BY  HOST")) {
						Object object=receiveObject();
						if (object instanceof ClientHandler) {
							clientHandler=(ClientHandler) object;
							submissionArea.appendText("Vote Submited \n");
							submissionArea.appendText("Information: \n");
							submissionArea.appendText("Graph State: \n" +clientHandler.getClients().toString() +"\n");
							submissionArea.appendText("Blockchain State: \n" +clientHandler.getBlockChain().toString() +"\n");
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
					if (senderSocket!=null) {
						senderSocket.close();
					}
				}
			}
			
			
		});
		standingsButton.setOnAction(e->{
			//Display the standings
			clientHandler.getBlockChain().standings(submissionArea);
		});
	}
	
	/**
	 * Constructs the GUI that will be used by the voter
	 */
	public void assembleGUI() {
		//Assemble the GUI
		setAlignment(Pos.CENTER);
		setHgap(22);
		setVgap(10);
		idLabel=new Label("Identity Number: ");
		ageLabel=new Label("Age: ");
		partyLabel=new Label("Political Party: ");
		idField=new TextField();
		ageField=new TextField();
		partyField=new TextField();
		hostButton=new Button("Host");
		connectButton=new Button("Connect");
		submitButton=new Button("Submit");
		standingsButton=new Button("Standings");
		submissionArea=new TextArea();
		add(idLabel, 0, 0);
		add(idField, 1, 0);
		add(ageLabel, 0, 1);
		add(ageField, 1, 1);
		add(partyLabel, 0, 2);
		add(partyField, 1, 2);
		add(hostButton, 0, 3);
		add(connectButton, 1, 3);
		add(submitButton, 0, 4);
		add(standingsButton, 1, 4);
		add(submissionArea, 0, 5, 2, 1);
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

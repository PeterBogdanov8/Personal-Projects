
import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import javafx.scene.control.TextArea;


/**
 * The class deals with connections between clients
 * @author PO Bogdanov 218029215
 *
 */
public class ClientHandler implements Comparable<ClientHandler>, Serializable {
	/**
	 * serial id of the block chain
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The last client that sent a packet
	 */
	private ClientHandler sender;
	/**
	 * The blockchain
	 */
	private BlockChain blockChain=new BlockChain();
	/**
	 * The clients connected to network
	 */
	private Graph<ClientHandler> clients=new Graph<>();
	/**
	 * The port number on which this client runs on
	 */
	private int port;
	/**
	 * The serialised version of the public key
	 */
	private byte[] publicKey;
	/**
	 * The serialised version of the private key
	 */
	private byte[] privateKey;
	
	/**
	 * Constructor
	 * @param port The port number on which this client runs on
	 */
	public ClientHandler(int port) {
		this.blockChain = new BlockChain();
		this.clients = new Graph<>();
		this.port = port;
		generatKeys();
	}
	
	/**
	 * Creates a client that can send and received packets
	 * @param textArea The text area that will be updated
	 */
	@SuppressWarnings("unchecked")
	public void createClient(TextArea textArea) {
		DatagramSocket receiverSocket=null;
		DatagramSocket senderSocket=null;
		try {
			//Run on a specified port (store in the integer port)
			receiverSocket=new DatagramSocket(port);
			//receive the packet
			DatagramPacket rcvPacket=receivePacket(receiverSocket);
			//Convert the  packet's data to a string
			String rcvString=new String(rcvPacket.getData()).trim();
			
			System.out.println(rcvString);
			//Process the "HI" command
			if (rcvString.equals("HI")) {
				//Create an instance of the user that sent the packet (the sender)
				sender=new ClientHandler(port);
				sender.setSender(sender);
				System.out.println(clients.getVertices().size());
				if (clients.getVertices().size()<2) { //If this is the first connection taking place in the network
					//Add the the sender to the graph
					Graph.Vertex<ClientHandler> vertex1=new Graph.Vertex<ClientHandler>(this);
					Graph.Vertex<ClientHandler> vertex2=new Graph.Vertex<ClientHandler>(sender);
					Graph.Edge<ClientHandler> edge=new Graph.Edge<>(0, vertex1, vertex2);
					Collection<Graph.Vertex<ClientHandler>> vertices=new ArrayList<>();
					Collection<Graph.Edge<ClientHandler>> edges=new ArrayList<>();
					vertices.add(vertex1);
					vertices.add(vertex2);
					edges.add(edge);
					clients=new Graph<>(vertices, edges);
					//Update the graphs and blockchain of the clients
					updateClients();
				} else {
					//Add the the sender to the graph
					ArrayList<Graph.Vertex<ClientHandler>> vertices=(ArrayList<Graph.Vertex<ClientHandler>>) clients.getVertices();
					ArrayList<Graph.Edge<ClientHandler>> edges= (ArrayList<Graph.Edge<ClientHandler>>) clients.getEdges();
					Graph.Vertex<ClientHandler> vertex=new Graph.Vertex<>(sender);
					vertices.add(vertex);
					Random random=new Random();
					int index=random.nextInt(vertices.size());
					Graph.Edge<ClientHandler> edge=new Graph.Edge<>(0, vertex, vertices.get(index));
					edges.add(edge);
					clients=new Graph<>(new Graph<>(vertices, edges));
					//Update the graphs and blockchain of the clients
					updateClients();
				}
				//Display the host information
				textArea.setText("Host Information: \n");
				textArea.appendText("Graph State: " +clients.toString() +"\n");
				textArea.appendText("Blockchain State: " +blockChain.toString() +"\n");
				//Send a response to the user that sent the packet
				sendMessage("HELO FROM HOST", rcvPacket);
				sendObject(sender, rcvPacket);
			} else if (rcvString.equals("ADD")) { //Process the "ADD" command
				//Receive the object
				Object object=receiveObject(receiverSocket);
				if (object instanceof Record) {
					Record record=(Record) object;
					
					//Encrypt the record
					Cipher cipher=Cipher.getInstance("RSA");
					PrivateKey privateKey=(PrivateKey) deserialiseSerialisedObject(this.privateKey);
					PublicKey publicKey=(PublicKey) deserialiseSerialisedObject(this.publicKey);
					
					cipher.init(Cipher.ENCRYPT_MODE, privateKey);
					SealedObject encryptedRecord=new SealedObject(record, cipher);
					
					ArrayList<Graph.Vertex<ClientHandler>> vertices=(ArrayList<Graph.Vertex<ClientHandler>>) clients.getVertices();
					Random random=new Random();
					int index=random.nextInt(vertices.size());
					//Choose a random client to verify the encrypted record
					ClientHandler verifier=vertices.get(index).getValue();
					//If the block was verified
					if (verifier.checkBlock(encryptedRecord, blockChain.getLastRecord(), this)) {
						//Add the block to the blockchain
						blockChain.add(encryptedRecord, publicKey);
						System.out.println("Verified");
						//Respond to the user that sent record
						sendMessage("RECORD VERIFIED BY  HOST", rcvPacket);
						//Display the host information
						textArea.setText("Host Information: \n");
						textArea.appendText("Graph State: " +clients.toString() +"\n");
						textArea.appendText("Blockchain State: " +blockChain.toString() +"\n");
					} else {
						System.out.println("Not verified");
						//Respond to the user that sent record
						sendMessage("RECORD NOT VERIFIED BY  HOST", rcvPacket);
						//Display the host information
						textArea.setText("Host Information: \n");
						textArea.appendText("Graph State: " +clients.toString() +"\n");
						textArea.appendText("Blockchain State: " +blockChain.toString() +"\n");
					}
					System.out.println(blockChain.toString());
				}
				//Update the graphs and block chains of the clients
				updateClients();
				sendObject(sender, rcvPacket);
			} else if (rcvString.equals("ADD PARTIES")) { //Process the "ADD PARTIES" command
				Object object=receiveObject(receiverSocket);
				if (object instanceof ArrayList) {
					//Change the list of the political parties that will be running in the elections  
					ArrayList<PoliticalParty> politicalParties=(ArrayList<PoliticalParty>) object;
					blockChain.setPoliticalParties(politicalParties);
				}
				//Update the graphs and block chains of the clients
				updateClients();
			} 
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} finally {
			if (receiverSocket!=null) {
				receiverSocket.close();
			}
			
			if (senderSocket!=null) {
				senderSocket.close();
			}
		}
	}

	/**
	 * Compares the clients based on the port number they are running on
	 * @param The client that will be compared
	 */
	@Override
	public int compareTo(ClientHandler o) {
		return port-o.port;
	}
	
	/**
	 * Updates the graphs and block chains of the clients
	 */
	private void updateClients() {
		for (int i = 0; i < clients.getVertices().size(); i++) {
			//Update the block chains of the clients
			clients.getVertices().get(i).getValue().setBlockChain(blockChain);
			//Update the graphs of the clients
			clients.getVertices().get(i).getValue().setClients(clients);
		}
		//Update the block chain and graph of the sender
		sender.setBlockChain(blockChain);
		sender.setClients(clients);
	}
	
	/**
	 * Checks if the encypted record is a valid block
	 * @param encrytedRecord The the encypted record
	 * @param prevRecord The last record added to the block chain
	 * @param clientHandler The client that encrypted the record
	 * @return Returns true if the encypted record is a valid block otherwise false.
	 * @throws NoSuchAlgorithmException
	 * @throws RuntimeException
	 * @throws InvalidKeyException
	 * @throws ClassNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 */
	@SuppressWarnings("unused")
	public boolean checkBlock(SealedObject encrytedRecord, Record prevRecord, ClientHandler clientHandler) throws NoSuchAlgorithmException, RuntimeException, InvalidKeyException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException, NoSuchPaddingException {
		//List of ids of the users that voted
		ArrayList<String> ids=blockChain.getIds();
		//Decrypt the record
		Record recordToChecked=null;
		ArrayList<Graph.Vertex<ClientHandler>> vertics= (ArrayList<Graph.Vertex<ClientHandler>>) clientHandler.getClients().getVertices();
		for (Graph.Vertex<ClientHandler> vertex : vertics) {
			if (clientHandler.equals(vertex.getValue())) {
				PublicKey key= (PublicKey) deserialiseSerialisedObject(vertex.getValue().getPublicKey());
				Cipher cipher=Cipher.getInstance("RSA");
				cipher.init(Cipher.DECRYPT_MODE, key);
				recordToChecked=(Record) encrytedRecord.getObject(cipher);
			}
		}
		
		//Check if the user has already voted
		for (String id : ids) {
			if (recordToChecked.getVote().getId().equals(id)) {
				return false;
			}
		}
		
		// Check if the record is valid
		if (!recordToChecked.getCurHash().startsWith("0")) {
			return false;
		}
		
		if (prevRecord==null) {
			if (recordToChecked.getI()!=0) {
				return false;
			}
			
			if (recordToChecked.getPrevHash()!=null) {
				return false;
			}
			
			if (recordToChecked.getCurHash()==null || !recordToChecked.calcHash().equals(recordToChecked.getCurHash())) {
				return false;
			}
			return true;
		} else {
			if (recordToChecked!=null) {
				if ((prevRecord.getI()+1) != recordToChecked.getI()) {
					return false;
				}
				
				if (recordToChecked.getPrevHash()==null || !recordToChecked.getPrevHash().equals(prevRecord.getCurHash())) {
					return false;
				}
				
				if (recordToChecked.getCurHash()==null || !recordToChecked.calcHash().equals(recordToChecked.getCurHash())) {
					return false;
				}
				return true;
			}
			
			return false;
		}
	}

	/**
	 * Returns the port number on which this client runs on
	 * @return The port number on which this client runs on
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Returns the blockchain
	 * @return  The blockchain
	 */
	public BlockChain getBlockChain() {
		return blockChain;
	}

	/**
	 * Returns the clients connected to network
	 * @return The clients connected to network
	 */
	public Graph<ClientHandler> getClients() {
		return clients;
	}
	
	/**
	 * Returns the last client that sent a packet
	 * @return The last client that sent a packet
	 */
	public ClientHandler getSender() {
		return sender;
	}
	
	/**
	 * Returns the serialised version of the public key
	 * @return The serialised version of the public key 
	 */
	public byte[] getPublicKey() {
		return publicKey;
	}
	
	/**
	 * Returns the serialised version of the private key
	 * @return The serialised version of the private key
	 */
	public byte[] getPrivateKey() {
		return privateKey;
	}

	/**
	 * Changes the block chain of the client 
	 * @param blockChain The new block chain of the client
	 */
	public void setBlockChain(BlockChain blockChain) {
		this.blockChain = blockChain;
	}
	
	/**
	 * Changes the graph of the client
	 * @param clients The new graph of the client
	 */
	public void setClients(Graph<ClientHandler> clients) {
		this.clients = clients;
	}
	
	/**
	 * Changes the sender of the client
	 * @param sender The new sender of the client
	 */
	public void setSender(ClientHandler sender) {
		this.sender = sender;
	}
	
	/**
	 * Sends a message to the user that sent a packet
	 * @param message The message that will be sent to the user
	 * @param rcvPacket The packet that the user sent
	 * @throws IOException
	 */
	private void sendMessage(String message, DatagramPacket rcvPacket) throws IOException {
		DatagramSocket senderSocket=new DatagramSocket();
		System.out.println("Sending...");
		byte[] sndBytes=message.getBytes();
		DatagramPacket sndPacket=new DatagramPacket(sndBytes, sndBytes.length, rcvPacket.getAddress(), rcvPacket.getPort());
		senderSocket.send(sndPacket);
		senderSocket.close();
	}
	
	/**
	 * Receive a message from a client that connected to the host
	 * @param receiverSocket The socket on which the client and the host are connect to
	 * @return The packet that the client sent
	 * @throws IOException
	 */
	private DatagramPacket receivePacket(DatagramSocket receiverSocket) throws IOException {
		byte[] rcvBytes=new byte[6000];
		System.out.println("Waiting for Packet...");
		DatagramPacket rcvPacket=new DatagramPacket(rcvBytes, rcvBytes.length);
		receiverSocket.receive(rcvPacket);
		//String rcvString=new String(rcvPacket.getData()).trim();
		//receiverSocket.close();
		return rcvPacket;
	}
	
	/**
	 * Sends an object to the user that recently send a packet to the host
	 * @param object The object being sent
	 * @param rcvPacket The packet the user sent
	 * @throws IOException
	 */
	private void sendObject(Object object, DatagramPacket rcvPacket) throws IOException {
		DatagramSocket senderSocket=new DatagramSocket();
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream(5000);
		ObjectOutputStream objectOutputStream=new ObjectOutputStream(new BufferedOutputStream(byteArrayOutputStream));
		objectOutputStream.flush();
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		byte[] sndBytes=byteArrayOutputStream.toByteArray();
		DatagramPacket sndPacket=new DatagramPacket(sndBytes, sndBytes.length, rcvPacket.getAddress(), rcvPacket.getPort());
		senderSocket.send(sndPacket);
		System.out.println("Packet sent");
		senderSocket.close();
	}
	
	/**
	 * Received a packet from a client that connected to the host
	 * @param receiverSocket The socket on which the client and the host are connect to
	 * @return The object that was received from the client that connected to the host
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object receiveObject(DatagramSocket receiverSocket) throws IOException, ClassNotFoundException {
		byte[] rcvBytes2=new byte[60000];
		DatagramPacket rcvPacket=new DatagramPacket(rcvBytes2, rcvBytes2.length);
		receiverSocket.receive(rcvPacket);
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(rcvBytes2);
		ObjectInputStream objectInputStream=new ObjectInputStream(new BufferedInputStream(byteArrayInputStream));
		Object object=objectInputStream.readObject();
		return object;
	}
	
	/**
	 * Serialises objects
	 * @param object The object that will be serialised
	 * @return The serialised version of the object
	 * @throws IOException
	 */
	public byte[] serialiseObject(Object object) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream=new ObjectOutputStream(new BufferedOutputStream(byteArrayOutputStream));
		objectOutputStream.writeObject(object);
		objectOutputStream.flush();
		byte[] byteArray=byteArrayOutputStream.toByteArray();
		objectOutputStream.close();
		byteArrayOutputStream.close();
		return byteArray;
	}
	
	/**
	 * Dederialises serialised objects
	 * @param serialisedObject The serialised version of the object
	 * @return The deserialised object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object deserialiseSerialisedObject(byte[] serialisedObject) throws IOException, ClassNotFoundException {
		Object object;
		ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(serialisedObject);
		ObjectInputStream objectInputStream=new ObjectInputStream(new BufferedInputStream(byteArrayInputStream));
		object=objectInputStream.readObject();
		objectInputStream.close();
		byteArrayInputStream.close();
		return object;
	}
	
	/**
	 * Generates a pair of public and protected keys
	 */
	private void generatKeys() {
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(5000);
			KeyPair keyPair=keyPairGenerator.generateKeyPair();
			PrivateKey privateKey=keyPair.getPrivate();
			PublicKey publicKey=keyPair.getPublic();
			this.privateKey=serialiseObject(privateKey);
			this.publicKey=serialiseObject(publicKey);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

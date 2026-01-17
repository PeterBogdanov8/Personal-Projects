

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;

import javafx.scene.control.TextArea;

/**
 * The class adds blocks to the block chain and keeps track of the election standings 
 * @author PO Bogdanov 218029215
 *
 */
public class BlockChain implements Serializable {
	/**
	 * serial id of the block chain
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The blocks are stored in a doubly linked list of type SealedObject
	 */
	private DoublyLinkedList<SealedObject> blocks;
	/**
	 * Identity number of the users that casted their vote
	 */
	private ArrayList<String> ids;
	/**
	 * The genisis block of the block chain
	 */
	private SealedObject genisisBlock=null;
	/**
	 * The last record added to the block chain
	 */
	private Record lastRecord=null;
	/**
	 * The total number of people that casted their vote in the elections
	 */
	private int total=0;
	//private int ancVotes=0;
	//private int daVotes=0;
	//private int effVotes=0;
	/**
	 * The political parties participating in the election
	 */
	private ArrayList<PoliticalParty> politicalParties;
	
	/**
	 * Constructor
	 */
	public BlockChain() {
		blocks=new DoublyLinkedList<>();
		ids=new ArrayList<>();
		politicalParties=new ArrayList<>();
		PoliticalParty ancParty=new PoliticalParty("ANC", 0);
		PoliticalParty daParty=new PoliticalParty("DA", 0);
		PoliticalParty effParty=new PoliticalParty("EFF", 0);
		politicalParties.add(ancParty);
		politicalParties.add(daParty);
		politicalParties.add(effParty);
	}
	
	/**
	 * Adds a block to the block chain and increment the total and the number of votes of  political party the user elected 
	 * @param encryptedRecord The block that will be add to the block chain
	 * @param publicKey The public key can decrypt the block
	 * @throws IllegalArgumentException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws ClassNotFoundException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws IOException
	 */
	public void add(SealedObject encryptedRecord, PublicKey publicKey) throws IllegalArgumentException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException, IOException{
		//Decrypt the block
		Cipher cipher=Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		Record record=(Record) encryptedRecord.getObject(cipher);
		if (blocks.isEmpty()) {
			genisisBlock=encryptedRecord;
			//Add the block the block chain
			blocks.addLast(encryptedRecord);
			lastRecord=record;
			//Count the vote
			for (int i = 0; i < politicalParties.size(); i++) {
				if (record.getVote().getParty().equals(politicalParties.get(i).getName())) {
					politicalParties.get(i).setNumVotes(politicalParties.get(i).getNumVotes()+1);
					total++;
				}
			}
			
//			if(record.getVote().getParty().equals("ANC")) {
//				ancVotes++;
//				total++;
//				ids.add(record.getVote().getId());
//			} else if(record.getVote().getParty().equals("DA")){
//				daVotes++;
//				total++;
//				ids.add(record.getVote().getId());
//			} else if(record.getVote().getParty().equals("EFF")) {
//				effVotes++;
//				total++;
//				ids.add(record.getVote().getId());
//			}
		} else {
			System.out.println(record.getPrevHash());
			System.out.println(lastRecord.getCurHash());
			//Add the block to the block chain
			blocks.addLast(encryptedRecord);
			lastRecord=record;
			//Check if the voter did not vote twice
			for (String id : ids) {
				if (record.getVote().getId()==id) {
					throw new IllegalArgumentException("A citizen may not vote more than once");
				}
			}
			//Count the vote
			for (int i = 0; i < politicalParties.size(); i++) {
				if (record.getVote().getParty().equals(politicalParties.get(i).getName())) {
					politicalParties.get(i).setNumVotes(politicalParties.get(i).getNumVotes()+1);
					total++;
				}
			}
			
//			if(record.getVote().getParty().equals("ANC")) {
//				ancVotes++;
//				total++;
//				ids.add(record.getVote().getId());
//			} else if(record.getVote().getParty().equals("DA")){
//				daVotes++;
//				total++;
//				ids.add(record.getVote().getId());
//			} else if(record.getVote().getParty().equals("EFF")) {
//				effVotes++;
//				total++;
//				ids.add(record.getVote().getId());
//			}
			
		}
	}
	
	/** 
	 * Returns the string format of the blockchain
	 * @return the string format of the blockchain
	 */
	public String toString() {
		return blocks.toString();
	}

	/**
	 * Returns the block chain
	 * @return the block chain
	 */
	public DoublyLinkedList<SealedObject> getBlocks() {
		return blocks;
	}

	/**
	 * Returns the genisis block
	 * @return the genisis block
	 */
	public SealedObject getGenisisBlock() {
		return genisisBlock;
	}

	/**
	 * Returns the last record add to the block chain
	 * @return the last record add to the block chain
	 */
	public Record getLastRecord() {
		return lastRecord;
	}
	
//	public double ancVotes() {
//		if (total==0) {
//			return 0;
//		}
//		double ratio=((double)ancVotes)/total;
//		return ratio*100.00;
//	}
//	
//	public double daVotes() {
//		if (total==0) {
//			return 0;
//		}
//		double ratio=((double)daVotes)/total;
//		return ratio*100.00;
//	}
//	
//	public double effVotes() {
//		if (total==0) {
//			return 0;
//		}
//		double ratio=((double)effVotes)/total;
//		return ratio*100.00;
//	}

	/**
	 * Displays the standings on a particular text area
	 * @param textArea the text area where the standings will be displayed
	 */
	public void standings(TextArea textArea) {
		textArea.setText("Election Standings: \n");
		//Display the standings
		for (PoliticalParty politicalParty : politicalParties) {
			if (total==0) {
				textArea.appendText(politicalParty.getName() +": " +"0%" +"\n");
			} else {
				double ratio=((double)politicalParty.getNumVotes())/total;
				textArea.appendText(politicalParty.getName() +": " +(ratio*100) +"%" +"\n");
			}
		}
	}
	
	/**
	 * Returns the size of the block chain
	 * @return the size of the block chain
	 */
	public int size() {
		return blocks.size();
	}

	public ArrayList<String> getIds() {
		return ids;
	}

	/**
	 * Returns the political parties participating in the election
	 * @return the political parties participating in the election 
	 */
	public ArrayList<PoliticalParty> getPoliticalParties() {
		return politicalParties;
	}

	/**
	 * Set the political parties that will be participating in the election
	 * @param politicalParties the political parties participating in the election 
	 */
	public void setPoliticalParties(ArrayList<PoliticalParty> politicalParties) {
		this.politicalParties = politicalParties;
	}
}

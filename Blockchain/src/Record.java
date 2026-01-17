import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The record that will be encrypted and added to the block chain if it is verified
 * @author PO Bogdanov 218029215
 *
 */
public class Record implements Serializable {
	/**
	 *  Serial ID of the Record
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * This will be index of the encrypted in the block chain if it is verified
	 */
	private int i;
	/**
	 * Unique random long which is the current time in milliseconds
	 */
	private long time;
	/**
	 * The current hash of the record
	 */
	private String curHash;
	/**
	 * The previous hash of the record
	 */
	private String prevHash;
	/**
	 * Concatenated string of all the data in vote
	 */
	private String data;
	/**
	 * The vote that the voter has casted
	 */
	private Vote vote;
	/**
	 * Used to calculate the current hash
	 */
	private int nonce;
	
	/**
	 * Constructor
	 * @param i This will be index of the encrypted in the block chain if it is verified
	 * @param prevHash The previous hash of the record
	 * @param vote The vote that the voter has casted
	 * @throws IllegalArgumentException
	 */
	public Record(int i, String prevHash, Vote vote) throws IllegalArgumentException{
		if (vote.getAge()<18) {
			throw new IllegalArgumentException("Citizens under the age of 18 cannot vote.");
		}
		this.i=i;
		time=System.currentTimeMillis();
		this.prevHash=prevHash;
		this.vote=vote;
		nonce=0;
		try {
			this.curHash=calcHash();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		data=vote.getId() +vote.getAge() +vote.getParty();
	}
	
	/**
	 * Calculates the current hash of record
	 * @return the current hash of record
	 * @throws RuntimeException
	 * @throws NoSuchAlgorithmException
	 */
	public String calcHash() throws RuntimeException,NoSuchAlgorithmException{
		StringBuffer sb=new StringBuffer();
		try {
			data="";
			String string= i+time+prevHash+data+nonce;
			MessageDigest mg=MessageDigest.getInstance("SHA-256");
			byte[] hashBytes;
			hashBytes = mg.digest(string.getBytes("UTF-8"));
			for (int j = 0; j < hashBytes.length; j++) {
				String hexString=Integer.toHexString(0xff & hashBytes[j]);
				if (hexString.length()==1) {
					sb.append(0);
				}
				sb.append(hexString);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * Ensures that the block starts with a zero
	 */
	public void mine() {
		nonce=0;
		String targ=new String(new char[1]).replace('\0', '0');
		while (!curHash.substring(0, 1).equals(targ)) {
			nonce++;
			try {
				curHash=calcHash();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the index of block if the record is verified
	 * @return the index of block if the record is verified
	 */
	public int getI() {
		return i;
	}

	/**
	 * Returns a unique random long which is the current time in milliseconds
	 * @return a unique random long which is the current time in milliseconds
	 */
	public long getTime() {
		return time;
	}

	/**
	 * Returns the current hash of the record
	 * @return The current hash of the record
	 */
	public String getCurHash() {
		return curHash;
	}

	/**
	 * Returns the previous hash of the record
	 * @return The previous hash of the record
	 */
	public String getPrevHash() {
		return prevHash;
	}

	/**
	 * 	Return a concatenated string of all the data in vote
	 * @return Concatenated string of all the data in vote
	 */
	public String getData() {
		return data;
	}

	/**
	 * Returns the vote that the voter has casted
	 * @return The vote that the voter has casted
	 */
	public Vote getVote() {
		return vote;
	}

	/**
	 * Returns the nonce use to calculate the hash
	 * @return the nonce use to calculate the hash
	 */
	public int getNonce() {
		return nonce;
	}

//	public void setI(int i) {
//		this.i = i;
//	}
//
//	public void setTime(long time) {
//		this.time = time;
//	}
//
//	public void setCurHash(String curHash) {
//		this.curHash = curHash;
//	}
//
//	public void setPrevHash(String prevHash) {
//		this.prevHash = prevHash;
//	}
//
//	public void setData(String data) {
//		this.data = data;
//	}
//
//	public void setVote(Vote vote) {
//		this.vote = vote;
//	}
//
//	public void setNonce(int nonce) {
//		this.nonce = nonce;
//	}
}

import java.io.Serializable;

/**
 * The store the name of the political and the votes of the party
 * The vote of the party can  be updated using the setter function provided in the class
 * @author PO Bogdanov 218029215
 *
 */
public class PoliticalParty implements Serializable {
	/**
	 * Serial ID of the PoliticalParty
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The name of the political party
	 */
	String name;
	/**
	 * The number of votes the political party currently has
	 */
	int numVotes;
	
	/**
	 * Constructor
	 * @param name The name of the political party
	 * @param numVotes The number of votes the political party currently has
	 */
	public PoliticalParty(String name, int numVotes) {
		this.name = name;
		this.numVotes = numVotes;
	}
	
	/**
	 * Returns the name of the political party
	 * @return The name of the political party
	 */
	public String getName() {
		return name;
	}
	/**
	 * Returns the number of votes the political party currently has
	 * @return The number of votes the political party currently has
	 */
	public int getNumVotes() {
		return numVotes;
	}
	/**
	 * Updates the number of votes the political party currently has
	 * @param numVotes the new number of votes the political party currently has
	 */
	public void setNumVotes(int numVotes) {
		this.numVotes = numVotes;
	}
}

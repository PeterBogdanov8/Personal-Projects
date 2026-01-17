import java.io.Serializable;

/**
 * The class has the identity number and age of the voter as well as the political party they voted for
 * @author PO Bogdanov 218029215
 *
 */
public class Vote implements Serializable {
	/**
	 *  Serial ID of the Vote
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The identity number of the voter
	 */
	private String id;
	/**
	 * The age of the voter
	 */
	private int age;
	/**
	 * The political party the voter voted for
	 */
	private String party;
	
	/**
	 * Constructor
	 * @param id The identity number of the voter
	 * @param age The age of the voter
	 * @param party The political party the voter voted for
	 */
	public Vote(String id, int age, String party) {
		//Initialises the attributes
		this.id = id;
		this.age = age;
		this.party = party;
	}

	/**
	 * Returns the identity number of the voter
	 * @return The identity number of the voter
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the age of the voter
	 * @return The age of the voter
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Returns the political party the voter voted for
	 * @return The political party the voter voted for
	 */
	public String getParty() {
		return party;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + age;
//		result = prime * result + ((id == null) ? 0 : id.hashCode());
//		result = prime * result + ((party == null) ? 0 : party.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Vote other = (Vote) obj;
//		if (age != other.age)
//			return false;
//		if (id == null) {
//			if (other.id != null)
//				return false;
//		} else if (!id.equals(other.id))
//			return false;
//		if (party == null) {
//			if (other.party != null)
//				return false;
//		} else if (!party.equals(other.party))
//			return false;
//		return true;
//	}
}


public class Message {
	MessageType type;
	int phase = 0;
	int hop = 0;
	int identifier;
	
	
	public Message(MessageType type) {
		this.type = type;
	}
	
	/**
	 * This constructor is for PROBE type of messages
	 * @param type
	 * @param id
	 * @param k
	 * @param hop
	 */
	public Message(MessageType type, int id, int k, int hop) {
		this.type = type;
		this.identifier = id;
		this.phase = k;
		this.hop = hop;
	}
	
	/**
	 * This constructor is for TERMINATE type of messages
	 * @param type
	 * @param id
	 */
	public Message(MessageType type, int id) {
		this.type = type;
		this.identifier = id;
	}
	
	/**
	 * This constructor is for REPLY type of messages
	 * @param type
	 * @param id
	 * @param phase
	 */
	public Message(MessageType type, int id, int phase) {
		this.type = type;
		this.identifier = id;
		this.phase = phase;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public int getHop() {
		return hop;
	}

	public void setHop(int hop) {
		this.hop = hop;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	
	
	
}


public class Message {
	MessageType type;
	int phase = 0;
	int hop = 0;
	Processor sender;
	
	
	public Message(MessageType type, Processor sender) {
		this.type = type;
	}
	
	/**
	 * This constructor is for PROBE type of messages
	 * @param type
	 * @param id
	 * @param k
	 * @param hop
	 */
	public Message(MessageType type, int k, int hop, Processor sender) {
		this.type = type;
		this.phase = k;
		this.hop = hop;
		this.sender = sender;
	}
	
	/**
	 * This constructor is for REPLY type of messages
	 * @param type
	 * @param id
	 * @param phase
	 */
	public Message(MessageType type, int phase, Processor sender) {
		this.type = type;
		this.phase = phase;
		this.sender = sender;
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

	public Processor getSender() {
		return sender;
	}

	public void setSender(Processor sender) {
		this.sender = sender;
	}
	
	
	
}

/**
 * This class simulates a message being shared on a communication channel
 * Each message stores its message type and id of sender processor
 * 
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Message {
	private MessageType messageType;
	private int senderID;

	public Message(MessageType type, int senderID) {
		this.messageType = type;
		this.senderID = senderID;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public int getSenderID() {
		return senderID;
	}

	public void setSenderID(int senderID) {
		this.senderID = senderID;
	}

}

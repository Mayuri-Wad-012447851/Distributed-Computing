/**
 * Message: This class simulates a message that is being passed between processors in the system
 * @author Mayuri Wadkar
 *
 */
public class Message {
	MessageType messageType;
	Processor from; //sender Processor

	public Message(MessageType mt, Processor from) {
		this.messageType=mt;
		this.from = from;
	}
	
	public Processor getFrom() {
		return from;
	}
	public void setFrom(Processor from) {
		this.from = from;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}
	
	@Override
	public String toString() {
		return this.messageType.toString();
	}
}

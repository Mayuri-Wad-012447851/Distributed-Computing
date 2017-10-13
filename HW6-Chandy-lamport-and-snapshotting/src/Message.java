/**
 * 
 * @author Mayuri Wadkar
 *
 */
public class Message {
	MessageType messageType;
	Processor from; //sender Processor

	public Processor getFrom() {
		return from;
	}
	public void setFrom(Processor from) {
		this.from = from;
	}
	public Message(MessageType mt) {
		this.messageType=mt;
	}
	public MessageType getMessageType() {
		return messageType;
	}
}

/**
 * 
 * @author Mayuri Wadkar
 *
 */
public class Message {
	private MessageType messageType;
	private Processor from;
	/**
	 * THe processor that is sending a message
	 * @return
	 */
	public Message(MessageType mt, Processor fromProcessor) {
		this.messageType=mt;
		this.from = fromProcessor;
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
}

public class Message {
	MessageType messageType;

	/**
	 * THe processor that is sending a message
	 * @return
	 */
	public Processor getFrom() {
		return from;
	}

	public void setFrom(Processor from) {
		this.from = from;
	}

	Processor from; //source
	
	public Message(MessageType mt) {
		this.messageType=mt;
	}

	public MessageType getMessageType() {
		return messageType;
	}
}

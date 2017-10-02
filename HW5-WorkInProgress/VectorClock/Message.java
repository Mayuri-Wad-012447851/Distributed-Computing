package VectorClock;

public class Message {
	private MessageType type;
	private Processor fromProcessor;
	private Processor toProcessor;
	public Message(MessageType type2, Processor fromProcessor2, Processor toProcessor2) {
		// TODO Auto-generated constructor stub
		this.type = type2;
		this.fromProcessor = fromProcessor2;
		this.toProcessor = toProcessor2;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public Processor getFromProcessor() {
		return fromProcessor;
	}
	public void setFromProcessor(Processor fromProcessor) {
		this.fromProcessor = fromProcessor;
	}
	public Processor getToProcessor() {
		return toProcessor;
	}
	public void setToProcessor(Processor toProcessor) {
		this.toProcessor = toProcessor;
	}
	
}

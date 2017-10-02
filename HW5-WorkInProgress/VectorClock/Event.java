package VectorClock;

public class Event {
	private EventType eventtype;
	private Processor fromProcessor;
	private Processor toProcessor;
	
	
	public Event(EventType et, Processor from,Processor to) {
		setEventtype(et);
		setFromProcessor(from);
		setToProcessor(to);
	}
	public EventType getEventtype() {
		return eventtype;
	}
	public void setEventtype(EventType eventtype) {
		this.eventtype = eventtype;
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

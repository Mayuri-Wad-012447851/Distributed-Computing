/**
 * Vector Clock Implementation
 * Event class represents an event happening at fromProcessor and/or toProcessor
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Event {
	private EventType eventtype;
	private Processor fromProcessor;
	private Processor toProcessor;
	
	public Event(EventType et) {
		setEventType(et);
	}
	public Event(EventType et, Processor from) {
		setEventType(et);
		setFromProcessor(from);
	}
	public Event(EventType et, Processor from,Processor to) {
		setEventType(et);
		setFromProcessor(from);
		setToProcessor(to);
	}
	public EventType getEventType() {
		return eventtype;
	}
	public void setEventType(EventType eventtype) {
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
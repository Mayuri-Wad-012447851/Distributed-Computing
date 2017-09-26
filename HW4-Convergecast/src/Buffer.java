import java.util.Observable;

/**
 * This Buffer class extends Observable interface and is being observed by Processor class.
 * It notifies Processor observer of any changes in its buffer. 
 * @author Mayuri Wadkar
 *
 */
public class Buffer extends Observable{
	
	private Message message;
	private StringBuffer traversedPath;
	//to track the message sender
	private Processor senderProcessor;

	public StringBuffer getTraversedPath() {
		return traversedPath;
	}

	public void setTraversedPath(StringBuffer traversedPath) {
		this.traversedPath = traversedPath;
	}

	public Processor getSenderProcessor() {
		return senderProcessor;
	}

	public void setSenderProcessor(Processor senderProcessor) {
		this.senderProcessor = senderProcessor;
	}

	public Message  getMessage() {
        return message;
    }
	
	/**
	 * This method notifies Processor of a change in Buffer.
	 * @param message			Message type
	 * @param senderProcessor	To track sender processor
	 * @param path				To track traversal path
	 */
    public void setMessage(Message message, Processor senderProcessor,StringBuffer path) {
        this.message = message;
        this.senderProcessor = senderProcessor;
        this.traversedPath = path;
        setChanged();
        //notifying observers of this message buffer regarding change of state
        notifyObservers();
    }
}

import java.util.Observable;

/**
 * This Buffer class extends Observable interface and is being observed by Processor class.
 * It notifies Processor observer of any changes in its buffer. 
 * @author Mayuri Wadkar
 *
 */
public class Buffer extends Observable{
	
	private Message message;
	//to track the message sender
	private int senderProcId;
 
    public int getSenderProcId() {
		return senderProcId;
	}

	public void setSenderProcId(int senderProcId) {
		this.senderProcId = senderProcId;
	}

	public Message  getMessage() {
        return message;
    }
    
	/**
	 * This method notifies Processor of a change in Buffer.
	 * @param message		Message type
	 * @param senderId		To track sender processor's ID
	 * 
	 */
    public void setMessage(Message message, int senderId) {
        this.message = message;
        this.senderProcId = senderId;
        setChanged();
        //notifying observers of this message buffer regarding change of state
        notifyObservers();
    }
}

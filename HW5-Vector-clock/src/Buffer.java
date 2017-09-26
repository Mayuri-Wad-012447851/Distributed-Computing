import java.util.Observable;

/**
 * Observable Buffer of each node
 * @author Mayuri Wadkar
 * @version 1.0
 */
public class Buffer extends Observable {
    private Message message;

    /**
     * 
     * Creates empty buffer
     */
    public Buffer(){
    	this.message = null;
    }

    /**
     * Creates buffer with message
     * @param message Message to be stored
     */
    public Buffer(Message message) {
        this.message = message;
    }
    
    /**
     * @return Message from the buffer
     */
    public Message  getMessage() {
        return message;
    }

    /**
     * Sets the message and notifies the observers with the sender node's information
     * @param message		Message to be stored in the buffer
     * @param fromProcessor Node who sent the message
     */
    public void setMessage(Message message ) {
        this.message = message;
        setChanged();
        notifyObservers();
    }
}


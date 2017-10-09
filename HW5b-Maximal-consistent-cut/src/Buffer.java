import java.util.Observable;

/**
 * Buffer class that is being observed by Processor observer
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Buffer extends Observable {
    private Message message;
    public Buffer(){
        //Create an empty Buffer
    }
    public Buffer(Message message) {
        this.message = message;
    }
    public Message  getMessage() {
        return message;
    }
    
    /**
     * this method sets a message at observer processor's observable buffer
     * it also notifies its observers of any change in its buffer
     * @param message
     */
    public void setMessage(Message message ) {
        this.message = message;
        setChanged();
        notifyObservers(this);
    }
}
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Observable Buffer of each node
 * A channel should have a buffer associated with it.
 * @author Mayuri Wadkar
 */
public class Buffer extends Observable {
    private String label;
    private List<Message> messages;
    private Message currentMsg;

    /**
     * Creates empty buffer
     */
    public Buffer() {
        this.messages = new ArrayList<>();
    }

    public Buffer(String label) {
        messages = new ArrayList<>();
        this.label = label;
    }
    public List<Message> getMessages(){	
		return this.messages;
    }
    public String getLabel() {
        return label;
    }

    /**
     * @return Message from the buffer
     */
    public Message getMessage(int index) {
		return messages.get(index);
    }
    
    public Message getCurrentMsg() {
		return currentMsg;
	}

	public void setCurrentMsg(Message currentMsg) {
		this.currentMsg = currentMsg;
	}

	/**
     * Sets the message and notifies the observers with the sender node's information
     * @param message Message to be stored in the buffer
     */
    public void saveMessage(Message message) {
    	this.currentMsg = message;
        this.messages.add(message);
        setChanged();
        notifyObservers();
    }

	int getTotalMessageCount() {
        return messages.size();
    }
}


import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Buffer : This class simulates a channel used to share messages between processors
 * It could be incoming channel for one processor and outgoing channel for another.
 * It stores a list of messages being passed
 * @author mayur
 *
 */
public class Buffer extends Observable {
    String label;
    private Queue<Message> messages;
    ThreadRecorder recorder;

    public Buffer(String label) {
        messages = new ConcurrentLinkedDeque<Message>();
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    public Queue<Message> getMessages() {
		return messages;
	}
	public void setMessages(Queue<Message> messages) {
		this.messages = messages;
	}
	
	public Message getMessage() {
		return this.getMessages().poll();
	}
	
	public Message peekMessage() {
		return this.getMessages().peek();
	}
	
	public void saveMessage(Message message) {
        this.messages.add(message);
        setChanged();
        notifyObservers();
    }
	
    public int getTotalMessageCount() {
        return messages.size();
    }
    
    public boolean isEmpty() {
    	return messages.isEmpty();
    }
}


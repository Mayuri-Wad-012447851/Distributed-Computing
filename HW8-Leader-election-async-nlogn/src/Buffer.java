import java.util.LinkedList;
import java.util.Observable;
import java.util.Queue;

/**
 * This class simulates a communication channel between two processors Messages
 * being shared between processors are stored in buffer Each buffer is being
 * observed by its observer processor Whenever a message is set here, buffer
 * notifies its observer processor Buffer notifies its observer by invoking its
 * update() method.
 * 
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Buffer extends Observable {
	private Queue<Message> messages;

	public Buffer() {
		this.messages = new LinkedList<Message>();
	}

	public void saveMessage(Message message) {
		this.messages.add(message);
		setChanged();
		notifyObservers();
	}

	public Queue<Message> getMessages() {
		return messages;
	}

	public void setMessages(Queue<Message> messages) {
		this.messages = messages;
	}

	public Message getMessage() {
		return this.messages.remove();
	}
}
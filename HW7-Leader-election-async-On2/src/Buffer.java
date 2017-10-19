import java.util.Observable;

/**
 * This class simulates a communication channel between two processors
 * Messages being shared between processors are stored in buffer
 * Each buffer is being observed by its observer processor
 * Whenever a message is set here, buffer notifies its observer processor
 * Buffer notifies its observer by invoking its update() method.
 * 
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Buffer extends Observable {
	Message message;
	String label;

	public Buffer(String label) {
		this.label = label;
	}
	
	public void setMessage(Message message) {
		this.message = message;
		setChanged();
		notifyObservers();
	}

	public Message getMessage() {
		return message;
	}

}

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Observable Buffer of each node
 * @author Mayuri Wadkar
 */
//A channel should have a buffer associated with it.
public class Buffer extends Observable {
    String label;
    private List<Message> messages;

    /**
     * Creates empty buffer
     */
    public Buffer(String label) {
        messages = new ArrayList<>();
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Message getMessage(int index) {
        return messages.get(index);
    }

    public void saveMessage(Message message) {
        this.messages.add(message);
        setChanged();
        notifyObservers();
    }

    int getTotalMessageCount() {
        return messages.size();
    }
}


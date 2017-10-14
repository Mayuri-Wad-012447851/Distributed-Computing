import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Buffer extends Observable {
    String label;
    private List<Message> messages;
    ThreadRecorder recorder;

    public Buffer(String label) {
        messages = new ArrayList<>();
        this.label = label;
    }
    public String getLabel() {
        return label;
    }
    public List<Message> getMessages() {
		return messages;
	}
	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
	public Message getMessage(int index) {
		return this.getMessages().get(index);
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


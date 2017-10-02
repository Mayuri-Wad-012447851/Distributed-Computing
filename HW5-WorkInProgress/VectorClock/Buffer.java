package VectorClock;

import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Buffer extends Observable {
	private Message msg;
	private Queue<Message> queue; 
	
	public Buffer(){
		this.queue = new ConcurrentLinkedDeque<Message>();
    }
	
    public void setMessage(MessageType type,Processor fromProcessor,Processor toProcessor) {
    	this.msg = new Message(type,fromProcessor,toProcessor);
    	if(msg.getType() == MessageType.SEND) {
        	toProcessor.getMessageBuffer().queue.add(this.msg);
        	System.out.println("Send M added to queue of P"+toProcessor.getProcID());
    		setChanged();
        	notifyObservers();
        }
    	else {
    		setChanged();
            notifyObservers();
    	}
    }
    public Message getMsg() {
		return msg;
	}

	public void setMsg(Message msg) {
		this.msg = msg;
	}

	public Queue<Message> getQueue() {
		return queue;
	}

	public void setQueue(Queue<Message> queue) {
		this.queue = queue;
	}
}
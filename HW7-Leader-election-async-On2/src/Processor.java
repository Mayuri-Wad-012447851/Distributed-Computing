import java.util.Observable;
import java.util.Observer;

/**
 * This class simulates a processor node which has its own id, in-channel and out-channel
 * Each processor starts at the same time and sends an IDENTIFIER message on its out-channel
 * 
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Processor extends Thread implements Observer {
	private int processorID;
	private boolean leader;
	private Processor left;
	private Buffer outChannel;
	private Buffer inChannel;

	public Processor(int id, Buffer inchannel, Buffer outchannel) {
		this.processorID = id;
		this.leader = false;
		this.inChannel = inchannel;
		this.outChannel = outchannel;
		this.inChannel.addObserver(this);
	}

	public int getProcessorID() {
		return processorID;
	}

	public void setProcessorID(int processorID) {
		this.processorID = processorID;
	}

	public boolean isLeader() {
		return this.leader;
	}

	public Processor getLeft() {
		return left;
	}

	public void setLeft(Processor left) {
		this.left = left;
	}

	public Buffer getOutChannel() {
		return outChannel;
	}

	public void setOutChannel(Buffer outChannel) {
		this.outChannel = outChannel;
	}

	public Buffer getInChannel() {
		return inChannel;
	}

	public void setInChannel(Buffer inChannel) {
		this.inChannel = inChannel;
	}
	
	/**
	 * This method is called when a processor receives an IDENTIFIER message
	 * Invoked when processor's own id is greater than the id it received with its IDENTIFIER message  
	 * @param msg	Message to swallow	
	 */
	public void swallow(Message msg) {
		MessageType type = msg.getMessageType();
		System.out.println(
				"Message received from Processor " + msg.getSenderID() + " with type " + type + " was swallowed at Processor "+this.processorID+" .");
	}
	
	/**
	 * This method is invoked by a processor to send message on its out-channel
	 * @param msg
	 * @param to
	 */
	public void sendMessageToBuffer(Message msg) {
		this.outChannel.setMessage(msg);
	}
	
	/**
	 * This method is called when current processor is elected as a leader
	 * It then sends TERMINATE message on its out-channel
	 * @throws InterruptedException 
	 */
	public void terminateAsLeader() {
		this.leader = true;
		this.sendMessageToBuffer(new Message(MessageType.TERMINATE, this.processorID));
		System.out.println("Processor " + this.processorID + " terminating as a LEADER.");
		this.interrupt();
	}
	
	/**
	 * On receiving message in its observable buffer, buffer notifies observer processor
	 * processor's update() method gets invoked upon notification
	 */
	public void update(Observable observable, Object arg1) {
		Buffer buff = (Buffer) observable;
		Message msg = buff.getMessage();
		switch (msg.getMessageType()) {

		case TERMINATE:
			
			if (this.left.getProcessorID() == msg.getSenderID()) {
				//termination condition for circulation of TERMINATE message
				this.interrupt();
			} else {
				this.sendMessageToBuffer(msg);
			}
			break;

		case IDENTIFIER:
			
			if (this.processorID == msg.getSenderID()) {
				//termination condition for circulation of IDENTIFIER message
				//this condition is satisfied when a processor receives its own id in the message
				//At this point current processor elects itself as a leader
				terminateAsLeader();
			} else {
				
				boolean result = compareTo(msg.getSenderID());
				
				if (result) {
					//This condition satisfies when id in the received message is less than current processor's id
					//After swallowing message, processor sends IDENTIFIER message with its own Id 
					swallow(msg);
					this.sendMessageToBuffer(new Message(MessageType.IDENTIFIER, this.processorID));
				} else {
					//This condition satisfies when id in the received message is greater than current processor's id
					//Processor forwards received IDENTFIER message with id of sender processor
					this.sendMessageToBuffer(msg);
				}
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * This method compares its own id with id received in message buffer
	 * It returns true when its own id is greater than the id received in message buffer
	 * @param id
	 * @return
	 */
	public boolean compareTo(int id) {
		return this.processorID > id;
	}

	@Override
	public void run() {
		System.out.println("Processor " + this.processorID + ": Sending its own identifier.");
		this.sendMessageToBuffer(new Message(MessageType.IDENTIFIER, this.processorID));
	}
}

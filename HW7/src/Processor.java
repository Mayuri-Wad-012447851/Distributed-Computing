import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Processor extends Thread implements Observer{
	private int processorID;
	private boolean leader;
	private Processor left;
	private Processor right;
	private Buffer rightInChannel;
	private Buffer leftInChannel;
	private Buffer rightOutChannel;
	private Buffer leftOutChannel;
	private boolean asleep;
	private boolean replyFromLeft;
	private boolean replyFromRight;

	public Processor(int id, Buffer rightInChannel, Buffer leftInChannel, Buffer rightOutChannel, Buffer leftOutChannel) {
		this.processorID = id;
		this.leader = false;
		this.rightInChannel = rightInChannel;
		this.leftInChannel = leftInChannel;
		this.rightOutChannel = rightOutChannel;
		this.leftOutChannel = leftOutChannel;
		this.leftInChannel.addObserver(this);
		this.rightInChannel.addObserver(this);
		this.asleep = true;
		this.replyFromLeft = false;
		this.replyFromRight = false;
	}
	
	
	
	public int getProcessorID() {
		return processorID;
	}

	public void setProcessorID(int processorID) {
		this.processorID = processorID;
	}



	public boolean isLeader() {
		return leader;
	}



	public void setLeader(boolean leader) {
		this.leader = leader;
	}



	public Processor getLeft() {
		return left;
	}



	public void setLeft(Processor left) {
		this.left = left;
		left.setRight(this);
	}



	public Processor getRight() {
		return right;
	}



	public void setRight(Processor right) {
		this.right = right;
	}


	public boolean isAsleep() {
		return asleep;
	}



	public void setAsleep(boolean asleep) {
		this.asleep = asleep;
	}



	@Override
	public void update(Observable arg0, Object arg1) {
		Buffer buff = (Buffer) arg0;
		Message message = buff.getMessage();
		MessageType type = message.getType();
		int receivedId = message.getIdentifier();
		int receivedPhase = message.getPhase();
		int receivedHop = message.getHop();
		
		switch(type) {
		case PROBE:
			System.out.println("Processor "+this.processorID+" received probe from Processor"+receivedId);
			if(buff == this.leftInChannel) {
				if(receivedId == this.processorID) {
					try {
						terminateAsLeader();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if((receivedId > this.processorID) && (receivedHop < Math.pow(2, receivedPhase))) {
					sendMessageToBuffer(new Message(MessageType.PROBE, receivedId, receivedPhase, receivedHop++),this.rightOutChannel);
				}
				if((receivedId > this.processorID) && (receivedHop >= Math.pow(2, receivedPhase))) {
					sendMessageToBuffer(new Message(MessageType.REPLY,receivedId, receivedPhase), this.leftOutChannel);
				}
				if(receivedId < this.processorID) {
					swallow(receivedId);
				}
				
			}
			if(buff == this.rightInChannel) {
				if(message.getIdentifier() == this.processorID) {
					try {
						terminateAsLeader();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if((receivedId > this.processorID) && (receivedHop < Math.pow(2, receivedPhase))) {
					sendMessageToBuffer(new Message(MessageType.PROBE, receivedId, receivedPhase, receivedHop++),this.leftOutChannel);
				}
				if((receivedId > this.processorID) && (receivedHop >= Math.pow(2, receivedPhase))) {
					sendMessageToBuffer(new Message(MessageType.REPLY,receivedId, receivedPhase), this.rightOutChannel);
				}
				if(receivedId < this.processorID) {
					swallow(receivedId);
				}
			}
			break;
		
		case TERMINATE:
			if(receivedId == this.left.processorID) {
				System.out.println("TERMINATE message has traversed the ring. Terminating all processes.");
				this.interrupt();
			}
			else {
				System.out.println("Processor"+this.processorID+" is terminating..");
				sendMessageToBuffer(message, this.leftOutChannel);
				this.interrupt();
			}
			
			break;
		
		case REPLY:
			System.out.println("Processor "+this.processorID+" received reply message from "+receivedId);
			if(buff == this.leftInChannel) {
				this.replyFromLeft = true;
				if(receivedId != this.processorID) {
					sendMessageToBuffer(message, this.rightOutChannel);
				}
				else if(this.replyFromRight) {
					System.out.println("Winner in this phase " + receivedPhase + " is Processor+ " + this.processorID);
					sendMessageToBuffer(new Message(MessageType.PROBE, this.processorID, receivedPhase++, 1), this.leftOutChannel);
				}
			}
			if(buff == this.rightInChannel) {
				this.replyFromRight = true;
				if(receivedId != this.processorID) {
					sendMessageToBuffer(message, this.leftOutChannel);
				} else if(this.replyFromLeft) {
					System.out.println("Winner in this phase " + receivedPhase + " is Processor+ " + this.processorID);
					sendMessageToBuffer(new Message(MessageType.PROBE, this.processorID, receivedPhase++, 1), this.rightOutChannel);
				}
			}
			break;
		
		default:
			break;
		}
	}
	
	private void swallow(int receivedId) {
		// TODO Auto-generated method stub
		System.out.println("Message received from Processor "+receivedId+ " is swallowed at Processor "+this.processorID);
	}



	private void terminateAsLeader() throws InterruptedException {
		
		sendMessageToBuffer(new Message(MessageType.TERMINATE, this.processorID), this.leftOutChannel);
		this.leader = true;
		
		System.out.println("Processor "+this.processorID+" is terminating as leader.");
		
	}



	public void sendMessageToBuffer(Message msg, Buffer channel) {
		channel.setMessage(msg);
	}
	
	@Override
	public void run() {
		
		if(this.asleep) {
			
			this.asleep = false;
			int phase = 0;
			int hop = 1;
			System.out.println("Processor "+this.processorID+" sending probe to Processor "+this.left.getProcessorID());
			sendMessageToBuffer(new Message(MessageType.PROBE, this.processorID, phase, hop), this.leftOutChannel);
			System.out.println("Processor "+this.processorID+" sending probe to Processor "+this.right.getProcessorID());
			sendMessageToBuffer(new Message(MessageType.PROBE, this.processorID, phase, hop), this.rightOutChannel);
			
		
		}
		
		
	}
	
}

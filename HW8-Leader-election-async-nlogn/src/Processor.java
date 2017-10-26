import java.util.Observable;
import java.util.Observer;

public class Processor extends Thread implements Observer{
	private int processorID;
	private boolean phaseLeader;
	private boolean leader;
	private Processor left;
	private Processor right;
	private Buffer messageBuffer;
	private int replyCounter;
	private int phase;
	

	public Processor(int id, Buffer messageBuffer) {
		this.processorID = id;
		this.leader = true;
		this.phaseLeader = false;
		this.messageBuffer = messageBuffer;
		this.messageBuffer.addObserver(this);
		this.phase = 0;
		this.replyCounter = 0;
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
	}

	public Processor getRight() {
		return right;
	}

	public void setRight(Processor right) {
		this.right = right;
	}

	public boolean isPhaseLeader() {
		return phaseLeader;
	}

	public void setPhaseLeader(boolean phaseLeader) {
		this.phaseLeader = phaseLeader;
	}

	public Buffer getMessageBuffer() {
		return messageBuffer;
	}

	public void setMessageBuffer(Buffer messageBuffer) {
		this.messageBuffer = messageBuffer;
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		Buffer buff = (Buffer) arg0;
		Message message = buff.getMessage();
		MessageType type = message.getType();
		int receivedId = message.getSender().getProcessorID();
		int receivedPhase = message.getPhase();
		int receivedHop = message.getHop();
		Processor sender = message.getSender();
		
		switch(type) {
		case PROBE:
			System.out.println("Processor "+this.processorID+" received probe from Processor "+message.getSender().getProcessorID());
			if(receivedId < this.processorID) {
				swallow(message);
			}
			else {
				if(message.getSender() == this.left) {
					
					if((receivedId > this.processorID) && (receivedHop < Math.pow(2, receivedPhase))) {
						System.out.println("Processor "+this.processorID+" sending probe to Processor "+this.right.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase, receivedHop+1, sender),this.right.getMessageBuffer());
					}
					if((receivedId > this.processorID) && (receivedHop >= Math.pow(2, receivedPhase))) {
						System.out.println("Processor "+this.processorID+" sending reply to Processor "+this.left.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.REPLY, receivedPhase, sender), this.left.getMessageBuffer());
					}
				}
				else if(message.getSender() == this.right) {
					
					if((receivedId > this.processorID) && (receivedHop < Math.pow(2, receivedPhase))) {
						System.out.println("Processor "+this.processorID+" sending probe to Processor "+this.left.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase, receivedHop+1, sender),this.left.getMessageBuffer());
					}
					if((receivedId > this.processorID) && (receivedHop >= Math.pow(2, receivedPhase))) {
						System.out.println("Processor "+this.processorID+" sending reply to Processor "+this.right.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.REPLY,receivedId, receivedPhase, sender), this.right.getMessageBuffer());
					}
				}
			}
			if(receivedId == this.processorID) {
				terminateAsLeader();
			}
			break;
		
		case REPLY:
			System.out.println("Processor "+this.processorID+" received reply message from "+receivedId);
			this.replyCounter += 1;
			if(message.getSender() == this.left) {
				if(receivedId != this.processorID) {
					sendMessageToBuffer(message, this.right.getMessageBuffer());
				}
			}
			if(message.getSender() == this.right) {
				if(receivedId != this.processorID) {
					sendMessageToBuffer(message, this.left.getMessageBuffer());
				} 
			}
			if(message.getSender() == this) {
				System.out.println("Processor P "+this.processorID+" Reply Counter == "+this.replyCounter);
				if(this.replyCounter == 2) {
					System.out.println("Winner in this phase " + receivedPhase + " is Processor+ " + this.processorID);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase+1, 1, this), this.left.getMessageBuffer());
					sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase+1, 1, this), this.right.getMessageBuffer());
					this.replyCounter = 0;
				}
			}
			break;
		
		default:
			break;
		}
	}
	
	private void swallow(Message message) {
		message.getSender().leader = false;
		System.out.println("Message received from Processor "+message.getSender().getProcessorID()+ " is swallowed at Processor "+this.processorID);
	}

	private void terminateAsLeader() {
		this.leader = true;
		System.out.println("Processor "+this.processorID+" is terminating as leader.");
	}

	public void sendMessageToBuffer(Message msg, Buffer buff) {
		buff.saveMessage(msg);
	}
	
	@Override
	public void run() {
		int hop = 1;
		System.out.println("Processor "+this.processorID+" sending probe to Processor "+this.left.getProcessorID());
		sendMessageToBuffer(new Message(MessageType.PROBE, phase, hop, this), this.left.getMessageBuffer());
		System.out.println("Processor "+this.processorID+" sending probe to Processor "+this.right.getProcessorID());
		sendMessageToBuffer(new Message(MessageType.PROBE, phase, hop, this), this.right.getMessageBuffer());
	}
}

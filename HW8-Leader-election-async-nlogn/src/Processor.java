import java.util.Observable;
import java.util.Observer;

/**
 * This class simulates a processor node which has its own id, left neighbor and
 * right neighbor Processor also maintains a queue messageBuffer and
 * replyCounter Each processor starts at the same time and sends a PROBE message
 * on to its neighbors
 * 
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Processor extends Thread implements Observer {
	private int processorID;
	private int processorValue;
	private boolean leader;
	private Processor left;
	private Processor right;
	private Buffer messageBuffer;
	/**
	 * ReplyCounter variable is to track replies received from neighbors
	 */
	private int replyCounter;
	private int phase;

	public Processor(int id, int value, Buffer messageBuffer) {
		this.processorID = id;
		this.processorValue = value;
		// initially each processor declares itself as a leader
		this.leader = true;
		this.messageBuffer = messageBuffer;
		// each processor is observing its own message buffer
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

	public int getProcessorValue() {
		return processorValue;
	}

	public void setProcessorValue(int processorValue) {
		this.processorValue = processorValue;
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

	public Buffer getMessageBuffer() {
		return messageBuffer;
	}

	public void setMessageBuffer(Buffer messageBuffer) {
		this.messageBuffer = messageBuffer;
	}

	/**
	 * On receiving message in its observable buffer, buffer notifies observer
	 * processor Processor's update() method gets invoked upon notification
	 */
	public void update(Observable arg0, Object arg1) {
		Buffer buff = (Buffer) arg0;
		Message message = buff.getMessage();
		MessageType type = message.getType();
		int receivedValue = message.getSender().getProcessorValue();
		int receivedPhase = message.getPhase();
		int receivedHop = message.getHop();
		Processor sender = message.getSender();

		switch (type) {

		case PROBE:

			System.out.println("Processor " + this.processorID + " received probe from Processor "
					+ message.getSender().getProcessorID());

			if (receivedValue < this.processorValue) {
				// This condition satisfies when sender processor's value is less than current
				// processor's value
				swallow(message);
			} else {
				if (message.getSender() == this.left) {
					// This condition satisfies when sender is left neighbor of current processor
					// and left neighbor's value is greater than own value

					if ((receivedValue > this.processorValue) && (receivedHop < Math.pow(2, receivedPhase))) {
						// It ensures that the probe message is forwarded to right if hop counter is
						// less than 2**k

						System.out.println("Processor " + this.processorID + " sending probe to Processor "
								+ this.right.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase, receivedHop + 1, sender),
								this.right.getMessageBuffer());
					}

					if ((receivedValue > this.processorValue) && (receivedHop >= Math.pow(2, receivedPhase))) {
						// It ensures that the reply message is sent to left if hop counter is
						// greater than or equal to 2**k

						System.out.println("Processor " + this.processorID + " sending reply to Processor "
								+ this.left.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.REPLY, receivedPhase, sender),
								this.left.getMessageBuffer());
					}
				} else if (message.getSender() == this.right) {
					// This condition satisfies when sender is right neighbor of current processor
					// and right neighbor's value is greater than own value

					if ((receivedValue > this.processorValue) && (receivedHop < Math.pow(2, receivedPhase))) {
						// It ensures that the probe message is forwarded to left if hop counter is
						// less than 2**k

						System.out.println("Processor " + this.processorID + " sending probe to Processor "
								+ this.left.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase, receivedHop + 1, sender),
								this.left.getMessageBuffer());
					}

					if ((receivedValue > this.processorValue) && (receivedHop >= Math.pow(2, receivedPhase))) {
						// It ensures that the reply message is sent to right if hop counter is
						// greater than or equal to 2**k

						System.out.println("Processor " + this.processorID + " sending reply to Processor "
								+ this.right.getProcessorID());
						sendMessageToBuffer(new Message(MessageType.REPLY, receivedValue, receivedPhase, sender),
								this.right.getMessageBuffer());
					}
				}
			}
			if (receivedValue == this.processorValue) {
				// This condition satisfies when processor receives its own probe message
				terminateAsLeader();
			}
			break;

		case REPLY:

			System.out.println("Processor " + this.processorID + " received reply message from " + sender.getProcessorID());

			this.replyCounter += 1;

			if (message.getSender() == this.left) {
				// This condition satisfies when sender is left neighbor of current processor

				if (receivedValue != this.processorValue) {
					// Reply message is forwarded to right until it reaches the intended winner of
					// the phase

					sendMessageToBuffer(message, this.right.getMessageBuffer());
				}
			}

			if (message.getSender() == this.right) {
				// This condition satisfies when sender is right neighbor of current processor

				if (receivedValue != this.processorValue) {
					// Reply message is forwarded to left until it reaches the intended winner of
					// the phase

					sendMessageToBuffer(message, this.left.getMessageBuffer());
				}
			}

			if (message.getSender() == this) {

				if (this.replyCounter == 2) {
					// this condition satisfies when processor receives reply from both left and
					// right neighbors
					// Processor is declared as a winner for current phase and it participates in
					// next phase

					System.out.println("----------Phase " + receivedPhase + " Winner:\tProcessor " + this.processorID+ " with value: "+this.processorValue);

					sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase + 1, 1, this),
							this.left.getMessageBuffer());
					sendMessageToBuffer(new Message(MessageType.PROBE, receivedPhase + 1, 1, this),
							this.right.getMessageBuffer());

					// reset its reply counter for future phases
					this.replyCounter = 0;
				}
			}
			break;

		default:
			System.out.println("Unknown message type");
			assert(false);
			break;
		}
	}

	/**
	 * This method is called when a processor receives a PROBE message and
	 * processor's own id is greater than the id it received with PROBE message
	 * 
	 * @param message
	 *            Message to swallow
	 */
	private void swallow(Message message) {
		// A probe from sender processor gets swallowed at current processor
		// sender processor is declared a non-leader
		message.getSender().leader = false;
		System.out.println("Message received from Processor " + message.getSender().getProcessorID()
				+ " is swallowed at Processor " + this.processorID);
	}

	/**
	 * This method is called when current processor is elected as a leader
	 */
	private void terminateAsLeader() {
		System.out.println("Processor " + this.processorID + " is terminating as leader.");
	}

	/**
	 * Processor invokes this method to send messages to its left or right neighbors
	 * 
	 * @param msg
	 * @param buff
	 */
	public void sendMessageToBuffer(Message msg, Buffer buff) {
		buff.saveMessage(msg);
	}

	/**
	 * This method is invoked when processor thread starts Each processor starts
	 * sending PROBE message to its left and right neighbor
	 */
	@Override
	public void run() {
		int hop = 1;

		System.out
				.println("Processor " + this.processorID + " sending probe to Processor " + this.left.getProcessorID());
		sendMessageToBuffer(new Message(MessageType.PROBE, phase, hop, this), this.left.getMessageBuffer());

		System.out.println(
				"Processor " + this.processorID + " sending probe to Processor " + this.right.getProcessorID());
		sendMessageToBuffer(new Message(MessageType.PROBE, phase, hop, this), this.right.getMessageBuffer());
	}
}

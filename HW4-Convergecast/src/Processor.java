
import java.util.Observable;
import java.util.Observer;

/**
 * Processor class which is implementing Observer interface for observable Buffer.
 * Every processor node in tree on receiving notification about change in Buffer, sends messages to its left and right subtrees.
 * Leaf nodes start sending PARENT messages along with with Max element information to parent nodes.
 * @author Mayuri Wadkar
 * 
 */
public class Processor implements Observer{
	public int processorId;
	public Processor left;
	public Processor right;
	private Buffer messageBuffer;
	private int max;
	
	/**
	 * This method returns Max element value at current Processor node
	 * @return max	Current Max value at Processor
	 */
	public int getMax() {
		return max;
	}
	
	/**
	 * This method sets Max value at current Processor node
	 * @param max
	 */
	public void setMax(int max) {
		this.max = max;
	}

	public Processor(int data) {
		this.processorId = data;
		this.left = null;
		this.right = null;
		//initializing max element value at each node to its own value
		this.max = this.processorId;
		this.messageBuffer = new Buffer();
		messageBuffer.addObserver(this);
	}
	
	/**
	 * This method will add a message to instance processor's buffer.
	 * Other processors will invoke this method to send a message to this Processor
	 * @param message
	 * @param senderProcessor	To keep track of sender processor
	 * @param path				Path of traversal in a tree
	 */
	public void sendMessgeToMyBuffer(Message message, Processor senderProcessor, StringBuffer path){
        messageBuffer.setMessage(message,senderProcessor,path);
    }
	
	/**
	 * This is analogous to receive method. 
	 * Whenever a message is dropped in its buffer this Processor will respond.
	 */
	@Override
	public void update(Observable observable, Object arg1) {
		// TODO Auto-generated method stub
		Buffer buff = (Buffer) observable;
		Message receivedMessage = buff.getMessage();
		Processor sender = buff.getSenderProcessor();
		
		switch(receivedMessage) {
		
		case M:
			//every node on receiving <M> forwards it to left and right subtrees
			if (this.left != null) {
				this.left.sendMessgeToMyBuffer(Message.M, this, buff.getTraversedPath());
			} 
			if (this.right != null) {
				this.right.sendMessgeToMyBuffer(Message.M, this, buff.getTraversedPath());
			}
			
			//every node sends a PARENT message to parent along with information about max element
			//in recursive manner, leaf nodes will be the first to send out PARENT message
			sender.sendMessgeToMyBuffer(Message.PARENT, this, buff.getTraversedPath());
			buff.getTraversedPath().append("("+this.processorId+") ");
			System.out.print("\nNode "+this.processorId+"\t");
			System.out.println("Max element at me : "+this.max);
			break;

		case PARENT:
				//max element computation at a non-leaf node
				if(this.left != null && this.right != null) {
					this.max = Integer.max(this.processorId, Integer.max(this.left.max, this.right.max));
				} else if(this.left == null && this.right != null) {
					this.max = Integer.max(this.processorId, this.right.max);
				} else if(this.right == null && this.left != null) {
					this.max = Integer.max(this.processorId, this.left.max);
				}
			break;
			
		default:
			break;
		}
	}

}

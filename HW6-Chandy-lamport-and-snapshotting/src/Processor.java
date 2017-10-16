import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

/**
 * Processor : This class simulates a Processor node in chandy lamport.
 * It runs through a pre-defined execution plan, sending and receiving messages to/from 
 * other processor nodes.
 * @author Mayuri Wadkar
 * 
 */
public class Processor extends Thread implements Observer {

	private static Logger logger = Logger.getLogger(ThreadRecorder.class.getName());
	
	private int procID;
	private List<Buffer> inChannels = null;
	private List<Buffer> outChannels = null;
	private int markerCount = 0;
	private int messageCount = 0;
	/**
	 * it stores mapping between inchannel and corresponding recorder thread
	 */
	private Map<Buffer,ThreadRecorder> channelRecorder = new HashMap<Buffer,ThreadRecorder>();
	
	public Processor(int id, List<Buffer> inChannels, List<Buffer> outChannels) {
		this.procID = id;
		this.inChannels = inChannels;
		this.outChannels = outChannels;
		markerCount = 0;
		this.messageCount = 0;
		
		// Adding this processor as an observer to each of its incoming channels 
		//and creating a recorder thread
		for(Buffer inChannel: inChannels) {
			inChannel.addObserver(this);
			
			String threadName = "ThreadRecorder-"+ inChannel.getLabel();
			ThreadRecorder recorder = new ThreadRecorder(threadName);
			recorder.setChannelToRecord(inChannel);
			channelRecorder.put(inChannel, recorder);
		}
	}

	public List<Buffer> getInChannels() {
		return inChannels;
	}

	public void setInChannels(List<Buffer> inChannels) {
		this.inChannels = inChannels;
	}

	public List<Buffer> getOutChannels() {
		return outChannels;
	}

	public void setOutChannels(List<Buffer> outChannels) {
		this.outChannels = outChannels;
	}

	public int getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
	
	public int getProcID() {
		return procID;
	}

	public void setProcID(int procID) {
		this.procID = procID;
	}

	/**
	 * This is a dummy implementation which will record current state of this processor
	 */
	public void recordMyCurrentState() {
		logger.info("First Marker Received : Processor "+this.getProcID()+
				" : Recording my registers,program counters,variables..."+
				" Processor" +this.getProcID() +"'s message count: " + messageCount);
	}

	/**
	 * Starts recording on given channel
	 * @param channel
	 */
	public void recordChannel(Buffer channel) {
		ThreadRecorder recorder = this.channelRecorder.get(channel);
		//start recording on channel
		recorder.start();
		synchronized(recorder) {
			recorder.notify();
		}
	}

	/**
     * Overloaded method, called with single argument
     * This method will add a message to this processors buffer.
     * Other processors will invoke this method to send a message to this Processor
     * @param message Message to be sent
     */
	public void sendMessgeTo(Message message, Buffer channel) {
		channel.saveMessage(message);
	}

	/**
	 * this method returns true when the Marker message at processor is first marker
	 * @return true if this is the first marker false otherwise
	 */
	public boolean isFirstMarker() {
		return markerCount == 0;
	}

	/**
	 * this method is invoked for every incoming message in buffer
	 * @param observable fromChannel of the incoming message
	 */
	public void update(Observable observable, Object arg) {
		Buffer buffer = (Buffer) observable;
		Message message = buffer.peekMessage();
		MessageType type = message.getMessageType();
		
		switch(type) {
		
		case MARKER:
			Buffer fromChannel = (Buffer) observable;
			if (isFirstMarker()) {
				// Remove MARKER message from the buffer as RecorderThread should not read it
				buffer.getMessage();

				//increasing count of MARKER messages at this processor to track duplicate marker messages
				markerCount++;
				
				//every processor on receiving MARKER message records its own state
				recordMyCurrentState();
				
				//start recording messages from incoming channels
				for (Buffer inChannel : inChannels) {
					//Exclude the "Channel from which marker has arrived.
					if(inChannel != fromChannel) {
						recordChannel(inChannel);
					}
				}
				
				// Sending marker messages to each of the out channels
				for (Buffer outChannel : outChannels) {
					Thread.yield();
					sendMessgeTo(new Message(MessageType.MARKER, this), outChannel);
				}
			} else {
				logger.info("Duplicate Marker Message received for Processor"+this.getProcID()+", fromProc = P"+ message.getFrom().getProcID());
			}
			break;
			
		case ALGORITHM:
		case SEND:
		case RECEIVE:
		case COMPUTE:
			this.messageCount++;
			break;
			
		default:
			logger.info("Unexpected Msg type");
			assert(false);
			break;
		}
		
		// Inform RecorderThread of new msg, so it processes it
		ThreadRecorder recorder = this.channelRecorder.get(buffer);
		if (recorder != null) {
			synchronized(recorder) {
				recorder.notify();
			}
		}
	}
	
	/**
	 * this method initiates snapshot on current processor
	 */
	public void initiateSnapShot() {
		//recording own state
		recordMyCurrentState();
		
		//Adding marker counts to the initiator node so as to avoid re-recording of its states
		markerCount++;

		//Starts recording on each of the incoming channels
		for(Buffer inChannel:inChannels) {
			recordChannel(inChannel);
		}
		
		//sending marker messages on outgoing channel of current processor
		for(Buffer outChannel:outChannels) {
			//wait for a while before sending marker message in order to allow other processors
			//to send out their application messages
			Thread.yield();
			Message m = new Message(MessageType.MARKER, this);
			sendMessgeTo(m,outChannel);
		}
	}
	
	@Override
    public void run() {
		switch(this.getProcID()) {
		case 1:
    		runProc1ExecutionPlan();
			break;
			
		case 2:
			runProc2ExecutionPlan();
			break;
			
		case 3:
			runProc3ExecutionPlan();
			break;
			
		default:
			logger.info("Invalid processor id - "+ this.getProcID());
			assert(false);
		}
		
		for (Buffer inchannel: this.inChannels) {
			ThreadRecorder rt = channelRecorder.get(inchannel);
			try {
				rt.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//RT coming back to parent thread Processor
		}
		
		logger.info("Processor"+ this.procID+ "total message count = "+ this.messageCount);
    }
	
	//fixed execution plan for processor 1
	private void runProc1ExecutionPlan() {
		Buffer c12 = outChannels.get(0);
		Buffer c13 = outChannels.get(1);
		
		this.initiateSnapShot();
		threadSleep(1000);
		
    	this.sendMessgeTo(new Message(MessageType.ALGORITHM, this), c12);
    	this.sendMessgeTo(new Message(MessageType.ALGORITHM, this), c13);
    	
    	compute();
    	for(int i=0; i < 10; i++) {
    		this.sendMessgeTo(new Message(MessageType.getRandomMessage(), this), c12);
    		this.sendMessgeTo(new Message(MessageType.getRandomMessage(), this), c13);
    	}
	}
	
	//fixed execution plan for processor 2
	private void runProc2ExecutionPlan() {

		Buffer c21 = outChannels.get(0);
		Buffer c23 = outChannels.get(1);
		
		this.sendMessgeTo(new Message(MessageType.ALGORITHM, this), c21);
    	this.sendMessgeTo(new Message(MessageType.ALGORITHM, this), c23);
    	compute();
    	for(int i=0; i < 10; i++) {
    		this.sendMessgeTo(new Message(MessageType.getRandomMessage(), this), c21);
    		this.sendMessgeTo(new Message(MessageType.getRandomMessage(), this), c23);
    	}
	}
	
	//fixed execution plan for processor 3
	private void runProc3ExecutionPlan() {
		Buffer c31 = outChannels.get(0);
		Buffer c32 = outChannels.get(1);

		this.sendMessgeTo(new Message(MessageType.ALGORITHM, this), c31);
    	this.sendMessgeTo(new Message(MessageType.ALGORITHM, this), c32);
    	compute();
    	for(int i=0; i < 10; i++) {
    		this.sendMessgeTo(new Message(MessageType.getRandomMessage(), this), c31);
    		this.sendMessgeTo(new Message(MessageType.getRandomMessage(), this), c32);
    	}
	}
	
	private void threadSleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * A dummy computation.
     * @param p
     */
    public void compute() {
        logger.info("Doing some dummy computations on Processor" + this.getProcID());
        for(int i=0; i<1000000; i++);
    }    
}

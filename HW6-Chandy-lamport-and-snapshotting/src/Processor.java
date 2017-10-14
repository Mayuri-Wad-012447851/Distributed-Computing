import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Processor class
 * @author Mayuri Wadkar
 * 
 */
public class Processor extends Thread implements Observer {

	private int procID;
	private List<Buffer> inChannels = new ArrayList<>();
	private List<Buffer> outChannels = null;
	private Map<Buffer, List<Message>> channelState = null;
	//to keep track of initiator processor
	boolean trackInitiatorProc;
	//keeping a count of MARKER messages at this processor
	private Map<Processor, Integer> markerCount = new HashMap<Processor, Integer>();
	private int messageCount;
	private List<Processor> executionPlan;
	
	public Processor(int id, List<Buffer> inChannels, List<Buffer> outChannels) {
		this.procID = id;
		this.inChannels = inChannels;
		this.outChannels = outChannels;
		this.trackInitiatorProc = false;
		markerCount.put(this, 0);
		channelState = new HashMap<Buffer, List<Message>>();
		this.messageCount = 0;
		//adding this processor as an observer to each of its incoming channels
		for(Buffer inChannel: inChannels) {
			inChannel.addObserver(this);
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

	public Map<Buffer, List<Message>> getChannelState() {
		return channelState;
	}

	public void setChannelState(Map<Buffer, List<Message>> channelState) {
		this.channelState = channelState;
	}

	public Map<Processor, Integer> getMarkerCount() {
		return markerCount;
	}

	public void setMarkerCount(Map<Processor, Integer> markerCount) {
		this.markerCount = markerCount;
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

	public boolean isTrackInitiatorProc() {
		return trackInitiatorProc;
	}

	public void setTrackInitiatorProc(boolean trackInitiatorProc) {
		this.trackInitiatorProc = trackInitiatorProc;
	}
	
	public List<Processor> getExecutionPlan() {
		return executionPlan;
	}

	public void setExecutionPlan(List<Processor> executionPlan) {
		this.executionPlan = executionPlan;
	}

	/**
	 * This is a dummy implementation which will record current state of this processor
	 */
	public void recordMyCurrentState() {
		System.out.println("\n\tProcessor "+this.getProcID()+" Recording its own state");
		System.out.println("\tProcessor "+this.getProcID()+" : Recording my registers,program counters,variables...");
		System.out.println("\tProcessor" +this.getProcID() +"'s message count: " + messageCount);
	}

	/**
	 * this method marks the channel as empty
	 * @param channel
	 */
	public void recordChannelAsEmpty(Buffer channel) {
		channelState.put(channel, Collections.emptyList());
	}

	/**
	 * Starts recording on given channel
	 * @param channel
	 */
	public void recordChannel(Buffer channel) {
		//creating ThreadRecorder instance to start threaded recording on channel
//		System.out.print("Messages at channel "+channel.getLabel()+"--> [");
//		for(Message message:channel.getMessages()) {
//			System.out.print(message.messageType+",");
//		}
		
		System.out.println("]");
		String threadName = "Thread-"+channel.getLabel();
		ThreadRecorder recorder = new ThreadRecorder(threadName);
		//assigning channel to recorder
		recorder.setChannel(channel);
		recorder.setChannelState(this.channelState);
		System.out.println("Recording on in-channel thread :\t"+recorder.getThreadName());
		//starts recording on channel
		recorder.start();
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
		if(markerCount.containsKey(this)) {
			if(markerCount.get(this) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param observable fromChannel of the incoming message
	 */
	public void update(Observable observable, Object arg) {
		Buffer buffer = (Buffer) observable;
		Message message = buffer.getMessage(buffer.getTotalMessageCount() - 1);
		MessageType type = message.getMessageType();
		
		switch(type) {
		case MARKER:
			Buffer fromChannel = (Buffer) observable;
			if (isFirstMarker()) {
				System.out.println("\tFirst Marker at Processor"+this.getProcID());
				//increasing count of MARKER messages at this processor to track duplicate marker messages
				if(markerCount.get(this) == null) {
					markerCount.put(this, 1);
				}
				else {
					markerCount.put(this, markerCount.get(this) + 1);
				}
				//every processor on receiving MARKER message records its own state
				recordMyCurrentState();
				recordChannelAsEmpty(fromChannel);
				//start recording messages from incoming channels
				for (Buffer inChannel : inChannels) {
					//Exclude the "Channel from which marker has arrived.
					if(inChannel != fromChannel) {
						recordChannel(inChannel);
					}
				}
				// Sending marker messages to each of the out channels
				for (Buffer outChannel : outChannels) {
					sendMessgeTo(new Message(MessageType.MARKER), outChannel);
				}
				
			} else {
				System.out.println("Duplicate Marker Message received for Processor"+this.getProcID()+", stopping recording");
			}

			break;
		case ALGORITHM:
			this.setMessageCount(this.getMessageCount()+1);
			System.out.println("Algorithm message at processor "+this.getProcID());
			break;
		default:
			break;
		}
	}
	
	/**
	 * this method initiates snapshot on current processor
	 */
	public void initiateSnapShot() {
		//recording own state
		recordMyCurrentState();
		//Adding dummy marker counts to the initiator node so as to avoid re-recording of its states
		//markerCount.put(this, 1);
		//sending marker messages on outgoing channel of current processor
				for(Buffer outChannel:outChannels) {
					Message m = new Message(MessageType.MARKER);
					sendMessgeTo(m,outChannel);
				}
		//Starts recording on each of the incoming channels
		for(Buffer inChannel:inChannels) {
			recordChannel(inChannel);
		}
		
	}
	
	@Override
    public void run() {
    	if(this.getProcID() == 1) {
            for(Buffer c : this.getOutChannels()) {
            	this.sendMessgeTo(new Message(MessageType.ALGORITHM), c);
            	compute();
            	this.sendMessgeTo(new Message(MessageType.ALGORITHM), c);
            }
    	}else if(this.getProcID() == 2) {
    		for (Buffer c1 : this.getOutChannels()) {
    			this.sendMessgeTo(new Message(MessageType.ALGORITHM), c1);
    			compute();
    			this.sendMessgeTo(new Message(MessageType.ALGORITHM), c1);
    		}
    	}
    	else if(this.getProcID() == 3) {
    		for (Buffer c2 : this.getOutChannels()) {
    			this.sendMessgeTo(new Message(MessageType.ALGORITHM), c2);
    			compute();
    			this.sendMessgeTo(new Message(MessageType.ALGORITHM), c2);
    		}
    	}
    }
	
	public void getSnapshot() {
    	for(Buffer channel:channelState.keySet()) {
    		System.out.print(channel.getLabel()+" :[");
    		for(Message msg:channelState.get(channel)) {
    			System.out.print(msg.getMessageType().toString()+" ");
    		}
    		System.out.println("]\n");
    	}
    }
	
	/**
     * A dummy computation.
     * @param p
     */
    public void compute() {
        System.out.println("Doing some computation on Processor" + this.getProcID());
    }
    /**
    *
    * @param to processor to which message is sent
    * @param channel the incoming channel on the to processor that will receive this message
    */
   public void send(Processor to, Buffer channel) {
       to.sendMessgeTo(null, channel); // ALGORITHM
   }
}

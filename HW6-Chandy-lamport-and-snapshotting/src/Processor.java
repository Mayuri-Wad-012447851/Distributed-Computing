import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Performs all the processor related tasks
 * @author Mayuri Wadkar
 */

public class Processor implements Observer {
	private int messageCount = 0;
    List<Buffer> inChannels = new ArrayList<>();
    /**
     * List of output channels
     */
    List<Buffer> outChannels = new ArrayList<>();
    /**
     * This is a map that will record the state of each incoming channel and all the messages
     * that have been received by this channel since the arrival of marker and receipt of duplicate marker
     */
    Map<Buffer, List<Message>> channelState = new HashMap<Buffer, List<Message>>();
    /**
     * This map can be used to keep track of markers received on a channel. When a marker arrives at a channel
     * put it in this map. If a marker arrives again then this map will have an entry already present from before.
     * Before  doing a put in this map first do a get and check if it is not null. ( to find out if an entry exists
     * or not). If the entry does not exist then do a put. If an entry already exists then increment the integer value
     * and do a put again.
     */
    Map<Buffer, Integer> channelMarkerCount = new HashMap<Buffer, Integer>();
    /**
     * @param id of the processor
     */
    private int id;
    
    public int getMessageCount() {
    		return this.messageCount;
    }
    
    public Processor(int id, List<Buffer> inChannels, List<Buffer> outChannels) {
    	this.id = id;
    	this.inChannels = inChannels;
        this.outChannels = outChannels;
    	channelMarkerCount = new HashMap<Buffer,Integer>();
    	channelState = new HashMap<Buffer, List<Message>>();
        //making this processor as the observer for each of its inChannel
        for(Buffer channel : inChannels) {
        	channel.addObserver(this);
        }
        init();
    }

    public int getId() {
    		return this.id;
    }
    public void init() {
    		for(Buffer c: inChannels) {
    			channelMarkerCount.put(c, 0);
    		}
    }
    /**
     * This is a dummy implementation which will record current state
     * of this processor
     */
    public void recordMyCurrentState() {
        System.out.println("Recording my registers...");
        System.out.println("Recording my program counters...");
        System.out.println("Recording my local variables...");
        System.out.println("Processor" +id +"'s message count: " + messageCount);
    }

    /**
     * THis method marks the channel as empty
     * @param channel
     */
    public void recordChannelAsEmpty(Buffer channel) {
        channelState.put(channel, Collections.emptyList());
    }

    /**
     * You should send a message to this recording so that recording is stopped
     * //TODO: Homework: Move this method recordChannel(..) out of this class. Start this method in a
     *                  separate thread. This thread will start when the marker arrives and it will stop
     *                  when a duplicate marker is received. This means that each processor will have the
     *                  capability to start and stop this channel recorder thread. The processor manages the
     *                  record Channel thread. Processor will have the ability to stop the thread.
     *                  Feel free to use any java concurrency  and thread classes to implement this method
     *
     *
     * @param channel The input channel which has to be monitored
     */

    public void recordChannel(Buffer channel) {
        //Here print the value stored in the inChannels to stdout or file
        //TODO:Homework: Channel will have messages from before a marker has arrived. Record messages only after a
        //               marker has arrived.
        //               [hint: Use the getTotalMessageCount () method to get the messages received so far.
    		int lastIdx = channel.getTotalMessageCount();
        List<Message> recordedMessagesSinceMarker = new ArrayList<>();
        List<Message> channelMessages = channel.getMessages();
        for(int i = lastIdx+1; i<channelMessages.size(); i++) {
        		Message m = channelMessages.get(i);
	        	if(m.getMessageType() != MessageType.MARKER) {
	        		recordedMessagesSinceMarker.add(m);	
	        	}
        }
        channelState.put(channel, recordedMessagesSinceMarker);
        //TODO: Homework: Record messages
        // [Hint: Get the array that is storing the messages from the channel. Remember that the Buffer class
        // has a member     private List<Message> messages;  which stores all the messages received.
        // When a marker has arrived sample this List of messages and copy only those messages that
        // are received since the marker arrived. Copy these messages into recordedMessagesSinceMarker
        // and put it in the channelState map.
        //
        // ]
    }

    /**
     * Overloaded method, called with single argument
     * This method will add a message to this processors buffer.
     * Other processors will invoke this method to send a message to this Processor
     *
     * @param message Message to be sent
     */
    public void sendMessageTo(Message message, Buffer channel) {
		channel.saveMessage(message);
    }
    /**
     * @param fromChannel channel where marker has arrived
     * @return true if this is the first marker false otherwise
     */
    public boolean isFirstMarker(Buffer fromChannel) {
    		int count = channelMarkerCount.get(fromChannel);
    		if(count==1) {
    			System.out.println("This is a first marker on " + fromChannel.getLabel());
    			return true; 
    		} else{
    			System.out.println("This is NOT THE first marker on " + fromChannel.getLabel());
    			return false;
    		}
        //[ Hint : Use the channelMarkerCount]
    }

    /**
     * Gets called when a Processor receives a message in its buffer
     * Processes the message received in the buffer
     */
    public void update(Observable observable, Object arg) {
    	Buffer fromChannel = (Buffer) observable;
    	Message message = fromChannel.getCurrentMsg();
        Processor sender = message.getFrom();
        
        if(!message.getMessageType().equals(MessageType.MARKER)) {
        		messageCount++;
        }
        if (message.getMessageType().equals(MessageType.MARKER)) {
            //Record from Channel as Empty
            recordChannelAsEmpty(fromChannel);
            //if the marker comes back to the initiator then it should stop recording.
            if(channelMarkerCount.get(fromChannel)>=1) {
            		stopChannel(fromChannel);
            }
            if (isFirstMarker(fromChannel)) {
                recordChannelAsEmpty(fromChannel);
                channelMarkerCount.put(fromChannel, channelMarkerCount.get(fromChannel) + 1);
                //From the other incoming Channels (excluding the fromChannel which has sent the marker
                // start recording messages
                //Trigger the recorder thread from this processor so that it starts recording for each channel
                // Exclude the "Channel from which marker has arrived.
                for(Buffer c : inChannels) {
                		if(c!= fromChannel) {
                			recordChannel(c);
                		}
                }
            } else {
            		stopChannel(fromChannel);
                //Means it isDuplicateMarkerMessage.
                //Stop the recorder thread.
            }
            //Send marker messages to each of the out channels
            for(Buffer outchannel : outChannels) {
	        		Message m = new Message(MessageType.MARKER,this);
	        		sendMessageTo(m, outchannel);
	        }
        }
        else if (message.getMessageType().equals(MessageType.ALGORITHM)) {
                System.out.println("Processing Algorithm message....");
        }
    }


	public void stopChannel(Buffer channel) {
		System.out.println("Stopping " + channel.getLabel());
		inChannels.remove(channel);
		channel.deleteObserver(this);
	}
	
    public void initiateSnapShot() {
    	//recording its current state
        recordMyCurrentState();
        //Sends out a marker message on outgoing channel
        for(Buffer outchannel : outChannels) {
        		Message m = new Message(MessageType.MARKER,this);
        		sendMessageTo(m, outchannel);
        }
        //Starts recording on each of the input channels
        for(Buffer inchannel : inChannels) {
    			recordChannel(inchannel);
        }
    }
}

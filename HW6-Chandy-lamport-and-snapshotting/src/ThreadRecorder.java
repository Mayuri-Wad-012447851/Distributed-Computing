import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * ThreadRecorder: This thread class is instantiated by processor on receiving first marker message
 * It monitors incoming channel's state for a processor
 * It records message seen from first marker until duplicate marker on given channel 
 * @author Mayuri Wadkar
 *
 */
public class ThreadRecorder extends Thread {
	
	private static Logger logger = Logger.getLogger(ThreadRecorder.class.getName());
	
	private String threadName;
	private Buffer channelToRecord;
	private List<Message> channelState;
	
	public ThreadRecorder(String threadName) {
		this.threadName = threadName;
		this.channelState = new ArrayList<Message>();
		this.channelToRecord = null;
	}
	
	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public Buffer getChannelToRecord() {
		return channelToRecord;
	}

	public void setChannelToRecord(Buffer channelToRecord) {
		this.channelToRecord = channelToRecord;
	}

	@Override
	public void run() {

		logger.info("Start recording on in-channel thread :\t"+ this.threadName);
		// Event loop for Recorder thread
		while (true) {
			try {
				synchronized (this) {
					wait(2000);	
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			StringBuilder sbuf = new StringBuilder();
			for(Message msg: channelToRecord.getMessages()) {
				sbuf.append(msg.getMessageType()+" ");
			}
			logger.info("Waking up Recorder Thread: "+ this.threadName + ". Processing channelToRecord size : "+ 
							channelToRecord.getTotalMessageCount() +" Msgs["+ sbuf.toString() +"]");
			
			while(!channelToRecord.isEmpty()) {
				Message message = channelToRecord.getMessage();
				
				if(MessageType.MARKER.equals(message.getMessageType())) {
					// Print Msgs in channel between the two Marker msgs
					StringBuilder sbuff = new StringBuilder();
					sbuff.append("Received duplicate marker...Terminating Recorder Thread: "+ this.threadName);
					sbuff.append("\nMessages Recorded until duplicate marker: ["); 
					for(Message msg: channelState) {
						sbuff.append(msg.getMessageType()+" ");
					}
					sbuff.append("]  Total recorded msgs ="+ channelState.size());
					logger.info(sbuff.toString());
					return;
				}
				channelState.add(message);
			}
			
		}
	}

}

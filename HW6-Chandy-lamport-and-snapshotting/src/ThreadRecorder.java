import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Mayuri Wadkar
 *
 */
public class ThreadRecorder extends Thread {
	
	private String threadName;
	private Buffer channelToRecord;
	private Map<Buffer, List<Message>> channelState;
	
	public ThreadRecorder(String threadName) {
		this.threadName = threadName;
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

	public synchronized void setChannel(Buffer channel) {
		this.channelToRecord = channel;
	}
	public Buffer getChannel() {
		return channelToRecord;
	}
	public Map<Buffer, List<Message>> getChannelState() {
		return channelState;
	}
	public synchronized void setChannelState(Map<Buffer, List<Message>> channelState) {
		this.channelState = channelState;
	}

	@Override
	public void run() {
		//creating ThreadRecorder instance to start threaded recording on channel
//		System.out.print("Messages at channel "+channelToRecord.getLabel()+"--> [");
//		for(Message message:channelToRecord.getMessages()) {
//			System.out.print(message.getMessageType()+",");
//		}
//		System.out.println("]");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
		}
		int lastIdx = channelToRecord.getTotalMessageCount() - 1;
		int count = lastIdx;
		List<Message> recordedMessagesSinceMarker = new ArrayList<>();
		while(count >= 0) {
			Message message = channelToRecord.getMessage(lastIdx);
			if(MessageType.MARKER.equals(message.getMessageType())) {
				System.out.println("Received duplicate marker...stop recording at channel "+channelToRecord.getLabel());
				System.out.print("Messages Recorded since marker until duplicate marker:\t [");
				for(Message msg:recordedMessagesSinceMarker) {
					System.out.print(msg.getMessageType()+" ");
				}
				System.out.print("]\n");
				break;
			}
			recordedMessagesSinceMarker.add(message);
			count--;
		}try {
			channelState.put(channelToRecord, recordedMessagesSinceMarker);
		}catch(NullPointerException e) {
			
		}
		
	}

}

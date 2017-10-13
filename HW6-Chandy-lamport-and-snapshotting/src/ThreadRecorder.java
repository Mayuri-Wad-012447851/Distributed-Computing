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
		int lastIdx = channelToRecord.getTotalMessageCount() - 1;
		List<Message> recordedMessagesSinceMarker = new ArrayList<>();
		while(lastIdx >= 0) {
			Message message = channelToRecord.getMessage(lastIdx);
			if(MessageType.MARKER.equals(message.getMessageType()))
				break;
			recordedMessagesSinceMarker.add(message);
			lastIdx--;
		}
		channelState.put(channelToRecord, recordedMessagesSinceMarker);
	}

}

import java.util.Observable;
import java.util.Observer;

/**
 * 
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Processor extends Thread implements Observer {
    private Buffer messageBuffer ;
    private int procID ;
	private VectorClock vc ; 
	private String threadname;
    public int eventCount;
    private int numProcessors;
    
    /**
     * Processor constructor creates an instance of its own messageBugger
     * Assigns itself a unique identifier
     * Creates an instance of its Vector clock
     * Adds itself as an observer for messageBuffer Buffer
     * @param id
     * @param totalProcessors
     * @param threadName
     */
    public Processor(int id, int totalProcessors, String threadName) {
        this.messageBuffer = new Buffer(); 
    		this.procID = id; 
    		this.numProcessors = totalProcessors;
        this.vc = new VectorClock(totalProcessors);
        this.threadname = threadName;
        this.messageBuffer.addObserver(this);
    }
    public int getProcID() {
		return procID;
	}
	public void setProcID(int procID) {
		this.procID = procID;
	}
	public VectorClock getVc() {
		return vc;
	}
	public void setVc(VectorClock vc) {
		this.vc = vc;
	}
    public Buffer getMessageBuffer() {
		return messageBuffer;
	}
	public void setMessageBuffer(Buffer messageBuffer) {
		this.messageBuffer = messageBuffer;
	}
	public void sendMessageToMyBuffer(Message msg){
		this.messageBuffer.setMessage(msg);
    }
	
	/**
	 * This method is invoked when a messageBuffer value is changed for this processor
	 */
    public void update(Observable observable, Object arg) {
    		Buffer buffer = (Buffer) observable;
    		Message msg = buffer.getMessage();
    		Event event = msg.getEvent();
    		EventType type = event.getEventType();
    		VectorClock vc = msg.getVectorClock();			
    		switch(type) {
			case SEND:
				System.out.println("SEND event found so it is a RECEIVE event at this P" + procID);
				calculateVectorClocks(vc);
				System.out.println("VectorClock after this RECEIVE event at P" + procID );
				printArray(vc.getTimestampArray());
				break;
			default:
				break;
		}
	}
    
    /**
     * compares two vector clock time-stamps
     * @param receivedVC
     * @return
     */
	public int compareTo(VectorClock receivedVC) {
		int[] ts = this.vc.getTimestampArray();
		int[] receivedTs = receivedVC.getTimestampArray();
		int retval =  Integer.valueOf(ts[this.procID]).compareTo(Integer.valueOf(receivedTs[this.procID]));
		if(retval > 0) {
	      System.out.println("obj1 i greater than obj2");
		} else if(retval < 0) {
	      System.out.println("obj1 is less than obj2");
		} else {
	      System.out.println("obj1 is equal to obj2");
		}
	    return retval; 
	}
	
	/**
	 * vector clock time-stamp indices are compared, computed and updated
	 * @param vc2
	 */
    public void calculateVectorClocks(VectorClock vc2) {
		for(int i = 0;i < numProcessors; i++) {
			if(i != procID) {
				int max = Math.max(vc.getTimestampArray()[i], vc2.getTimestampArray()[i]);
				vc.update(i, max);
			}
		}
		if(vc.getTimestampArray()[procID] < vc2.getTimestampArray()[procID]) {
			vc.update(procID,vc2.getTimestampArray()[procID]+1);
		}
    }
    
    /**
     * this method executes an event at a time from a list of events for specific processor
     * for any type of event it increments its own vector clock index by 1
     * @param event		list of events at current processor
     */
    public void executeEvent(Event event) {
		switch(event.getEventType()) {
		case RECEIVE:
			vc.update(procID, vc.getTimestampArray()[procID]+1);
			System.out.println("VectorClock after RECEIVE event at P" + procID );
			printArray(vc.getTimestampArray());
			eventCount++;
			break;
		case SEND:
			System.out.println("P" + procID + " SEND to P" +event.getToProcessor().getProcID());
			Message message = new Message(event,vc,this,event.getToProcessor());
			vc.update(procID, vc.getTimestampArray()[procID]+1);
			System.out.println("VectorClock after SEND event at P" + procID );
			printArray(vc.getTimestampArray());
			event.getToProcessor().sendMessageToMyBuffer(message);
			eventCount++;
			break;
		case COMPUTE:
			vc.update(procID , vc.getTimestampArray()[this.procID]+1);
			System.out.println("VectorClock after compute event at P" + procID );
			printArray(vc.getTimestampArray());
			eventCount++;
			break;
		default:
			break;
		}
    }
    
    /**
     * this method prints vector time-stamp array to console
     * @param array		time-stamp
     */
	public static void printArray(int[] array) {
		System.out.print("[");
	    for (int i : array) {

	        System.out.print(i + " ");
	    }
		System.out.print("]");
		System.out.println();
	}
}

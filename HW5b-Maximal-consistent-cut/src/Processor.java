import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

/**
 * Processor class
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Processor implements Observer {
    private Buffer messageBuffer ;
    private int procID ;
	private VectorClock vc ; 
    private int eventCount;
    private int numProcessors;
    List<Event> eventsThatHappened;
    List<Event> eventList;
    //stores list of vector clocks at this processor after every event
    private ArrayList<Entry<VectorClock,Event>> store;
    
    /**
     * Processor constructor creates an instance of its own messageBuffer
     * Assigns itself a unique identifier
     * Creates an instance of its Vector clock
     * Adds itself as an observer for messageBuffer Buffer
     * @param id
     * @param totalProcessors
     * @param threadName
     */
    public Processor(int id, int totalProcessors) {
    		eventList = new ArrayList<Event>();
    		eventsThatHappened = new ArrayList<Event>();
    		store = new ArrayList<Entry<VectorClock,Event>>();
    		this.messageBuffer = new Buffer(); 
    		this.procID = id; 
    		this.numProcessors = totalProcessors;
        this.vc = new VectorClock(totalProcessors);
        this.messageBuffer.addObserver(this);
    }
    public List<Event> getEventsThatHappened(){
    		return this.eventsThatHappened;
    }
    public int getEventCount() {
    		return this.eventCount;
    }
    public void setEvents(List<Event> eventList) {
		this.eventList = eventList;
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
	
    public ArrayList<Entry<VectorClock, Event>> getStore() {
		return store;
	}
	public void setStore(ArrayList<Entry<VectorClock, Event>> store) {
		this.store = store;
	}
	public void update(Observable observable, Object arg) {
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
			int max = Math.max(vc.getTimestampArray()[i], vc2.getTimestampArray()[i]);
			vc.update(i, max);
		}
    }
       
    /**
     * this method executes an event at a time from a list of events for specific processor
     * for any type of event it increments its own vector clock index by 1
     * @param event		list of events at current processor
     */
    public void executeEvent(Event event) throws InterruptedException {
    	eventsThatHappened.add(event);
		
    	switch(event.getEventType()) {
		case RECEIVE:
			System.out.println("VectorClock before RECEIVE event at P" + procID );
			printArray(vc.getTimestampArray());
			vc.update(procID, vc.getTimestampArray()[procID]+1);
			//sending vector clock of sender processor
			calculateVectorClocks(event.getFromProcessor().getVc());
			System.out.println("VectorClock after RECEIVE event at P" + procID );
			printArray(vc.getTimestampArray());
			VectorClock vc1 = new VectorClock(this.vc);
			Entry<VectorClock,Event> vcAtEvent = new AbstractMap.SimpleEntry<VectorClock,Event>(vc1,event);
			store.add(vcAtEvent);
			eventCount++;
			break;
			
		case SEND:
			System.out.println("P" + procID + " SEND to P" +event.getToProcessor().getProcID());
			Message message = new Message(event,vc,this,event.getToProcessor());
			System.out.println("VectorClock before SEND event at P" + procID );
			printArray(vc.getTimestampArray());
			vc.update(procID, vc.getTimestampArray()[procID]+1);
			System.out.println("VectorClock after SEND event at P" + procID );
			printArray(vc.getTimestampArray());
			event.getToProcessor().sendMessageToMyBuffer(message);
			VectorClock vc2 = new VectorClock(this.vc);
			Entry<VectorClock,Event> vcAtEvent2 = new AbstractMap.SimpleEntry<VectorClock,Event>(vc2,event);
			store.add(vcAtEvent2);
			eventCount++;
			break;
			
		case COMPUTE:
			System.out.println("VectorClock before compute event at P" + procID );
			printArray(vc.getTimestampArray());
			System.out.println("COMPUTING at P"+procID);
			vc.update(procID , vc.getTimestampArray()[procID]+1);
			System.out.println("VectorClock after compute event at P" + procID );
			printArray(vc.getTimestampArray());
			VectorClock vc3 = new VectorClock(this.vc);
			Entry<VectorClock,Event> vcAtEvent3 = new AbstractMap.SimpleEntry<VectorClock,Event>(vc3,event);
			store.add(vcAtEvent3);
			eventCount++;
			break;
			
		default:
			break;
		}
    }
    public int calculateMaximumCut(int[] inputcut) {
    		int cut = inputcut[this.procID]-1;
    		int result = 0;
    		Entry<VectorClock, Event> entry = store.get(cut);
    		EventType eventtype = entry.getValue().getEventType();
		if(eventtype == EventType.COMPUTE || eventtype == EventType.SEND) {
			result = cut + 1;
		}
		else if(entry.getValue().getEventType() == EventType.RECEIVE) {
			//if the current event in the store is receive event
			Processor fromProc = entry.getValue().getFromProcessor();
			int[] clk = entry.getKey().getTimestampArray();
			if(clk[fromProc.getProcID()] <= inputcut[fromProc.getProcID()]) {
				result = cut +1;
			} else {
				return cut;
			}
		}
    		return result;
    }
    /**
     * this method prints vector time-stamp array to console
     * @param array		time-stamp
     */
	public static void printArray(int[] array) {
	    for (int i : array) {
	        System.out.print(i + " ");
	    }
		System.out.println();
	}
	
}

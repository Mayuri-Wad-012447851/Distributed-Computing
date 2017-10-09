import java.util.ArrayList;
import java.util.Map.Entry;
/**
 * this class initiates processors and their corresponding execution plan to compute vector clocks
 * it also computes maximal consistent cut for given list of events and their vector clocks
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Algorithm {	
	int noOfProcessors;
	Processor p0,p1,p2;
	
	/**
	 * Constructor initializes processors and assigns unique identifier values to them
	 * @param noOfProcessors	total number of processors in algorithm
	 */
	public Algorithm(int noOfProcessors) {
		//total number of processors in algorithm is required to initialize the vector time-stamp array
		this.noOfProcessors = noOfProcessors;
		//initializing processor instances
		p0 = new Processor(0, noOfProcessors);
		p1 = new Processor(1, noOfProcessors);
		p2 = new Processor(2, noOfProcessors);
	}
	
	/**
	 * hard-coding execution plans for three processors
	 * it also starts execution for all planned events
	 */
	public void hardcodeExecutionPlan() throws InterruptedException {
		Event compute = new Event(EventType.COMPUTE);
		p2.executeEvent(compute);
		p0.executeEvent(new Event(EventType.SEND,p0,p1));
		p2.executeEvent(compute);
		Event receive1 = new Event(EventType.RECEIVE,p0);
		p1.executeEvent(receive1);
		p2.executeEvent(new Event(EventType.SEND,p2,p1));
		Event receive2 = new Event(EventType.RECEIVE,p2 );
		p1.executeEvent(receive2);
		p0.executeEvent(new Event(EventType.SEND,p0,p2));
		Event receive3 = new Event(EventType.RECEIVE,p0);
		p2.executeEvent(receive3);
		p2.executeEvent(new Event(EventType.SEND,p2,p1));
		p1.executeEvent(new Event(EventType.SEND,p1,p2));
		p0.executeEvent(compute);
		Event receive4 = new Event(EventType.RECEIVE,p1);
		p2.executeEvent(receive4);
		Event receive5 = new Event(EventType.RECEIVE,p2);
		p1.executeEvent(receive5);
		p1.executeEvent(new Event(EventType.SEND,p1,p0));
		Event receive6 = new Event(EventType.RECEIVE,p1);
		p0.executeEvent(receive6);
		p2.executeEvent(compute);
		p0.executeEvent(compute);
	}
	
	/**
	 * starting point of the program
	 * @param args
	 */
	public static void main(String[] args) {
		Algorithm algo = new Algorithm(3);
		algo.init();
	}
	
	/**
	 * init() method is the entry point for all event executions
	 * final values of vector clock time-stamps of each processor are available at the end 
	 */
	public void init() {
		try {
			hardcodeExecutionPlan();
			
			//get vector clock value at p0
			int [] vc0 = p0.getVc().getTimestampArray();
			System.out.println("\nEvent count at p0: "+ p0.getEventCount());
			System.out.print("Vector Clock at Processor P0:\t[");
			for (int i = 0; i < noOfProcessors; i++) {
				System.out.print(vc0[i]+" ");
			}
			System.out.print("]");
			System.out.println();
			//get vector clock value at p1
			System.out.println("Event count at p1: "+ p1.getEventCount());
			System.out.print("Vector Clock at Processor P1:\t[");
			int [] vc1 = p1.getVc().getTimestampArray();
			for (int i = 0; i < noOfProcessors; i++) {
				System.out.print(vc1[i]+" ");
			}
			System.out.print("]");
			System.out.println();
			//get vector clock value at p2
			System.out.println("Event count at p2: "+ p2.getEventCount());
			System.out.print("Vector Clock at Processor P2:\t[");
			int [] vc2 = p2.getVc().getTimestampArray();
			for (int i = 0; i < noOfProcessors; i++) {
				System.out.print(vc2[i]+" ");
			}
			System.out.print("]\n");
			
			//computing maximal consistent cuts
			
			System.out.println("\nComputing maximal consistent cut at {2,2,6}..\n");
			
			int[] result2 = computeMaximumConsistentCut(new int[]{2,2,6});
			System.out.println("\nMaximum consistent cut:");
			printArray(result2);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this method computes Maximum Consistent Cut given an inconsistent cut as input 
	 */
	public int[] computeMaximumConsistentCut(int[] inputcut) {
		int[] result = new int[noOfProcessors];
		
		//getting list of events and their corresponding clock values for 3 processors
		ArrayList<Entry<VectorClock, Event>> p0store = p0.getStore();
		ArrayList<Entry<VectorClock, Event>> p1store = p1.getStore();
		ArrayList<Entry<VectorClock, Event>> p2store = p2.getStore();
		
		System.out.println("\nEvents and VCs at P0:");
		for(Entry<VectorClock, Event> store:p0store) {
			System.out.print(store.getKey());
			System.out.println(store.getValue().getEventType());
		}
		System.out.println("\nEvents and VCs at P1:");
		for(Entry<VectorClock, Event> store:p1store) {
			System.out.print(store.getKey());
			System.out.println(store.getValue().getEventType());
		}
		System.out.println("\nEvents and VCs at P2:");
		for(Entry<VectorClock, Event> store:p2store) {
			System.out.print(store.getKey());
			System.out.println(store.getValue().getEventType());
		}
		
		for(int i = 0; i < inputcut.length; i++) {
			int index = inputcut[i]-1;
			Processor currentProc = null;
			if(this.p0.getProcID() == i) {
				currentProc = this.p0;
			}
			else if(this.p1.getProcID() == i) {
				currentProc = this.p1;
			}
			else if(this.p2.getProcID() == i) {
				currentProc = this.p2;
			}
			ArrayList<Entry<VectorClock, Event>> currentStore = currentProc.getStore();
			while(true) {
				Entry<VectorClock, Event> procEntry = currentStore.get(index);
				if(procEntry.getValue().getEventType() == EventType.COMPUTE || 
						procEntry.getValue().getEventType() == EventType.SEND) {
					result[i] = index+1;
					break;
				}
				else {
					//if the current event in the store is receive event
					Processor fromProc = procEntry.getValue().getFromProcessor();
					int[] clk = procEntry.getKey().getTimestampArray();
					if(clk[fromProc.getProcID()] <= inputcut[fromProc.getProcID()]) {
						result[i] = index+1;
						break;
					}
					else {
						index = index-1;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * this method prints the Maximal Consistent Cut array representation to console
	 * @param array
	 */
	public static void printArray(int[] array) {
	    for (int i : array) {
	        System.out.print(i + " ");
	    }
		System.out.println();
	}
}
/**
 * Vector Clock Implementation
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
		p0 = new Processor(0, noOfProcessors,"Thread"+Integer.toString(0));
		p1 = new Processor(1, noOfProcessors, "Thread"+Integer.toString(1));
		p2 = new Processor(2, noOfProcessors,"Thread"+Integer.toString(2));
	}
	
	/**
	 * hard-coding execution plans for three processors
	 * it also starts execution for all planned events
	 */
	public void hardcodeExecutionPlan() {
		Event receive = new Event(EventType.RECEIVE);
		Event compute = new Event(EventType.COMPUTE);
		p2.executeEvent(compute);
		Event e1 = new Event(EventType.SEND,p0,p1);
		p0.executeEvent(e1);
		p2.executeEvent(compute);
		p1.executeEvent(receive);
		Event e12 = new Event(EventType.SEND,p2,p1);
		p2.executeEvent(e12);
		p1.executeEvent(receive);
		Event e2 = new Event(EventType.SEND,p0,p2);
		p0.executeEvent(e2);
		p2.executeEvent(receive);
		Event e14 = new Event(EventType.SEND,p2,p1);
		p2.executeEvent(e14);
		Event e8 = new Event(EventType.SEND,p1,p2);
		p1.executeEvent(e8);
		p0.executeEvent(compute);
		p2.executeEvent(receive);
		Event e17 = new Event(EventType.SEND,p1,p0);
		p1.executeEvent(e17);
		p0.executeEvent(receive);
		p2.executeEvent(compute);
		p0.executeEvent(compute);
	}
	
	/**
	 * starting point of the program
	 * @param args
	 */
	public static void main(String[] args) {
		//initializing algorithm with 3 processors
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
			int [] vc0 = p0.getVc().getTimestampArray();
			System.out.println("Event count at p0: "+ p0.eventCount);
			System.out.print("\nVector Clock at Processor P0:\t[");
			for (int i = 0; i < noOfProcessors; i++) {
				System.out.print(vc0[i]+" ");
			}
			System.out.print("]");
			System.out.println();
			System.out.println("Event count at p1: "+ p1.eventCount);
			System.out.print("\nVector Clock at Processor P1:\t[");
			int [] vc1 = p1.getVc().getTimestampArray();
			for (int i = 0; i < noOfProcessors; i++) {
				System.out.print(vc1[i]+" ");
			}
			System.out.print("]");
			System.out.println();
			System.out.println("Event count at p2: "+ p2.eventCount);
			System.out.print("\nVector Clock at Processor P2:\t[");
			int [] vc2 = p2.getVc().getTimestampArray();
			for (int i = 0; i < noOfProcessors; i++) {
				System.out.print(vc2[i]+" ");
			}
			System.out.print("]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
package VectorClock;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {
	
	int noOfProcessors;
	Processor p0,p1,p2;

	public Algorithm(int noOfProcessors) {
		this.noOfProcessors = noOfProcessors;
		p0 = new Processor(0, noOfProcessors,"Thread"+Integer.toString(0));
		p1 = new Processor(1, noOfProcessors, "Thread"+Integer.toString(1));
		p2 = new Processor(2, noOfProcessors,"Thread"+Integer.toString(2));
	}
	
	public void hardcodeExecutionPlan() {
		List<Event> eventList = new ArrayList<Event>();
		Event e1 = new Event(EventType.SEND_EVENT,p0,p1);
		eventList.add(e1);
		Event e2 = new Event(EventType.SEND_EVENT,p0,p2);
		eventList.add(e2);
		Event e3 = new Event(EventType.COMPUTE_EVENT,p0,p0);
		eventList.add(e3);
		Event e4 = new Event(EventType.RECEIVE_EVENT,p1,p0);
		eventList.add(e4);
		Event e5 = new Event(EventType.COMPUTE_EVENT,p0,p0);
		eventList.add(e5);
		p0.setEvents(eventList);
		
		List<Event> eventList1 = new ArrayList<Event>();
		Event e6 = new Event(EventType.RECEIVE_EVENT,p0,p1);
		eventList1.add(e6);
		Event e7 = new Event(EventType.RECEIVE_EVENT,p2,p1);
		eventList1.add(e7);
		Event e8 = new Event(EventType.SEND_EVENT,p1,p2);
		eventList1.add(e8);
		Event e9 = new Event(EventType.RECEIVE_EVENT,p2,p1);
		eventList1.add(e9);
		Event e17 = new Event(EventType.SEND_EVENT,p1,p0);
		eventList1.add(e17);
		p1.setEvents(eventList1);
		
		
		List<Event> eventList2 = new ArrayList<Event>();
		Event e10 = new Event(EventType.COMPUTE_EVENT,p2,p2);
		eventList2.add(e10);
		Event e11 = new Event(EventType.COMPUTE_EVENT,p2,p2);
		eventList2.add(e11);
		Event e12 = new Event(EventType.SEND_EVENT,p2,p1);
		eventList2.add(e12);
		Event e13 = new Event(EventType.RECEIVE_EVENT,p0,p2);
		eventList2.add(e13);
		Event e14 = new Event(EventType.SEND_EVENT,p2,p1);
		eventList2.add(e14);
		Event e15 = new Event(EventType.RECEIVE_EVENT,p1,p2);
		eventList2.add(e15);
		Event e16 = new Event(EventType.COMPUTE_EVENT,p2,p2);
		eventList2.add(e16);
		p2.setEvents(eventList2);
		
	}
	
	public static void main(String[] args) {
		Algorithm algo = new Algorithm(3);
		try {
			algo.hardcodeExecutionPlan();
			algo.p0.start();
			algo.p1.start();
			algo.p2.start();
			
			algo.p0.join();
			algo.p1.join();
			algo.p2.join();
			
			int [] vc0 = algo.p0.getVc().getTimestampArray();
			System.out.print("\nVector Clock at Processor P0:\t[");
			for (int i = 0; i < algo.noOfProcessors; i++) {
				System.out.print(vc0[i]+" ");
			}
			System.out.print("]");
			System.out.print("\nVector Clock at Processor P1:\t[");
			int [] vc1 = algo.p1.getVc().getTimestampArray();
			for (int i = 0; i < algo.noOfProcessors; i++) {
				System.out.print(vc1[i]+" ");
			}
			System.out.print("]");
			System.out.print("\nVector Clock at Processor P2:\t[");
			int [] vc2 = algo.p2.getVc().getTimestampArray();
			for (int i = 0; i < algo.noOfProcessors; i++) {
				System.out.print(vc2[i]+" ");
			}
			System.out.print("]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
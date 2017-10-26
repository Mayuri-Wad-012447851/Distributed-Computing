import java.util.ArrayList;
import java.util.List;

/**
 * This class initiates Leader Election algorithm implementation
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Algorithm {
	
	Processor p0, p1, p2, p3, p4;
	List<Processor> processors = new ArrayList<Processor>();

	public static void main(String[] args) throws InterruptedException {
		Algorithm a = new Algorithm();
		a.init();
		a.printTopology();
		a.execute();
	}
	
	/**
	 * This method creates channels and processors required for algorithm
	 */
	public void init() {
		
		Buffer p0Buffer = new Buffer();
		Buffer p1Buffer = new Buffer();
		Buffer p2Buffer = new Buffer();
		Buffer p3Buffer = new Buffer();
		Buffer p4Buffer = new Buffer();
		
		p0 = new Processor(10, p0Buffer);
		p1 = new Processor(44, p1Buffer);
		p2 = new Processor(6,  p2Buffer);
		p3 = new Processor(50, p3Buffer);
		p4 = new Processor(2,  p4Buffer);
		
		p0.setLeft(p1);
		p0.setRight(p4);
		p1.setLeft(p2);
		p1.setRight(p0);
		p2.setLeft(p3);
		p2.setRight(p1);
		p3.setLeft(p4);
		p3.setRight(p2);
		p4.setLeft(p0);
		p4.setRight(p3);
		
		processors.add(p0);
		processors.add(p1);
		processors.add(p2);
		processors.add(p3);
		processors.add(p4);
	}
	
	/**
	 * This method starts each processor's thread
	 * Invokes each processor's run() method
	 * Each processor thread joins back here and stops.
	 * @throws InterruptedException
	 */
	public void execute() throws InterruptedException {
		
		p0.start();
		p1.start();
		p2.start();
		p3.start();
		p4.start();
		p0.join();
		p1.join();
		p2.join();
		p3.join();
		p4.join();
		
		System.out.println("--------------------------------------------------------------");
		for(Processor p : processors) {
			if(p.isLeader()) {
				System.out.println("Processor P"+p.getProcessorID()+" has been elected as leader.");
			}
		}
		System.out.println("--------------------------------------------------------------");
		
	}
	
	public void printTopology() {
		System.out.println("Ring Topology\n");
		System.out.print("-->("+p0.getProcessorID()+")-->");
		System.out.print("("+p1.getProcessorID()+")-->");
		System.out.print("("+p2.getProcessorID()+")-->");
		System.out.print("("+p3.getProcessorID()+")-->");
		System.out.print("("+p4.getProcessorID()+")-->\n\n");
	}
}

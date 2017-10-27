import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class initiates Leader Election algorithm implementation
 * 
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Algorithm {

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
		
		System.out.println("Enter number of processors:\t");
		Scanner sc = new Scanner(System.in);
		int noOfProcessors = sc.nextInt();
		
		//Initializing processor nodes with id, value and a message buffer
		for(int i = 0 ; i < noOfProcessors; i++) {
			System.out.println("Enter value for processor P"+i+" :\t");
			Processor p = new Processor(i, sc.nextInt(), new Buffer());
			processors.add(p);
		}
		
		// Assigning left and right neighbors to each processor in order to form a ring
		// topology
		for(int i = 0 ; i < noOfProcessors; i++) {
			Processor p = processors.get(i);
			
			if(i == 0) {
				p.setLeft(processors.get(i+1));
				p.setRight(processors.get(noOfProcessors - 1));
			}
			else if(i == noOfProcessors - 1) {
				p.setLeft(processors.get(0));
				p.setRight(processors.get(i-1));
			}
			else {
				p.setLeft(processors.get(i+1));
				p.setRight(processors.get(i-1));
			}
		}
	}

	/**
	 * This method starts each processor's thread Invokes each processor's run()
	 * method Each processor thread joins back here and stops.
	 * 
	 * @throws InterruptedException
	 */
	public void execute() throws InterruptedException {
		
		for (Processor p : processors) {
			p.start();
		}
		
		for (Processor p : processors) {
			p.join();
		}

		System.out.println("--------------------------------------------------------------");
		// After all the phases have been completed, the only Processor whose leader
		// value is set to true is the final leader
		for (Processor p : processors) {
			if (p.isLeader()) {
				System.out.println("Processor P" + p.getProcessorID() +" with value "+p.getProcessorValue()+ " has been elected as leader.");
			}
		}
		System.out.println("--------------------------------------------------------------");

	}

	/**
	 * This method displays nodes in ring with their corresponding integer values.
	 */
	public void printTopology() {
		System.out.println("Ring Topology\n");
		for(Processor p : processors) {
			System.out.print("-->(Id: " + p.getProcessorID() + ", Value: "+p.getProcessorValue()+")-->");
		}
		System.out.print("\n\n");
	}
}

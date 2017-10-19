import java.util.ArrayList;
import java.util.List;

/**
 * This class initiates Leader Election algorithm implementation
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class Algorithm {
	
	Processor p0, p1, p2, p3, p4;

	public static void main(String[] args) throws InterruptedException {
		Algorithm a = new Algorithm();
		a.init();
		a.execute();
	}
	
	/**
	 * This method creates channels and processors required for algorithm
	 */
	public void init() {
		
		//Channel between Processor p0 and p1
		Buffer channel01 = new Buffer("channel01");
		//Channel between Processor p1 and p2
		Buffer channel12 = new Buffer("channel12");
		//Channel between Processor p2 and p3
		Buffer channel23 = new Buffer("channel23");
		//Channel between Processor p3 and p4
		Buffer channel34 = new Buffer("channel34");
		//Channel between Processor p4 and p5
		Buffer channel40 = new Buffer("channel40");
		
		//Creating and initializing processors 
		//Assigning Id, in-channel and out-channel to each processor 
		p0 = new Processor(10, channel40, channel01);
		p1 = new Processor(66, channel01, channel12);
		p2 = new Processor(55, channel12, channel23);
		p3 = new Processor(11, channel23, channel34);
		p4 = new Processor(12, channel34, channel40);

		//Assigning left node to each processor as we are dealing with ring topology
		p0.setLeft(p1);
		p1.setLeft(p2);
		p2.setLeft(p3);
		p3.setLeft(p4);
		p4.setLeft(p0);

	}
	
	/**
	 * This method starts each processor's thread
	 * Invokes each processor's run() method
	 * Each processor thread joins back here and stops.
	 * @throws InterruptedException
	 */
	public void execute() throws InterruptedException {
		
		List <Processor> listOfProcessor = new ArrayList<Processor>();
		listOfProcessor.add(p0);
		listOfProcessor.add(p1);
		listOfProcessor.add(p2);
		listOfProcessor.add(p3);
		listOfProcessor.add(p4);
		
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
		
	}
}

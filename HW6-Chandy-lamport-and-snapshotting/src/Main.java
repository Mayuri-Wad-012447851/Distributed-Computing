import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Main : This class initiates chandy lamport algorithm
 * It creates processor instances and assigns their corresponding in and out channels.
 * @author mayur
 *
 */
public class Main {

	private static Logger logger = Logger.getLogger("Main");
		
    public static void main(String args[]) throws InterruptedException {
    	
    	// Create channels for 3 Processors P1, P2 & P3
    	//Channel from P1 to P2
    	Buffer channelP12 = new Buffer("P12");
    	//Channel from P1 to P3
    	Buffer channelP13 = new Buffer("P13");
    	//Channel from P2 to P1
    	Buffer channelP21 = new Buffer("P21");
    	//Channel from P2 to P3
    	Buffer channelP23 = new Buffer("P23");
    	//Channel from P3 to P1
    	Buffer channelP31 = new Buffer("P31");
    	//channel from P3 to P2
    	Buffer channelP32 = new Buffer("P32"); 
    	
    	//assigning processor 1's in and out channels
        List<Buffer> inChannelsP1 = new ArrayList<>();
        inChannelsP1.add(channelP31);
        inChannelsP1.add(channelP21);
        List<Buffer> outChannelsP1 = new ArrayList<>();
        outChannelsP1.add(channelP13);
        outChannelsP1.add(channelP12);
        Processor processor1 = new Processor(1, inChannelsP1, outChannelsP1); //Only observes in channels.
        
        //assigning processor 2's in and out channels
        List<Buffer> inChannelsP2 = new ArrayList<>();
        inChannelsP2.add(channelP32);
        inChannelsP2.add(channelP12);
        List<Buffer> outChannelsP2 = new ArrayList<>();
        outChannelsP2.add(channelP21);
        outChannelsP2.add(channelP23);
        Processor processor2 = new Processor(2, inChannelsP2, outChannelsP2); //Only observes in channels.
        
        //assigning processor 3's in and out channels
        List<Buffer> inChannelsP3 = new ArrayList<>();
        inChannelsP3.add(channelP13);
        inChannelsP3.add(channelP23);
        List<Buffer> outChannelsP3 = new ArrayList<>();
        outChannelsP3.add(channelP31);
        outChannelsP3.add(channelP32);
        Processor processor3 = new Processor(3, inChannelsP3, outChannelsP3); //Only observes in channels.
        
        //initiating snapshot on processor 1
        execute(processor1, processor2, processor3);
        
        threadSleep(2000);
        logger.info("============== All threads completed ... End of Chandy Lamport Algo =================");	
    }
    
    public static void execute(Processor processor1, Processor processor2,Processor processor3) {
        try {
        	processor2.start();
            processor3.start();
        	processor1.start();
			
			processor2.join();
	        processor3.join();
	        processor1.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        
    }
    
    private static void threadSleep(int msec) {
		try {
			Thread.sleep(msec);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

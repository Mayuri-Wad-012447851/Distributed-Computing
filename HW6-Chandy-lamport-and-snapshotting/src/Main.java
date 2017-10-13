import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Mayuri Wadkar
 *
 */
public class Main {

    public static void main(String args[]) throws InterruptedException {

        //Channels from P1 to P2 and P3
    	Buffer channelP12 = new Buffer("channel C12");
    	Buffer channelP13 = new Buffer("channel C13");
    	
    	//Channels from P2 to P1 and P3
    	Buffer channelP21 = new Buffer("channel C21");
    	Buffer channelP23 = new Buffer("channel C23");
        
        //Channels from P3 to P2 and P1
    	Buffer channelP31 = new Buffer("channel C31");
        Buffer channelP32 = new Buffer("channel C32");

        List<Buffer> inChannelsP1 = new ArrayList<>();
        inChannelsP1.add(channelP31);
        inChannelsP1.add(channelP21);
        List<Buffer> outChannelsP1 = new ArrayList<>();
        outChannelsP1.add(channelP13);
        outChannelsP1.add(channelP12);
        Processor processor1 = new Processor(1, inChannelsP1, outChannelsP1); //Only observes in channels.

        List<Buffer> inChannelsP2 = new ArrayList<>();
        inChannelsP2.add(channelP12);
        inChannelsP2.add(channelP32);
        List<Buffer> outChannelsP2 = new ArrayList<>();
        outChannelsP2.add(channelP21);
        outChannelsP2.add(channelP23);
        Processor processor2 = new Processor(2, inChannelsP2, outChannelsP2); //Only observes in channels.


        List<Buffer> inChannelsP3 = new ArrayList<>();
        inChannelsP3.add(channelP13);
        inChannelsP3.add(channelP23);
        List<Buffer> outChannelsP3 = new ArrayList<>();
        outChannelsP3.add(channelP31);
        outChannelsP3.add(channelP32);
        Processor processor3 = new Processor(3, inChannelsP3, outChannelsP3); //Only observes in channels.

        Algorithm a = new Algorithm(processor1, processor2, processor3);
        a.executionPlanP1();
		a.executionPlanP2();
		a.executionPlanP3();
		//Choosing processor1 to initiate a snapshot. 
        processor1.initiateSnapShot();
        
        System.out.println("\nProcessor2's message count: " + processor2.getMessageCount());
        System.out.println("\nProcessor3's message count: " + processor3.getMessageCount());
    }
}

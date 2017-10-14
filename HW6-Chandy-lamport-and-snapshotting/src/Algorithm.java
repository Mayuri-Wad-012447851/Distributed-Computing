/**
 * This is the simulation of a main algorithm that will run on processors P1, P2, P3
 * This could be a banking application, payroll application or any other distributed application
 */
public class Algorithm {

    /**
     * The processors which will participate in a distributed application
     */
    Processor processor1, processor2, processor3;

    public Algorithm(Processor processor1, Processor processor2, Processor processor3) {
    	this.processor1 = processor1;
		this.processor2 = processor2;
		this.processor3 = processor3;
    }

    // Write hard coded execution plan for processors
    public void executionPlan() {
    	compute(processor1);
        compute(processor1);
        for(Buffer c : processor1.getOutChannels()) {
    		Message m = new Message(MessageType.SEND);
    		processor1.sendMessgeTo(m, c);
    		compute(processor1);
        }
    }
    
    public void executionPlan2() {
    	compute(processor2);
        compute(processor2);
		for (Buffer c1 : processor2.getOutChannels()) {
			processor2.sendMessgeTo(new Message(MessageType.ALGORITHM), c1);
			processor2.sendMessgeTo(new Message(MessageType.COMPUTATION), c1);
			processor2.sendMessgeTo(new Message(MessageType.RECEIVE), c1);
			processor2.sendMessgeTo(new Message(MessageType.SEND), c1);
		}
    }
    
    public void executionPlan3() {
    	compute(processor3);
        compute(processor3);
		for (Buffer c2 : processor3.getOutChannels()) {
			processor3.sendMessgeTo(new Message(MessageType.ALGORITHM), c2);
			processor3.sendMessgeTo(new Message(MessageType.SEND), c2);
			processor3.sendMessgeTo(new Message(MessageType.RECEIVE), c2);
			processor3.sendMessgeTo(new Message(MessageType.COMPUTATION), c2);
		}
    }

    /**
     * A dummy computation.
     * @param p
     */
    public void compute(Processor p) {
        System.out.println("Doing some computation on Processor" + p.getProcID());
    }

    /**
     *
     * @param to processor to which message is sent
     * @param channel the incoming channel on the to processor that will receive this message
     */
    public void send(Processor to, Buffer channel) {
        to.sendMessgeTo(null, channel); // ALGORITHM
    }

}

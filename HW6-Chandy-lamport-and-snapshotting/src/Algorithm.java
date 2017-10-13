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

    public void executionPlanP1 () throws InterruptedException{
        compute(processor1);
        compute(processor1);
        compute(processor1);
/**
 * TODO: Homework: Implement send message from processor1 to different processors. Add a time gap betweeen two different
 *                send events. Add computation events between two different sends.
 *      [Hint: Create a loop that kills time, sleep , wait on some value etc..]
 *
 */
        for(Buffer c : processor1.outChannels) {
        		Message m = new Message(MessageType.SEND,processor1);
        		processor1.sendMessageTo(m, c);
        		Thread.sleep(200);
        }
    }

    public void executionPlanP2() {
        compute(processor2);
        compute(processor2);
        compute(processor2);
		for (Buffer c : processor2.outChannels) {
			processor2.sendMessageTo(new Message(MessageType.ALGORITHM,processor2), c);
			processor2.sendMessageTo(new Message(MessageType.COMPUTATION,processor2), c);
			processor2.sendMessageTo(new Message(MessageType.RECEIVE,processor2), c);
			processor2.sendMessageTo(new Message(MessageType.SEND,processor2), c);
		}
    }
    
    public void executionPlanP3() {
        compute(processor3);
        compute(processor3);
        compute(processor3);
		for (Buffer c : processor3.outChannels) {
			processor3.sendMessageTo(new Message(MessageType.ALGORITHM,processor3), c);
			processor3.sendMessageTo(new Message(MessageType.SEND,processor3), c);
			processor3.sendMessageTo(new Message(MessageType.RECEIVE,processor3), c);
			processor3.sendMessageTo(new Message(MessageType.COMPUTATION,processor3), c);
		}
    }

    /**
     * A dummy computation.
     * @param p
     */
    public void compute(Processor p) {
        System.out.println("Compute event at Processor" + p.getId());
    }

    /**
     *
     * @param to processor to which message is sent
     * @param channel the incoming channel on the to processor that will receive this message
     */
    public void send(Processor to, Buffer channel) {
        to.sendMessageTo(new Message(MessageType.ALGORITHM,to), channel); // ALGORITHM
    }

}

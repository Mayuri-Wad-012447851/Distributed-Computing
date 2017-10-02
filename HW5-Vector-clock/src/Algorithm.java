public class Algorithm {
	int noOfProcessors;
	Processor p0, p1, p2; // Assume there are three processors.

	public Algorithm() {
		super();
		this.noOfProcessors = 3;
		// TODO : initialize all the processors
		p0 = new Processor(0, 3);
		// p1=
		// p2 =
	}

	// Write hard coded execution plan for processors
	public void executionPlanForP0() {
		// TODO: call events on P0 for compute.
		// Call send events to send message
		// Call receive messages
		

	}

	// Write hard coded execution plan for processors
	public void executionPlanForP1() {
		// TODO: call events on P0 for compute.
		// Call send events to send message
		// Call receive messages

	}

	// Write hard coded execution plan for processors
	public void executionPlanForP2() {
		// TODO: call events on P0 for compute.
		// Call send events to send message
		// Call receive messages

	}
	/**
	 * 
	 * @param p
	 * @param computeMessage
	 */
	public void compute(Processor p, Message computeMessage) {
		// TODO: implement. What will be the value of vector clock passed to
		// this message?
		p.sendMessgeToMyBuffer(computeMessage);
	}

	public void send(Processor to, Message m) {
		// TODO: implement. What will be the value of vector clock passed to
		// this message?

	}

}

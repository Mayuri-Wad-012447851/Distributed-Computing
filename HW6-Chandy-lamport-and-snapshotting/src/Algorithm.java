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
    	super();
    	this.processor1 = processor1;
		this.processor2 = processor2;
		this.processor3 = processor3;
    }
    
    public Processor getProcessor(int procID) {
    	if(procID == 1) {
    		return processor1;
    	}else if(procID == 2) {
    		return processor2;
    	}else if(procID == 3) {
    		return processor3;
    	}
    	return null;
    }
    
    public void execute() {
        try {
        	processor2.start();
            processor3.start();
        	processor1.start();
			processor1.join();
			processor2.join();
	        processor3.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}

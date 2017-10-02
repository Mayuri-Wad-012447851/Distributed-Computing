package VectorClock;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Processor extends Thread implements Observer {
    private Buffer messageBuffer ;
    private int procID ;
	private VectorClock vc ; 
	private List <Event> events;
	private String threadname ;
    
    public Processor(int id, int totalProcesors, String threadName) {
        this.messageBuffer = new Buffer(); 
    	this.procID = id; 
        this.vc = new VectorClock(totalProcesors);
        this.threadname = threadName;
        this.messageBuffer.addObserver(this);
    }
    
    public int getProcID() {
		return procID;
	}
	public void setProcID(int procID) {
		this.procID = procID;
	}
	public VectorClock getVc() {
		return vc;
	}
	public void setVc(VectorClock vc) {
		this.vc = vc;
	}
    public Buffer getMessageBuffer() {
		return messageBuffer;
	}
	public void setMessageBuffer(Buffer messageBuffer) {
		this.messageBuffer = messageBuffer;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
	public void sendMessgeToMyBuffer(MessageType type, Processor fromProcessor, Processor toProcessor){
		this.messageBuffer.setMessage(type,fromProcessor, toProcessor);
    }

    public void update(Observable observable, Object arg) {
    	calculateVectorClocks(observable, arg);
    }

    public void calculateVectorClocks(Observable observable, Object arg) {
    	//TODO: Implement the logic to check based on the current vector clocks and the vector clock
    	//Hint: Get vector clocks for this processor and message from this processors buffer
    	//invoke methods of VectorClock
    	Buffer buff = (Buffer) observable;
		MessageType receivedMessageType = buff.getMsg().getType();
		Processor fromProcessor = buff.getMsg().getFromProcessor();
		Processor toProcessor = buff.getMsg().getToProcessor();
		VectorClock receivedVC = fromProcessor.getVc();
		int [] receivedTS = receivedVC.getTimestampArray();
		int[] value = null;
		
		switch(receivedMessageType) {
		case SEND:
			value = this.getVc().getTimestampArray();
			value[this.procID] += 1;
			synchronized(toProcessor) {
				System.out.println("\tWaking P"+toProcessor.procID);
				toProcessor.notify();
			}
			break;
			
		case RECEIVE:
//			int comparison = this.getVc().compareTo(receivedVC);
//			if(comparison == -1) {
//				
//			}else if(comparison == 0) {
//				
//			}
//			else if(comparison == 1) {
//				
//			}else if(comparison == 2) {
//				
//			}
			if(this.getMessageBuffer().getQueue().isEmpty()) {
				try {
					synchronized(this){
						this.wait();
						System.out.println("\tWoke up P"+this.procID);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			value = this.getVc().getTimestampArray();
			Message poppedMsg = (Message) this.getMessageBuffer().getQueue().remove();
			value[this.procID] += 1;
			for(int i = 0;i < 3;i++) {
				if(i != this.procID) {
					int max = Math.max(value[i], poppedMsg.getToProcessor().getVc().getTimestampArray()[i]);
					value[i] = max;
				}
			}
			System.out.println(this.getVc().toString());
			break;
			
		case COMPUTE:
			value = this.getVc().getTimestampArray();
			value[this.procID] += 1;
			break;
			
		default:
			break;

		}
    }
    
    @Override
	public void run() {
		System.out.println("Starting "+this.threadname);
		for(Event event : this.events) {
			EventType eventtype = event.getEventtype();
			switch(eventtype) {
			case SEND_EVENT:
				System.out.println("SEND_EVENT P"+event.getFromProcessor().procID);
				event.getFromProcessor().sendMessgeToMyBuffer(MessageType.SEND, event.getFromProcessor(), event.getToProcessor());
				break;
				
			case RECEIVE_EVENT:
				System.out.println("RECEIVE_EVENT from P"+event.getFromProcessor().procID+" to P"+event.getToProcessor().procID);
				Processor from = event.getFromProcessor();
				Processor to = event.getToProcessor();
				to.sendMessgeToMyBuffer(MessageType.RECEIVE, from, to);
				break;
				
			case COMPUTE_EVENT:
				System.out.println("COMPUTE_EVENT at P"+event.getFromProcessor().procID);
				Processor fromP = event.getFromProcessor();
				Processor toP = event.getToProcessor();
				fromP.sendMessgeToMyBuffer(MessageType.COMPUTE, fromP, toP);
				break;
				
			default:
				break;
			}
		}
	}
    
}

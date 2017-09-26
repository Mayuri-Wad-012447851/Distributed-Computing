import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Processor class which is implementing Observer interface for observable Buffer.
 * Every processor node in graph on receiving notification about change in Buffer, sends messages to its neighbors.
 * @author Mayuri Wadkar
 * 
 */
public class Processor implements Observer {   
	
	//Each processor has a message Buffer to store messages
    private Buffer messageBuffer ;
    //Each processor has a unique Id
    private Integer id ;
    private List<Processor> children ;
    private Processor parent;
    //Initially it will be all the neighbors of a Processor. When a graph is created this list is populated
    private List<Processor> unexplored ;

    public Processor(int processorId) {
        this.messageBuffer = new Buffer();
        this.id = processorId;
        children = new ArrayList<>();
        //Initially it will be all the neighbors of a Processor. When a graph is created this list is populated
        unexplored = new ArrayList<>();
        //Each processor is observing itself;
        messageBuffer.addObserver(this);
        //Initially parent is not know
        parent = null;
    }
    
    @Override
	public int hashCode() {
    	return id;
    }
	
    /**
     * This method overrides Object's equal method.
     * Processor Objects will be compared based on their IDs.
     */
	@Override
	public boolean equals(Object o) {
		if(this.id == ((Processor) o).getId()) {
			return true;
		}
		else {
			return false;
		}
	}
	
    public Processor getParent() {
		return parent;
	}

	public void setParent(Processor parent) {
		this.parent = parent;
	}
    
    public Buffer getMessageBuffer() {
		return messageBuffer;
	}

	public void setMessageBuffer(Buffer messageBuffer) {
		this.messageBuffer = messageBuffer;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Processor> getChildren() {
		return children;
	}

	public void setChildren(List<Processor> children) {
		this.children = children;
	}

	public List<Processor> getUnexplored() {
		return unexplored;
	}

	public void setUnexplored(List<Processor> unexplored) {
		this.unexplored = unexplored;
	}

	/**
	 * This method will only be used by the Processor to remove other processor nodes from its own unexplored list.
	 * @param procExplored		Id of the processor node to remove from Unexplored
	 */
    private void removeFromUnexplored(int procExplored){
    	for(Processor p: unexplored) {
    		if(p.getId().equals(procExplored)) {
    			unexplored.remove(p);
    			break;
    		}
    	}
    }

    /**
     * This method will add a message to this processors buffer.
     * Other processors will invoke this method to send a message to this Processor
     * @param message
     * @param senderProcessorId
     */
    public void sendMessgeToMyBuffer(Message message, int senderProcessorId){
        messageBuffer.setMessage(message,senderProcessorId);
    }

    /**
     * This is analogous to receive method.
     * Whenever a message is dropped in its buffer this Processor will respond
     */
    public void update(Observable observable, Object arg) {
    		Buffer buff = (Buffer) observable;
    		Message receivedMessage = buff.getMessage();
    		int senderID = buff.getSenderProcId();
    		Processor senderProcessor = Main.getlistOfProcessors().get(senderID);
    		
    		switch(receivedMessage) {
    		case M:
    			System.out.println("\t<M> received at P"+this.getId());
    			if(parent == null) {
    				this.setParent(senderProcessor);
        			this.removeFromUnexplored(senderID);
        			this.explore();
    			}
    			else {
    				senderProcessor.sendMessgeToMyBuffer(Message.ALREADY,this.id);
    				this.removeFromUnexplored(senderID);
    			}
    			break;
    			
    		case PARENT:
    			System.out.println("\t<PARENT> received at P"+this.getId());
    			this.children.add(senderProcessor);
    			this.explore();
    			break;
    			
    		case ALREADY:
    			System.out.println("\t<ALREADY> received at P"+this.getId());
    			this.explore();
    			break;
    			
    		default:
    			break;
    		}
    }

    /**
     * this method allows a Processor to send <M> to its unexplored neighbors and it also allows it to identify its parent
     */
    private void explore(){
    	if(!unexplored.isEmpty()) {
    		Processor procToExplore = unexplored.remove(0);
    		procToExplore.sendMessgeToMyBuffer(Message.M,this.id);
    	}
    	else {
    		if(parent.getId() != this.getId()) {
    			if(parent != null) {
    				parent.sendMessgeToMyBuffer(Message.PARENT,this.id);
    			}
    		}
    	}
    	
    }
}

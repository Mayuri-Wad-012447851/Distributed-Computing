import java.util.Random;

/**
 * MessageType enum: Messages that can be passed between processors in the system
 * @author Mayuri Wadkar
 */
public enum  MessageType {
	ALGORITHM (0),
	SEND (1),
	RECEIVE (2),
	COMPUTE (3),
	MARKER (4)
	;
	
	private final static int TOTAL_MESSAGE_TYPES = 5;
	private final int messageCode;

    MessageType(int code) {
        this.messageCode = code;
    }
    
    public int getLevelCode() {
        return this.messageCode;
    }
    
    public static MessageType getRandomMessage() {
    	Random rand = new Random();
    	int  n = rand.nextInt(TOTAL_MESSAGE_TYPES) + 1;
    	
    	switch(n) {
		case 0:
			return MessageType.ALGORITHM;
			
		case 1:
			return SEND;

		case 2:
			return RECEIVE;
			
		case 3:
			return COMPUTE;		
			
		case 4:
			return COMPUTE;		// Do not simulate MARKER
    	}
    	return MessageType.ALGORITHM;
    }
}

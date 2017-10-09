import java.util.Arrays;

/**
 * VectorClock class stores integer array of timestamp
 * @author Mayuri Wadkar, Eric Han, Sonali Mishra
 *
 */
public class VectorClock {
	private int[] timestampArray;
	public VectorClock(int numberOfProcessors) {
		timestampArray = new int[numberOfProcessors];
		//initialize each processor's timestampArray element to 0
		for(int i = 0; i < numberOfProcessors ; i++) {
			timestampArray[i] = 0;
		}
	}
	public VectorClock(VectorClock copy) {
		int[] cpTSArray = copy.timestampArray;
		timestampArray = new int[cpTSArray.length];
		//initialize each processor's timestampArray element to 0
		for(int i = 0; i < cpTSArray.length ; i++) {
			timestampArray[i] = cpTSArray[i];
		}
	}
	public int[] getTimestampArray() {
		return this.timestampArray;
	}
	public void update(int index, int value) {
		timestampArray[index] = value;
	}
	
	@Override
	public String toString() {
		return "VectorClock [VC=" + Arrays.toString(this.getTimestampArray()) + "]";
	}
	
	public int happenBefore(int[] cut) {
		for(int i = 0; i < timestampArray.length; i ++) {
			if(timestampArray[i] > cut[i]) {
				System.out.println("\ntimestampArray[" + i + "] is greater than cut["+i+"]");
				return i;
			}
		}
		return -1;
	}
}

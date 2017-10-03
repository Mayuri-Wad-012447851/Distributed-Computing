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
	public int[] getTimestampArray() {
		return this.timestampArray;
	}
	public void update(int index, int value) {
		timestampArray[index] = value;
	}
	
	@Override
	public String toString() {
		return "VectorClock [vc=" + Arrays.toString(this.getTimestampArray()) + "]";
	}
	
}

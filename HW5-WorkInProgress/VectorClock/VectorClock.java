package VectorClock;

import java.util.Arrays;

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
	
	public int compareTo(VectorClock receivedVC) {
		// TODO Auto-generated method stub
		//return -1 if 
		//return 0 if 
		//return 1 if 
		//return 2 if not comparable
		int[] ts = this.getTimestampArray();
		int[] receivedTs = receivedVC.getTimestampArray();
//		if ts[0] != receivedTs[0];
//			return 0;
//		if ts[1] != receivedTs[1];
//			return 
		return 0;
	}
}

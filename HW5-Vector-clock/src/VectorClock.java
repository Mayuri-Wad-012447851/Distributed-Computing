public class VectorClock implements Comparable<VectorClock>{
	//TODO: read up how to use a comparable and a comparator
	//TODO: Do you see an advantage in making it an Integer ?? 
	int[] vc;

	public VectorClock( int noOfProcesses ) {
		vc = new int [noOfProcesses];
		//TODO : Set all to 0.  Do you need to explicitly initilize to 0
	}

	@Override
	public int compareTo(VectorClock o) {
		// TODO implement a compare to method that will compare two vector clocks
		//What if the definition of equality of two vector clocks
		//return -1 if 
		//return 0 if 
		//return 1 if 
		//return 2 if not comparable
		return 0;
	}
	/**
	 * Based on a event vector clock will be incremented, changed or updated.
	 * Which index should be updated will be decided by a processor
	 * @param index
	 * @param value
	 */
	public void updateAt(int index, int value){
		//TODO : Apply Vector clock algorithm 
		vc[index]= value;
	}
	
	
}

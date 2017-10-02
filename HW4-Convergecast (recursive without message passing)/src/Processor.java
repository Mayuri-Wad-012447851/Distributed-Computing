/**
 * Processor class for Convergecast, where each processor is a node in a binary tree
 * @author Mayuri Wadkar
 *
 */
public class Processor{
	private int proc_data;
	private Processor left;
	private Processor right;
	//to store max value at each node
	private int maxElement;
	
	public Processor(int data) {
		this.proc_data = data;
		this.left = null;
		this.right = null;
		this.maxElement = this.proc_data;
	}

	public int getProc_data() {
		return proc_data;
	}

	public void setProc_data(int proc_data) {
		this.proc_data = proc_data;
	}

	public Processor getLeft() {
		return left;
	}

	public void setLeft(Processor left) {
		this.left = left;
	}

	public Processor getRight() {
		return right;
	}

	public void setRight(Processor right) {
		this.right = right;
	}

	public int getMaxElement() {
		return maxElement;
	}

	public void setMaxElement(int maxElement) {
		this.maxElement = maxElement;
	}
}

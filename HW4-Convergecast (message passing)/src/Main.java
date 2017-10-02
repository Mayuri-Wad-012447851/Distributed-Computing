/**
 * Converge-cast implementation by Mayuri Wadkar
 * @author Mayuri Wadkar
 *
 */
public class Main {
	
	/**
	 * this method populates tree required for our algorithm
	 * @return root	Root processor node of a tree
	 */
	public Processor populateTree() {
		
		Processor root = new Processor(2);
		Processor node1 = new Processor(7);
		Processor node2 = new Processor(5);
		root.left = node1;
		root.right = node2;
		
		Processor node3 = new Processor(2);
		Processor node4 = new Processor(6);
		node1.left = node3;
		node1.right = node4;
		
		Processor node5 = new Processor(9);
		node2.right = node5;
		
		Processor node6 = new Processor(5);
		Processor node7 = new Processor(11);
		node4.left = node6;
		node4.right = node7;
		
		Processor node8 = new Processor(4);
		node5.left = node8;
		return root;
	}
	
	/**
	 * This main method is the start point for program.
	 * It prints the populated input tree and wakes root up in order to start converge-cast.
	 * It prints the Max value and concatenated string of all node values available at root node.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main m = new Main();
		StringBuffer traversedPath = new StringBuffer();
		//populating tree rooted at node root
		Processor root = m.populateTree();
		
		//printing input tree
		BTreePrinter.printNode(root);
		
		//waking root
		root.sendMessgeToMyBuffer(Message.M, root, traversedPath);
		
		System.out.print("\n---------Max at Root Node:\t");
		System.out.println(root.getMax());
		System.out.println("\n---------Traversed path at Root Node:\t");
		System.out.println(traversedPath.toString());
	}

}

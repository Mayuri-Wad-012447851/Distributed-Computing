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
		root.setLeft(node1);
		root.setRight(node2);
		
		Processor node3 = new Processor(2);
		Processor node4 = new Processor(6);
		node1.setLeft(node3);
		node1.setRight(node4);
		
		Processor node5 = new Processor(9);
		node2.setRight(node5);
		
		Processor node6 = new Processor(5);
		Processor node7 = new Processor(11);
		node4.setLeft(node6);
		node4.setRight(node7);
		
		Processor node8 = new Processor(4);
		node5.setLeft(node8);
		return root;
	}
	
	/**
	 * This is a recursive method to find max element at each and every node starting from leaf node
	 * @param node
	 * @param path
	 */
	public void findMaxElement(Processor node,StringBuffer path) {
		if(node == null) 
			return;
		
		if(node.getLeft() != null)
			findMaxElement(node.getLeft(),path);
		if(node.getRight() != null)
			findMaxElement(node.getRight(),path);	
		
		if(node.getLeft() != null && node.getRight() != null)
			node.setMaxElement(Math.max(Math.max(node.getLeft().getMaxElement(),node.getRight().getMaxElement()),node.getMaxElement())); 
		if(node.getLeft() != null && node.getRight() == null)
			node.setMaxElement(Math.max(node.getLeft().getMaxElement(),node.getMaxElement()));
		if(node.getLeft() == null && node.getRight() != null)
			node.setMaxElement(Math.max(node.getRight().getMaxElement(),node.getMaxElement()));
		
		System.out.format("Processor %d:\t",node.getProc_data());
		path.append(" ->"+Integer.toString(node.getProc_data()));
		System.out.format("MaxElementAtMe:\t = %d\n", node.getMaxElement());
		
		return;
	}
	
	/**
	 * This main method is the start point for program.
	 * It prints the populated input tree and calls recursive findMaxElement() on root to start converge-cast.
	 * It prints the Max value and concatenated string of all node values available at root node.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main m = new Main();
		StringBuffer traversedPath = new StringBuffer();
		Processor root = m.populateTree();
		
		BTreePrinter.printNode(root);
		
		m.findMaxElement(root,traversedPath);
		
		System.out.format("\nMax Element of Tree:\t%d",root.getMaxElement());
		
		System.out.print("\n\nTraversed Path:\t");
		System.out.print(traversedPath+"\n\n");
	}

}

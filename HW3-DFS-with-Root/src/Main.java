import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * Depth First Search Spanning Tree implementation with specified root
 * @author Mayuri Wadkar
 *
 */
public class Main {
	//map to store initial graph to span
	private Map <Processor, List<Processor> > graph = new HashMap<Processor, List<Processor> >();
	//list of processors in graph
	private static List <Processor> listOfProcessors = new ArrayList<Processor>();
	//root of the tree to be formed
	private Processor root = null;
	
    public Processor getRoot() {
		return root;
	}

	public void setRoot(Processor root) {
		this.root = root;
	}

	public static List<Processor> getlistOfProcessors() {
		return listOfProcessors;
	}

	public static void setlistOfProcessors(List<Processor> listOfProcessors) {
		Main.listOfProcessors = listOfProcessors;
	}
	
	/**
	 * This method populates input Graph 
	 * @return root	Root node to build the spanning tree on.
	 */
	private Processor hardcodeGraph() {
    	
		//Creating Processor nodes
    	int numProcs = 6; 
    	for(int i = 0 ; i < numProcs ; i++) {
    		Processor p = new Processor(i);
    		listOfProcessors.add(p);
    	}
    	
    	//choosing a particular processor from graph as a root
   	 	try 
        {
        	Scanner sc = new Scanner(System.in); 
        	System.out.println("\nEnter a specific root node to start from (Values from 0-5):\t");
        	int specifiedRoot = sc.nextInt();
        	sc.close();
        	setRoot(getlistOfProcessors().get(specifiedRoot));

    	// Initializing P0 unexplored list
    	Processor p0 = listOfProcessors.get(0);
    	List <Processor> neighboursP0 = new ArrayList<Processor>();
    	if(root.equals(p0)) {
    		neighboursP0.add(root);
    	}
    	neighboursP0.add(listOfProcessors.get(1));
    	neighboursP0.add(listOfProcessors.get(2));
    	neighboursP0.add(listOfProcessors.get(3));
    	graph.put(p0, neighboursP0);
    	p0.setUnexplored(neighboursP0);
    	
    	// Initializing P1 unexplored list
    	Processor p1 = listOfProcessors.get(1);
    	List <Processor> neighboursP1 = new ArrayList<Processor>();
    	if(root.equals(p1)) {
    		neighboursP1.add(root);
    	}
    	neighboursP1.add(listOfProcessors.get(0));
    	neighboursP1.add(listOfProcessors.get(2));
    	neighboursP1.add(listOfProcessors.get(4));
    	graph.put(p1, neighboursP1);
    	p1.setUnexplored(neighboursP1);
    	
    	// Initializing P2 unexplored list
    	Processor p2 = listOfProcessors.get(2);
    	List <Processor> neighboursP2 = new ArrayList<Processor>();
    	if(root.equals(p2)) {
    		neighboursP2.add(root);
    	}
    	neighboursP2.add(listOfProcessors.get(0));
    	neighboursP2.add(listOfProcessors.get(1));
    	neighboursP2.add(listOfProcessors.get(5));
    	graph.put(p2, neighboursP2);
    	p2.setUnexplored(neighboursP2);
    	
    	// Initializing P3 unexplored list
    	Processor p3 = listOfProcessors.get(3);
    	List <Processor> neighboursP3 = new ArrayList<Processor>();
    	if(root.equals(p3)) {
    		neighboursP3.add(root);
    	}
    	neighboursP3.add(listOfProcessors.get(0));
    	graph.put(p3, neighboursP3);
    	p3.setUnexplored(neighboursP3);
    	
    	// Initializing P4 unexplored list
    	Processor p4 = listOfProcessors.get(4);
    	List <Processor> neighboursP4 = new ArrayList<Processor>();
    	if(root.equals(p4)) {
    		neighboursP4.add(root);
    	}
    	neighboursP4.add(listOfProcessors.get(1));
    	neighboursP4.add(listOfProcessors.get(5));
    	graph.put(p4, neighboursP4);
    	p4.setUnexplored(neighboursP4);
    	
    	// Initializing P5 unexplored list
    	Processor p5 = listOfProcessors.get(5);
    	List <Processor> neighboursP5 = new ArrayList<Processor>();
    	if(root.equals(p5)) {
    		neighboursP5.add(root);
    	}
    	neighboursP5.add(listOfProcessors.get(2));
    	neighboursP5.add(listOfProcessors.get(4));
    	graph.put(p5, neighboursP5);
    	p5.setUnexplored(neighboursP5);
        }
        catch(Exception e){
        	System.err.println("\nRoot ID entered was invalid. Values only from 0-5 are allowed.");
        	System.exit(0);
        }
   	 	//returning root for spanning tree to be generated
		return root;
	}
	
	/**
	 * this method prints the initial graph stored as (key,value) pairs in a map, where key is a Processor and value is List of its neighbors
	 */
	private void printGraph() {
    	Iterator<Entry<Processor, List<Processor>>> iter = graph.entrySet().iterator();
    	
    	System.out.println("\nInput Graph:\n");
    	while(iter.hasNext()) {
    		Map.Entry<Processor, List<Processor>> pair = iter.next();
    		System.out.print("P"+pair.getKey().getId()+": ");
    		System.out.print("[");
    		List<Processor> listP =  pair.getValue();
    		for(Processor pi: listP) {
    			System.out.print("P"+pi.getId()+" ");
    		}
    		System.out.println("]");
    	}
    	System.out.println("\n");
    }
    
	/**
	 * this method prints final spanning tree generated by performing DFS on a graph
	 */
    private void printSpanningTree() {
    	System.out.println("\nFinal Spanning Tree:\n");
    	for(int i = 0; i < 6 ; i++ ) {
    		System.out.print("P"+Main.getlistOfProcessors().get(i).getId()+": ");
    			List<Processor> children = listOfProcessors.get(i).getChildren();
    			System.out.print("[");
    			for(Processor child:children) {
    				System.out.print("P"+child.getId()+" ");
    			}
    			System.out.println("]");
    	}
    }
    
    /**
     * This method prints the populated input graph and wakes root up in order to start DFS.
     */
	public void init(){
    	root = hardcodeGraph();
    	printGraph();
    	//waking root node by sending message <M> to its own buffer
    	root.sendMessgeToMyBuffer(Message.M,root.getId());
    	printSpanningTree();
    }
	
	/**
	 * This main method is the start point for program.
	 */
    public static void main (String args[]){
        Main m = new Main();
    	m.init();
    }
}

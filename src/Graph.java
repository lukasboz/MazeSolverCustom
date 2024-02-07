import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Lukas Bozinov 
 * Description: A class that is used to create an object Graph, using the previously created objects GraphEdge and GraphNode. 
 * This class also implements the methods outlined in GraphADT.
 */

public class Graph implements GraphADT {

	// initialize fields
	private GraphNode[] graphNodes;
	private GraphEdge[][] graphEdges;
	private int numNodes;

	// creates a constructor for the Graph, with n nodes in it
	Graph(int n) {

		numNodes = n; // set the number of nodes to n
		graphNodes = new GraphNode[n]; // create an array of GraphNodes with size n
		graphEdges = new GraphEdge[n][n]; // create a 2d array of GraphEdges (adjacency matrix) with size n*n

		// fill the array of GraphNodes with their respective names (ints)
		for (int i = 0; i < n; i++)
			graphNodes[i] = new GraphNode(i);

	}

	// this method inserts an edge in the given graph using two GraphNodes, one
	// type, and one label
	@Override
	public void insertEdge(GraphNode u, GraphNode v, int type, String label) throws GraphException {

		try {

			// call getNode on each GraphNode; this will throw a GraphException if there are
			// any to be thrown
			getNode(u.getName());
			getNode(v.getName());

			// if no exceptions are thrown by those calls, we can create new GraphEdges and
			// insert them at the respective indices in the adjacency matrix
			graphEdges[u.getName()][v.getName()] = new GraphEdge(u, v, type, label);
			graphEdges[v.getName()][u.getName()] = new GraphEdge(v, u, type, label);

			// catch the GraphException and tell user the error
		} catch (GraphException e) {
			throw new GraphException("ERROR: Either node u or v does not exist.");
		}

	}

	// this method gets the node with the given name (int)
	@Override
	public GraphNode getNode(int name) throws GraphException {
		if (name >= 0 && name < numNodes) { // if the name of the node is valid, return it
			return graphNodes[name];
		}
		throw new GraphException("ERROR: GraphNode not found."); // otherwise thrown an exception
	}

	//this method returns all incident edges of node u in an iterator
	@SuppressWarnings("rawtypes")
	@Override
	public Iterator incidentEdges(GraphNode u) throws GraphException {

		try {
			getNode(u.getName()); //first check if the node exists by calling getNode and catching the exception that could be thrown

			ArrayList<GraphEdge> uArrList = new ArrayList<GraphEdge>(); //create an empty arraylist (will return this later)

			//fill the array list with every edge incident on u
			for (GraphEdge edge : graphEdges[u.getName()]) {
				if (edge != null) {
					uArrList.add(edge); //add the element to the arrayList
				}
			}

			//if the arrayList is empty (i.e., no edges were incident on u) return null
			if (uArrList.isEmpty()) {
				return null;
			} else {
				return uArrList.iterator(); //otherwise return the iterator
			}

		} catch (GraphException e) {
			throw new GraphException("ERROR: Node doesn't exist."); //tell the user the node doesn't exist if the GraphException is thrown
		}

	}

	//this method gets the edge with the given endpoints by looking for the node in the adjacency matrix
	@Override
	public GraphEdge getEdge(GraphNode u, GraphNode v) throws GraphException {

		try {

			//check if getNode returns an exception or not
			getNode(u.getName());
			getNode(v.getName());

			//if noe xception is thrown, check for another exception (if the edge with those two nodes isn't there)
			if (graphEdges[u.getName()][v.getName()] == null && graphEdges[v.getName()][u.getName()] == null) {
				throw new GraphException("ERROR: No edge with these two nodes exists.");
			}

			return graphEdges[u.getName()][v.getName()]; //otherwise, return the edge

		} catch (GraphException e) {
			throw new GraphException("ERROR: Either node u or v does not exist."); //tell the user if either two nodes do not exist
		}

	}

	//this method checks if two node are adjacent to each other (if they have an edge between them)
	@Override
	public boolean areAdjacent(GraphNode u, GraphNode v) throws GraphException {

		try {

			//try to have an exception thrown (if either two nodes doesn't exist)
			getNode(u.getName());
			getNode(v.getName());

			//check if the edge exists
			if (graphEdges[u.getName()][v.getName()] != null) {
				return true; //true if it does, false if not
			}
			return false;

		} catch (GraphException e) {
			throw new GraphException("ERROR: Either node u or v does not exist."); //tell the user the error if the GraphException is thrown at any point
		}

	}

}

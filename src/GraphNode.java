/**
 * @author Lukas Bozinov
 * Professor: Mr. Solis-Oba
 * Course: CS2210
 * Description: A class that is used to create an object GraphNode, used as a node in a graph.
 */
public class GraphNode {

	//initialize fields
	private int nodeName;
	private boolean marking;

	//constructor sets name of node (integer number) and boolean marking (if the node has been searched or not)
	GraphNode(int name) {

		this.nodeName = name;
		this.marking = false;

	}

	//marks this node with a current marking or not
	public void mark(boolean mark) {
		this.marking = mark;
	}

	//checks if this node is marked and returns true/false based on the output
	public boolean isMarked() {
		return this.marking;
	}

	//get the name of this node
	public int getName() {
		return this.nodeName;
	}
	
}

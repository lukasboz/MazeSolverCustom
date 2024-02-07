/**
 * @author Lukas Bozinov
 * Professor: Mr. Solis-Oba
 * Course: CS2210
 * Description: A class that is used to create an object GraphEdge, used as an edge connecting two GraphNodes in a graph.
 */
public class GraphEdge {

	//initialize fields
	private GraphNode nodeU, nodeV;
	private int edgeType;
	private String edgeLabel;

	//constructor sets nodes, type, and label of the edge
	GraphEdge(GraphNode u, GraphNode v, int type, String label) {
		this.nodeU = u;
		this.nodeV = v;
		this.edgeType = type;
		this.edgeLabel = label;
	}

	//gets the first endpoint of this node
	public GraphNode firstEndpoint() {
		return this.nodeU;
	}

	//gets the second endpoint of this node
	public GraphNode secondEndpoint() {
		return this.nodeV;
	}

	//gets the type of this node
	public int getType() {
		return this.edgeType;
	}
	
	//sets the type of this node
	public void setType(int newType) {
		this.edgeType = newType;
	}

	//gets the label of this node
	public String getLabel() {
		return this.edgeLabel;
	}

	//sets the label of this node
	public void setLabel(String newLabel) {
		this.edgeLabel = newLabel;
	}

}

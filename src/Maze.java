import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Lukas Bozinov 
 * Description: A class that is used to create an object Graph, using the previously created objects GraphEdge and GraphNode. 
 * 				This class also implements the methods outlined in GraphADT.
 */

public class Maze {

	// initialize fields
	private Graph graph;
	private int coins;
	private GraphNode start, end;
	private ArrayList<GraphNode> pathway = new ArrayList<GraphNode>();

	// this constructor method builds the graph given from the inputfile
	Maze(String inputFile) throws MazeException {

		FileInputStream inFile = null; // initialize the input file

		// make a try-catch to catch any filenotfoundexceptions
		try {
			inFile = new FileInputStream(inputFile);
		} catch (FileNotFoundException e) {
			throw new MazeException("ERROR: Input file does not exist!");
		}

		// create a bufferedreader with the correct inputfile
		BufferedReader file = new BufferedReader(new InputStreamReader(inFile));

		// another try-catch
		try {

			file.readLine(); // skip S in the input file

			// initialize two strings, hLine and vLine, for use in the constructor to read
			// in lines vertically and horizontally
			String hLine, vLine;
			int width = Integer.parseInt(file.readLine()); // read in width of the grap
			int length = Integer.parseInt(file.readLine()); // read in length of the graph
			coins = Integer.parseInt(file.readLine()); // read in coins required for the maze

			int hNodes = 0, vCons = 0; // initialize the number of horizontal and vertical nodes/connections for the maze

			graph = new Graph(length * width); // create a new graph with the specified size (area=l*w)

			// while the inputfile still has more lines to read
			while ((hLine = file.readLine()) != null) {

				// create a for loop to read in every 2nd character (every "room") in the graph
				for (int i = 0; i < hLine.length(); i += 2) {

					// if the character is one of these three characters, then
					if (hLine.charAt(i) == 's' || hLine.charAt(i) == 'o' || hLine.charAt(i) == 'x') {

						// if the character is 's', set the start field using the current number of
						// hNodes traversed
						if (hLine.charAt(i) == 's') {
							this.start = graph.getNode(hNodes);
						}

						// if the character is 'x', set the end fied using the current number of hNodes
						// traversed
						if (hLine.charAt(i) == 'x') {
							this.end = graph.getNode(hNodes);
						}

						// if there is another node 2 characters ahead that is still less than the total
						// length of the line
						if (i + 2 < hLine.length()) {

							String[] retArr = coinsRequired(hLine.charAt(i + 1)); // store helper method return value in
																					// the string array retArr

							// initialize type and label for the edge
							int type;
							String label;

							// if the return value is a corridor, set tyep to 0 (it costs nothing to go
							// through a corridor) and type to corridor
							if (retArr[1].equals("corridor")) {

								type = Integer.parseInt(retArr[0]);
								label = retArr[1];

							} else if (retArr[1].equals("wall")) { // if the return value is a wall, increase the node
																	// counter and skip the rest of the for loop

								hNodes++; // increment number of hNodes
								continue;

							} else { // otherwise, the type and label can be assumed to be a door and will be set to
										// those values

								type = Integer.parseInt(retArr[0]);
								label = retArr[1];
							}

							graph.insertEdge(graph.getNode(hNodes), graph.getNode(hNodes + 1), type, label); // insert
																												// edge
						}
						hNodes++; // increment number of hNodes
					}

				}

				vLine = file.readLine(); // read in the next line of the input file (vertical line)

				// if there is no next line, break out of the loop
				if (vLine == null) {
					break;
				}

				//create another for loop doing the same general idea (every other character in the for loop)
				for (int i = 0; i < vLine.length(); i += 2) {

					//if the character isn't a wall (in which case we'd skip it)
					if (vLine.charAt(i) != 'w' || vLine.charAt(i) != 'W') {

						//call coinsRequired again and perform similar operations to the previous for loop's "door" if statement case
						String[] retArr = coinsRequired(vLine.charAt(i));
						int type = Integer.parseInt(retArr[0]);
						String label = retArr[1];

						graph.insertEdge(graph.getNode(vCons), graph.getNode(vCons + width), type, label); //insert the edge in the correct position

					}
					vCons++; //increase number of vertical node connections
				}

			}

			file.close(); //close the input file once the graph has been filled

			//catch all related exceptions that can be thrown by the constructor
		} catch (IOException e) {
			throw new MazeException("ERROR: Problem with input!");
		} catch (GraphException e) {
			throw new MazeException("ERROR: Problem inserting edge!");
		}

	}

	//this is a helper method for calculating the type and label for each GraphEdge in the Maze constructor. it returns a string array with the type and label.
	private String[] coinsRequired(char c) {
		if (c == 'c' || c == 'C') { //if the char is c, return the corridor label and the type of zero

			String[] ret = { "0", "corridor" };

			return ret;
		} else if (c == 'w' || c == 'W') { //if it's a wall, return the wall label and the type of -1 (this array return value is not used in the constructor)

			String[] ret = { "-1", "wall" };

			return ret;
		} else { //otherwise it's a door, so return the corresponding number on the door and the door label

			String[] ret = { String.valueOf(c), "door" };

			return ret;
		}
	}

	//this graph gets the current graph of the Maze (unless the graph is null, in which case an exception informing the user is thrown)
	public Graph getGraph() throws MazeException {
		if (this.graph != null) {
			return this.graph;
		} else {
			throw new MazeException("ERROR: Graph is null.");
		}
	}

	//this method solves the maze by calling a recursive helper method (starting node and number of coins are passed in)
	@SuppressWarnings("rawtypes")
	public Iterator solve() throws GraphException {
		return modDFS(start, coins);
	}

	//this recursive method solves the maze
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Iterator modDFS(GraphNode start, int numCoins) throws GraphException {

		start.mark(true); //mark the starting node as true (since it's the first to be visited)
		pathway.add(start); //add the starting node to the pathway that will be used to find the end

		//if the end has been found, return the iterator of the arrayList
		if (start.getName() == end.getName()) {
			return pathway.iterator();
		}

		Iterator<GraphEdge> currEdgeIterator = graph.incidentEdges(start); //create an iterator that will iterate through all the incident edges of the current node

		//while there are still more incident edges to search:
		while (currEdgeIterator.hasNext()) {

			int coins = numCoins; //set the number of coins
			GraphEdge currEdge = currEdgeIterator.next(); //set the current edge to be searched
			GraphNode nextNode = currEdge.secondEndpoint(); //set the next node to be searched by finding the second endpoint from the edge

			//if the next node is the node we just started with, we can set the next node to be the first endpoint instead
			if (nextNode == start) {
				nextNode = currEdge.firstEndpoint();
			}

			//if the next node in the graph has not already been visited (marked)
			if (!nextNode.isMarked()) {
				
				//get the type of the current edge; if it's greater than zero make sure that we have enough coins to go through the door
				if (currEdge.getType() > 0 && (coins - currEdge.getType() >= 0)) {
					coins -= currEdge.getType(); //subtract the coins after going through the door
					Iterator retIterator = modDFS(nextNode, coins); //recursively call the method and store the return value in an iterator (which will have the next few nodes that are a part of the path being traversed stored in it)
					if (retIterator != null) { //if the iterator has elements to iterate
						return retIterator; //return it
					}
					//otherwise if the edge is a door
				} else if (currEdge.getType() == 0) { 
					Iterator retIterator = modDFS(nextNode, coins); //recursively call the method and store the return value in an iterator (which will have the next few nodes that are a part of the path being traversed stored in it)
					if (retIterator != null) {//if the iterator has elements to iterate
						return retIterator;//return it
					}
				} else { //if there is any other case to consider, continue the loop as normal and don't perform an operations
					continue;
				}
			}

		}

		//if the method reaches this point, that means there is no path to traverse
		
		//unmark the starting node, remove it from the arrayList, and return null (no path found)
		start.mark(false);
		pathway.remove(start);

		return null;

	}

}


/**
 * Represents an edge between two destinations in a graph. Each edge has a
 * destination identifier and a weight representing the distance between the
 * destinations in miles.
 * 
 * @author ShaazSaleem
 * @since May 2024
 */
public class Edge {
	String destination;
	double weight;

	/**
	 * Constructs an edge with the given destination identifier and weight.
	 *
	 * @param destination The identifier of the destination connected by the edge.
	 * @param weight      The weight of the edge representing the distance between
	 *                    the destinations.
	 */
	public Edge(String destination, double weight) {
		this.destination = destination;
		this.weight = weight;
	}
}

import java.util.*;

/**
 * Represents a graph data structure consisting of destinations and edges
 * between them. This class provides methods to add destinations, add edges
 * between destinations, print the graph, perform Dijkstra's algorithm for
 * finding shortest paths, filter destinations based on criteria, and retrieve
 * destination information.
 * 
 * @author ShaazSaleem
 * @since May 2024
 */
public class Graph {
	private Map<String, Destination> destinations;
	public Map<String, List<Edge>> adjacencyList;

	/**
	 * Constructs a new graph with empty destinations and adjacency list.
	 */
	public Graph() {
		destinations = new HashMap<>();
		adjacencyList = new HashMap<>();
	}

	/**
	 * Adds a new destination to the graph.
	 *
	 * @param destination The destination to add.
	 */
	public void addDestination(Destination destination) {
		destinations.put(destination.id, destination);
		adjacencyList.putIfAbsent(destination.id, new ArrayList<>());
	}

	/**
	 * Adds an edge between two destinations with a given weight.
	 *
	 * @param sourceId      The identifier of the source destination.
	 * @param destinationId The identifier of the destination destination.
	 * @param weight        The weight of the edge representing the distance in
	 *                      miles between the destinations.
	 */
	public void addEdge(String sourceId, String destinationId, double weight) {
		adjacencyList.putIfAbsent(sourceId, new ArrayList<>());
		adjacencyList.putIfAbsent(destinationId, new ArrayList<>());
		adjacencyList.get(sourceId).add(new Edge(destinationId, weight));
		adjacencyList.get(destinationId).add(new Edge(sourceId, weight));
	}

	/**
	 * Prints the graph showing connections between destinations and their
	 * distances.
	 */
	public void printGraph() {
		for (String vertex : adjacencyList.keySet()) {
			List<Edge> edges = adjacencyList.get(vertex);
			System.out.print(vertex + " (" + destinations.get(vertex).name + ") is connected to: ");
			for (Edge e : edges) {
				System.out.print(e.destination + " (" + destinations.get(e.destination).name + ") [distance: "
						+ e.weight + " miles] ");
			}
			System.out.println();
		}
	}

	/**
	 * Performs Dijkstra's algorithm to find the shortest paths from a start
	 * destination.
	 *
	 * @param startId The identifier of the start destination.
	 * @return A map of destination identifiers to their distances from the start
	 *         destination.
	 */
	public Map<String, Double> dijkstra(String startId) {
		PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingDouble(e -> e.weight));
		Map<String, Double> distances = new HashMap<>();
		Map<String, String> previous = new HashMap<>();

		for (String vertex : adjacencyList.keySet()) {
			distances.put(vertex, Double.MAX_VALUE);
		}
		distances.put(startId, 0.0);

		pq.add(new Edge(startId, 0.0));

		while (!pq.isEmpty()) {
			Edge current = pq.poll();
			String currentVertex = current.destination;
			double currentDistance = current.weight;

			if (currentDistance > distances.get(currentVertex))
				continue;

			for (Edge neighbor : adjacencyList.get(currentVertex)) {
				double distance = distances.get(currentVertex) + neighbor.weight;

				if (distance < distances.get(neighbor.destination)) {
					distances.put(neighbor.destination, distance);
					previous.put(neighbor.destination, currentVertex);
					pq.add(new Edge(neighbor.destination, distance));
				}
			}
		}

		return distances;
	}

	/**
	 * Filters destinations based on a given criteria.
	 *
	 * @param criteria The criteria used for filtering destinations.
	 * @return A set of destinations that match the criteria.
	 */
	public Set<Destination> filterDestinations(String criteria) {
		Set<Destination> filtered = new HashSet<>();
		for (Destination destination : destinations.values()) {
			if (destination.categories.contains(criteria)) {
				filtered.add(destination);
			}
		}
		return filtered;
	}

	/**
	 * Retrieves the name of a destination given its identifier.
	 *
	 * @param destinationId The identifier of the destination.
	 * @return The name of the destination if found, otherwise "Unknown".
	 */
	public String getDestinationName(String destinationId) {
		Destination destination = destinations.get(destinationId);
		return destination != null ? destination.name : "Unknown";
	}

	/**
	 * Prints information about all destinations in the graph.
	 */
	public void printDestinationsInfo() {
		System.out.println("\nDestinations Information:");
		for (Destination destination : destinations.values()) {
			System.out.println("Destination ID: " + destination.id);
			System.out.println("Name: " + destination.name);
			System.out.println("Description: " + destination.description);
			System.out.println("Categories: " + destination.categories);
			System.out.println();
		}
	}
}

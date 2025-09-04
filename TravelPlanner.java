import java.util.*;

/**
 * Represents a travel planner application that interacts with a graph of
 * destinations. Users can perform various actions such as printing destination
 * information, printing the graph, filtering destinations based on criteria,
 * finding the shortest path between destinations, and planning a trip by
 * selecting multiple destinations.
 * 
 * This class provides a menu-driven interface for users to interact with an
 * implementation of a travel planner application.
 * 
 * @author ShaazSaleem
 * @since May 2024
 */
public class TravelPlanner {
	private Graph graph;
	private Scanner input;

	/**
	 * Constructs a travel planner with the specified graph.
	 *
	 * @param graph The graph of destinations to use for planning trips.
	 */
	public TravelPlanner(Graph graph) {
		this.graph = graph;
		input = new Scanner(System.in);
	}

	/**
	 * Starts the travel planner application. Displays a menu for users to choose
	 * from different actions until they choose to exit.
	 */
	public void start() {
		int choice = 0;
		while (choice != 6) {
			displayMenu();
			choice = input.nextInt();
			input.nextLine();

			switch (choice) {
			case 1:
				printDestinationInfo();
				break;
			case 2:
				printGraph();
				break;
			case 3:
				filterDestinations();
				break;
			case 4:
				findShortestPath();
				break;
			case 5:
				planTrip();
				break;
			case 6:
				System.out.println("Exiting...");
				input.close();
				break;
			default:
				System.out.println("Invalid option. Please try again.");
			}
		}
	}

	/**
	 * Displays the menu of options available to the user.
	 */
	private void displayMenu() {
		System.out.println("\nTravel Planner Menu:");
		System.out.println("1. Print Destinations Info");
		System.out.println("2. Print Graph");
		System.out.println("3. Filter Destinations");
		System.out.println("4. Find Shortest Path");
		System.out.println("5. Plan Trip");
		System.out.println("6. Exit");
		System.out.print("Choose an option: ");
	}

	/**
	 * Prints the graph of destinations showing connections and distances.
	 */
	private void printGraph() {
		System.out.println("\nGraph of Destinations:");
		graph.printGraph();
	}

	/**
	 * Filters destinations based on user-specified criteria.
	 */
	private void filterDestinations() {
		System.out.print("Enter filter criteria (e.g., historical, coastal): ");
		String criteria = input.nextLine();

		Set<Destination> filtered = graph.filterDestinations(criteria);
		System.out.println("Filtered destinations: " + filtered);
	}

	/**
	 * Finds the shortest path between two destinations based on user input.
	 */
	private void findShortestPath() {
		System.out.print("Enter starting destination ID: ");
		String startId = input.nextLine();
		System.out.print("Enter destination ID: ");
		String endId = input.nextLine();

		Map<String, Double> distances = graph.dijkstra(startId);
		if (!distances.containsKey(endId)) {
			System.out.println("No path found between " + startId + " and " + endId);
		} else {
			double shortestDistance = distances.get(endId);
			List<String> path = new ArrayList<>();
			String current = endId;
			while (!current.equals(startId)) {
				path.add(current);
				double minDistance = Double.MAX_VALUE;
				String nextNode = null;
				for (Edge edge : graph.adjacencyList.get(current)) {
					if (distances.get(edge.destination) + edge.weight == distances.get(current)
							&& distances.get(edge.destination) < minDistance) {
						minDistance = distances.get(edge.destination);
						nextNode = edge.destination;
					}
				}
				current = nextNode;
			}
			path.add(startId);
			Collections.reverse(path);

			System.out.println("Shortest path from " + startId + " to " + endId + ":");
			System.out.println("Distance: " + shortestDistance + " mi");
			System.out.println("Path: " + path);
		}
	}

	/**
	 * Plans a trip by allowing the user to select multiple destinations.
	 */
	private void planTrip() {
		System.out.println("Plan Your Trip:");

		System.out.print("Enter the number of destinations you want to visit: ");
		int numDestinations = input.nextInt();
		input.nextLine(); // Consume newline

		List<String> destinations = new ArrayList<>();
		for (int i = 0; i < numDestinations; i++) {
			System.out.print("Enter destination " + (i + 1) + " ID: ");
			String destinationId = input.nextLine();
			destinations.add(destinationId);
		}

		System.out.println("Your Trip Plan:");
		for (int i = 0; i < destinations.size() - 1; i++) {
			String source = destinations.get(i);
			String destination = destinations.get(i + 1);
			Map<String, Double> distances = graph.dijkstra(source);
			if (!distances.containsKey(destination)) {
				System.out.println("No path found between " + source + " and " + destination);
				return;
			}
			double distance = distances.get(destination);
			List<String> path = reconstructPath(graph, source, destination);

			System.out.println("From " + graph.getDestinationName(source) + " to "
					+ graph.getDestinationName(destination) + " (Distance: " + distance + " mi)");
			System.out.println("Path:");
			for (String vertex : path) {
				System.out.println("- " + graph.getDestinationName(vertex));
			}
		}
	}

	/**
	 * Reconstructs the path between two destinations. Helper method for the
	 * planTrip method
	 *
	 * @param graph   The graph containing destination information and connections.
	 * @param startId The ID of the starting destination.
	 * @param endId   The ID of the ending destination.
	 * @return The list of destination IDs representing the path from start to end.
	 */
	private List<String> reconstructPath(Graph graph, String startId, String endId) {
		List<String> path = new ArrayList<>();
		String current = endId;
		while (!current.equals(startId)) {
			path.add(current);
			for (Edge edge : graph.adjacencyList.get(current)) {
				if (graph.dijkstra(startId).get(edge.destination) + edge.weight == graph.dijkstra(startId)
						.get(current)) {
					current = edge.destination;
					break;
				}
			}
		}
		path.add(startId);
		Collections.reverse(path);
		return path;
	}

	/**
	 * Prints information about all destinations in the graph.
	 */
	private void printDestinationInfo() {
		graph.printDestinationsInfo();
	}

	/**
	 * Main method to start the travel planner application. Initializes the graph
	 * with destinations and starts the travel planner. Main entry point for the
	 * program.
	 *
	 * @param args Command-line arguments.
	 */
	public static void main(String[] args) {
		Graph graph = new Graph();

		Destination sanFrancisco = new Destination("SF", "San Francisco", "The City by the Bay",
				new HashSet<>(Arrays.asList("urban", "coastal", "affordable")));
		Destination newDelhi = new Destination("ND", "New Delhi", "The Capital of India",
				new HashSet<>(Arrays.asList("historical", "urban")));
		Destination paris = new Destination("PAR", "Paris", "The City of Love",
				new HashSet<>(Arrays.asList("romantic", "cultural", "historical")));
		Destination newYork = new Destination("NY", "New York", "The Big Apple",
				new HashSet<>(Arrays.asList("historical", "urban", "coastal")));
		Destination losAngeles = new Destination("LA", "Los Angeles", "Tinseltown",
				new HashSet<>(Arrays.asList("urban", "coastal")));
		Destination venice = new Destination("VN", "Venice", "The Floating City",
				new HashSet<>(Arrays.asList("romantic", "coastal", "historical")));
		Destination rome = new Destination("RM", "Rome", "The Eternal City",
				new HashSet<>(Arrays.asList("romantic", "cultural", "historical", "affordable")));
		Destination london = new Destination("LN", "London", "The Big Smoke",
				new HashSet<>(Arrays.asList("historical", "affordable", "urban")));

		graph.addDestination(sanFrancisco);
		graph.addDestination(newDelhi);
		graph.addDestination(paris);
		graph.addDestination(newYork);
		graph.addDestination(losAngeles);
		graph.addDestination(venice);
		graph.addDestination(rome);
		graph.addDestination(london);
		
		graph.addEdge("NY", "ND", 7300);
		graph.addEdge("NY", "PAR", 5500);
		graph.addEdge("ND", "PAR", 7000);
		graph.addEdge("NY", "SF", 2500);
		graph.addEdge("LA", "SF", 380);
		graph.addEdge("VN", "PAR", 524);
		graph.addEdge("VN", "ND", 3656);
		graph.addEdge("RM", "VN", 245);
		graph.addEdge("RM", "PAR", 688);
		graph.addEdge("LN", "NY", 3461);
		graph.addEdge("LN", "PAR", 213);
		graph.addEdge("RM", "LN", 910);
		graph.addEdge("LA", "NY", 2445);

		TravelPlanner travelPlanner = new TravelPlanner(graph);
		travelPlanner.start();
	}
}
import java.util.Set;

/**
 * Represents a destination in the graph with a unique identifier, name,
 * description, and categories. Each destination can be identified by its ID and
 * has a name and description. Additionally, it belongs to one or more
 * categories, which classify the type or nature of the destination.
 * 
 * @author ShaazSaleem
 * @since May 2024
 */
public class Destination {
	String id;
	String name;
	String description;
	Set<String> categories;

	/**
	 * Constructs a destination with the given attributes.
	 *
	 * @param id          The unique identifier of the destination.
	 * @param name        The name of the destination.
	 * @param description A brief description of the destination.
	 * @param categories  The set of categories to which the destination belongs.
	 */
	public Destination(String id, String name, String description, Set<String> categories) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.categories = categories;
	}

	/**
	 * Returns a string representation of the destination.
	 *
	 * @return The name and ID of the destination.
	 */
	@Override
	public String toString() {
		return name + " (" + id + ")";
	}
}

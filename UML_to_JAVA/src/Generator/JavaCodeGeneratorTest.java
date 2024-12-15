package Generator;
import java.util.*;

public class JavaCodeGeneratorTest {
    public static void main(String[] args) {
        // Define the test case syntax tree
        Map<String, Map<String, Object>> syntaxTree = new HashMap<>();

        // Define the Book class
        Map<String, Object> bookClass = new HashMap<>();
        bookClass.put("type", "class");
        bookClass.put("name", "Book");

        // Define Book properties
        Map<String, Map<String, String>> bookProperties = new HashMap<>();
        bookProperties.put("1", Map.of("access", "private", "name", "title", "type", "String"));
        bookProperties.put("2", Map.of("access", "private", "name", "author", "type", "String"));
        bookProperties.put("3", Map.of("access", "private", "name", "price", "type", "double"));
        bookClass.put("properties", bookProperties);

        // Define Book methods
        Map<String, Map<String, String>> bookMethods = new HashMap<>();
        bookMethods.put("1", Map.of("access", "public", "name", "Book(String title)", "return_type", "void"));
        bookMethods.put("2", Map.of("access", "public", "name", "getTitle", "return_type", "String"));
        bookMethods.put("3", Map.of("access", "public", "name", "getPrice", "return_type", "double"));
        bookMethods.put("4", Map.of("access", "public", "name", "setPrice(double price)", "return_type", "void"));
        bookClass.put("methods", bookMethods);

        // Define Book relationships
        Map<String, List<String>> bookRelationships = new HashMap<>();
        bookRelationships.put("implements", new ArrayList<>());
        bookRelationships.put("extends", new ArrayList<>());
        bookRelationships.put("association", List.of("test456")); // Associated with Publisher
        bookClass.put("relationships", bookRelationships);

        syntaxTree.put("test123", bookClass);

        // Define the Publisher class
        Map<String, Object> publisherClass = new HashMap<>();
        publisherClass.put("type", "class");
        publisherClass.put("name", "Publisher");

        // Define Publisher properties
        Map<String, Map<String, String>> publisherProperties = new HashMap<>();
        publisherProperties.put("1", Map.of("access", "private", "name", "name", "type", "String"));
        publisherProperties.put("2", Map.of("access", "private", "name", "location", "type", "String"));
        publisherClass.put("properties", publisherProperties);

        // Define Publisher methods
        Map<String, Map<String, String>> publisherMethods = new HashMap<>();
        publisherMethods.put("1", Map.of("access", "public", "name", "getName", "return_type", "String"));
        publisherMethods.put("2", Map.of("access", "public", "name", "getLocation", "return_type", "String"));
        publisherClass.put("methods", publisherMethods);

        // Define Publisher relationships
        Map<String, List<String>> publisherRelationships = new HashMap<>();
        publisherRelationships.put("implements", new ArrayList<>());
        publisherRelationships.put("extends", new ArrayList<>());
        publisherRelationships.put("association", new ArrayList<>());
        publisherClass.put("relationships", publisherRelationships);

        syntaxTree.put("test456", publisherClass);

        // Initialize the JavaCodeGenerator
        JavaCodeGenerator generator = new JavaCodeGenerator(syntaxTree);

        // Generate the description
        generator.generateCode();
        String structure = generator.generateClassStructure();
        System.out.println(structure);
        String description = generator.generateDescription();
        System.out.println(description);
    }
}

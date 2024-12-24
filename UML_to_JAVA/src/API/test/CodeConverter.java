package API.test;
public class CodeConverter {

    public static String convertCode(String input) {
        // Split input into lines
        String[] lines = input.split("\\r?\\n");
        StringBuilder output = new StringBuilder();
        String currentClass = "";

        for (String line : lines) {
            // Remove all whitespace from the beginning and end
            line = line.trim();

            // Handle class declarations
            if (line.startsWith("public class") || line.startsWith("abstract class") || line.startsWith("interface")) {
                currentClass = line.split(" ")[2];
                output.append(line.replace("{", "").trim()).append("\n");
            }
            // Handle constructors
            else if (line.contains(currentClass + "(")) {
                String constructor = line.substring(0, line.indexOf("{")).trim();
                output.append("   ").append(constructor).append(":\n");
            }
            // Handle methods
            else if (line.contains("(") && line.endsWith(")") && !line.startsWith("import") && !line.contains("class")) {
                String method = line.substring(0, line.indexOf("{")).trim();
                output.append("   ").append(method).append(":\n");
            }
        }

        return output.toString();
    }

    public static void main(String[] args) {
        // Example input
        String input = """
            public class DigitalVideoDisc extends Disc implements Playable {

                public DigitalVideoDisc(String title) {}

                public DigitalVideoDisc(String title, String category, float cost) {}

                public DigitalVideoDisc(String title, String category, String director, float cost) {}

                public DigitalVideoDisc(String title, String category, String director, int length, float cost) {}

                public String toString() {}

                public void play() {}

                public void play() {
                    // **requires implementation**
                }

            }
            abstract class Media {
                Media(String title) {}

                Media(String title, String category, float cost) {}

                public int getId() {}

                public String getTitle() {}

                public String getCategory() {}

                public float getCost() {}

                public boolean equals(Media other) {}
            }
            """;

        // Convert the input and print the output
        String output = convertCode(input);
        System.out.println(output);
    }
}
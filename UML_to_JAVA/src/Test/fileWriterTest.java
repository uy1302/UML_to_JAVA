package Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class fileWriterTest {
    public static void main(String[] args) {
        try {
            // Step 1: Create a file object
            File myFile = new File("D:\\OneDrive - Hanoi University of Science and Technology\\Domain Basics\\Object Oriented Programming\\UML_to_JAVA\\UML_to_JAVA\\src\\example\\example.txt");

            // Step 2: Create a new file
            if (myFile.createNewFile()) {
                System.out.println("File created: " + myFile.getName());
            } else {
                System.out.println("File already exists.");
            }

            // Step 3: Write content to the file
            FileWriter writer = new FileWriter("example.txt");
            writer.write("Hello, this is a new file created using Java!\n");
            writer.write("You can write multiple lines of text here.");
            writer.close();

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
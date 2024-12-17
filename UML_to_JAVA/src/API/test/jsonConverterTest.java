package API.test;

import java.util.HashMap;
import java.util.Map;

public class jsonConverterTest {
    public static void main(String[] args) {
        String responseBody = "Response Body: {\"all_data\":[{\"Vehicle\":\"public class Vehicle {\\n\\tprivate String make;\\n\\tprivate String model;\\n\\tprivate int year;\\n\\tprivate double speed;\\n\\n\\tvoid start(){\\n\\t\\tSystem.out.println(\\\"Engine started.\\\");\\n\\t}\\n\\tvoid stop(){\\n\\t\\tspeed = 0;\\n\\t\\tSystem.out.println(\\\"Engine stopped.\\\");\\n\\t}\\n\\tvoid accelerate(double increment){\\n\\t\\tspeed += increment;\\n\\t}\\n\\tvoid brake(double decrement){\\n\\t\\tspeed -= decrement;\\n\\t\\tif(speed < 0) speed = 0;\\n\\t}\\n}\\n\"},{\"Car\":\"public class Car {\\n\\tprivate String entertainmentSystem;\\n\\tprivate int seatingCapacity;\\n\\n\\tvoid playMusic(String songName){\\n\\t\\tSystem.out.println(\\\"Playing \\\" + songName + \\\" on \\\" + entertainmentSystem);\\n\\t}\\n\\tvoid enableCruiseControl(double speed){\\n\\t\\tSystem.out.println(\\\"Cruise control enabled at \\\" + speed + \\\" km/h\\\");\\n\\t}\\n}\\n\"}]}";

        // Step 1: Remove "Response Body: " prefix
        String jsonString = responseBody.replace("Response Body: ", "").trim();

        // Step 2: Extract the "all_data" array
        int startIndex = jsonString.indexOf("[");
        int endIndex = jsonString.lastIndexOf("]");
        String allDataContent = jsonString.substring(startIndex + 1, endIndex);

        // Step 3: Process entries manually (preserve code brackets and inner quotes)
        String[] entries = allDataContent.split("\\},\\{");
        Map<String, String> dataMap = new HashMap<>();

        for (String entry : entries) {
            // Step 4: Clean up the entry to isolate key-value pairs
            entry = entry.trim();

            if (entry.startsWith("{")) entry = entry.substring(1); // Remove leading '{' if present
            if (entry.endsWith("}")) entry = entry.substring(0, entry.length() - 1); // Remove trailing '}' if present

            // Step 5: Split key and value
            int colonIndex = entry.indexOf("\":\"");
            if (colonIndex != -1) {
                String key = entry.substring(0, colonIndex).replace("\"", "").trim();
                String value = entry.substring(colonIndex + 3, entry.length() - 1).trim(); // Remove ending quote

                // Replace only escaped JSON characters (but keep quotes inside Java code intact)
                value = value.replace("\\n", "\n")
                             .replace("\\t", "\t")
                             .replace("\\\"", "\""); // Replace \" with " only for JSON escaping

                dataMap.put(key, value);
            }
        }

        // Step 6: Print the map
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            System.out.println("Key: " + entry.getKey());
            System.out.println("Value: " + entry.getValue());
            System.out.println();
        }
    }
}
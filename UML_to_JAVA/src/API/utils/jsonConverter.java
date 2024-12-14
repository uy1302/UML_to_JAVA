package API.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class jsonConverter {
	public static String readFiletoJson(String filename){
		String descriptionString = readFileAsString(filename);
		return StringtoJson(descriptionString);
	}
	
	public static String StringtoJson(String inputString) {
		String[] lines = inputString.split("\n");
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("{");

		String currentClass = null;
		boolean isFirstClass = true;

		for (String line : lines) {
			line = line.trim();

			if (line.startsWith("public class")) {
				if (currentClass != null) {
					jsonBuilder.append("},");
				}

				currentClass = line;
				if (!isFirstClass) {
					jsonBuilder.append("\n");
				}
				isFirstClass = false;

				jsonBuilder.append("\"").append(currentClass).append("\": {");
			} else if (!line.isEmpty()) {
				String[] parts = line.split(":");
				if (parts.length == 2) {
					String methodName = parts[0].trim();
					String description = parts[1].trim();

					jsonBuilder.append("\n    \"")
							.append(methodName).append("\": \"")
							.append(description).append("\",");
				}
			}
		}

		int lastCommaIndex = jsonBuilder.lastIndexOf(",");
		if (lastCommaIndex != -1) {
			jsonBuilder.deleteCharAt(lastCommaIndex);
		}

		jsonBuilder.append("\n  }\n}");
		String jsonString = jsonBuilder.toString();
		jsonString = jsonString.replaceAll(",(\\s*})", "$1"); 
    	jsonString = jsonString.replaceAll(",(\\s*])", "$1"); 
		return jsonString;
	}

	public static List<String> parse(String jsonString) throws StringIndexOutOfBoundsException{
		if (jsonString.length()!=0) {
			jsonString = jsonString.substring(jsonString.indexOf("[") + 1, jsonString.lastIndexOf("]"));

	        String[] entries = jsonString.split("\\},\\{");

	        List<String> contents = new ArrayList<>();
	        for (String entry : entries) {
	            int startIndex = entry.indexOf("\"Content\":\"") + 11;
	            int endIndex = entry.lastIndexOf("\"");
	            String content = entry.substring(startIndex, endIndex).replace("\\n", "\n").replace("\\t", "\t").replace("\\\"", "\"");
	            contents.add(content);
	        }

	        return contents;
		}else {
			throw new StringIndexOutOfBoundsException("ERROR! Input string is wrong!");
		}
    }

	public static String readFileAsString(String filePath) {
        try {
            // Read all lines and join them with newline characters
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            return String.join("\n", lines);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

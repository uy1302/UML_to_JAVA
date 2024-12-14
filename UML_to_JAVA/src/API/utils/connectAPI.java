package API.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.descriptionException;

public class connectAPI {
	private static String apiUrl = "http://127.0.0.1:8000";
	
	public static int postAPI(String descriptions, String classes) {
		String jsonDescriptions = "{\"descriptions\" :" + descriptions + "," +
								"\"classes\" :" + classes + "}";
		// System.out.println(jsonDescriptions);
		try {
			URL postUrl = new URL(apiUrl+"/send_descriptions");

			HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();


			connection.setRequestMethod("POST");

			connection.setDoOutput(true);
			connection.setDoInput(true);

			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			connection.setRequestProperty("Accept", "application/json");

			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = jsonDescriptions.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			int responseCode = connection.getResponseCode();
			return responseCode;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 500;
	}
	
	public static void runPython(String filename) {
		try {
            ProcessBuilder pb = new ProcessBuilder("python", filename);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
//                System.out.println(line); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public static Map<String, String> getCode() {
		String getUrl = apiUrl + "/get_code";
        
        try {
            URL url = new URL(getUrl);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
//            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { 
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
//                    System.out.println(inputLine);
                }
                in.close();
                
                String jsonString = response.toString().replace("Response Body: ", "").trim();

                // Step 2: Extract the "all_data" array
                int startIndex = jsonString.indexOf("[");
                int endIndex = jsonString.lastIndexOf("]");
                String allDataContent = jsonString.substring(startIndex + 1, endIndex);

                // Step 3: Process entries manually (preserve code brackets)
                String[] entries = allDataContent.split("\\},\\{");
                Map<String, String> javaCode = new HashMap<>();

                for (String entry : entries) {
                    // Step 4: Clean up the entry to isolate key-value pairs
                    entry = entry.trim();

                    if (entry.startsWith("{")) entry = entry.substring(1); // Remove leading '{' if present
                    if (entry.endsWith("}")) entry = entry.substring(0, entry.length() - 1); // Remove trailing '}' if present

                    // Step 5: Split key and value
                    int colonIndex = entry.indexOf("\":\"");
                    if (colonIndex != -1) {
                        String key = entry.substring(0, colonIndex).replace("\"", "").trim();
                        String value = entry.substring(colonIndex + 3).replace("\"", "").trim();

                        // Replace escaped characters in the value
                        value = value.replace("\\n", "\n")
                                     .replace("\\t", "\t")
                                     .replace("\\\"", "\"");

                        javaCode.put(key, value);
                    }
                }
                
                return javaCode;
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public static void clearCode() {
		String getUrl = apiUrl + "/clear_data";
        
        try {
            URL url = new URL(getUrl);
            
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}

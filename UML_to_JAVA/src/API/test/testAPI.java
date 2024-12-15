package API.test;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import API.utils.connectAPI;
import API.utils.jsonConverter;
import exceptions.descriptionException;

import java.io.*;

public class testAPI {
	 public static void main(String[] args) {
			String apiUrl = "http://127.0.0.1:8000";
			 String descriptions = "{\"public class Employer extends Person\": {\r\n"
			 		+ "    \"hireEmployee\": \"hire a new employee, and make them work\",\r\n"
			 		+ "    \"getEmployerDetails\": \"show all information\",\r\n"
			 		+ "    \"Employer\": \"constructor\"},\r\n"
			 		+ "\"public class Employee extends Person\": {\r\n"
			 		+ "    \"work\": \"do a specific task\",\r\n"
			 		+ "    \"getEmployeeDetails\": \"show all information\"},\r\n"
			 		+ "\"public class Person\": {\r\n"
			 		+ "    \"getDetails\": \"show all information\"\r\n"
			 		+ "  }\r\n"
			 		+ "}";
//			String descriptions = jsonConverter.readFiletoJson("test.txt");
			String classes = "{    \"public class Employer extends Person\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"company\": \"public String company\",\r\n"
					+ "            \"position\": \"public String position\"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"hireEmployee\": \"public void hireEmployee(Employee employee)\",\r\n"
					+ "            \"getEmployerDetails\": \"public String getEmployerDetails()\",\r\n"
					+ "            \"Employer\": \"public void Employer(String company, String position)\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"public class Employee extends Person\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"employeeId\": \"public int employeeId\",\r\n"
					+ "            \"salary\": \"public float salary\",\r\n"
					+ "            \"department\": \"public String department\"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"work\": \"public void work()\",\r\n"
					+ "            \"getEmployeeDetails\": \"public String getEmployeeDetails()\"\r\n"
					+ "        }\r\n"
					+ "    },\r\n"
					+ "    \"public class Person\": {\r\n"
					+ "        \"attributes\": {\r\n"
					+ "            \"name\": \"public String name\",\r\n"
					+ "            \"id\": \"public int id\",\r\n"
					+ "            \"address\": \"public String address\"\r\n"
					+ "        },\r\n"
					+ "        \"methods\": {\r\n"
					+ "            \"getDetails\": \"public String getDetails()\"\r\n"
					+ "        }\r\n"
					+ "    }\r\n"
					+ "}";
			System.out.println(descriptions);
			System.out.println(classes);
			int postResponseCode = connectAPI.postAPI(descriptions, classes);
			System.out.println("Response Code: " + postResponseCode);
			if (postResponseCode == HttpURLConnection.HTTP_OK) {
				connectAPI.runPython("test.py");
				Map<String, String> javaCode = connectAPI.getCode();
				for (Map.Entry<String, String> entry : javaCode.entrySet()) {
                    System.out.println("Key: " + entry.getKey());
                    System.out.println("Value: " + entry.getValue().replace("\\t", "\t"));
                    System.out.println();
                }
			}else {
				System.out.println("ERROR! Wrong format description!");
			}
	}
}


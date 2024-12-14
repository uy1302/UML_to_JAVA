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
//			 String descriptions = "{\"public class Vehicle\" :" +
//			 						"{\"start\":\"The Vehicle initializes its engine and prepares all systems for operation.\"," +
//			 						"\"stop\": \"The Vehicle powers down its engine and deactivates non-essential systems.\","+
//			 						"\"accelerate\": \"The Vehicle increases its speed by applying more power to its drivetrain.\","+
//			 						"\"brake\": \"The Vehicle slows down or comes to a stop by applying its braking mechanism.\"}," +
//			 					"\"public class Car\" :"+
//			 						"{\"playMusic\": \"The Car's entertainment system plays music for the passengers.\","+
//			 						"\"enableCruiseControl\": \"The Car maintains a steady speed without driver input through its cruise control system.\"},"+
//			 					"\"public class ElectricCar\" :"+
//			 					" {\"chargeBattery\": \"The ElectricCar connects to a charging station to replenish its battery.\","+
//			 					" \"regeneratePower\": \"The ElectricCar converts kinetic energy into electrical energy during braking to recharge the battery.\"}}";
			String descriptions = jsonConverter.readFiletoJson("test.txt");
//			descriptions = "";
			String classes =  "{" + //
			"    \"public class Vehicle\": {" + //
			"        \"attributes\": {" + //
			"            \"make\": \"private String make\"," + //
			"            \"model\": \"private String model\"," + //
			"            \"year\": \"private int year\"," + //
			"            \"speed\": \"private double speed\"" + //
			"        }," + //
			"        \"methods\": {" + //
			"            \"start\": \"void start()\"," + //
			"            \"stop\": \"void stop()\"," + //
			"            \"accelerate\": \"void accelerate(double increment)\"," + //
			"            \"brake\": \"void brake(double decrement)\"" + //
			"        }" + //
			"    }," + //
			"    \"public class Car\": {" + //
			"        \"attributes\": {" + //
			"            \"entertainmentSystem\": \"private String entertainmentSystem\"," + //
			"            \"seatingCapacity\": \"private int seatingCapacity\"" + //
			"        }," + //
			"        \"methods\": {" + //
			"            \"playMusic\": \"void playMusic(String songName)\"," + //
			"            \"enableCruiseControl\": \"void enableCruiseControl(double speed)\"" + //
			"        }" + //
			"    }," + //
			"    \"public class ElectricCar\": {" + //
			"        \"attributes\": {" + //
			"            \"batteryCapacity\": \"private double batteryCapacity\"," + //
			"            \"currentCharge\": \"private double currentCharge\"," + //
			"            \"chargingPortType\": \"private String chargingPortType\"" + //
			"        }," + //
			"        \"methods\": {" + //
			"            \"chargeBattery\": \"void chargeBattery(double hours)\"," + //
			"            \"regeneratePower\": \"void regeneratePower(double energy)\"" + //
			"        }" + //
			"    }" + //
			"}";
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

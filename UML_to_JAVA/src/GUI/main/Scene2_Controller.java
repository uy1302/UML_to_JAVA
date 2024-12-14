package GUI.main;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Map;

import API.utils.connectAPI;
import API.utils.jsonConverter;
import Decode.DecodeAndCompress;
import Generator.JavaCodeGenerator;
import Parser.StyleParser;
import Parser.SyntaxParser;
import exceptions.descriptionException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Scene2_Controller {
	
	@FXML
	private Button browse = new Button();
	
	@FXML
	private AnchorPane scenePane;
	
	@FXML
    private Button btnGenCode;
	
	@FXML
    private TextArea descriptionText;
	
	@FXML
    private Button descriptionBrowse;
	
	private Stage stage;
	private Scene scene;
	private String classes = "{" + //
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
	
	private String apiUrl = "http://127.0.0.1:8000";
	
	@FXML
	public void browse_file(ActionEvent event) throws Exception{
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+ "/Desktop"));
        Stage stage = (Stage) browse.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selectedFile);
        String drawioFilepath = selectedFile.getAbsolutePath();
        System.out.println(drawioFilepath);
        String decoded_xml = DecodeAndCompress.convert(drawioFilepath);
		StyleParser parser_style = new StyleParser(decoded_xml);
		Map<String, Object> style_tree = parser_style.convertToStyleTree();
		
		SyntaxParser parser_syntax = new SyntaxParser(style_tree);
		Map<String, Map<String, Object>> syntax_tree = parser_syntax.convertToSyntaxTree();

		JavaCodeGenerator java_gen = new JavaCodeGenerator(syntax_tree);
		java_gen.generateCode();
	}
	
	@FXML
    public void readDescriptionFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

        // Set an initial directory (e.g., Desktop)
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        // Set a file extension filter to show only .txt files
        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);

        // Open the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Check if the user selected a file
        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            descriptionText.setText(jsonConverter.readFileAsString(selectedFile.getAbsolutePath()));
        } else {
            System.out.println("File selection canceled.");
        }
    }
	
	@FXML
	public void gen_code(ActionEvent event) throws Exception{
		String descriptionString = descriptionText.getText();
//		System.out.println(descriptionString);
		String descriptions = jsonConverter.StringtoJson(descriptionString);
//		System.out.println(descriptions);
		int postResponseCode = connectAPI.postAPI(descriptions, classes);
		if (postResponseCode == HttpURLConnection.HTTP_OK) {
			connectAPI.runPython("test.py");
			Map<String, String> javaCode = connectAPI.getCode();
			connectAPI.clearCode();
//			Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene3.fxml"));
//			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//			scene = new Scene(root);
//			stage.setScene(scene);
//			stage.show();
			new Scene3(javaCode);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.close();
		}else {
	        
	        Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText("Error: Wrong description format!");
	        if (alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) scenePane.getScene().getWindow();
			}
		}
		
	}
	
	@FXML
	public void exit(ActionEvent event) throws IOException{
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Exit");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure to exit?");
		
		if (alert.showAndWait().get() == ButtonType.OK) {
			stage = (Stage) scenePane.getScene().getWindow();
			System.out.println("Exit successfully");
			stage.close();
		}
	}
	
	@FXML
	public void logout(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene1.fxml"));
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logging out");
		alert.setHeaderText(null);
		alert.setContentText("Do you want to log out?");
		
		if (alert.showAndWait().get() == ButtonType.OK) {
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		
	}
}

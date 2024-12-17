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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
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
    private TextArea codeText;
	
	@FXML
    private Button descriptionBrowse;
	
	@FXML
    private MenuButton btnSelectFile;
	
	private Stage stage;
	private Scene scene;
	private String classes;
	private static String descriptions;
	
	private String apiUrl = "http://127.0.0.1:8000";
	
	@FXML
	public void browse_file(ActionEvent event) throws Exception{
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+ "/Desktop"));
        Stage stage = (Stage) browse.getScene().getWindow();
        FileChooser.ExtensionFilter drawioFilter = new FileChooser.ExtensionFilter("DrawIO Files (*.drawio)", "*.drawio");
        fileChooser.getExtensionFilters().add(drawioFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
//        System.out.println(selectedFile);
        String drawioFilepath = selectedFile.getAbsolutePath();
//        System.out.println(drawioFilepath);
        String decoded_xml = DecodeAndCompress.convert(drawioFilepath);
		StyleParser parser_style = new StyleParser(decoded_xml);
		Map<String, Object> style_tree = parser_style.convertToStyleTree();
		
		SyntaxParser parser_syntax = new SyntaxParser(style_tree);
		Map<String, Map<String, Object>> syntax_tree = parser_syntax.convertToSyntaxTree();

		JavaCodeGenerator java_gen = new JavaCodeGenerator(syntax_tree);
		java_gen.generateCode();
		
		descriptions = java_gen.generateDescription();
		classes = java_gen.generateClassStructure();
//		System.out.println(classes);
//		System.out.println(descriptions);
//		descriptionText.setText(descriptions);
		Map<String, String >pureCode = java_gen.getMapOutput();
		for (Map.Entry<String, String> entry : pureCode.entrySet()) {
            MenuItem menuItem = new MenuItem(entry.getKey()+".java");
            menuItem.setOnAction(e -> codeText.setText(entry.getValue()));
            btnSelectFile.getItems().add(menuItem);
        }
	}
	
	
	@FXML
    private void openNewWindow() {
		try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/DescriptionWindow.fxml"));
            Parent root = loader.load();
            Description_Controller descriptionController = loader.getController();
//            System.out.println(descriptions);
	        descriptionController.setDescription(descriptions);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(new Scene(root, 800, 600));
            
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	@FXML
	public void gen_code(ActionEvent event) throws Exception{
//		String descriptionString = descriptionText.getText().trim();
//		String descriptionString = "public class Employer extends Person\r\n"
//				+ "    hireEmployee: hire a new employee, and make them work\r\n"
//				+ "    getEmployerDetails: show all information\r\n"
//				+ "    Employer: constructor\r\n"
//				+ "public class Employee extends Person\r\n"
//				+ "    work: do a specific task\r\n"
//				+ "    getEmployeeDetails: show all information\r\n"
//				+ "public class Person\r\n"
//				+ "    getDetails: show all information";
//		System.out.println(descriptionString);
		String descriptionJson = jsonConverter.StringtoJson(descriptions);
		System.out.println(descriptionJson);
		System.out.println(classes);
		int postResponseCode = connectAPI.postAPI(descriptionJson, classes);
		if (postResponseCode == HttpURLConnection.HTTP_OK) {
			connectAPI.runPython("test.py");
			Map<String, String> javaCode = connectAPI.getCode();
			connectAPI.clearCode();
//			Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene3.fxml"));
//			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//			scene = new Scene(root);
//			stage.setScene(scene);
//			stage.show();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/Scene3.fxml"));
	        Parent root = loader.load();

	        // Get the Scene3 controller and pass the javaCode
	        Scene3_Controller scene3Controller = loader.getController();
	        scene3Controller.setJavaCode(javaCode);

	        // Switch scenes
	        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        scene = new Scene(root);
	        stage.setScene(scene);
	        stage.show();
//			new Scene3(javaCode);
//			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//	        stage.close();
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
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene1.fxml"));
		
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
	
	public static void getDescription(String description) {
		descriptions = description;
	}
}

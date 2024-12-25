package GUI.main;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import API.utils.connectAPI;
import API.utils.jsonConverter;
import Decode.DecodeAndCompress;
import Generator.JavaCodeGenerator;
import Parser.StyleParser;
import Parser.SyntaxParser;
import exceptions.DescriptionException;
import exceptions.EmptyDiagramException;
import exceptions.EmptyHistoryException;
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
import javafx.scene.control.Menu;
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
	
	@FXML
    private MenuButton historyOption;
	
	@FXML
    private MenuItem menuClear;
	
	@FXML
    private Button btnClear;
	
	
	
	private Stage stage;
	private Scene scene;
	private String classes;
	private static String descriptions;
	private Map<String, String> currentFile = new HashMap<>();
	private static Map<String, String> history = new HashMap<>();
	private Map<String, String > pureCode = new HashMap<>();
	
	private String apiUrl = "http://127.0.0.1:8000";
	
	@FXML
    private void initialize() {
		btnClear.setVisible(false);
	}
	
	@FXML
	public void browse_file(ActionEvent event) throws Exception, EmptyDiagramException{
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open a file");
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")+ "/Desktop"));
        Stage stage = (Stage) browse.getScene().getWindow();
        FileChooser.ExtensionFilter drawioFilter = new FileChooser.ExtensionFilter("DrawIO Files (*.drawio)", "*.drawio");
        fileChooser.getExtensionFilters().add(drawioFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        descriptions = "";
        btnSelectFile.getItems().clear();
		codeText.setText("//Preview Code");
        if (selectedFile == null) {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText("No file selected!");
	        if (alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) scenePane.getScene().getWindow();
			}
        }else {
        	String drawioFilepath = selectedFile.getAbsolutePath();
        	try {
	            String decoded_xml = DecodeAndCompress.convert(drawioFilepath);
	    		StyleParser parser_style = new StyleParser(decoded_xml);
	    		Map<String, Object> style_tree = parser_style.convertToStyleTree();
	    		
	    		SyntaxParser parser_syntax = new SyntaxParser(style_tree);
	    		Map<String, Map<String, Object>> syntax_tree = parser_syntax.convertToSyntaxTree();
	    		
	    		JavaCodeGenerator java_gen = new JavaCodeGenerator(syntax_tree);
	    		java_gen.generateCode();
	    		
	    		descriptions = java_gen.generateDescription();
	    		classes = java_gen.generateClassStructure();
//	    		System.out.println(classes);
//	    		System.out.println(descriptions);
	//    		descriptionText.setText(descriptions);
	    		pureCode = java_gen.getMapOutput();
	    		btnSelectFile.getItems().clear();
	    		for (Map.Entry<String, String> entry : pureCode.entrySet()) {
	                MenuItem menuItem = new MenuItem(entry.getKey().replace("\n", "")+".java");
	                menuItem.setOnAction(e ->  {codeText.setText(entry.getValue()); 
	                							currentFile.clear();
	                							currentFile.put(entry.getKey(), "select");
	                							btnClear.setVisible(false);});
	                btnSelectFile.getItems().add(menuItem);
	            }
	    		Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Success");
	            alert.setHeaderText("Diagram Load Successful");
	            alert.setContentText("Your diagram has been successfully loaded to the system!");
	            if (alert.showAndWait().get() == ButtonType.OK) {
    				stage = (Stage) scenePane.getScene().getWindow();
    			}
        	}catch(StringIndexOutOfBoundsException e) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    	        alert.setTitle("Error");
    	        alert.setHeaderText("Warning!");
    	        alert.setContentText("Error! Diagram has no classes!");
    	        if (alert.showAndWait().get() == ButtonType.OK) {
    				stage = (Stage) scenePane.getScene().getWindow();
    			}
    		}catch(NullPointerException e) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    	        alert.setTitle("Error");
    	        alert.setHeaderText("Warning!");
    	        alert.setContentText("Error! Diagram is not valid!");
    	        if (alert.showAndWait().get() == ButtonType.OK) {
    				stage = (Stage) scenePane.getScene().getWindow();
    			}
    		}
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
	public void gen_code(ActionEvent event) throws Exception, DescriptionException{
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
		try {
			String descriptionJson = jsonConverter.StringtoJson(descriptions);
			if (classes.length()==0) {
    			throw new EmptyDiagramException("Error! Please choose a diagram");
    		}
			int postResponseCode = connectAPI.postAPI(descriptionJson, classes);
			System.out.println(descriptions);
			System.out.println(classes);
			if (postResponseCode == HttpURLConnection.HTTP_OK) {
				connectAPI.runPython("test.py");
				Map<String, String> javaCode = connectAPI.getCode();
				connectAPI.clearCode();
				if (javaCode.size()==0) {
					throw new DescriptionException("Error! Wrong description format!");
				}
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/Scene3.fxml"));
		        Parent root = loader.load();
		        Scene3_Controller scene3Controller = loader.getController();
		        scene3Controller.setJavaCode(javaCode);
		        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		        scene = new Scene(root);
		        stage.setScene(scene);
		        stage.show();
			}else {
				throw new DescriptionException("Error! Wrong description format!");
			}
		} catch(NullPointerException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText("Error! Empty input diagram");
	        if (alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) scenePane.getScene().getWindow();
			}
		}catch(DescriptionException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText(e.getMessage());
	        if (alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) scenePane.getScene().getWindow();
			}
		}catch(EmptyDiagramException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText(e.getMessage());
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
		
		descriptions = "";
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Logging out");
		alert.setHeaderText(null);
		alert.setContentText("Do you want to log out?");
		
		if (alert.showAndWait().get() == ButtonType.OK) {
			history.clear();
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		
	}
	
	public static void getDescription(String description) {
		descriptions = description;
	}
	
	public void addHistory(Map<String, String> javaCode) {
		history.putAll(javaCode);
		for (Map.Entry<String, String> entry : history.entrySet()) {
            MenuItem menuItem = new MenuItem(entry.getKey());
            menuItem.setOnAction(e -> {codeText.setText(entry.getValue()); 
            						   currentFile.clear(); 
            						   currentFile.put(entry.getKey(), "history");
            						   btnClear.setVisible(true);});
            historyOption.getItems().add(menuItem);
        }
	}
	
	public static Map<String, String> getHistory(){
		return history;
	}
	
	@FXML
    public void clearHistory(ActionEvent event) throws EmptyHistoryException{
		try {
			if (history.size() > 0) {
				history.clear();
				historyOption.getItems().removeIf(menuItem -> !menuItem.getText().equals("Clear All"));
				codeText.setText("//Preview Code");
			}else {
				throw new EmptyHistoryException("Error! Nothing to clear!");
			}
		}catch(EmptyHistoryException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText(e.getMessage());
	        if (alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) scenePane.getScene().getWindow();
			}
		}finally{
			btnClear.setVisible(false);
		}
    }
	
	@FXML
    public void clearFile(ActionEvent event) {
		for (Map.Entry<String, String> entry : currentFile.entrySet()) {
			System.out.println(entry.getKey()+".java" + ", " + entry.getValue());
			codeText.setText("//Preview Code");
			historyOption.getItems().forEach(menuItem -> {
			    System.out.println(menuItem.getText().equals(entry.getKey().replace("\n", "")));
			    System.out.println(menuItem.getText());
			    System.out.println(entry.getKey().replace("\n", ""));
			});
			if (entry.getValue()=="history") {
				history.remove(entry.getKey().replace("\n", ""));
				historyOption.getItems().removeIf(menuItem -> menuItem.getText().equals(entry.getKey().replace("\n", "")));
				Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Success");
	            alert.setHeaderText("Code Exported Successfully");
	            alert.setContentText("Your code has been written to your selected directory!");
	            if (alert.showAndWait().get() == ButtonType.OK) {
	            	stage = (Stage) scenePane.getScene().getWindow();
	 			}
			}else {
//				btnSelectFile.getItems().removeIf(menuItem -> menuItem.getText().equals(entry.getKey().replace("\n", "")+".java"));
//				pureCode.remove(entry.getKey().replace("\n", ""));
			}
		}
		btnClear.setVisible(false);
		currentFile.clear();
    }
}

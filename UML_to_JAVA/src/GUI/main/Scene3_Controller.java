package GUI.main; 

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import API.utils.connectAPI;
import exceptions.HistoryLimitExceedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Scene3_Controller {
	private Stage stage;
	private Scene scene;
	
	@FXML
	private AnchorPane scenePane;
	
	@FXML
    private MenuButton fileSelector;
	
	@FXML
    private TextArea codeField;
	
	@FXML
    private Button btnExport;
	
	@FXML
    private Button btnSaveHistory;
	
	boolean save = false;
	
	private Map<String, String> javaCode = new HashMap<>();
	
	@FXML
    public void exportFiles(ActionEvent event) {
		 DirectoryChooser directoryChooser = new DirectoryChooser();
         directoryChooser.setTitle("Select Directory");
         
         directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
         File selectedDirectory = directoryChooser.showDialog(stage);
         
         if (selectedDirectory != null) {
             System.out.println("Selected Directory: " + selectedDirectory.getAbsolutePath());
             String filePath = selectedDirectory.getAbsolutePath();
             for (Map.Entry<String, String> entry : javaCode.entrySet()) {
            	 System.out.println(entry.getKey());
             }
//             System.out.println("Hello World!");
             for (Map.Entry<String, String> entry : javaCode.entrySet()) {
            	 System.out.println(entry.getKey());
            	 try {
                     // Step 1: Create a file object
                     File myFile = new File(filePath+"/"+entry.getKey());

                     // Step 2: Create a new file
                     if (myFile.createNewFile()) {
                         System.out.println("File created: " + myFile.getName());
                     } else {
                         System.out.println("File already exists.");
                     }

                     // Step 3: Write content to the file
                     FileWriter writer = new FileWriter(filePath+"/"+entry.getKey());
                     writer.write(entry.getValue());
                     writer.close();

                     System.out.println("Successfully wrote to the file.");
                 } catch (IOException e) {
                     System.out.println("An error occurred.");
                     e.printStackTrace();
                 }
//                 System.out.println("Key: " + entry.getKey());
//                 System.out.println("Value: " + entry.getValue().replace("\\t", "\t"));
//                 try (FileWriter writer = new FileWriter(filePath+"/"+entry.getKey())) {
//                     writer.write(entry.getValue());
//                 } catch (IOException e) {
//                     e.printStackTrace();
                 }
             } else {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
     	        alert.setTitle("Error");
     	        alert.setHeaderText("Warning!");
     	        alert.setContentText("You have not chosen any directory!");
     	        if (alert.showAndWait().get() == ButtonType.OK) {
     				stage = (Stage) scenePane.getScene().getWindow();
     			}
             }
    }
	
//	public Scene3_Controller(Map<String, String> javaCode) {
//		this.javaCode = javaCode;
//	}
	
	public void new_file(ActionEvent event) throws Exception{
//		Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene2.fxml"));
//		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//		scene = new Scene(root);
//		stage.setScene(scene);
//		stage.show();
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/fxml/Scene2.fxml"));
        Parent root = loader.load();

        Scene2_Controller scene2Controller = loader.getController();
        if (save) {
        	scene2Controller.addHistory(javaCode);
        }
        Scene2_Controller.getDescription("");

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
	}
	
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
	
	public void setJavaCode(Map<String, String> javaCode) {
		this.javaCode = javaCode;
		for (Map.Entry<String, String> entry : javaCode.entrySet()) {
            MenuItem menuItem = new MenuItem(entry.getKey());
            menuItem.setOnAction(e -> codeField.setText(entry.getValue()));
            fileSelector.getItems().add(menuItem);
        }
	}
	
	@FXML
    public void saveHistory() throws HistoryLimitExceedException{
		try {
	        if (Scene2_Controller.getHistory().size() + javaCode.size() > 2) {
	            throw new HistoryLimitExceedException("Error! History reached limit!\nPlease clear history or Upgrade your account to continue!");
	        }
	        save = true;
	    } catch (HistoryLimitExceedException e) {
	        // Create an alert to display the exception message
	        Alert alert = new Alert(AlertType.ERROR);
	        alert.setTitle("History Limit Exceeded");
	        alert.setHeaderText("History Limit Exceeded");
	        alert.setContentText(e.getMessage());

	        // Show the alert to the user
	        alert.showAndWait();
	    }
    }
	
}

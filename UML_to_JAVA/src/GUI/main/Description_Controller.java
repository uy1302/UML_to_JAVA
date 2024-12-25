package GUI.main;

import java.io.File;

import API.utils.jsonConverter;
import exceptions.DescriptionException;
import exceptions.NotFoundDirectoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Description_Controller {
	@FXML
	private Button uploadDes;
	
	@FXML
    private TextArea descriptionText;
	
	@FXML
    private Button btnSave;
	
	private String descriptions;
	
	@FXML
    public void readDescriptionFile(ActionEvent event) throws NotFoundDirectoryException{
		FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);
        Stage stage = (Stage) uploadDes.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        
        try {
	        if (selectedFile != null) {
	            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
	            descriptionText.setText(jsonConverter.readFileAsString(selectedFile.getAbsolutePath()));
	            Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Success");
	            alert.setHeaderText("Success Description Loaded");
	            alert.setContentText("Your descriptions has been successfully loaded to the system!");
	            if (alert.showAndWait().get() == ButtonType.OK) {
	            	stage = (Stage) uploadDes.getScene().getWindow();
	    		}
	        } else {
	            System.out.println("File selection canceled.");
	            throw new NotFoundDirectoryException("No file chosen!");
	        }
        }catch(NotFoundDirectoryException e) {
        	Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText(e.getMessage());
	        if (alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) uploadDes.getScene().getWindow();
			}
        }
    }
	
	public void setDescription(String descriptions) {
		this.descriptions = descriptions;
//		System.out.println(descriptions);
		descriptionText.setText(descriptions);
	}
	
	@FXML
    void saveDescription(ActionEvent event) throws DescriptionException {
		descriptions = descriptionText.getText(); 
		try {
			if (descriptions == null ||descriptions.length() == 0) {
				throw new DescriptionException("The description is blank");
			}
			Scene2_Controller.getDescription(descriptions);
			Stage stage = (Stage) btnSave.getScene().getWindow();
			Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Success");
	        alert.setHeaderText("Success Description Saved");
	        alert.setContentText("Your descriptions has been successfully saved to the system!");
	        if (alert.showAndWait().get() == ButtonType.OK) {
	        	stage = (Stage) uploadDes.getScene().getWindow();
			}
		    stage.close();
		}catch(DescriptionException e) {
			Stage stage = (Stage) btnSave.getScene().getWindow();
			Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText(e.getMessage());
	        if (alert.showAndWait().get() == ButtonType.OK) {
	        	stage = (Stage) uploadDes.getScene().getWindow();
	        }
		}
    }
}
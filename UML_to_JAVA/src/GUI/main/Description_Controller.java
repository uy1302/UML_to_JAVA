package GUI.main;

import java.io.File;

import API.utils.jsonConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
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
    public void readDescriptionFile(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(txtFilter);
        Stage stage = (Stage) uploadDes.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());
            descriptionText.setText(jsonConverter.readFileAsString(selectedFile.getAbsolutePath()));
        } else {
            System.out.println("File selection canceled.");
        }
    }
	
	public void setDescription(String descriptions) {
		this.descriptions = descriptions;
//		System.out.println(descriptions);
		descriptionText.setText(descriptions);
	}
	
	@FXML
    void saveDescription(ActionEvent event) {
		descriptions = descriptionText.getText(); 
		Scene2_Controller.getDescription(descriptions);
		Stage stage = (Stage) btnSave.getScene().getWindow();
	    stage.close();
    }
}
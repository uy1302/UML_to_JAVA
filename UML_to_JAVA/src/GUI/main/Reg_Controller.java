package GUI.main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Reg_Controller{
	private Stage stage;
	private Scene scene;

	
	public void switch_to_login(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene1.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void sign_up(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene1.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void success(ActionEvent event) throws IOException{
		Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sign Up Status");
        alert.setHeaderText(null);
        alert.setContentText("You have signed up successfully!");
        alert.showAndWait();
		
	}
	
	
}

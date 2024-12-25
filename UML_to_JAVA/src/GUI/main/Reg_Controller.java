package GUI.main;

import java.io.IOException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Reg_Controller{
	private Stage stage;
	private Scene scene;
	
    @FXML
    private Button SignupButton;
    
    @FXML
    private TextField password_reg;
    
    @FXML
    private TextField username_reg;
	
	public void switch_to_login(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene1.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void sign_up(ActionEvent event) throws IOException {
		String name = username_reg.getText();
		String pass = password_reg.getText();
		if (name == null || name.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            showAlert("Error", "Username and password cannot be empty.");
            return;
        }

        try {
            // Call the register method from DBUtils
            DBUtils.register(name, pass);
            showAlert("Success", "You have signed up successfully!");
            // If successful, redirect to Scene1
            Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene1.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            showAlert("Error", "Registration failed: " + e.getMessage());
        }
    }

    // Utility method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
//	public void success(ActionEvent event) throws IOException{
//		Alert alert = new Alert(AlertType.INFORMATION);
//        alert.setTitle("Sign Up Status");
//        alert.setHeaderText(null);
//        alert.setContentText("You have signed up successfully!");
//        alert.showAndWait();
//		
//	}	
}

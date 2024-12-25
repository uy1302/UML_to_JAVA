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
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Scene1_Controller {
	private Stage stage;
	private Scene scene;
	@FXML
	private AnchorPane scenePane;
	
    @FXML
    private TextField username;

    @FXML
    private PasswordField password;
    
	public void Reg(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Register.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void sign_in(ActionEvent event) throws Exception{
		String name = username.getText();
		String pass = password.getText();
		try {
			boolean success = DBUtils.connect(name, pass); 
			if (success) {
				
				int userId = DBUtils.getUserId(name, pass);
				SessionContext.setUserId(userId);  
				System.out.println(userId);
				Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene2.fxml"));
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				}else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
			        alert.setTitle("Error");
			        alert.setHeaderText("Warning!");
			        alert.setContentText("Wrong username or password!");
			        if (alert.showAndWait().get() == ButtonType.OK) {
						stage = (Stage) scenePane.getScene().getWindow();
			        }
				}
		}catch(SQLException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
	        alert.setTitle("Error");
	        alert.setHeaderText("Warning!");
	        alert.setContentText("Error! Database Connection!");
	        if (alert.showAndWait().get() == ButtonType.OK) {
				stage = (Stage) scenePane.getScene().getWindow();
	        }
		}
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
	
	
	public class SessionContext {
	    private static int userId;

	    public static int getUserId() {
	        return userId;
	    }

	    public static void setUserId(int id) {
	        userId = id;
	    }
	}
}

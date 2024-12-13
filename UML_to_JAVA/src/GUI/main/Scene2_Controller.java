package GUI.main;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import Decode.DecodeAndCompress;
import Generator.JavaCodeGenerator;
import Parser.StyleParser;
import Parser.SyntaxParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Scene2_Controller {
	
	@FXML
	private Button browse = new Button();
	@FXML
	private AnchorPane scenePane;
	private Stage stage;
	private Scene scene;
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
	public void gen_code(ActionEvent event) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/GUI/fxml/Scene3.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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

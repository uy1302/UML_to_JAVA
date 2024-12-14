package GUI.main;

import java.io.IOException;
import java.util.Map;

import javax.swing.JFrame;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Scene3 extends JFrame{

	public Scene3(Map<String, String>javacode) {
		super();
		
		JFXPanel fxPanel = new JFXPanel();
		this.add(fxPanel);
		
		this.setTitle("Scene3");
		this.setVisible(true);
		this.setSize(1050, 800);
		
		this.setLocationRelativeTo(null);
		
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				try {
					FXMLLoader loader = new FXMLLoader(getClass()
							.getResource("/GUI/fxml/Scene3.fxml"));
					Scene3_Controller controller = new Scene3_Controller(javacode);
					loader.setController(controller);
					Parent root = loader.load();
					fxPanel.setScene(new Scene(root));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

}

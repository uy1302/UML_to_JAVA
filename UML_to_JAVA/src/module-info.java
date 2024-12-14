/**
 * 
 */
/**
 * 
 */
module UML_to_JAVA {
	requires javafx.graphics;
	requires javafx.controls;
	requires javafx.fxml;
	requires org.jsoup;
	requires java.desktop;
	requires javafx.swing;
	exports GUI.main;
	opens GUI.main to javafx.fxml;
}
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
	exports GUI.main;
	opens GUI.main to javafx.fxml;
}
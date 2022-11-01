module EECE435LGame2 {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.media;
	
	opens application to javafx.graphics, javafx.fxml;
}

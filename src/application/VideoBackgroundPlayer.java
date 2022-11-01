/**
 * @author Nadim Chibani, Youssef Mousallem
 * @version 1.1
 */
package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.*;
import javafx.stage.Stage;

public class VideoBackgroundPlayer extends Application{
	
	  public static void main(String[] args) throws Exception { launch(args); }
	  @Override public void start(final Stage stage) throws Exception {
	    final MediaPlayer oracleVid = new MediaPlayer(
	      new Media("http://download.oracle.com/otndocs/products/javafx/oow2010-2.flv")
	    );
	    stage.setScene(new Scene(new Group(new MediaView(oracleVid)), 540, 208));
	    stage.show();

	    oracleVid.setMute(true);
	    oracleVid.setRate(20);

	    oracleVid.setCycleCount(MediaPlayer.INDEFINITE);

	    oracleVid.play();
	  }
}

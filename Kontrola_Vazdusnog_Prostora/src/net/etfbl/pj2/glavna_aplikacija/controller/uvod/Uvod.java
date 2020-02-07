package net.etfbl.pj2.glavna_aplikacija.controller.uvod;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Uvod extends Application {

	@Override
	public void start(Stage primaryStage) {
			 FXMLLoader fxmlLoader = null;
		        try {
		            fxmlLoader = new FXMLLoader(new File("src\\net\\etfbl\\pj2\\glavna_aplikacija\\view\\uvod.fxml").toURI().toURL());
		            AnchorPane pane = fxmlLoader.load();

		                Scene mainScene = new Scene(pane);
		                primaryStage.setScene(mainScene);
		                primaryStage.setTitle("Aplikacija RADAR ");
		                primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

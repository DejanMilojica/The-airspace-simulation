package net.etfbl.pj2.glavna_aplikacija.main;

import java.io.File;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import net.etfbl.pj2.glavna_aplikacija.controller.main_controller.MainController;
import net.etfbl.pj2.konfiguracija.ConfigRadarManager;
import net.etfbl.pj2.logger.Logger_;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private Logger_ objekatLogger = new Logger_(this);
	private static boolean KRAJ = false; // Promjenjiva kojom definisemo zavrsetak PROGRAMA!

	public static boolean isKRAJ() {
		return KRAJ;
	}

	public static void setKRAJ(boolean kRAJ) {
		KRAJ = kRAJ;
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader  = new FXMLLoader(new File("src\\net\\etfbl\\pj2\\glavna_aplikacija\\view\\main.fxml").toURI().toURL());
	        AnchorPane pane =  fxmlLoader.load();
//	        Label vrijeme = (Label)pane.getChildren().get(5);
//	        MainController.setVrijeme(vrijeme);

			primaryStage.setScene(new Scene(pane,580,700));
			primaryStage.setTitle("Aplikacija RADAR!");
			primaryStage.show();
		} catch(Exception e) {
			objekatLogger.log(e.getMessage(), e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

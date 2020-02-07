package net.etfbl.pj2.glavna_aplikacija.controller.pregled_dogadjaja_sudara_controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import net.etfbl.pj2.logger.Logger_;

public class PregledDogadjajaSudara extends Application {
	
	private Logger_ objekatLogger = new Logger_(this);

	@Override
	public void start(Stage primaryStage) throws Exception {
	}
	
	public static void main(String[] args) {
        launch(args);
    }
	
	//Pregled Sudara: 
	 public void pregledSudara(PregledDogadjajaSudaraController dogSudController, Stage primaryStage){
	        FXMLLoader fxmlLoader = null;
	        try {
	        	
	            fxmlLoader = new FXMLLoader(new File("src\\net\\etfbl\\pj2\\glavna_aplikacija\\view\\pregledSvihSudara.fxml").toURI().toURL());
	        	dogSudController.setAnchorPane(fxmlLoader.load());
	       
	        } catch (IOException e) {
	            e.printStackTrace();
	            this.objekatLogger.log(e.getMessage(),e);
	        }
	        dogSudController.setScrollPane((ScrollPane)dogSudController.getAnchorPane().getChildren().get(0));
	        dogSudController.setLabelaSudara((Label)dogSudController.getScrollPane().getContent());
	        dogSudController.postavljanjeLabeleSudara();

	        Scene mainScene = new Scene(dogSudController.getAnchorPane());
	        primaryStage.setScene(mainScene);
	        primaryStage.setTitle("Pregled Sudara!");
	        primaryStage.show();
	    }

	 //Pregled dogadjaja
	 public void pregledDogadjaja(PregledDogadjajaSudaraController dogSudController, Stage primaryStage){
	        FXMLLoader fxmlLoader = null;
	        try {
	            fxmlLoader = new FXMLLoader(new File("src\\net\\etfbl\\pj2\\glavna_aplikacija\\view\\pregledSvihSudara.fxml").toURI().toURL());
	            dogSudController.setAnchorPane(fxmlLoader.load());
	        } catch (IOException e) {
	            e.printStackTrace();
	            this.objekatLogger.log(e.getMessage(),e);
	        }
	        dogSudController.setScrollPane((ScrollPane)dogSudController.getAnchorPane().getChildren().get(0));
	        dogSudController.setLabelaSudara((Label)dogSudController.getScrollPane().getContent());
	        dogSudController.postavljanjeLabeleOPojaviNovogDogadjaja();

	        Scene mainScene = new Scene(dogSudController.getAnchorPane());
	        primaryStage.setScene(mainScene);
	        primaryStage.setTitle("Pregled Pojave Stranih Vojnih Aviona!");
	        primaryStage.show();
	    }
	
	

}

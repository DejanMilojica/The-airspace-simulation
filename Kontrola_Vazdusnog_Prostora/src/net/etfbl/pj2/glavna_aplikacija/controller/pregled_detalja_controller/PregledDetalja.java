package net.etfbl.pj2.glavna_aplikacija.controller.pregled_detalja_controller;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.etfbl.pj2.logger.Logger_;

public class PregledDetalja extends Application {
	private Logger_ loggerObjekat = new Logger_(this);

	@Override
	public void start(Stage primaryStage){		}
	
    public static void main(String[] args) {
        launch(args);
    }

    public void setTabsandStart(int i, int j, Stage primaryStage, PregledDetaljaController pregledDetaljaController){
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(new File("src\\net\\etfbl\\pj2\\glavna_aplikacija\\view\\letjelicaDetalji.fxml").toURI().toURL());

                pregledDetaljaController.setAnchorPane(fxmlLoader.load());
                pregledDetaljaController.setDetailsPane((ScrollPane)pregledDetaljaController.getAnchorPane().getChildren().get(1));
                pregledDetaljaController.setScrollPane(i,j);

                Scene mainScene = new Scene(pregledDetaljaController.getAnchorPane());
                primaryStage.setScene(mainScene);
                primaryStage.setTitle("Detalji o letjelici: ");
                primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
           this.loggerObjekat.log(e.getMessage(),e);
        }
    }

}

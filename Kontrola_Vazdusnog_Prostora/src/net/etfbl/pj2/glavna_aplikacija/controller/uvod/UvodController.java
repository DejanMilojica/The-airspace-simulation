package net.etfbl.pj2.glavna_aplikacija.controller.uvod;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.etfbl.pj2.glavna_aplikacija.main.Main;
import net.etfbl.pj2.model.mapa.Mapa;
import net.etfbl.pj2.radar.Radar;
import net.etfbl.pj2.simulacija.Simulator;

public class UvodController implements Initializable{
	@FXML
	private Button pocetakSimulacije;
	
	@FXML
	private Button informacije;
	
	@FXML
	private Button izlaz;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//IZLAZ iz aplikacije:
				izlaz.setOnAction(action -> {
					Platform.exit();
				});
				
				//INFORMACIJE:
				this.informacije.setOnAction(action -> {
					FXMLLoader fxmlLoader = null;
			        try {
			            fxmlLoader = new FXMLLoader(new File("src\\net\\etfbl\\pj2\\glavna_aplikacija\\view\\informacije.fxml").toURI().toURL());
			            AnchorPane pane = fxmlLoader.load();

			                Scene mainScene = new Scene(pane);
			                Stage primaryStage = new Stage();
			                primaryStage.setScene(mainScene);
			                primaryStage.setTitle("INFORMACIJE:  ");
			                primaryStage.show();

			} catch(Exception e) {
				e.printStackTrace();
			}
				});
				
				//POCETAK SIMULACIJE: 
				this.pocetakSimulacije.setOnAction(action -> {
//					Platform.exit();
					Main main = new Main();
					main.start( new Stage());
					Mapa mapa = new Mapa();								  //	M A P A !
					Simulator simulator = new Simulator(mapa);	 //	S I M U L A T O R !
					Radar radar = new Radar(mapa);						//	R A D A R !
				});
	}
	
}

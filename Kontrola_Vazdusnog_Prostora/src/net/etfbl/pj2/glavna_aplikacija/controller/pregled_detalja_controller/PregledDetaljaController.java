package net.etfbl.pj2.glavna_aplikacija.controller.pregled_detalja_controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.etfbl.pj2.glavna_aplikacija.controller.main_controller.MainController;
import net.etfbl.pj2.glavna_aplikacija.util.PredstavljanjeLetjelicaTekstomiBojom;
import net.etfbl.pj2.konfiguracija.UpravljanjeDogadjajima;
import net.etfbl.pj2.logger.Logger_;

public class PregledDetaljaController implements Initializable {
	
	    @FXML
	    private ScrollPane detailsPane;
	    
	    @FXML
	    private AnchorPane anchorPane;
	    
	    private Logger_  loggerObjekat = new Logger_(this);
	    private MainController mainController;
	    

	    @Override
	    public void initialize(URL url, ResourceBundle resourceBundle) {
	    	
	    }

	    public MainController getMainController() {
			return mainController;
		}

		public void setMainController(MainController mainController) {
			this.mainController = mainController;
		}

		public AnchorPane getAnchorPane() {
	        return anchorPane;
	    }

	    public void setAnchorPane(AnchorPane anchorPane) {
	        this.anchorPane = anchorPane;
	    }

	    public ScrollPane getDetailsPane() {
	        return detailsPane;
	    }

	    public void setDetailsPane(ScrollPane detailsPane) {
	        this.detailsPane = detailsPane;
	    }

	    public void prikazDetaljaOLetjelici(int i, int j) {
	        new PregledDetalja().setTabsandStart(i,j,new Stage(), this);
	    }

	    //Prikazivanje podataka, o letjelici na poziciji (i , j): 
	    //Postoji mogucnost da na nekoj poziciji, ima vise letjelica, na razlicitoj visini: 
	    public void setScrollPane(int i, int j) {
	        List<String> letjelice  = this.mainController.getMap();
	        List<String> letjeliceNaTojPoziciji = new ArrayList<>();
	        
	        String[] filtriranaLetjelica;
	        boolean postojiLetjelica = false;
	        
	        if(letjelice==null) letjelice = new ArrayList<>();
	        //Prolazak kroz mapu:
	        for(String s: letjelice) {
	        	filtriranaLetjelica = s.split("#");
	        	if(filtriranaLetjelica.length == 7) {//Imamo korektnu formu letjelice, unutar mape!
	        		//Ukoliko imamo letjelicu na toj poziciji: 
	        		if( i == Integer.parseInt(filtriranaLetjelica[5]) && j == Integer.parseInt(filtriranaLetjelica[6])) {
	        			postojiLetjelica = true;
	        			letjeliceNaTojPoziciji.add(s); //Dodajemo tu letjelicu, u listu letjelica na toj poziciji!
	        		}
	        	}
	        }
	        
	        if(postojiLetjelica) {
	        	 TabPane tabPane = new TabPane();
                 
                 for (int k = 0; k < letjeliceNaTojPoziciji.size(); k++) {
                     Tab tab = new Tab();
                	 //Uzimanje letjelice: 
                	 String letjelica = letjeliceNaTojPoziciji.get(k);
                	 String[] detaljiOLetjelici = letjelica.split("#");
                	 String sveOLetjelici = "Model Letjelice: "+detaljiOLetjelici[0] + ",\nId: " + detaljiOLetjelici[1]+
                			 ",\nVisina: "+detaljiOLetjelici[2]+",\nPozicija: ( "+i+" , "+ j +" )";

                	 //Postavljanje Detalja o Letjelici: 
                	 try {
						FXMLLoader  fxmlLoader = new FXMLLoader(new File("src\\net\\etfbl\\pj2\\glavna_aplikacija\\view\\detaljiOLetjelici.fxml").toURI().toURL());
						AnchorPane pane = fxmlLoader.load(); 
						tab.setContent(pane);
						ImageView slika =(ImageView)pane.getChildren().get(1);

						Label vrstaLetjelice = (Label) pane.getChildren().get(4);
						vrstaLetjelice.setText(detaljiOLetjelici[0]);
						
						String adresaSlike = "file:/C:\\Users\\PC\\Desktop\\FAKULTET\\III\\II SEMESTAR\\JAVA\\VJEZBA\\Java_PROJEKAT\\SLIKE\\";
						adresaSlike+=detaljiOLetjelici[0]+".jpg";
			            slika.setImage(new Image(adresaSlike));
						
						Label model = (Label)pane.getChildren().get(7);
						model.setText(detaljiOLetjelici[1]);
						
						Label id = (Label)pane.getChildren().get(10);
						id.setText(detaljiOLetjelici[2]);
						
						Label ostaleKarakteristike = (Label)pane.getChildren().get(13);
						ostaleKarakteristike.setText("Visina: "+detaljiOLetjelici[3]+",\n"+detaljiOLetjelici[4]+", \nPozicija: ( "+i+" , "+ j +" )");
						
                	 } catch ( IOException e) {
						e.printStackTrace();
					}

                     tabPane.getTabs().add(tab);
                 }
                 
	                detailsPane.setContent(tabPane);
	        }
	        else
	            detailsPane.setContent(new Label("NA POLJU, TRENUTNO NEMA AKTIVNIH LETJELICA!"));
	    }
}

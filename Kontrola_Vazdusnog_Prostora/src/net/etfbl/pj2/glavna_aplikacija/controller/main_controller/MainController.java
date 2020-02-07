package net.etfbl.pj2.glavna_aplikacija.controller.main_controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.glavna_aplikacija.controller.pregled_detalja_controller.PregledDetaljaController;
import net.etfbl.pj2.glavna_aplikacija.controller.pregled_dogadjaja_sudara_controller.DetekcijaSudara;
import net.etfbl.pj2.glavna_aplikacija.controller.pregled_dogadjaja_sudara_controller.PregledDogadjajaSudaraController;
import net.etfbl.pj2.glavna_aplikacija.main.Main;
import net.etfbl.pj2.konfiguracija.ConfigSimulatorManager;
import net.etfbl.pj2.konfiguracija.UpravljanjeDogadjajima;
import net.etfbl.pj2.logger.Logger_;

public class MainController implements Initializable{
	
	 private static boolean zabranaLetenja = false;
	 private static List<String> rasporedLetjelica = new ArrayList<String>();
	 private static String putanja = DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator+DirektorijumiDatoteke.MAPA_DATOTEKA.getVrijednost();
	 private static Path adresaDatotekeMapa = Paths.get(putanja);
	 
	 private Logger_ loggerObjekat = new Logger_(this.getClass());


	 //Dijelovi grafickog sadrzaja:
	    @FXML
	    private GridPane zonaLetenjaGP;

	    @FXML
	    private CheckBox zabranaLetenjaCB;

	    @FXML
	    private Button izlazBT;

	    @FXML
	    private Button pregledSudaraBT;

	    @FXML
	    private Button pregledDogadjajaBT;

	    @FXML
	    private Label labelaZaPrikazPojaveNovogDogadjaja;
	    
	    @FXML
	    private Circle zabranaLetenjaKrug;
	    @FXML
	    private ImageView zabranaLetenjaAvion;
	    @FXML
	    private Line zabranaLetenjaLinija;
	    
	    @FXML
	    private Label labelaVrijeme;
	    @FXML
	    private Label brojLetjelica;
	    
	    public void postaviBrojLetjelica(int broj) {
	    	brojLetjelica.setText(broj+"");
	    }
	    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		zonaLetenjaGP.setGridLinesVisible(true);
		labelaVrijeme.setText((new SimpleDateFormat("hh:mm")).format(new Date()));
		
		//Postavljanje Pejnova(Labela), za graficki prikaz: 
		for(int i = 0; i < ConfigSimulatorManager.BROJ_REDOVA; i++)
            for(int j = 0; j < ConfigSimulatorManager.BROJ_KOLONA; j++) {
            	//Dodavanje LABELA, u PANE-ove!
/**/        Label labela = new Label("");
				labela.prefWidth(40);
				labela.prefHeight(40);
				labela.setTextAlignment(TextAlignment.CENTER);
/**/        Pane pane = new Pane(labela);

                pane.setPrefHeight(ConfigSimulatorManager.VISINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU);
                pane.setPrefWidth(ConfigSimulatorManager.SIRINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU);
                onGridPanePositionClick(pane);
                zonaLetenjaGP.add(pane, i, j); //Dodavanje Labele!
            }
		
		//IZLAZ iz aplikacije:
		izlazBT.setOnAction(action -> {
			//Postavimo KRAJ na true:
			Main.setKRAJ(true);
            File dir = new File(DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator +DirektorijumiDatoteke.EVENTS.getVrijednost()+File.separator );
            for(File file : dir.listFiles()){  //Pobrisemo sve event fajlove, koji su detektovali stranu letjelicu!
               if(file != null)
            	   file.delete();
            }
            //Pobrisemo sve 'ALERT' fajlove, koji su detektovali sudar!
            dir = new File(DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator +DirektorijumiDatoteke.ALERT.getVrijednost()+File.separator);
            for(File file : dir.listFiles()){
                file.delete();
            }
 /**/          Platform.exit();
        });
		
        //Akcija definisana pri pritisku dugmeta za PREGLED SUDARA: 
		pregledSudaraBT.setOnAction(dogadjaj -> new PregledDogadjajaSudaraController().prikazSudara());
        this.zabranaLetenjaCB.setOnAction(event -> cekiranjeZabraneLetenja());
     
        //Akcija definisana pri pritisku dugmeta za PREGLED DOGADJAJA: 
        this.pregledDogadjajaBT.setOnAction(dogadjaj-> new PregledDogadjajaSudaraController().prikazDogadjaja());
   
//        Deamon NIT, koristi se za upravljanje dogadjajima( pojava strane letjelice)!      
        UpravljanjeDogadjajima ud = new UpravljanjeDogadjajima(this);
        ud.setDaemon(true);
        ud.start();

//        Deamon NIT, koristi se za postavljanje letjelica na GUI!
        PostavljanjeObjekata postavljanjeLetjelice = new PostavljanjeObjekata(this);
        postavljanjeLetjelice.setDaemon(true);
        postavljanjeLetjelice.start();
        
      //Deamon NIT, koristi se za detekciju sudara letjelica!
        //Detekcija Sudara: 
        DetekcijaSudara ca = new DetekcijaSudara();
        ca.setDaemon(true);
        ca.start();
        
        /*Ciscenje datoteke, map.txt!*/
    		FileOutputStream writer = null;
    		try {
    			writer = new FileOutputStream(DirektorijumiDatoteke.ALLFILES.getVrijednost() + File.separator +"mapa.txt");
    			writer.write("".getBytes());
    		} catch (IOException e) {
    			loggerObjekat.log(e.getMessage(), e);
    		}
    		finally {
    		try {
    			writer.close();
    		} catch (IOException e) {
    			loggerObjekat.log(e.getMessage(), e);
    		}
    		}
	}
	
	public void preuzimanjeRasporedaLetjelicaIzDatoteke(){
		rasporedLetjelica = null;
        try {
        	rasporedLetjelica = Files.readAllLines(adresaDatotekeMapa);
        } catch (IOException e) {
            this.loggerObjekat.log(e.getMessage(),e);
        }
    }
	
	private synchronized void cekiranjeZabraneLetenja(){
		byte zabranaLetenjaByte = 0;
        if(zabranaLetenjaCB.isSelected()) {
        	zabranaLetenja = true;
        	zabranaLetenjaByte = -1;
        	zabranaLetenjaKrug.setVisible(true);
        	zabranaLetenjaAvion.setVisible(true);
        	zabranaLetenjaLinija.setVisible(true);
        }
        else {
        	zabranaLetenja = false;
        	zabranaLetenjaKrug.setVisible(false);
        	zabranaLetenjaAvion.setVisible(false);
        	zabranaLetenjaLinija.setVisible(false);   
        	}
        ConfigSimulatorManager.setZABRANA_LETENJA();
        
        //Upis u file, config.properties:
        RandomAccessFile aFile;
		try {
			aFile = new RandomAccessFile( DirektorijumiDatoteke.ALLFILES.getVrijednost()+ File.separator  + DirektorijumiDatoteke.CONFIGURATION.getVrijednost()  + File.separator  +  DirektorijumiDatoteke.CONFIG_PROPERTIES.getVrijednost() , "rw");
			FileChannel kanal = aFile.getChannel();
			int pozicija = 4;
			kanal.position(pozicija);
			ByteBuffer baferByte = ByteBuffer.allocate(1);
			baferByte.clear();
			baferByte.put(zabranaLetenjaByte);
			baferByte.flip();
			kanal.write(baferByte);
			kanal.close();
		} catch (IOException e) {
			this.loggerObjekat.log(e.getMessage(), e);
		}
     }
	
	//Postavljanje Labele:
	 public synchronized void postavljanjeLetjelice(String sadrzaj, Color color, int x_pozicija, int y_pozicija){
		 //Prolazimo kroz sva polja(koliko ih ima), vazdusnog prostora: 
	        for(Node polje : this.zonaLetenjaGP.getChildren()){ //Uzimamo svu 'djecu' letacke zone, i provjeravamo: 
	            if(polje instanceof Pane
	                    && GridPane.getRowIndex(polje) == y_pozicija
	                    && GridPane.getColumnIndex(polje) == x_pozicija)
	            { 
	            	//Postavljanje 'Letjelice': 
	                Pane pane = (Pane)this.zonaLetenjaGP.getChildren().get(x_pozicija*ConfigSimulatorManager.BROJ_KOLONA + y_pozicija);
	                Label letjelica = (Label)pane.getChildren().get(0);
	                letjelica.setText(sadrzaj);
	                letjelica.setTextFill(color);
	                letjelica.setVisible(true);
	                letjelica.prefWidth(40);
	                letjelica.prefHeight(30);
	                letjelica.setTextAlignment(TextAlignment.CENTER);
	            }
	        }
	    }
	 
	 //Uklanjanje letjelice: 
	 public synchronized void uklanjanjeLetjelica(int x_pozicija, int y_pozicija){
	        for(Node polje : this.zonaLetenjaGP.getChildren()){
	            if(polje instanceof Pane && GridPane.getRowIndex(polje) == y_pozicija && GridPane.getColumnIndex(polje) == x_pozicija){
	                Pane pane = (Pane)zonaLetenjaGP.getChildren().get(x_pozicija*ConfigSimulatorManager.BROJ_KOLONA + y_pozicija);
	                Label letjelica = (Label)pane.getChildren().get(0);
	                letjelica.setVisible(false);
	                letjelica.setText("");
	            }
	        }
	    }
	 
	 public synchronized Label uzimanjeLabeleIzVazdusnogProstoraNaTrazenojPoziciji(int x_pozicija, int y_pozicija){
	        Label labela = null;
	        	
	        //Prolazimo kroz polja vazdusnog prostora:
	        for(Node polje : this.zonaLetenjaGP.getChildren()){
	            if(polje instanceof Pane && GridPane.getRowIndex(polje) == y_pozicija && GridPane.getColumnIndex(polje) == x_pozicija) {
	                    labela = (Label)((Pane)polje).getChildren().get(0);
	            }
	        }
	        return labela;
	    }

	 /**
	     * Postavljanje Hendlera na Pejnove, duz mape!
	     * Klik na neku od pozicija, rezultuje pojavom novog prozora!
	     * @param specifiedField    Polje na koje se postavlja handler
	     */
	    private void onGridPanePositionClick(Node specifiedField){
	        specifiedField.setOnMouseClicked(x->{
	            Node node = (Node)x.getSource();
	            int i = GridPane.getColumnIndex(node);
	            int j = GridPane.getRowIndex(node);
//	            System.out.println("Klik na Pane: "+i+" , "+j);
	            
	            //Prikaz detalja o Letjelici!
	           PregledDetaljaController pdController= new PregledDetaljaController();
	           pdController.setMainController(this);
	           pdController.prikazDetaljaOLetjelici(i, j);
	        });
	    }

	public static boolean isZabranaLetenja() {
		return zabranaLetenja;
	}

	public static void setZabranaLetenja(boolean zabranaLetenja) {
		MainController.zabranaLetenja = zabranaLetenja;
	}

	public GridPane getZonaLetenjaGP() {
		return zonaLetenjaGP;
	}

	public void setZonaLetenjaGP(GridPane zonaLetenjaGP) {
		this.zonaLetenjaGP = zonaLetenjaGP;
	}

	public CheckBox getZabranaLetenjaCB() {
		return zabranaLetenjaCB;
	}

	public void setZabranaLetenjaCB(CheckBox zabranaLetenjaCB) {
		this.zabranaLetenjaCB = zabranaLetenjaCB;
	}

	public Button getIzlazBT() {
		return izlazBT;
	}

	public void setIzlazBT(Button izlazBT) {
		this.izlazBT = izlazBT;
	}

	public Button getPregledSudaraBT() {
		return pregledSudaraBT;
	}

	public void setPregledSudaraBT(Button pregledSudaraBT) {
		this.pregledSudaraBT = pregledSudaraBT;
	}

	public Button getPregledDogadjajaBT() {
		return pregledDogadjajaBT;
	}

	public void setPregledDogadjajaBT(Button pregledDogadjajaBT) {
		this.pregledDogadjajaBT = pregledDogadjajaBT;
	}

	public Label getLabelaZaPrikazPojaveNovogDogadjaja() {
		return labelaZaPrikazPojaveNovogDogadjaja;
	}

	public void setLabelaZaPrikazPojaveNovogDogadjaja(Label labelaZaPrikazPojaveNovogDogadjaja) {
		this.labelaZaPrikazPojaveNovogDogadjaja = labelaZaPrikazPojaveNovogDogadjaja;
	}
	
	public List<String> getMap() {
		return this.rasporedLetjelica;
	}
	
	public ImageView getZabranaLetenjaAvion() {
		return zabranaLetenjaAvion;
	}

	public void setZabranaLetenjaAvion(ImageView zabranaLetenjaAvion) {
		this.zabranaLetenjaAvion = zabranaLetenjaAvion;
	}

	public Circle getZabranaLetenjaKrug() {
		return zabranaLetenjaKrug;
	}

	public void setZabranaLetenjaKrug(Circle zabranaLetenjaKrug) {
		this.zabranaLetenjaKrug = zabranaLetenjaKrug;
	}

	public Line getZabranaLetenjaLinija() {
		return zabranaLetenjaLinija;
	}

	public void setZabranaLetenjaLinija(Line zabranaLetenjaLinija) {
		this.zabranaLetenjaLinija = zabranaLetenjaLinija;
	}

	public Label getLabelaVrijeme() {
		return labelaVrijeme;
	}

	public void setLabelaVrijeme(Label labelaVrijeme) {
		this.labelaVrijeme = labelaVrijeme;
	}
	
	public void postaviVrijeme(String trenutnoVrijeme) {
		labelaVrijeme.setText(trenutnoVrijeme);
	}

}


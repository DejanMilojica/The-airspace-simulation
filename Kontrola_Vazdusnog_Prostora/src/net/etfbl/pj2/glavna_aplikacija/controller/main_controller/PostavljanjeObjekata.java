package net.etfbl.pj2.glavna_aplikacija.controller.main_controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.glavna_aplikacija.util.PredstavljanjeLetjelicaTekstomiBojom;
import net.etfbl.pj2.konfiguracija.ConfigRadarManager;
import net.etfbl.pj2.konfiguracija.ConfigSimulatorManager;
import net.etfbl.pj2.logger.Logger_;
import net.etfbl.pj2.model.letjelica.avion.ProtivPozarniAvion;
import net.etfbl.pj2.model.letjelica.avion.PutnickiAvion;
import net.etfbl.pj2.model.letjelica.avion.TransportniAvion;
import net.etfbl.pj2.model.letjelica.bespilotna_letjelica.BespilotnaLetjelica;
import net.etfbl.pj2.model.letjelica.helikopter.ProtivPozarniHelikopter;
import net.etfbl.pj2.model.letjelica.helikopter.PutnickiHelikopter;
import net.etfbl.pj2.model.letjelica.helikopter.TransportniHelikopter;

public class PostavljanjeObjekata extends Thread{
	
	private static MainController mainController;
    private static Path adresaMape = Paths.get(DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator+DirektorijumiDatoteke.MAPA_DATOTEKA.getVrijednost());
    private  Logger_ objekatLogger = new Logger_(this);
    private StringProperty[][] labelValues = new StringProperty[ConfigSimulatorManager.BROJ_REDOVA][ConfigSimulatorManager.BROJ_KOLONA];
    private Byte pomocnaPromjenjiva = 0;
    
	public PostavljanjeObjekata(MainController mainController) {
        this.mainController  = mainController;
        for(int i = 0; i < ConfigSimulatorManager.BROJ_KOLONA; i++) {
            for (int j = 0; j < ConfigSimulatorManager.BROJ_REDOVA; j++) {
                labelValues[i][j] = new SimpleStringProperty();
                mainController.uzimanjeLabeleIzVazdusnogProstoraNaTrazenojPoziciji(i,j).textProperty().bind(labelValues[i][j]);
                mainController.uzimanjeLabeleIzVazdusnogProstoraNaTrazenojPoziciji(i,j).setTextAlignment(TextAlignment.CENTER);
                mainController.uzimanjeLabeleIzVazdusnogProstoraNaTrazenojPoziciji(i,j).setFont(Font.font(17));
            }
        }
    }

    public String letjelicaDobijanjeOdgovarajucegTeksta(String vrstaAviona){

        switch(vrstaAviona) {
        	case "Putnicki Avion": return PredstavljanjeLetjelicaTekstomiBojom.PUTNICKI_AVION.getVrijednost();
        	case "Transportni Avion": return PredstavljanjeLetjelicaTekstomiBojom.TRANSPORTNI_AVION.getVrijednost(); 
        	case "ProtivPozarni Avion": return PredstavljanjeLetjelicaTekstomiBojom.PROTIV_POZARNI_AVION.getVrijednost();
        	case "Putnicki Helikopter": return PredstavljanjeLetjelicaTekstomiBojom.PUTNICKI_HELIKOPTER.getVrijednost();
        	case "Transportni Helikopter": return PredstavljanjeLetjelicaTekstomiBojom.TRANSPORTNI_HELIKOPTER.getVrijednost();
        	case "ProtivPozarni Helikopter": return PredstavljanjeLetjelicaTekstomiBojom.PROTIV_POZARNI_HELIKOPTER.getVrijednost();
        	case "Bespilotna Letjelica": return PredstavljanjeLetjelicaTekstomiBojom.BESPILOTNA_LETJELICA.getVrijednost();
        	case "Strani Vojni Avion": return PredstavljanjeLetjelicaTekstomiBojom.STRANA_VOJNA_LETJELICA.getVrijednost();
        	case "Lovac": return PredstavljanjeLetjelicaTekstomiBojom.LOVAC.getVrijednost();
        	case "Bombarder": return PredstavljanjeLetjelicaTekstomiBojom.BOMBARDER.getVrijednost();
        }
		return " ";
    }

    public Color letjelicaDobijanjeOdgovarajuceBoje(String vrstaAviona){
    	
    	 switch(vrstaAviona) {
     		case "Putnicki Avion": return PredstavljanjeLetjelicaTekstomiBojom.PUTNICKI_AVION.getBoja();
     		case "Transportni Avion": return PredstavljanjeLetjelicaTekstomiBojom.TRANSPORTNI_AVION.getBoja(); 
     		case "ProtivPozarni Avion": return PredstavljanjeLetjelicaTekstomiBojom.PROTIV_POZARNI_AVION.getBoja();
     		case "Putnicki Helikopter": return PredstavljanjeLetjelicaTekstomiBojom.PUTNICKI_HELIKOPTER.getBoja();
     		case "Transportni Helikopter": return PredstavljanjeLetjelicaTekstomiBojom.TRANSPORTNI_HELIKOPTER.getBoja();
     		case "ProtivPozarni Helikopter": return PredstavljanjeLetjelicaTekstomiBojom.PROTIV_POZARNI_HELIKOPTER.getBoja();
     		case "Bespilotna": return PredstavljanjeLetjelicaTekstomiBojom.BESPILOTNA_LETJELICA.getBoja();
     		case "Strani Vojni Avion": return PredstavljanjeLetjelicaTekstomiBojom.STRANA_VOJNA_LETJELICA.getBoja();
     		case "Lovac": return PredstavljanjeLetjelicaTekstomiBojom.LOVAC.getBoja();
     		case "Bombarder": return PredstavljanjeLetjelicaTekstomiBojom.BOMBARDER.getBoja();
    	 }
    	 return Color.WHITE;
    }

    @Override
    public void run(){
        while(true){
            try {
               
                List<String> sveLetjelice = mainController.getMap();      //Preuzimanje rasporeda letjelica!
                /*Postavljanje Labele, za prikaz broja letjelica u Vp!*/
///**/               mainController.postaviBrojLetjelica(sveLetjelice.size());
                if(pomocnaPromjenjiva == 0) {
                //Prolazak kroz mapu, i 'Izbacivanje' starih letjelica!
                for(int i = 0; i < ConfigSimulatorManager.BROJ_KOLONA; i++) {
                    for (int j = 0; j < ConfigSimulatorManager.BROJ_REDOVA; j++) {
                        final int x= i;
                        final int y =j;
                        Platform.runLater(() -> labelValues[x][y].set(""));
                    	}
                	}
                }
                
                if(Files.size(this.adresaMape) != 0){     
                	pomocnaPromjenjiva = 0;
                //Ponovno preuzimanje rasporeda letjelic:
                mainController.preuzimanjeRasporedaLetjelicaIzDatoteke();
                sveLetjelice = mainController.getMap();

                //Prolazak kroz prethodno preuzete letjelice, i njihova analiza: 
                for(String letjelica : sveLetjelice){
                    List<String> osobineLetjelice = Arrays.asList(letjelica.split("#"));

                    String predstavljanjeLetjelicaTekstom = letjelicaDobijanjeOdgovarajucegTeksta(osobineLetjelice.get(0));
                    Color predstavljanjeLetjelicaBojom = letjelicaDobijanjeOdgovarajuceBoje(osobineLetjelice.get(0));
                   String id_letjelice = osobineLetjelice.get(2);
                    		
                    int x_pozicija , y_pozicija;
                    if(osobineLetjelice.size()>=6) {
                    	 x_pozicija = Integer.parseInt(osobineLetjelice.get(5));
                    	 y_pozicija = Integer.parseInt(osobineLetjelice.get(6));
                    }
                    else
                    	throw new IOException("Nekorektna Forma Mape!");
                    
                    //Formiramo novu letjelicu, i smjestamo je na graficki prozor!
                    Platform.runLater(()->{
                    	 /*Postavljanje Sadrzaja letjelice!*/
                    	String vrstaAviona = osobineLetjelice.get(0);
                        if(vrstaAviona.contains("Vojni Avion") || vrstaAviona.equals("Lovac") || vrstaAviona.equals("Bombarder") ) {
                        	labelValues[ x_pozicija][y_pozicija].set(predstavljanjeLetjelicaTekstom);
                            mainController.uzimanjeLabeleIzVazdusnogProstoraNaTrazenojPoziciji(x_pozicija,y_pozicija).setFont(Font.font(20));
                        }
                        else {
                        	labelValues[ x_pozicija][y_pozicija].set(predstavljanjeLetjelicaTekstom+id_letjelice);
                            mainController.uzimanjeLabeleIzVazdusnogProstoraNaTrazenojPoziciji(x_pozicija,y_pozicija).setFont(Font.font(17));
                        }
                        /*Postavljanje Boje sadrzaja letjelice!*/
                        mainController.uzimanjeLabeleIzVazdusnogProstoraNaTrazenojPoziciji(x_pozicija,y_pozicija).setTextFill(predstavljanjeLetjelicaBojom);
                    });
                }
               }
                else
                	pomocnaPromjenjiva++;
                
                Thread.sleep(ConfigRadarManager.INTERVAL_AZURIRANJA_MAPE);
                
            } catch (InterruptedException | IOException e) {
                objekatLogger.log(e.getMessage(), e);
            }
        }
    }

}

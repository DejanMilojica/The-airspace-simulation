package net.etfbl.pj2.glavna_aplikacija.controller.pregled_dogadjaja_sudara_controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.etfbl.pj2.konfiguracija.SerijalizacijaSudara;
import net.etfbl.pj2.konfiguracija.UpravljanjeDogadjajima;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PregledDogadjajaSudaraController {
    @FXML
    private Label labelaSudara;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ScrollPane scrollPane;

    //Geteri, i Seteri FXML objekata:
    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public Label getLabelaSudara() {
        return labelaSudara;
    }

    public void setLabelaSudara(Label labelaSudara) {
        this.labelaSudara = labelaSudara;
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public void setAnchorPane(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    public void prikazSudara() {
        new PregledDogadjajaSudara().pregledSudara(this, new Stage());
    }

    //Deserijalizacija sudara!
    public void postavljanjeLabeleSudara(){
        List<SerijalizacijaSudara> detaljiSudara = new ArrayList<>();
        detaljiSudara = SerijalizacijaSudara.procitajSudare();
        if(detaljiSudara.size() == 0){
        	labelaSudara.setText("LISTA SUDARA PRAZNA!");
        }else{
            TabPane tabPane = new TabPane();
            for (int k = 0; k < detaljiSudara.size(); k++) {
                Tab tab = new Tab();
                SerijalizacijaSudara sudar = detaljiSudara.get(k);
                String detaljiOSudaru = "Detalji Sudara: "+sudar.getDetalji() + "\n" +"Vrijeme: "+ sudar.getVrijeme() + "\n" +"Pozicija: "+ sudar.getPozicija();
                tab.setText(sudar.getVrijeme());
                tab.setContent(new Label(detaljiOSudaru));
                tabPane.getTabs().add(tab);
            }
            scrollPane.setContent(tabPane);
        }
    }

    public void prikazDogadjaja() {
        new PregledDogadjajaSudara().pregledDogadjaja(this, new Stage());
    }

    //Labela, kojom signaliziramo pojavu novog dogadjaja: 
    public void postavljanjeLabeleOPojaviNovogDogadjaja(){
    	//Lista dogadjaja, sa listom koja cini detalje tog dogadjaja!
        List<List<String>> detaljiDogadjaja = new ArrayList<>();
        detaljiDogadjaja = UpravljanjeDogadjajima.procitajDogadjaje();
        
        if(detaljiDogadjaja.size() == 0){
        	labelaSudara.setText("PRAZNA LISTA DOGADJAJA!");
        }
        else{
            TabPane tabPane = new TabPane();
            //Prolazak kroz listu, uzimanje njene liste, i formiranje detalja za taj dogadjaj: 
            for (int k = 0; k < detaljiDogadjaja.size(); k++) {
                Tab tab = new Tab();
                List<String> sudar = detaljiDogadjaja.get(k);
                String detaljiSudara = new String();
                for(String s : sudar){
                	detaljiSudara = detaljiSudara + "" + s + "\n";
                }
                tab.setText(sudar.get(0));
                tab.setContent(new Label(detaljiSudara));
                tabPane.getTabs().add(tab);
            }
            scrollPane.setContent(tabPane);
        }
    }
}


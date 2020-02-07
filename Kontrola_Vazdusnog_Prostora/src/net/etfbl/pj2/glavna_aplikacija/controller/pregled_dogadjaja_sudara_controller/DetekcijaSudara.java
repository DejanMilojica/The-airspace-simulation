package net.etfbl.pj2.glavna_aplikacija.controller.pregled_dogadjaja_sudara_controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.konfiguracija.SerijalizacijaSudara;
import net.etfbl.pj2.logger.Logger_;

//Detekcija pojave nove datoteke, unutar ALERT direktorijuma!
public class DetekcijaSudara extends Thread {
	private String currentLast;
    private Logger_ loggerObjekat = new Logger_(this);
    public static String putanjaDoAlertDirektorijuma = DirektorijumiDatoteke.ALLFILES.getVrijednost() +File.separator + DirektorijumiDatoteke.ALERT.getVrijednost()+File.separator;
    
    public DetekcijaSudara() {
    	super();
    	File rootD = new File(DirektorijumiDatoteke.ALLFILES.getVrijednost());
    	if(!rootD.exists())  rootD.mkdir();
    	
    	File alertD = new File(putanjaDoAlertDirektorijuma);
    	if(!alertD.exists())  alertD.mkdir();
    }
    
    @Override
    public void run() {
        while(true){
            File dir = new File(this.putanjaDoAlertDirektorijuma);
            File[] allFiles = null;
            allFiles = dir.listFiles();
            if(allFiles.length <= 0 && currentLast == null){
            }
            if(allFiles.length>=1) {
                if(currentLast == null){
                    currentLast = allFiles[0].getAbsolutePath();
                }
                List<File> files = Arrays.asList(allFiles);
                files.sort((x,y)->{
                    BasicFileAttributes fileX = null;
                    BasicFileAttributes fileY = null;
                    try {
                        fileX = Files.readAttributes(Paths.get(x.getAbsolutePath()),BasicFileAttributes.class);
                        fileY = Files.readAttributes(Paths.get(y.getAbsolutePath()),BasicFileAttributes.class);
                    } catch (IOException e) {
                        this.loggerObjekat.log(e.getMessage(),e);
                    }
                    return fileY.creationTime()
                            .compareTo(fileX.creationTime());
                });
                BasicFileAttributes currentLastFile = null;
                BasicFileAttributes potentialLastFile = null;
                try {
                    currentLastFile = Files.readAttributes(Paths.get(currentLast),BasicFileAttributes.class);
                    potentialLastFile = Files.readAttributes(Paths.get(files.get(0).getAbsolutePath()),BasicFileAttributes.class);
                } catch (IOException e) {
                    this.loggerObjekat.log(e.getMessage(),e);
                }
                boolean greaterThan
                        = currentLastFile.creationTime()
                        .compareTo(potentialLastFile.creationTime()) < 0;
                if(greaterThan){
                    currentLast = files.get(0).getAbsolutePath();
                    try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(currentLast))){
                    	SerijalizacijaSudara cm = (SerijalizacijaSudara) ois.readObject();
                        Platform.runLater(()->{
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("SUDAR");
                            alert.setHeaderText(cm.getVrijeme());
                            alert.setContentText(cm.getDetalji() + "\n" + cm.getPozicija());
                            alert.showAndWait();
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        loggerObjekat.log(e.getMessage(), e);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        loggerObjekat.log(e.getMessage(), e);
                    }
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
                loggerObjekat.log(e.getMessage(), e);
            }
        }
    }
}

package net.etfbl.pj2.konfiguracija;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.glavna_aplikacija.controller.main_controller.MainController;
import net.etfbl.pj2.logger.Logger_;
import net.etfbl.pj2.model.letjelica.avion.vojni.VojniAvion;

/*Upravlja pojavom stranih vojnih aviona, kontrolise neprekidno da li se pojavio neki strani avion, te to dostavi Labeli na GUIu!*/ 
public class UpravljanjeDogadjajima extends Thread{
    	static MainController mainController;
		static Logger_ loggerObjekat = null;
	    static String trenutnaDatoteka = null;
	    
	    private static String putanjaDoEventDirektorijuma = DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator+DirektorijumiDatoteke.EVENTS.getVrijednost() + File.separator;

	    public UpravljanjeDogadjajima(MainController mainController){
	        this.mainController = mainController;
	    }

	    @Override
	    public void run(){
	        SimpleStringProperty labelValue = new SimpleStringProperty();
	        this.mainController.getLabelaZaPrikazPojaveNovogDogadjaja().textProperty().bind(labelValue);
	        
	        while(true){
	            File eventsDirektorijum = new File(putanjaDoEventDirektorijuma);
	            File[] allFiles = null;
	            allFiles = eventsDirektorijum.listFiles();
	            
	            //Ukoliko nemamo datoteka u Events folderu: 
	            if(allFiles.length <= 0 && trenutnaDatoteka == null){
	                Platform.runLater(()-> labelValue.set("Trenutno nema stranih letjelica!"));
	            }
	            
	            //Samo 1:
	            if(allFiles.length == 1){
	                trenutnaDatoteka = allFiles[0].getAbsolutePath();
	            }
	            
	            //Vise njih:
	            if(allFiles.length>=1) {
	                if(trenutnaDatoteka == null){
	                    trenutnaDatoteka = allFiles[0].getAbsolutePath();
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
	                    currentLastFile = Files.readAttributes(Paths.get(trenutnaDatoteka),BasicFileAttributes.class);
	                    potentialLastFile = Files.readAttributes(Paths.get(files.get(0).getAbsolutePath()),BasicFileAttributes.class);
	                } catch (IOException e) {
                        this.loggerObjekat.log(e.getMessage(),e);
	                }
	                boolean greaterThan
	                        = currentLastFile.creationTime()
	                        .compareTo(potentialLastFile.creationTime()) < 0;
	                if(greaterThan){
	                    trenutnaDatoteka = files.get(0).getAbsolutePath();
	                    Platform.runLater(()-> labelValue.set("Pojavio se strani avion!   " + new File(trenutnaDatoteka).getName()));
	                }
	            }
	            try {
	                Thread.sleep(250);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    public static void upisUFileStranogVojnogAviona(String va) {
			PrintWriter vojniAvion = null;	
			try {
					String datoteka = (new SimpleDateFormat("yy_MM_dd_hh_mm_ss")).format(new Date())+".txt";
					File  file = new File(putanjaDoEventDirektorijuma +datoteka); //Pravljenje DATOTEKE!
					if(!file.exists()) 
						file.createNewFile();
					 vojniAvion =  new PrintWriter(new BufferedWriter(new FileWriter(putanjaDoEventDirektorijuma +datoteka)));
/**/			vojniAvion.println(va);
					 
			} catch (IOException e) {
					loggerObjekat.log("Nepostojeca Putanja!", e);
					e.printStackTrace();
				}
				finally {
					vojniAvion.close();
				}
		}

	    public static List<List<String>> procitajDogadjaje() {
	        List<List<String>> retVal = new ArrayList<>();

	        File dir = new File(putanjaDoEventDirektorijuma);
	        File[] directoryListing = dir.listFiles();
	        if (directoryListing != null) {
	            for (File child : directoryListing) {
	                try {
	                    retVal.add(Files.readAllLines(Paths.get(child.getAbsolutePath())));
	                } catch (IOException e) {
	                    e.printStackTrace();
                        loggerObjekat.log(e.getMessage(),e);
	                }
	            }
	        }
	        return retVal;
	    }

}

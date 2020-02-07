package net.etfbl.pj2.konfiguracija;

import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.logger.Logger_;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

//	config.properties
public class ConfigSimulatorManager{
	//Dimenzije Matrice:
	public static int BROJ_REDOVA;
    public static int BROJ_KOLONA;
    public static int INTERVAL_KREIRANJA_LETJELICA;
    public static int ZABRANA_LETENJA;
    
    public static double SIRINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU;
    public static double VISINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU;

    private static Logger_ objekatLogger;
    private static Path putanjaDoConfigProperties = Paths.get(DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator+DirektorijumiDatoteke.CONFIGURATION.getVrijednost()+File.separator + DirektorijumiDatoteke.CONFIG_PROPERTIES.getVrijednost());

	public static void setZABRANA_LETENJA() {
		ZABRANA_LETENJA = 1;
	}

	static{
        byte[] properties = null;
        try {
        	File file = new File(putanjaDoConfigProperties.toString());
        	if(!file.exists())
        		file.mkdir();
        	
            properties = Files.readAllBytes(putanjaDoConfigProperties);
            //INICIJALIZACIJA:
            BROJ_REDOVA = BROJ_KOLONA = properties[0];
            INTERVAL_KREIRANJA_LETJELICA = properties[1];
            ZABRANA_LETENJA = properties[2];
//            if(properties.length >  3) {
//            	SIRINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU = properties[3];
//            	VISINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU = properties[4];
//            }
//            else {
            	SIRINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU = 40;
            	VISINA_OBJEKTA_PRI_GRAFICKOM_PRIKAZIVANJU = 40;
//            }
        } catch (IOException e) {
            e.printStackTrace();
            objekatLogger.log(e.getMessage(),e);
        }
     }
    
}


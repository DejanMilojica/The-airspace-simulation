package net.etfbl.pj2.konfiguracija;

import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.logger.Logger_;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

					/*radar.properties*/
public class ConfigRadarManager {
	public static int INTERVAL_AZURIRANJA_MAPE;
    private  static Logger_ objekatLogger ;
    private static Path putanjaDoRadarProperties = Paths.get(DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator+DirektorijumiDatoteke.CONFIGURATION.getVrijednost()+File.separator + DirektorijumiDatoteke.RADAR_PROPERTIES.getVrijednost());
	private static String rootDirektorijum = DirektorijumiDatoteke.ALLFILES.getVrijednost();
	private static String direktorijum = DirektorijumiDatoteke.CONFIGURATION.getVrijednost();
	private static String datoteka = DirektorijumiDatoteke.RADAR_PROPERTIES.getVrijednost();
	
		static{
		     RandomAccessFile aFile = null;
	        
	        try {
	        	File file = new File(putanjaDoRadarProperties.toString());
	        	if(!file.exists()) {
	        		file.mkdir();
	        	}
	        	else {
					aFile = new RandomAccessFile( rootDirektorijum+ File.separator  + direktorijum  + File.separator  +  datoteka , "rw");
					FileChannel  kanal = aFile.getChannel();
					INTERVAL_AZURIRANJA_MAPE = ucitajIntervalAzuriranja(kanal);
///**/			System.out.println("INTERVAL: " + ConfigRadarManager.INTERVAL_AZURIRANJA_MAPE);
	        	}
	        } catch (IOException e) {
	            e.printStackTrace();
	            objekatLogger.log(e.getMessage(),e);
	        }
	     }
		
		private static int ucitajIntervalAzuriranja(FileChannel kanal) {

			try {
				kanal.position(1);
				int broj1 = 0;
				ByteBuffer skladiste1 = ByteBuffer.allocate(4);
		
				if(kanal.read(skladiste1)!=-1) {
					broj1 = skladiste1.getInt(0);
					skladiste1.clear();
				}
				kanal.position(0);
				return broj1;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return 0;
		}
	    
	}
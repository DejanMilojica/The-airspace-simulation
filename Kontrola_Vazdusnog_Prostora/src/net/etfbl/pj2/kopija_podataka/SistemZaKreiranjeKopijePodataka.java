package net.etfbl.pj2.kopija_podataka;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.etfbl.pj2.logger.Logger_;
import net.etfbl.pj2.model.mapa.Mapa;

public class SistemZaKreiranjeKopijePodataka extends Thread {
//		private String folderZaCuvanje = "src\\";
		private  Logger_ loggerObjekat = new Logger_(this);
		private  List<String> fajloviZaCuvanje = new ArrayList<>();
		

		public SistemZaKreiranjeKopijePodataka() {
		}

		/*Svakih 60 sekundi pravi kopiju svih tekstualnih fajlova!*/
	    @Override
	    public synchronized void run() {
	        while(true){
	            try {
	                sleep(60_000);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	                loggerObjekat.log(e.getMessage(), e);
	            }
	            try{
	                String nazivZipFajla ="backup_" + (new SimpleDateFormat("yy_MM_dd_hh_mm")).format(new Date());
	                File dir = new File("allFiles");
	                String dirPutanja = dir.getAbsolutePath();

	                //Ucitavamo sve fajlove iz foldera, te njegovih podfoldera:
	                this.sviFajloviIzFoldera(dir);

	                File zipFile = new File(nazivZipFajla);


	                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
	                byte[] buffer = new byte[1024];
	                int len;
	                for(String filePath : this.fajloviZaCuvanje){
	                    ZipEntry ze = new ZipEntry(filePath);

	                    String filePathRelative = "allFiles\\" + filePath.substring(dirPutanja.length() + 1, filePath.length());
/**/	System.out.println(filePathRelative);
	                    zos.putNextEntry(ze);
	                    FileInputStream fis = new FileInputStream(filePathRelative);
	                    synchronized(fis) {
	                    	while((len = fis.read(buffer))> 0){
	                    		zos.write(buffer, 0 , len);
	                    	}
	                    }
	                    zos.closeEntry();
	                    fis.close();
	                }
	                zos.close();

	                this.fajloviZaCuvanje = null;
	                this.fajloviZaCuvanje = new ArrayList<>();
	            } catch (FileNotFoundException e) {
	            		this.loggerObjekat.log(e.getMessage(), e);
	            } catch (IOException e) {
		               this.loggerObjekat.log(e.getMessage(), e);
	            }
	        }
	    }

	    private void sviFajloviIzFoldera(File dir){
	        File[] files = dir.listFiles();
	        for(File file : files){
	            if(file.isDirectory())
	            	sviFajloviIzFoldera(file);
	            else{
	            	fajloviZaCuvanje.add(file.getAbsolutePath());
	            }
	        }
	    }
}

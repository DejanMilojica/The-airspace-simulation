package net.etfbl.pj2.logger;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;

public class Logger_ {
	private Logger logger;
	
	private static Handler handler;
	
	static {
		try {
			//Staticko kreiranje ROOT foldera: 
			File rootFile = new File(DirektorijumiDatoteke.ALLFILES.getVrijednost());
			if(!rootFile.exists())
				rootFile.mkdir();
			
			//Staticko kreiranje LOG foldera: 
			File logFile = new File(rootFile.getPath()+File.separator+DirektorijumiDatoteke.LOG.getVrijednost());
			if(!logFile.exists())
				logFile.mkdir();
			
			handler = new FileHandler(DirektorijumiDatoteke.ALLFILES.getVrijednost() + File.separator +DirektorijumiDatoteke.LOG.getVrijednost() + File.separator + "log.log");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public Logger_(Object klaseZaLogovanje) {
		logger = Logger.getLogger(klaseZaLogovanje.getClass().getName());
		logger.addHandler(handler);
		logger.setLevel(Level.ALL);
		handler.setLevel(Level.ALL);
	}
	
	public synchronized  void  log(String poruka, Throwable e) {
		logger.log(Level.SEVERE, poruka, e);
	}
}

package net.etfbl.pj2.radar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.glavna_aplikacija.main.Main;
import net.etfbl.pj2.konfiguracija.ConfigRadarManager;
import net.etfbl.pj2.konfiguracija.ConfigSimulatorManager;
import net.etfbl.pj2.konfiguracija.SerijalizacijaSudara;
import net.etfbl.pj2.konfiguracija.UpravljanjeDogadjajima;
import net.etfbl.pj2.logger.Logger_;
import net.etfbl.pj2.model.letjelica.Letjelica;
import net.etfbl.pj2.model.letjelica.avion.ProtivPozarniAvion;
import net.etfbl.pj2.model.letjelica.avion.PutnickiAvion;
import net.etfbl.pj2.model.letjelica.avion.TransportniAvion;
import net.etfbl.pj2.model.letjelica.avion.vojni.Bombarder;
import net.etfbl.pj2.model.letjelica.avion.vojni.Lovac;
import net.etfbl.pj2.model.letjelica.avion.vojni.VojniAvion;
import net.etfbl.pj2.model.letjelica.bespilotna_letjelica.BespilotnaLetjelica;
import net.etfbl.pj2.model.letjelica.helikopter.ProtivPozarniHelikopter;
import net.etfbl.pj2.model.letjelica.helikopter.PutnickiHelikopter;
import net.etfbl.pj2.model.letjelica.helikopter.TransportniHelikopter;
import net.etfbl.pj2.model.mapa.Mapa;
import net.etfbl.pj2.model.raketa.Raketa;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;
import net.etfbl.pj2.simulacija.Simulator;

public class Radar extends Thread{
	public static Mapa mapa;
	private RandomAccessFile aFile = null;
	private  FileChannel   kanal =null;
	//Spisak direktorjuma: 
	private String rootDirektorijum = DirektorijumiDatoteke.ALLFILES.getVrijednost();
	private String direktorijum = DirektorijumiDatoteke.CONFIGURATION.getVrijednost();
	private String direktorijumEvants = DirektorijumiDatoteke.EVENTS.getVrijednost();
	private String direktorijumAlert = DirektorijumiDatoteke.ALERT.getVrijednost();
	private String datoteka = DirektorijumiDatoteke.RADAR_PROPERTIES.getVrijednost();
	private String datotekaMapa = DirektorijumiDatoteke.MAPA_DATOTEKA.getVrijednost();
	private int  intervalAzuriranja;
	private PrintWriter out = null;
	private Logger_ loggerObjekat = new Logger_(this.getClass());

	private Letjelica detektovanaStranaLetjelica = new VojniAvion();
	
	public Radar(Mapa mapa) {
		this.mapa = mapa;
		/*Interval Pregleda Radara: */
		intervalAzuriranja = (1+(new Random()).nextInt(1))*1000;
		try {
			File  rootFile = new File(rootDirektorijum); 	//	Pravljenje ROOT direktorijuma!
			if(!rootFile.exists()) 
				rootFile.mkdir();
	out =  new PrintWriter(new BufferedWriter(new FileWriter(rootDirektorijum+File.separator+datotekaMapa)));
			
			File  file = new File(rootDirektorijum+ File.separator+direktorijum); 	//	Pravljenje direktorijuma!
			if(!file.exists()) 
				file.mkdir();
		

			File  file1 = new File(rootDirektorijum + File.separator+direktorijumEvants);  //	 Pravljenje direktorijuma!
			if(!file1.exists()) 
				file1.mkdir();

			aFile = new RandomAccessFile( rootDirektorijum+File.separator+direktorijum  + File.separator+ datoteka , "rw");
			kanal = aFile.getChannel();
			upisUKonfiguracioniFajl();
/**/			ConfigRadarManager.INTERVAL_AZURIRANJA_MAPE = this.intervalAzuriranja;
			this.start();
			
		} catch (IOException e ) {
			loggerObjekat.log("Onemoguceno kreiranje radara!", e);
		}
	}
	
	@Override
	public void run() {		
		while(!Main.isKRAJ()) {
			if(!Simulator.LOVCI_NA_STRANU_LETJELICU && Simulator.ZABRANA_KREIRANJA_LETJELICA)
				Simulator.sm.tryAcquire(2);   //Radar prvo sto uradi, pokusa da zauzme 2 resursa, koji su mu potrebni za 2 lovca na strani avion!
			//Azuriranje map.txt - a!
			try {
				this.cuvanjeObjekataUFile();
			} catch (IOException e) {
				e.printStackTrace();
				this.loggerObjekat.log(e.getMessage(), e);
			}
			try {
				sleep(this.intervalAzuriranja);
			} catch (InterruptedException e) {
				loggerObjekat.log(e.getMessage(), e);
			}
		}
		
			try {
				kanal.close();
//				out.close();
			} catch (IOException e) {
				loggerObjekat.log("Onemoguceno zatvaranje kanala!!", e);
			}
	}
	private void cuvanjeObjekataUFile() throws IOException {
		out =  new PrintWriter(new BufferedWriter(new FileWriter(rootDirektorijum+File.separator+datotekaMapa)));

		synchronized(mapa) {
			for(int i=0;i<ConfigSimulatorManager.BROJ_REDOVA;i++) 
				for(int j=0; j<ConfigSimulatorManager.BROJ_REDOVA; j++) {
					if(mapa.mapa[i][j].getElement()!=null) {
						if(mapa.mapa[i][j].getElement() instanceof Letjelica) {
							Letjelica letjelica = (Letjelica)mapa.mapa[i][j].getElement();				
							cuvanjeLetjelicaNaDatojPoziciji(letjelica,i,j);
						}
						else {
							Raketa raketa = (Raketa)mapa.mapa[i][j].getElement();
							out.println("Raketa: "+ " Domen: "+raketa.getDomen() + ", Visina: "+raketa.getVisina());
						}
					}
				}
		}
		out.close();
	}
	
	/*Na istoj poziciji, moze biti vise letjelica, u skladu sa tim, svaka letjelica ima pomocnu Letjelicu, sto je letjelica na toj poziciji, tako da je potrebno i nju upisati!*/
	private synchronized void cuvanjeLetjelicaNaDatojPoziciji(Letjelica letjelica,int i, int j) {
		String vrstaLetjelice = " ";  
		HashSet<Integer> visine = new HashSet<>(); //Smjestamo visine, i na taj nacin znamo da li je doslo do sudara!
		Letjelica prethodnaLetjelica = null;
		
	while(letjelica!=null){
		if(letjelica instanceof PutnickiAvion)  vrstaLetjelice = "Putnicki Avion";
		else if(letjelica instanceof TransportniAvion)  vrstaLetjelice = "Transportni Avion";
		else if(letjelica instanceof ProtivPozarniAvion)  vrstaLetjelice = "ProtivPozarni Avion";
		else if(letjelica instanceof PutnickiHelikopter)  vrstaLetjelice = "Putnicki Helikopter";
		else if(letjelica instanceof TransportniHelikopter)  vrstaLetjelice = "Transportni Helikopter";
		else if(letjelica instanceof ProtivPozarniHelikopter)  vrstaLetjelice = "ProtivPozarni Helikopter";
		else if(letjelica instanceof BespilotnaLetjelica)  vrstaLetjelice = "Bespilotna Letjelica";
		else if(letjelica instanceof VojniAvion) {
			VojniAvion va =  (VojniAvion)letjelica;
			//Ako se radi o Vojnoj Letjelici, provjeravamo da li je to SVA, jer ako jeste, dolazi do obavjestavanja SIMULATOR-a!
			if(va.isStraniVojniAvion()) {
				vrstaLetjelice = "Strani Vojni Avion";
				if(	detektovanaStranaLetjelica!=null && !detektovanaStranaLetjelica.equals(va)) { //Ukoliko radar prije nije detektovao datu letjelicu!
						//Prvo se kreira fajl, sa podacima!
						String podaciOSVA = "Strana Vojna Letjelica: "+va.toString() +",\nVrijeme Detekcije: "+((new SimpleDateFormat("dd/MM/yy  hh:mm:ss")).format(new Date()))
									+",\nPozicija: ( "+va.getX_pozicija()+" , "+va.getY_pozicija()+" )";
						UpravljanjeDogadjajima.upisUFileStranogVojnogAviona(podaciOSVA);
						this.obavjestavanjeSimulatora(va);
						detektovanaStranaLetjelica = va; 
				}
			}
			else {
				 if(letjelica instanceof Lovac) vrstaLetjelice = "Lovac";
				 else  if(letjelica instanceof Bombarder) vrstaLetjelice = "Bombarder";
			}
		}
		if(visine.add(letjelica.getVisina())) { //Ukoliko uspjesno doda novi clan, znaci da nema aviona na toj visini!
//				out.println(Vrsta Letjelice#Model#Id#Visina#Ostale_Karakteristike# Pozicija: ("+i+" , "+j+")");
					out.println(vrstaLetjelice+"#"+letjelica.getModel()+"#"+letjelica.getId()+"#"+letjelica.getVisina()+"#"+letjelica.getKarakteristike()+"#"+i+"#"+j);
			}
			else {
				System.out.println("SUDAR!");
				//Preusmjeravanje 1:
				prethodnaLetjelica.setPomocna_letjelica(letjelica.getPomocna_letjelica());
				
				/*Treba jos da unistimo letjelicu iz liste pomocnih letjelica!*/
				Letjelica pl = (Letjelica) mapa.mapa[i][j].getElement();
				Letjelica prLet = null;
				while( pl!=null && pl.getVisina() != letjelica.getVisina()) {
					prLet = pl;
					pl = pl.getPomocna_letjelica();
				}
				
				if(prLet!=null) {
					prLet.setPomocna_letjelica(pl.getPomocna_letjelica());
				}
				else { //Ukoliko nema prethodne letjelice, znaci da je prva u listi!
					mapa.mapa[i][j].setElement(pl.getPomocna_letjelica());
				}
				
				/*Kako je doslo do sudara, letjelice nestaju sa mape!*/
/**/		letjelica.setKraj(true);
/**/		pl.setKraj(true);

				//Detalji ; Vrijeme; Pozicija
				String vrijeme =  (new SimpleDateFormat("yy_MM_dd_hh_mm")).format(new Date());
				SerijalizacijaSudara ss = new SerijalizacijaSudara("Sudar unutar vazdusnog prostora!", vrijeme,i+","+j);
			}
			prethodnaLetjelica = letjelica;
			letjelica = letjelica.getPomocna_letjelica();
		}
	}
		
	private void obavjestavanjeSimulatora(VojniAvion va) {
		Simulator.LOVCI_NA_STRANU_LETJELICU = true;
		Simulator.STRANA_VOJNA_LETJELICA = va;
		Simulator.sm.release(2);
	}

	private void upisUKonfiguracioniFajl() {
		synchronized(kanal) {
			try {
				kanal.position(0);
				//Dimenzije matrice!
				upisObjekta( ConfigSimulatorManager.BROJ_REDOVA , (int) kanal.position(),1);
				//Interval azuriranja!
				upisObjekta( this.intervalAzuriranja , (int) kanal.position(),4);
			} catch (IOException e) {
				loggerObjekat.log(e.getMessage(), e);
			}
		}
	}
	
	private void upisObjekta(int objekat,int pozicija, int velicina) {
		try {
			kanal.position(pozicija);
			ByteBuffer baferByte = ByteBuffer.allocate(velicina);
			baferByte.clear();
			
			if(velicina == 1)
				baferByte.put((byte)objekat);
			else if(velicina == 4)
				baferByte.putInt(objekat);
			
			baferByte.flip();
			kanal.write(baferByte);
		} catch (IOException e) {
			this.loggerObjekat.log("Problem pozicioniranja!", e);
			loggerObjekat.log(e.getMessage(), e);
		}
	}
}

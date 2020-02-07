package net.etfbl.pj2.simulacija;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import net.etfbl.pj2.model.letjelica.Letjelica;
import net.etfbl.pj2.model.letjelica.avion.Avion;
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
import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.glavna_aplikacija.main.Main;
import net.etfbl.pj2.konfiguracija.ConfigSimulatorManager;
import net.etfbl.pj2.logger.Logger_;
public class Simulator extends Thread{
	
	private  Mapa mapa;
	private Random random = new Random();
	private RandomAccessFile aFile = null;
	private  FileChannel   kanal = null;
	private String rootDirektorijum = DirektorijumiDatoteke.ALLFILES.getVrijednost();
	private String direktorijum = DirektorijumiDatoteke.CONFIGURATION.getVrijednost();
	private String datoteka = DirektorijumiDatoteke.CONFIG_PROPERTIES.getVrijednost();
	
	private byte intervalKreiranjaObjekata;
	private Logger_ loggerObjekat = new Logger_(this.getClass());
	private int brojDomacihLovaca = 0;
	private CitanjeKonfiguracionogFajla ckfThread;

	public static  boolean ZABRANA_KREIRANJA_LETJELICA = false;
	public static boolean LOVCI_NA_STRANU_LETJELICU = false;
	public static VojniAvion STRANA_VOJNA_LETJELICA = null;
	public static Semaphore sm = new Semaphore(3);

	public Simulator(Mapa mapa) {
		this.mapa = mapa;
		File rootFile = new File(rootDirektorijum);
		if(!rootFile.exists())
			rootFile.mkdir();
		
		File  file = new File(rootDirektorijum + File.separator +direktorijum); //Pravljenje direktorijuma!
		if(!file.exists()) {
			file.mkdir();
		}
		
		try {
			aFile = new RandomAccessFile( rootDirektorijum+ File.separator  + direktorijum  + File.separator  +  datoteka , "rw");
			kanal = aFile.getChannel();
			 ckfThread = new CitanjeKonfiguracionogFajla(direktorijum+ File.separator +datoteka,sm , kanal);
			 ckfThread.setDaemon(true); //Postavljanje za demonsku nit!
			 ckfThread.start();
			 /*Upis pocetnog stanja promjenjivih, u konfiguracioni fajl!*/
			this.upisUKonfiguracioniFajl();
		} catch (FileNotFoundException e) {
			loggerObjekat.log("Nepostojeca Putanja!", e);
		}
		
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.start(); //Pokretanje Simulatora!
	}
	
	@Override
	public void run() {
		
		int i=0;
		while(!Main.isKRAJ()) {	//i++<10

			if(ZABRANA_KREIRANJA_LETJELICA) { //Blokiranje simulatora!
					Letjelica.PRINUDNO_NAPUSTANJE = true;
					try {
						sm.acquire(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}
			
			Letjelica civilnaLetjelica = null;
			int sekunde = random.nextInt(5)*1000;  // Pauza izmedju generisanja slucajnih letjelica!
			intervalKreiranjaObjekata = (byte) sekunde;
			
			//Kreiranje letjelice, te izbor strane za polijetanje!
			civilnaLetjelica = this.izborLetjeliceZaLetenje();
			if(LOVCI_NA_STRANU_LETJELICU && this.brojDomacihLovaca==1) sekunde = 0; //Kreiramo instantno i drugog LOVCA!
			
			//Postavljanje na mapu!
			synchronized(mapa) {
//				System.out.println("Postavljanje Letjelice: ("+civilnaLetjelica.getX_pozicija()+" , "+civilnaLetjelica.getY_pozicija()+") ");
				mapa.postaviLetjelicuRaketu(civilnaLetjelica);
///**/		mapa.ispisMape();
			}
			
			(new Thread(civilnaLetjelica)).start(); //Pokretanje civilne letjelice!
			try {
				Thread.sleep(sekunde);  //Pauza izmedju prelaska na slj. poziciju!
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//	}
		try {
			kanal.close();
		} catch (IOException e) {
			this.loggerObjekat.log("Onemoguceno zatvaranje kanala! ",e);
			e.printStackTrace();
		}
}
	
	private Letjelica izborLetjeliceZaLetenje() {
		Byte domaciVojniAvionFlag = 0, straniVojniAvionFlag = 0;
		int izborLetjelice = (new Random()).nextInt(6);
		
		try {
				if(LOVCI_NA_STRANU_LETJELICU && STRANA_VOJNA_LETJELICA!= null) { //Kreiranje domacih lovaca!
					//Kreiranje domaceg lovca!
					VojniAvion domacaVojnaLetjelica = new Lovac(GenerickeLetjelice.LOVAC);  
					int strana = 0;
					int x_pozicija = 0, y_pozicija = 0;
					domacaVojnaLetjelica.setStranaMapeOdKojePocinjeKretanje(strana = STRANA_VOJNA_LETJELICA.getStranaMapeOdKojePocinjeKretanje());
					int dodatniPomak = (brojDomacihLovaca==0 ? 1 : -1);
					int x_pozicijaSVL = STRANA_VOJNA_LETJELICA.getX_pozicija(); 
					int y_pozicijaSVL = STRANA_VOJNA_LETJELICA.getY_pozicija(); 
					switch(strana) {
					case 0:	{x_pozicija = x_pozicijaSVL +dodatniPomak; if(x_pozicija<0) x_pozicija = 0;	break;}
					case 1: {y_pozicija = ConfigSimulatorManager.BROJ_REDOVA-1; x_pozicija = x_pozicijaSVL+dodatniPomak;  if(x_pozicija<0) x_pozicija = 0; break;}
					case 2: {y_pozicija = y_pozicijaSVL+dodatniPomak; if(y_pozicija<0) y_pozicija = 0; break;}
					case 3: {x_pozicija = ConfigSimulatorManager.BROJ_REDOVA-1; y_pozicija = y_pozicijaSVL+dodatniPomak; if(y_pozicija<0) y_pozicija = 0; break;}
					}
					domacaVojnaLetjelica.setY_pozicija(y_pozicija);		
					domacaVojnaLetjelica.setX_pozicija(x_pozicija);
					brojDomacihLovaca++;
/**/					sm.release(2);
				if(this.brojDomacihLovaca == 2) {
					brojDomacihLovaca = 0;
/**/					sm.tryAcquire();
				}
				return domacaVojnaLetjelica;
				}
				
/**/		domaciVojniAvionFlag =	this.citanjeIzKonfiguracionogFajla(2);  //Domaci Vojni Avion!
/**/		straniVojniAvionFlag =	this.citanjeIzKonfiguracionogFajla(3); //Strani Vojni Avion!

				if(domaciVojniAvionFlag>0 || straniVojniAvionFlag>0) { //Kreiranje vojne letjelice!					
					VojniAvion vojnaLetjelica = null;
					if(domaciVojniAvionFlag>0)
						vojnaLetjelica = new Bombarder(GenerickeLetjelice.BOMBARDER);
					else
						vojnaLetjelica = new Bombarder(GenerickeLetjelice.STRANI_VOJNI_AVION);
					
					this.upisByteObjekta((byte) -1, 2);
					this.upisByteObjekta((byte) -1, 3);
			
					if(straniVojniAvionFlag>0) {
						//Kreirana je STRANA vojna letjelica!
						vojnaLetjelica.setStraniVojniAvion(true);
						ZABRANA_KREIRANJA_LETJELICA  = true;
/**/ 						sm.tryAcquire(1); //Pokusaj zauzimanja!
						}
					
					//Postavljanje pocetne strane objekta za letenje:
					this.postavljanjeStranePocetkaLetenja(vojnaLetjelica);
					return vojnaLetjelica;
				}
				else {
					Letjelica civilnaLetjelica = null;
					//Kreiranje civilne letjelice:
					switch(izborLetjelice) {
							case 0: civilnaLetjelica = new PutnickiAvion(GenerickeLetjelice.PUTNICKI_AVION); break;
							case 1:	civilnaLetjelica =  new TransportniAvion(GenerickeLetjelice.TRANSPORTNI_AVION); break; 
							case 2: civilnaLetjelica = new ProtivPozarniAvion(GenerickeLetjelice.PROTIVPOZARNI_AVION); break;
							case 3: civilnaLetjelica = new PutnickiHelikopter(GenerickeLetjelice.PUTNICKI_HELIKOPTER);break;
							case 4: civilnaLetjelica = new TransportniHelikopter(GenerickeLetjelice.TRANSPORTNI_HELIKOPTER); break;
							case 5: civilnaLetjelica = new ProtivPozarniHelikopter(GenerickeLetjelice.PROTIVPOZARNI_HELIKOPTER);break;
							case 6: civilnaLetjelica = new  BespilotnaLetjelica(GenerickeLetjelice.BESPILOTNA_LETJELICA); break;
					}
					this.postavljanjeStranePocetkaLetenja(civilnaLetjelica);
					return civilnaLetjelica;
				}
			} catch (IOException e) {
			e.printStackTrace();
			loggerObjekat.log(e.getMessage(), e);
		}
		return null;
	}
	
	private void postavljanjeStranePocetkaLetenja(Letjelica civilnaLetjelica) {
		int strana = random.nextInt(3); //Izbor jedne od 4 strane, od koje kreirana letjelica krece!
		int granica = ConfigSimulatorManager.BROJ_KOLONA - 1;
		int rendomBroj = random.nextInt(granica);

		civilnaLetjelica.setStranaMapeOdKojePocinjeKretanje(strana);
		switch(strana) {
			//Lijeva strana.
			case 0:{
				civilnaLetjelica.setY_pozicija(0);		
				civilnaLetjelica.setX_pozicija(rendomBroj);		
				break;
			}
			//Desna strana.
			case 1:{
				civilnaLetjelica.setY_pozicija(ConfigSimulatorManager.BROJ_REDOVA-1);		
				civilnaLetjelica.setX_pozicija(rendomBroj);
				break;
			}
			//Gornja strana.
			case 2:{ 
				civilnaLetjelica.setX_pozicija(0);		
				civilnaLetjelica.setY_pozicija(rendomBroj);
				break;
			}
			//Donja strana.
			case 3:{ 
				civilnaLetjelica.setX_pozicija(ConfigSimulatorManager.BROJ_REDOVA-1);		
				civilnaLetjelica.setY_pozicija(rendomBroj);
				break;
			}
		}
	}

	synchronized private void upisUKonfiguracioniFajl() {
		try {
			kanal.position(0);
			//Dimenzije matrice!
				upisByteObjekta((byte) mapa.getVELICINA_MAPE(), (int) kanal.position());
			//Interval kreiranja objekata!
				upisByteObjekta(this.intervalKreiranjaObjekata , (int) kanal.position());
			//Prisustvo DOMACEG vojnog objekta!
				upisByteObjekta((byte) -1 , (int) kanal.position());
			//Prisustvo STRANOG vojnog objekta!
				upisByteObjekta((byte) -1 , (int) kanal.position());
			//FLAG zabrane letenja!
				upisByteObjekta((byte) 0 , (int) kanal.position());
			//Sirina PANE objekta!
				upisByteObjekta((byte) 48 , (int) kanal.position());
			//Visina PANE objekta!
				upisByteObjekta((byte) 46 , (int) kanal.position());
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	synchronized private void upisByteObjekta(Byte objekat,int pozicija) {
		try {
			kanal.position(pozicija);
			ByteBuffer baferByte = ByteBuffer.allocate(1);
			baferByte.clear();
			baferByte.put(objekat);
			baferByte.flip();
			kanal.write(baferByte);
		} catch (IOException e) {
			this.loggerObjekat.log("Problem pozicicioniranja!", e);
			e.printStackTrace();
		}
	}
	
	synchronized private byte citanjeIzKonfiguracionogFajla(int pomjeraj) throws IOException {
		kanal.position(pomjeraj);
		Byte broj1 = 0;
		ByteBuffer skladiste1 = ByteBuffer.allocate(1);
		
		if(kanal.read(skladiste1)!=-1) {
			broj1 = skladiste1.get(0);
			skladiste1.clear();
		}
		kanal.position(0);
		return broj1;
	}
}

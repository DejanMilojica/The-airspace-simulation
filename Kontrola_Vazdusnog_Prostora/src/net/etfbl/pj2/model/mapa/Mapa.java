package net.etfbl.pj2.model.mapa;

import java.util.ArrayList;
import java.util.Random;

import net.etfbl.pj2.konfiguracija.ConfigSimulatorManager;
import net.etfbl.pj2.model.letjelica.Letjelica;
import net.etfbl.pj2.model.letjelica.avion.vojni.VojniAvion;
import net.etfbl.pj2.simulacija.Simulator;

public class Mapa { /*100 x 100*/
	private static int VELICINA_MAPE = 15;
	public static  Polje[][] mapa = new Polje[VELICINA_MAPE][VELICINA_MAPE];
	
	//Pomocne promjenjive:
	private static boolean zavrsenaOperacijaPomjeranja = true;
	private static boolean zavrsenaOperacijaPostavljanja = true;
	private Random generatorSlucajnogBroja = new Random();
	
	public Mapa() {
		for(int i=0;i<VELICINA_MAPE;i++)
			for(int j=0; j<VELICINA_MAPE; j++)
				mapa[i][j] = new Polje();
		System.out.println("Mapa spremna!");
		
/**/		this.VELICINA_MAPE = ConfigSimulatorManager.BROJ_KOLONA;
	}
	
	public void postaviLetjelicuRaketu(Element letjelicaRaketa) {
		if(letjelicaRaketa instanceof Letjelica) { //Radi se o Letjelici!
			Letjelica letjelica = (Letjelica)letjelicaRaketa;
			this.postaviLetjelicu(letjelica);
		}
		else { //Radi se o Raketi!
			System.out.println("Postavljanje Rakete!");
		}
	}
	
	//Postavljanje Letjelice: (Potencijalni sudar, jer se na toj poziciji vec nalazi letjelica: )
	public void postaviLetjelicu(Letjelica letjelica) {
			int y = letjelica.getY_pozicija();
			int x = letjelica.getX_pozicija();
			if (mapa[x][y].getElement() == null) { //Ukoliko je polje prazno, postavimo letjelicu!
				mapa[x][y].setElement(letjelica);
			}else {
				if(mapa[x][y].getElement() instanceof Letjelica) { 
					//Dalje, bez obzira da li se nalaze na istoj visini, ili ne, ide dodavanje nove letjelice, u listu pomocnih letjelica!
					Letjelica l = (Letjelica)mapa[x][y].getElement();
					
					//Ne Treba while petlja, da bi dosli do one pomocne letjelice, sto nema pomocne letjelice, jer imamo SWAP!
					if(l.getVisina() == letjelica.getVisina()){
						System.out.println("SUDAR(Postavljanje): "+l.getId()+" , "+letjelica.getId()); 

						mapa[x][y].setElement(letjelica);
						letjelica.setPomocna_letjelica(l);
						
					}
				}
			}
	}
	
	public static void pomjeriLetjelicu(Letjelica letjelica, int strana) {
		int x_pomak = 0 , y_pomak = 0;
		switch(strana) {
			// Lijeva Strana!
			case 0:		y_pomak = 1; 	break;
			//Desna Strana!		
			case 1: 	y_pomak = -1;	break;
			//Gornja Strana!
			case 2:		x_pomak = 1;		break;
			//Donja Strana!
			case 3:  	x_pomak = -1;	break;
		}
		if(!letjelica.isKraj()) {
//			pomocnaFunkcijaZaPomjeranjeLetjelice(letjelica, x_pomak, y_pomak);
			funkcijaZaPomjeranjeLetjelice(letjelica, x_pomak, y_pomak);
		}
	}
	
	private static synchronized void funkcijaZaPomjeranjeLetjelice(Letjelica letjelica, int pomjerajRed, int pomjerajKolona) {
/**/		Letjelica letjelica1 = letjelica.getPomocna_letjelica();
/**/		letjelica.setPomocna_letjelica(null);
		if(mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].getElement()!=null) {
			if(mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].getElement() instanceof Letjelica) {
				 letjelica1= (Letjelica)mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].getElement();
/**/			System.out.println("NEMA SUDARA!");				
					Letjelica pomocnaLetjelica = letjelica;
					Letjelica pomocnaLetjelica_1 = letjelica;
/**/			while((pomocnaLetjelica = pomocnaLetjelica.getPomocna_letjelica())!=null) pomocnaLetjelica_1 = pomocnaLetjelica;
					pomocnaLetjelica_1.setPomocna_letjelica(letjelica1);
/**/			letjelica1 =null;
			}
		}
		
		Letjelica pomocnaLetjelica = (Letjelica) mapa[letjelica.getX_pozicija()][letjelica.getY_pozicija()].getElement();
		if(pomocnaLetjelica !=null && !pomocnaLetjelica.equals(letjelica)) { 
/**/			letjelica1 = pomocnaLetjelica;
			Letjelica pomLetjelica = pomocnaLetjelica;
/**/	while((pomocnaLetjelica = pomocnaLetjelica.getPomocna_letjelica())!=null) {
				if(pomocnaLetjelica.equals(letjelica)) {
					pomLetjelica.setPomocna_letjelica(pomocnaLetjelica.getPomocna_letjelica());
					break;
				}
				pomLetjelica = pomocnaLetjelica;
			}
		}

		mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].setElement(letjelica);
		mapa[letjelica.getX_pozicija()][letjelica.getY_pozicija()].setElement(letjelica1);
		letjelica.setX_pozicija(letjelica.getX_pozicija()+pomjerajRed);
		letjelica.setY_pozicija(letjelica.getY_pozicija()+pomjerajKolona);

	}
	
	private static synchronized void pomocnaFunkcijaZaPomjeranjeLetjelice(Letjelica letjelica, int pomjerajRed, int pomjerajKolona) {
	if(!zavrsenaOperacijaPomjeranja) { //Blokiranje niti letjelica, usljed zauzeca promjenjive!
			try {
				letjelica.wait();
			} catch (InterruptedException e) {}
		}
/**/		zavrsenaOperacijaPomjeranja = false;
/**/		Letjelica letjelica1 = letjelica.getPomocna_letjelica();
/**/		letjelica.setPomocna_letjelica(null);
		if(mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].getElement()!=null) {
			if(mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].getElement() instanceof Letjelica) {
				 letjelica1= (Letjelica)mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].getElement();
				if(letjelica1.getVisina() == letjelica.getVisina()) {
					System.out.println("SUDAR(Let): "+letjelica1.getId()+" , "+letjelica.getId()); 
					/*Ukoliko dodje do sudara, sve letjelice se uklanjaju sa mape!*/
						Letjelica pomocnaLetjelica  =  letjelica1;
/**/				while((pomocnaLetjelica = pomocnaLetjelica.getPomocna_letjelica())!=null) pomocnaLetjelica.setKraj(true);
						letjelica1.setKraj(true); //Prekida se izvrsavanje leta letjelice!
						letjelica.setKraj(true);
/**/					mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].setElement(null);
/**/					mapa[letjelica.getX_pozicija()][letjelica.getY_pozicija()].setElement(null);
	
/**/ 				zavrsenaOperacijaPomjeranja = true;
/**/ 				letjelica.notify();
				return;
				}
				else { //Ukoliko do sudara ne dodje, jer se letjelice ne nalaze na istoj visini, tada referencu date letjelice, postavljamo na objekat trenutno zauzetog polja!
/**/			System.out.println("NEMA SUDARA!");				
					Letjelica pomocnaLetjelica = letjelica;
					Letjelica pomocnaLetjelica_1 = letjelica;
/**/			while((pomocnaLetjelica = pomocnaLetjelica.getPomocna_letjelica())!=null) pomocnaLetjelica_1 = pomocnaLetjelica;
					pomocnaLetjelica_1.setPomocna_letjelica(letjelica1);
/**/			letjelica1 =null;
				}
			}
		}
		
		Letjelica pomocnaLetjelica = (Letjelica) mapa[letjelica.getX_pozicija()][letjelica.getY_pozicija()].getElement();
		if(pomocnaLetjelica !=null && !pomocnaLetjelica.equals(letjelica)) { 
/**/			letjelica1 = pomocnaLetjelica;
			Letjelica pomLetjelica = pomocnaLetjelica;
/**/	while((pomocnaLetjelica = pomocnaLetjelica.getPomocna_letjelica())!=null) {
				if(pomocnaLetjelica.equals(letjelica)) {
					pomLetjelica.setPomocna_letjelica(pomocnaLetjelica.getPomocna_letjelica());
					break;
				}
				pomLetjelica = pomocnaLetjelica;
			}
		}

		mapa[letjelica.getX_pozicija()+pomjerajRed][letjelica.getY_pozicija()+pomjerajKolona].setElement(letjelica);
		mapa[letjelica.getX_pozicija()][letjelica.getY_pozicija()].setElement(letjelica1);
		letjelica.setX_pozicija(letjelica.getX_pozicija()+pomjerajRed);
		letjelica.setY_pozicija(letjelica.getY_pozicija()+pomjerajKolona);
		//Nakon sto se odradi pomijeranje, treba da na datom mjestu provjerimo da li ima aktivnih letjelica:
///**/		System.out.println("OSLOBADJANJE PROM.: "+letjelica);
/**/ 	zavrsenaOperacijaPomjeranja = true;
/**/ 	letjelica.notify();
	}

	
	public static synchronized void uklanjanjeLetjeliceZavrsetkomLetenja(Letjelica letjelica) {
			int x = letjelica.getX_pozicija();
			int y = letjelica.getY_pozicija();
			mapa[x][y].setElement(null);
			System.gc(); //Prijedlog JVM da pozove Garbage Collector!
	}

	public static int getVELICINA_MAPE() {
		return VELICINA_MAPE;
	}
}

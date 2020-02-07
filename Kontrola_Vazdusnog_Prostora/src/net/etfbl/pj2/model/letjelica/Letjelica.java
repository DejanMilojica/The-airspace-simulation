package net.etfbl.pj2.model.letjelica;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;
import java.util.SortedSet;

import net.etfbl.pj2.glavna_aplikacija.main.Main;
import net.etfbl.pj2.konfiguracija.ConfigSimulatorManager;
import net.etfbl.pj2.logger.Logger_;
import net.etfbl.pj2.model.letjelica.avion.vojni.VojniAvion;
import net.etfbl.pj2.model.mapa.Element;
import net.etfbl.pj2.model.mapa.Mapa;
import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.model.osoba.Putnik;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;
import net.etfbl.pj2.simulacija.Simulator;

public abstract class Letjelica extends Element  implements Runnable {
	protected int id;
	protected String model;
	protected int visina;
	protected String karakteristike;
//	protected  ArrayList<Karakteristike<Integer>> karakteristike; 
	protected ArrayList<Osoba> osobeULetjelici;
	protected Letjelica pomocna_letjelica = null;
	protected Logger_ loggerObjekat = new Logger_(this);

	private static int IDENTIFIKATOR = 0;
	private boolean kraj = false;
	private int x_pozicija = 0 , y_pozicija = 0;
	private final  int brzinaLeta =  (new Random().nextInt(2)+1) * 1000;
	private  int stranaMapeOdKojePocinjeKretanje; 
	private int  brojPomjeranja = ConfigSimulatorManager.BROJ_REDOVA - 1;
	
	public static boolean PRINUDNO_NAPUSTANJE = false;
	
	public Letjelica getPomocna_letjelica() {
		return pomocna_letjelica;
	}

	public void setPomocna_letjelica(Letjelica pomocna_letjelica) {
		this.pomocna_letjelica = pomocna_letjelica;
	}

	public Letjelica() {
		super();
//		karakteristike = new ArrayList<>();
		osobeULetjelici = null;
		this.id = (IDENTIFIKATOR++);
	}
	
	public Letjelica( int visina ) {
		this();
		this.visina = visina;
	}
	
	public Letjelica(String model, int visina, ArrayList<Osoba> osobeULetjelici) {
		this();
		this.model = model;
		this.visina = visina;
		this.osobeULetjelici = osobeULetjelici;
//		this.setKarakteristike();
	}
	
	public Letjelica(String model, int visina) {
		this();
		this.model = model;
		this.visina = visina;
//		this.setKarakteristike();
	}
	
	public Letjelica(String genLetjelica) {
		this();
		String[] splitovanaLetjelica = genLetjelica.split("#");
		this.model = splitovanaLetjelica[1];
		this.visina = Integer.parseInt(splitovanaLetjelica[2]);
		this.karakteristike = splitovanaLetjelica[3];	
	}

	public String  getKarakteristike() {
//		String str = "\nKarakteristike Letjelice: \n";
//		
//		for(Karakteristike<Integer> k : karakteristike)
//			str+=karakteristike;
//		return str;
		return karakteristike;
	}

//	public void setKarakteristike() {
//		System.out.println("Unos karakteristika za letjelicu: "+id+" (kljuc - vrijednost)");
//		System.out.print("Broj karakteristika: ");
//		int brojKarakteristika = (new Scanner(System.in)).nextInt();
//		for(int i=0;i<brojKarakteristika;i++) {
//			System.out.print("kljuc: ");
//			String kljuc =  (new Scanner(System.in)).nextLine();
//			System.out.print("vrijednost: ");
//			int vrijednost = (new Scanner(System.in)).nextInt();
//			karakteristike.add(new Karakteristike<Integer>(kljuc,vrijednost));
//		}
//	}
	
	public int getY_pozicija() {
		return y_pozicija;
	}

	public void setY_pozicija(int y_pozicija) {
		this.y_pozicija = y_pozicija;
	}
	
	public String getOsobe() {
		if(this.osobeULetjelici!=null) {
			String putnici = "\nPutnici: \n";
			String piloti = "\nPiloti: \n";
		
			for(Osoba osoba: osobeULetjelici) {
				if(osoba instanceof Putnik)
					putnici+=osoba;
				else
					piloti+=osoba;
			}
			return putnici+" "+piloti;
		}
		return "Nema osoba u letjelici!";
	}
	
	@Override
	public String toString() {
		return "Id: "+id+", Model: "+this.model + ", Visina Letenja:  "+visina + ", Brzina Letenja: "+brzinaLeta+this.getKarakteristike() + this.getOsobe();
	}
	
	@Override
	public synchronized void run() {
		int pozicija = 0;
		int preostaliBrojKoraka = -1;
		boolean usaoUBlok = false;
		
		while(!kraj && pozicija<brojPomjeranja && !Main.isKRAJ()){

			if((!(this instanceof VojniAvion) && PRINUDNO_NAPUSTANJE && !usaoUBlok) ||  (this instanceof VojniAvion && !Simulator.LOVCI_NA_STRANU_LETJELICU && PRINUDNO_NAPUSTANJE && !usaoUBlok && !((VojniAvion)this).isStraniVojniAvion())) {
				//Letjelice, koje nisu vojni avioni(Domaci Lovci na S.V.A.), napustaju sto prije Vazdusni Prostor!
					usaoUBlok = true; //Da ne bismo svaki put provjeravali, i postavljali izbor strane!
					preostaliBrojKoraka = this.postaviNovuStranu();
					brojPomjeranja = pozicija + preostaliBrojKoraka;
			}
			
			
			if(this instanceof VojniAvion && Simulator.LOVCI_NA_STRANU_LETJELICU ) {
				VojniAvion dva = (VojniAvion)this;
				if(!dva.isStraniVojniAvion() && Simulator.STRANA_VOJNA_LETJELICA!=null) { //Ukoliko se radi o domacem vojnom avionu, treba da obezbjedimo unistenje SVA-a!
					provjeraUnistenjaStranogVojnogAviona(Simulator.STRANA_VOJNA_LETJELICA);
					}
			}
			
			try {
				Thread.sleep(brzinaLeta);
			} catch (InterruptedException e) {
				e.printStackTrace();
				loggerObjekat.log(e.getMessage(), e);
			}
			if(preostaliBrojKoraka!=0)
				Mapa.pomjeriLetjelicu(this,this.getStranaMapeOdKojePocinjeKretanje());		
			pozicija++;
		}
		if(this instanceof VojniAvion ) { 
			VojniAvion va = (VojniAvion)this;
			if(va.isStraniVojniAvion()) {  //Nakon sto strani vojni avion zavrsi svoj let, dolazi do prestanka zabrane kreiranja letjelica!
				Simulator.ZABRANA_KREIRANJA_LETJELICA = false; 
				PRINUDNO_NAPUSTANJE = false;
/**/		Simulator.sm.release();
/**/		Simulator.sm.release();
				Simulator.STRANA_VOJNA_LETJELICA = null;
				Simulator.LOVCI_NA_STRANU_LETJELICU = false;
			}
		}
		Mapa.uklanjanjeLetjeliceZavrsetkomLetenja(this);
	}
	
	private void provjeraUnistenjaStranogVojnogAviona(VojniAvion sva) {
		int x_posDVA = this.getX_pozicija();
		int y_posDVA = this.getY_pozicija();
		int x_posSVA = sva.getX_pozicija();
		int y_posSVA = sva.getY_pozicija();
		int strana = sva.getStranaMapeOdKojePocinjeKretanje();
		
		switch(strana) {
		case 0:	if(y_posDVA == y_posSVA)  {sva.setKraj(true); System.out.println("Unistenje SVA-a!");break;}
		case 1: if(y_posDVA == y_posSVA)  {sva.setKraj(true); System.out.println("Unistenje SVA-a!");break;}
		case 2: if(x_posDVA == x_posSVA) {sva.setKraj(true); System.out.println("Unistenje SVA-a!");break;}
		case 3: if(x_posDVA == x_posSVA) {sva.setKraj(true); System.out.println("Unistenje SVA-a!");break;}
		}	
}

	private synchronized int postaviNovuStranu() {
		int array[] = {ConfigSimulatorManager.BROJ_REDOVA -1- this.y_pozicija, this.y_pozicija , ConfigSimulatorManager.BROJ_REDOVA -1- this.x_pozicija ,  this.x_pozicija };
		int[] pomArray = array.clone();
		Arrays.sort(array);
		int broj = array[0];
		for(int i=0;i<4;i++)
			if(broj==pomArray[i]) {
				this.setStranaMapeOdKojePocinjeKretanje(i);
			}
		return broj;
	}

	public void setBrojPomjeranja(int brojPomjeranja) {
		this.brojPomjeranja = brojPomjeranja;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Letjelica) {
			return this.id == ((Letjelica)obj).id;
		}
		return false;
    }

	public int getBrzinaLeta() {
		return brzinaLeta;
	}

	public int getVisina() {
		return visina;
	}

	public void setVisina(int visina) {
		this.visina = visina;
	}

	public int getX_pozicija() {
		return x_pozicija;
	}

	public void setX_pozicija(int x_pozicija) {
		this.x_pozicija = x_pozicija;
	}

	public int getStranaMapeOdKojePocinjeKretanje() {
		return stranaMapeOdKojePocinjeKretanje;
	}

	public void setStranaMapeOdKojePocinjeKretanje(int stranaMapeOdKojePocinjeKretanje) {
		this.stranaMapeOdKojePocinjeKretanje = stranaMapeOdKojePocinjeKretanje;
	}

	public void setKraj(boolean kraj) {
		this.kraj = kraj;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getId() {
		return id;
	}

	public boolean isKraj() {
		return kraj;
	}

class Karakteristike<T>{
	private String kljuc;
	private T vrijednost;

	public Karakteristike(String kljuc, T vrijednost) {
		this.kljuc = kljuc;
		this.vrijednost = vrijednost;
	}
	
	@Override
	public String toString(){
		return kljuc+" - "+vrijednost;
	}

	public String getKljuc() {
		return kljuc;
	}

	public void setKljuc(String kljuc) {
		this.kljuc = kljuc;
	}

	public T getVrijednost() {
		return vrijednost;
	}

	public void setVrijednost(T vrijednost) {
		this.vrijednost = vrijednost;
	}
	}
}

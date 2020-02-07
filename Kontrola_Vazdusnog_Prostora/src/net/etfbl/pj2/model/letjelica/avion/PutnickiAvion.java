package net.etfbl.pj2.model.letjelica.avion;

import java.util.ArrayList;

import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class PutnickiAvion extends Avion {
	private int brojSjedista;
	private int maksimalnaTezinaPrtljaga;
	
	public PutnickiAvion() {
		super();
	}
	
	public PutnickiAvion( int visina) {
		super(visina);
	}
	
	public PutnickiAvion(String model, int visina) {
		super(model,visina);
	}
	
	public PutnickiAvion(String model, int visina,int maksimalnaTezinaPrtljaga,int brojSjedista) {
		this(model,visina,maksimalnaTezinaPrtljaga,brojSjedista,null);
	}
	
	public PutnickiAvion(String model,  int visina,int maksimalnaTezinaPrtljaga,int brojSjedista,ArrayList<Osoba> osobeULetjelici) {
		super(model,visina,osobeULetjelici);
		this.maksimalnaTezinaPrtljaga = maksimalnaTezinaPrtljaga;
		this.brojSjedista = brojSjedista;
	}
	
	public PutnickiAvion(String genLetjelica) {
		super(genLetjelica);
	}

	public int getBrojSjedista() {
		return brojSjedista;
	}

	public void setBrojSjedista(int brojSjedista) {
		this.brojSjedista = brojSjedista;
	}

	public int getMaksimalnaTezinaPrtljaga() {
		return maksimalnaTezinaPrtljaga;
	}

	public void setMaksimalnaTezinaPrtljaga(int maksimalnaTezinaPrtljaga) {
		this.maksimalnaTezinaPrtljaga = maksimalnaTezinaPrtljaga;
	}
	
	@Override
	  public String toString() {
		  return " Putnicki Avion " + super.toString();
	  }
}

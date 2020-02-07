package net.etfbl.pj2.model.letjelica.helikopter;

import java.util.ArrayList;

import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class PutnickiHelikopter extends Helikopter {

	private int brojSjedista;
	
	public PutnickiHelikopter() {
		super();
	}
	
	public PutnickiHelikopter( int visina) {
		super(visina);
	}
	
	public PutnickiHelikopter(String model, int visina) {
		super(model,visina);
	}
	
	public PutnickiHelikopter(String model,  int visina,int brojSjedista) {
		this(model,visina,brojSjedista,null);
	}
	
	public PutnickiHelikopter(String model, int visina,int brojSjedista,ArrayList<Osoba> osobeULetjelici) {
		super(model,visina,osobeULetjelici);
		this.brojSjedista = brojSjedista;
	}
	
	public PutnickiHelikopter(String genLetjelica) {
		super(genLetjelica);
	}

	public int getBrojSjedista() {
		return brojSjedista;
	}

	public void setBrojSjedista(int brojSjedista) {
		this.brojSjedista = brojSjedista;
	}
	
	@Override
	public String toString() {
		return " Putnicki Helikopter "+super.toString();
	}
}

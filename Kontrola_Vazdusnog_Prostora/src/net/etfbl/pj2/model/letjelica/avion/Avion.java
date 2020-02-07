package net.etfbl.pj2.model.letjelica.avion;

import java.util.ArrayList;

import net.etfbl.pj2.model.letjelica.Letjelica;
import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public abstract class Avion extends Letjelica{
	
	public Avion() {
		super();
	}
	
	public Avion(int visina) {
		super(visina);
	}
	
	public Avion(String model, int visina) {
		super(model,visina);
	}
	
	public Avion(String model, int visina, ArrayList<Osoba> osobeULetjelici) {
		super(model,visina,osobeULetjelici);
	}
	
	public Avion(String genLetjelica) {
		super(genLetjelica);
	}
	
	@Override
	public String toString() {
		return super.toString();
	}
	
} 

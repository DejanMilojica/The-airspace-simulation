package net.etfbl.pj2.model.letjelica.helikopter;

import java.util.ArrayList;

import net.etfbl.pj2.model.letjelica.Letjelica;
import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public abstract class Helikopter  extends Letjelica{
	
	public Helikopter() {
		super();
	}
	
	public Helikopter(int visina) {
		super(visina);
	}
	
	public Helikopter(String model, int visina) {
		super(model,visina);
	}
	
	public Helikopter(String model, int visina, ArrayList<Osoba> osobeULetjelici) {
		super(model,visina,osobeULetjelici);
	}
	
	public Helikopter(String genLetjelica) {
		super(genLetjelica);
	}
	
}

package net.etfbl.pj2.model.letjelica.avion.vojni;

import java.util.ArrayList;

import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class Bombarder extends VojniAvion implements NapadNaCiljeveNaZemljiInterface {

	public Bombarder() {
		  super();
	  }
	  
	  public Bombarder(int visina) {
		  super(visina);
	  }
	  
	  public Bombarder(String model , int visina) {
		  super(model,visina);
	  }
	  
	  public Bombarder(String model, int visina, ArrayList<Osoba> osobeULetjelici) {
			super(model, visina, osobeULetjelici);
		}
	  
	  public Bombarder(String genLetjelica) {
			super(genLetjelica);
		}
	  
	  @Override
	  public String toString() {
		  return " Bombarder "+super.toString();
	  }
}

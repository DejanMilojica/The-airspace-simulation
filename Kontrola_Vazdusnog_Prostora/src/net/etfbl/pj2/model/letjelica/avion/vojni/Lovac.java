package net.etfbl.pj2.model.letjelica.avion.vojni;

import java.util.ArrayList;

import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class Lovac extends VojniAvion implements NapadNaCiljeveNaZemljiInterface,NapadNaCiljeveUVazduhuInterface{
	
	public Lovac() {
		  super();
	  }
	  
	  public Lovac(int visina) {
		  super(visina);
	  }
	  
	  public Lovac(String model , int visina) {
		  super(model,visina);
	  }
	  
	  public Lovac(String model, int visina, ArrayList<Osoba> osobeULetjelici) {
			super(model, visina, osobeULetjelici);
		}
	  
	  public Lovac(String genLetjelica) {
			super(genLetjelica);
		}
	  
	  @Override
	  public String toString() {
		  return " Lovac "+super.toString();
	  }
}

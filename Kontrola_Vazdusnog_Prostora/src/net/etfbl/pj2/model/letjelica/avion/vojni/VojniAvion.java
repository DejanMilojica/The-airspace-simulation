package net.etfbl.pj2.model.letjelica.avion.vojni;

import java.util.ArrayList;

import net.etfbl.pj2.model.letjelica.avion.Avion;
import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public  class VojniAvion extends Avion implements NosenjeNaoruzanjaInterface , NapadNaCiljeveInterface {
  protected boolean straniVojniAvion;
  
  public VojniAvion() {
	  super();
	  straniVojniAvion = false;
  }
  
  public VojniAvion(int visina) {
	  super(visina);
	  straniVojniAvion = false;	  
  }
  
  public VojniAvion(String model , int visina) {
	  super(model,visina);
	  straniVojniAvion = false;	  
  }
  
  public VojniAvion(String model, int visina, ArrayList<Osoba> osobeULetjelici) {
		super(model, visina, osobeULetjelici);
		  straniVojniAvion = false;	  		
	}
  
  public VojniAvion(String genLetjelica) {
		super(genLetjelica);
		straniVojniAvion = false;	  
	}

public boolean isStraniVojniAvion() {
	return straniVojniAvion;
}

public void setStraniVojniAvion(boolean straniVojniAvion) {
	this.straniVojniAvion = straniVojniAvion;
}

@Override
public String toString() {
	if(!straniVojniAvion)
		return "DVA "; //Domaci Vojni Avion.
	return "SVA "; //Strani Vojni Avion.
}
   
}

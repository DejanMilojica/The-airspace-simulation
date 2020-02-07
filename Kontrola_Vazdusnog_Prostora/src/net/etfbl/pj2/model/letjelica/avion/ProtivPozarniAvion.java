package net.etfbl.pj2.model.letjelica.avion;

import java.util.ArrayList;

import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class ProtivPozarniAvion extends Avion implements GasiPozarInterface{
	private int kolicinaVodeZaGasenje;
	
	public ProtivPozarniAvion() {
		super();
	}
	
	public ProtivPozarniAvion( int visina) {
		super(visina);
	}
	
	public ProtivPozarniAvion(String model, int visina) {
		super(model,visina);
	}
	
	public ProtivPozarniAvion(String model, int visina,int kolicinaVodeZaGasenje) {
		super(model,visina);
		this.kolicinaVodeZaGasenje = kolicinaVodeZaGasenje;
	}
	
	public ProtivPozarniAvion(String genLetjelica) {
		super(genLetjelica);
	}
	
	@Override
	  public String toString() {
		  return " ProtivPozarni Avion " + super.toString();
	  }

	public int getKolicinaVodeZaGasenje() {
		return kolicinaVodeZaGasenje;
	}

	public void setKolicinaVodeZaGasenje(int kolicinaVodeZaGasenje) {
		this.kolicinaVodeZaGasenje = kolicinaVodeZaGasenje;
	}
}

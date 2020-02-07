package net.etfbl.pj2.model.letjelica.helikopter;

import net.etfbl.pj2.model.letjelica.avion.GasiPozarInterface;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class ProtivPozarniHelikopter extends Helikopter implements GasiPozarInterface{
	private int kolicinaVodeZaGasenje;
	
	public ProtivPozarniHelikopter() {
		super();
	}
	
	public ProtivPozarniHelikopter( int visina) {
		super(visina);
	}
	
	public ProtivPozarniHelikopter(String model, int visina) {
		super(model,visina);
	}
	
	public ProtivPozarniHelikopter(String model, int visina,int kolicinaVodeZaGasenje) {
		super(model,visina);
		this.kolicinaVodeZaGasenje = kolicinaVodeZaGasenje;
	}
	
	public ProtivPozarniHelikopter(String genLetjelica) {
		super(genLetjelica);
	}

	public int getKolicinaVodeZaGasenje() {
		return kolicinaVodeZaGasenje;
	}

	public void setKolicinaVodeZaGasenje(int kolicinaVodeZaGasenje) {
		this.kolicinaVodeZaGasenje = kolicinaVodeZaGasenje;
	}
	
	@Override
	public String toString() {
		return " ProtivPozarni Helikopter "+super.toString();
	}

}

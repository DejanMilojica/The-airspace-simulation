package net.etfbl.pj2.model.letjelica.helikopter;

import net.etfbl.pj2.model.letjelica.avion.PrevoziTeretInterface;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class TransportniHelikopter extends Helikopter  implements PrevoziTeretInterface{
private int maksimalnaTezinaTeretaKojiSePrevozi; 
	
	public TransportniHelikopter() {
		super();
	}
	
	public TransportniHelikopter( int visina) {
		super(visina);
	}
	
	public TransportniHelikopter(String model, int visina) {
		super(model,visina);
	}
	
	public TransportniHelikopter(String model, int visina,int maksimalnaTezinaTeretaKojiSePrevozi) {
		super(model,visina);
		this.maksimalnaTezinaTeretaKojiSePrevozi = maksimalnaTezinaTeretaKojiSePrevozi;
	}
	
	public TransportniHelikopter(String genLetjelica) {
		super(genLetjelica);
	}

	public int getMaksimalnaTezinaTeretaKojiSePrevozi() {
		return maksimalnaTezinaTeretaKojiSePrevozi;
	}

	public void setMaksimalnaTezinaTeretaKojiSePrevozi(int maksimalnaTezinaTeretaKojiSePrevozi) {
		this.maksimalnaTezinaTeretaKojiSePrevozi = maksimalnaTezinaTeretaKojiSePrevozi;
	}
	
	@Override
	public String toString() {
		return " Transpotni Helikopter"+super.toString();
	}
	
}

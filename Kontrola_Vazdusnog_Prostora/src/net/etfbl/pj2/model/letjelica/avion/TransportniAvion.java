package net.etfbl.pj2.model.letjelica.avion;

import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class TransportniAvion extends Avion implements PrevoziTeretInterface {
	private int maksimalnaTezinaTeretaKojiSePrevozi; 
	
	public TransportniAvion() {
		super();
	}
	
	public TransportniAvion( int visina) {
		super(visina);
	}
	
	public TransportniAvion(String model, int visina) {
		super(model,visina);
	}
	
	public TransportniAvion(String model,  int visina,int maksimalnaTezinaTeretaKojiSePrevozi) {
		super(model,visina);
		this.maksimalnaTezinaTeretaKojiSePrevozi = maksimalnaTezinaTeretaKojiSePrevozi;
	}

	public TransportniAvion( String genLetjelica) {
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
		  return " Transportni Avion " + super.toString();
	  }
}

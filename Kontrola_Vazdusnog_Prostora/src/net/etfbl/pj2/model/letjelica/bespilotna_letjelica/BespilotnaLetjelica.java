package net.etfbl.pj2.model.letjelica.bespilotna_letjelica;

import net.etfbl.pj2.model.letjelica.Letjelica;
import net.etfbl.pj2.simulacija.GenerickeLetjelice;

public class BespilotnaLetjelica extends Letjelica implements SnimanjeTerenaInterface {
	
	public BespilotnaLetjelica() {
		super();
	}
	
	public BespilotnaLetjelica( int visina) {
		super(visina);
	}
	
	public BespilotnaLetjelica(String model, int visina) {
		super(model,visina);
	}
	
	public BespilotnaLetjelica(String genLetjelica) {
		super(genLetjelica);
	}
	
	@Override
	public String toString() {
		return " Bespilotna Letjelica"+super.toString();
	}
}

package net.etfbl.pj2.model.osoba;

public class Pilot extends Osoba {
	private int licencaZaLetenje;
	
	public Pilot() {
		super("pp","pp");
		this.licencaZaLetenje = 0;
	}
	
	public Pilot(String ime, String prezime, int licencaZaLetenje) {
		super(ime,prezime);
		this.licencaZaLetenje = licencaZaLetenje;
	}

	public int getLicencaZaLetenje() {
		return licencaZaLetenje;
	}

	public void setLicencaZaLetenje(int licencaZaLetenje) {
		this.licencaZaLetenje = licencaZaLetenje;
	}
	
	@Override
	public String toString() {
		return super.toString() + "\tBroj Licence: " + this.licencaZaLetenje;
	}
	
}

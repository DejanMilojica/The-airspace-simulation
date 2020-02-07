package net.etfbl.pj2.model.osoba;

public class Putnik extends Osoba {
	private int brojPasosa;
	
	public Putnik() {
		super("nn","nn");
		this.brojPasosa = 0;
	}
	
	public Putnik(String ime, String prezime, int brojPasosa) {
		super(ime,prezime);
		this.brojPasosa = brojPasosa;
	}

	public int getBrojPasosa() {
		return brojPasosa;
	}

	public void setBrojPasosa(int brojPasosa) {
		this.brojPasosa = brojPasosa;
	}

	@Override
	public String toString() {
		return super.toString() + "\tBroj Pasosa: " + this.brojPasosa;
	}
}

package net.etfbl.pj2.model.osoba;

public abstract class Osoba {
	    private String ime;
	    private String prezime;
	   
	    public Osoba() {
	    	super();
	    }

	    public Osoba(String ime, String prezime) {
	        this.ime = ime;
	        this.prezime = prezime;
	    }

	    public String getIme() {
	        return ime;
	    }

	    public void setIme(String ime) {
	        this.ime = ime;
	    }

	    public String getPrezime() {
	        return prezime;
	    }

	    public void setPrezime(String prezime) {
	        this.prezime = prezime;
	    }
	    
	    @Override
		public String toString() {
			return "\tIme: " + ime + "\n\tPrezime: " + prezime+"\n";
		}

}

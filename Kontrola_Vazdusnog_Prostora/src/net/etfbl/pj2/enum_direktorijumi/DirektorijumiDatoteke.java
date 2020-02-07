package net.etfbl.pj2.enum_direktorijumi;

public enum DirektorijumiDatoteke {
	ALLFILES ("allFiles"),
	CONFIGURATION("configuration"),
	EVENTS("evants"),
	ALERT("alert"),
	MAPA_DATOTEKA("mapa.txt"),
	RADAR_PROPERTIES("radar.properties"),
	CONFIG_PROPERTIES("config.properties"),
	LOG("log"),
	GENERICKE_LETJELICE("genericke_letjelice.txt");
	
	 private String vrijednost;
	 
	  DirektorijumiDatoteke(String vrijednost) {
		 this.vrijednost = vrijednost;
	 }
	  
	  public String getVrijednost() {
		  return vrijednost;
	  }
}

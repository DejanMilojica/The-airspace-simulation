package net.etfbl.pj2.glavna_aplikacija.util;

import javafx.scene.paint.Color;

public enum PredstavljanjeLetjelicaTekstomiBojom {

		TRANSPORTNI_AVION ("TA",Color.RED),
		TRANSPORTNI_HELIKOPTER ("TH",Color.BLUE),
		BESPILOTNA_LETJELICA ("BL",Color.DARKMAGENTA),
		PROTIV_POZARNI_AVION ("PPA",Color.CRIMSON),
		PUTNICKI_AVION ("PA",Color.BROWN),
		PUTNICKI_HELIKOPTER ("PH",Color.LIGHTGREEN),
		PROTIV_POZARNI_HELIKOPTER ("PPH",Color.DARKBLUE),
		BOMBARDER ("B",Color.GREEN),
		LOVAC("L",Color.GREEN),
    
		STRANA_VOJNA_LETJELICA ( "SVA",Color.DARKORANGE);
	
	private String vrijednost;
	private Color boja;
	
	PredstavljanjeLetjelicaTekstomiBojom(String vrijednost, Color boja){
		this.vrijednost = vrijednost;
		this.boja = boja;
	}
	
	public String getVrijednost() {
		return vrijednost;
	}
	
	public Color getBoja() {
		return boja;
	}
}

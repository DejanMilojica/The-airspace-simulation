package net.etfbl.pj2.main;

/**
 * @authot Dejan Milojica
 * @category Java Projekat, Kontrola Vazdusnog saobracaja!
 * @version 
 */

import java.util.ArrayList;

import javafx.application.Application;
import net.etfbl.pj2.konfiguracija.ConfigSimulatorManager;
import net.etfbl.pj2.kopija_podataka.SistemZaKreiranjeKopijePodataka;
import net.etfbl.pj2.model.mapa.Mapa;
import net.etfbl.pj2.model.osoba.Osoba;
import net.etfbl.pj2.model.osoba.Pilot;
import net.etfbl.pj2.model.osoba.Putnik;
import net.etfbl.pj2.radar.Radar;
import net.etfbl.pj2.simulacija.Simulator;
import net.etfbl.pj2.glavna_aplikacija.controller.uvod.Uvod;
import net.etfbl.pj2.glavna_aplikacija.main.Main;

public class App {
	
	public static void main(String[] args) {

        Application.launch(Uvod.class,args); 

//		Mapa mapa = new Mapa();								  //	M A P A !
//		Simulator simulator = new Simulator(mapa);	 //	S I M U L A T O R !
//		Radar radar = new Radar(mapa);						//	R A D A R !
//		SistemZaKreiranjeKopijePodataka szkkp = new SistemZaKreiranjeKopijePodataka();
//		szkkp.setDaemon(true);
//		szkkp.start();
		//Pokretanje Graficke Reprezentacije:
//        Application.launch(Main.class,args); 

	}
}

package net.etfbl.pj2.model.osoba;

import java.util.ArrayList;

import net.etfbl.pj2.model.letjelica.Letjelica;

public class App {
	public static void main(String[] args) {
		Putnik putnik = new Putnik();
		Pilot pilot = new Pilot();
		
		System.out.println(putnik);
		System.out.println(pilot);
		ArrayList<Osoba> osobe = new ArrayList<>();
		osobe.add(pilot);
		osobe.add(putnik);
//		Letjelica letjelica = new Letjelica("Mig",121,3000,600,osobe);
//		System.out.println(letjelica);
	}
}

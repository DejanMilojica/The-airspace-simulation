package net.etfbl.pj2.model.raketa;

import java.util.Random;

import net.etfbl.pj2.model.mapa.Element;

public abstract  class Raketa extends Element  implements Runnable{
	protected int domen;
	protected int visina;
	
	private int pozicija = 0;
	private int brzinaLetenja = (new Random().nextInt(3)) * 1000;
	
	public Raketa() {
		super();
	}
	
	public Raketa(int domen, int visina) {
		super();
		this.domen = domen;
		this.visina = visina;
	}
	
	@Override
	public void run() {
		
	}

	public int getDomen() {
		return domen;
	}

	public void setDomen(int domen) {
		this.domen = domen;
	}

	public int getVisina() {
		return visina;
	}

	public void setVisina(int visina) {
		this.visina = visina;
	}
	
}

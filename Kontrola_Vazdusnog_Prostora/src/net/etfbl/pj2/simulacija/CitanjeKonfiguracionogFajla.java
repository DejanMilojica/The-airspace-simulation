package net.etfbl.pj2.simulacija;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.Semaphore;

import net.etfbl.pj2.model.letjelica.Letjelica;

//Provjera Flega: 
public class CitanjeKonfiguracionogFajla extends Thread{
	private String putanja; 
	private Semaphore sm;
	private  FileChannel  kanal =null;

	public CitanjeKonfiguracionogFajla(String putanja, Semaphore sm, FileChannel  kanal) {
		this.putanja = putanja;
		this.sm = sm;
		this.kanal = kanal;
	}
	
	@Override
	public void run() {
		int flag = 0;
		boolean flagPostavljen = false;
		
		while(true) {// Jer je demonska nit!    		!Simulator.isKRAJ()
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			flag = this.citanjeIzKonfiguracionogFajla(4);
			if(flag!=0) { //Flag, kojim definisemo zabranu letenja!
				flagPostavljen = true;
				Simulator.ZABRANA_KREIRANJA_LETJELICA = true; //Imamo zabranu!
				sm.tryAcquire(2);
			}
			if(flag==0 && flagPostavljen){
				flagPostavljen = false;
				Simulator.ZABRANA_KREIRANJA_LETJELICA = false;
				Letjelica.PRINUDNO_NAPUSTANJE = false;
				sm.release(2);
			}
		}
	}
	
	 private byte citanjeIzKonfiguracionogFajla(int pomjeraj){
		synchronized(kanal) {	
			if(kanal.isOpen()) {
				try {
					kanal.position(pomjeraj);
					Byte broj1 = 0;
					ByteBuffer skladiste1 = ByteBuffer.allocate(1);
		
					if(kanal.read(skladiste1)!=-1) {
						broj1 = skladiste1.get(0);
						skladiste1.clear();
					}
					kanal.position(0);
					return broj1;
				} catch (IOException e) {
					e.printStackTrace();
				}
				return 0;
			}
		}
		return 0;
	 }
	 
}

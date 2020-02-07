package net.etfbl.pj2.konfiguracija;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;
import net.etfbl.pj2.glavna_aplikacija.controller.pregled_dogadjaja_sudara_controller.DetekcijaSudara;

public class SerijalizacijaSudara implements Serializable {
	private String vrijeme;
	private String detalji;
	private String pozicija;
	
	public SerijalizacijaSudara(String detalji, String vrijeme, String pozicija) {
		this.vrijeme = vrijeme;
		this.detalji = detalji;
		this.pozicija = pozicija;
		
		String putanja =DirektorijumiDatoteke.ALLFILES.getVrijednost() + File.separator + DirektorijumiDatoteke.ALERT.getVrijednost() + File.separator +vrijeme +".ser";
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(putanja));
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getVrijeme() {
		return vrijeme;
	}

	public void setVrijeme(String vrijeme) {
		this.vrijeme = vrijeme;
	}

	public String getDetalji() {
		return detalji;
	}

	public void setDetalji(String detalji) {
		this.detalji = detalji;
	}

	public String getPozicija() {
		return pozicija;
	}

	public void setPozicija(String pozicija) {
		this.pozicija = pozicija;
	}
	
	public static List<SerijalizacijaSudara> procitajSudare() {
        List<SerijalizacijaSudara> retVal = new ArrayList<>();
        File dir = new File(DetekcijaSudara.putanjaDoAlertDirektorijuma);
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (File child : directoryListing) {
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(child.getAbsolutePath()));
                    SerijalizacijaSudara cm = (SerijalizacijaSudara) ois.readObject();
                    ois.close();
                    retVal.add(cm);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return retVal;
    }

}

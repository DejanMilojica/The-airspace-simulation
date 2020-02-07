package net.etfbl.pj2.simulacija;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import net.etfbl.pj2.logger.Logger_;
import net.etfbl.pj2.enum_direktorijumi.DirektorijumiDatoteke;

public class GenerickeLetjelice {
	
    public static String PUTNICKI_AVION;
    public static String PUTNICKI_HELIKOPTER;
	public static String TRANSPORTNI_AVION;
    public static String TRANSPORTNI_HELIKOPTER;
    public static String LOVAC;
    public static String STRANI_VOJNI_AVION;
    public static String BESPILOTNA_LETJELICA;
    public static String PROTIVPOZARNI_AVION;
    public static String PROTIVPOZARNI_HELIKOPTER;
    public static String BOMBARDER;
    private static Logger_ loggerObjekat;
    
    static
    {
        List<String> karakteristikeLetjelica = null;
        try {
        	karakteristikeLetjelica = Files.readAllLines(Paths.get(DirektorijumiDatoteke.ALLFILES.getVrijednost()+File.separator+ DirektorijumiDatoteke.GENERICKE_LETJELICE.getVrijednost()));
        } catch (IOException e) {
            e.printStackTrace();
            loggerObjekat.log(e.getMessage(),e);
        }
        
        TRANSPORTNI_AVION = karakteristikeLetjelica.stream().filter(x->x.startsWith("Transportni Avion")).collect(Collectors.toList()).get(0);
        TRANSPORTNI_HELIKOPTER = karakteristikeLetjelica.stream().filter(x->x.startsWith("Transportni Helikopter")).collect(Collectors.toList()).get(0);
        LOVAC = karakteristikeLetjelica.stream().filter(x->x.startsWith("Lovac")).collect(Collectors.toList()).get(0);
        STRANI_VOJNI_AVION = karakteristikeLetjelica.stream().filter(x->x.startsWith("Strani Vojni Avion")).collect(Collectors.toList()).get(0);
        BESPILOTNA_LETJELICA = karakteristikeLetjelica.stream().filter(x->x.startsWith("Bespilotna Letjelica")).collect(Collectors.toList()).get(0);
        PROTIVPOZARNI_AVION = karakteristikeLetjelica.stream().filter(x->x.startsWith("ProtivPozarni Avion")).collect(Collectors.toList()).get(0);
        PUTNICKI_AVION = karakteristikeLetjelica.stream().filter(x->x.startsWith("Putnicki Avion")).collect(Collectors.toList()).get(0);
        PUTNICKI_HELIKOPTER = karakteristikeLetjelica.stream().filter(x->x.startsWith("Putnicki Helikopter")).collect(Collectors.toList()).get(0);
        PROTIVPOZARNI_HELIKOPTER = karakteristikeLetjelica.stream().filter(x->x.startsWith("Protiv Pozarni Helikopter")).collect(Collectors.toList()).get(0);
        BOMBARDER = karakteristikeLetjelica.stream().filter(x->x.startsWith("Bombarder")).collect(Collectors.toList()).get(0);
    }
}

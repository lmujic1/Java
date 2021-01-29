package ba.unsa.etf.rpr;

public class Artikl {
    private String naziv, kod;
    private int cijena;
    Artikl () {
        naziv="";
        cijena=0;
        kod="";
    }
    Artikl ( String naziv,  int cijena, String kod ) {
        this.naziv = naziv;
        this.kod = kod;
        this.cijena = cijena;
    }
    public String getNaziv() {
        return naziv;
    }
    public String getKod() {
        return  kod;
    }
    public int getCijena() {
        return  cijena;
    }

}

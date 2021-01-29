package ba.unsa.etf.rpr;

public class Supermarket {
    private Artikl[] artikli;
    private int brojArtikala;
    public Supermarket() {
        artikli=new Artikl[1000];
        brojArtikala=0;
    }
    public Artikl[] getArtikli() {
        return  artikli;
    }

    public int getBrojArtikala() { return  brojArtikala; }

    public Artikl izbaciArtiklSaKodom(String kod) throws IllegalAccessException {
        if(this.brojArtikala==0) throw new IllegalAccessException("Prazan supermarket");
        Artikl izbaci=new Artikl();
        for(int i = 0; i < brojArtikala ; i++) {
            if(this.artikli[i].getKod().equals(kod)) {
                izbaci=new Artikl(this.artikli[i].getNaziv(),this.artikli[i].getCijena(),this.artikli[i].getKod());
                this.artikli[i] = null;
                for(int j = i ; j < brojArtikala-1 ; j++) {
                    this.artikli[j]=this.artikli[j+1];
                }
                this.artikli[brojArtikala-1]=null;
                break;
            }
        }
        brojArtikala--;
        return izbaci;
    }

    public void dodajArtikl(Artikl a) throws IllegalAccessException {
        if(brojArtikala==1000) throw new IllegalAccessException("Kapacitet supermarketa dostigao maksimum");
         else {
            artikli[brojArtikala] = new Artikl(a.getNaziv(), a.getCijena(), a.getKod());
            brojArtikala++;
         }
    }
}

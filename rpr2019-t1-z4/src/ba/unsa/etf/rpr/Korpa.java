package ba.unsa.etf.rpr;

public class Korpa {
    private  Artikl[] artikli;
    private int brojArtikala;
    public Korpa() {
        artikli=new Artikl[50];
        brojArtikala=0;
    }
    public boolean dodajArtikl(Artikl a) throws IllegalAccessException {
        if(brojArtikala==50) throw new IllegalAccessException("Puna korpa");
        this.artikli[brojArtikala]=new Artikl(a.getNaziv(),a.getCijena(),a.getKod());
        brojArtikala++;
        return  true;
    }
    public Artikl[] getArtikli() {
        return  artikli;
    }
    public int getBrojArtikala() {
        return brojArtikala;
    }
    public int dajUkupnuCijenuArtikala() {
        int suma=0;
        for(int i = 0 ; i < brojArtikala ; i++) {
                suma+=this.artikli[i].getCijena();
        }
        return suma;
    }
    public Artikl izbaciArtiklSaKodom(String kod) throws IllegalAccessException {
        if(brojArtikala==0) throw new IllegalAccessException("Prazna korpa");
        Artikl izbaci=null;
        for(int i = 0; i < brojArtikala; i++) {
            if(this.artikli[i].getKod().equals(kod)) {
                izbaci=new Artikl(this.artikli[i].getNaziv(),this.artikli[i].getCijena(),this.artikli[i].getKod());
                this.artikli[i] = null;
                for(int j=i;j<brojArtikala-1;j++) {
                    this.artikli[j]=this.artikli[j+1];
                }
                this.artikli[brojArtikala-1]=null;
                brojArtikala--;
                break;
            }
        }
        return izbaci;
    }
}

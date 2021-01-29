package ba.unsa.etf.rpr;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KorpaTest {

    @Test
    void dodajArtikl() throws IllegalAccessException {
        Korpa s = new Korpa();
        s.dodajArtikl(new Artikl("Biciklo", 1000, "4"));
        s.dodajArtikl(new Artikl("Kolica", 1500, "8"));
        s.dodajArtikl(new Artikl("Romobil", 1000, "019"));
        assertEquals(3,s.getBrojArtikala());
    }

    @Test
    void dodajArtikliPrekoracenje() {
        Korpa s = new Korpa();
        assertThrows(IllegalAccessException.class, () -> {
            for(int i=0  ;i<51; i++) {
                s.dodajArtikl(new Artikl("Proizvod",10,"4"));
            }
        }) ;

    }

    @Test
    void izbaciArtiklSaKodom() throws IllegalAccessException {
        Korpa s = new Korpa();
        s.dodajArtikl(new Artikl("Biciklo", 1000, "4"));
        s.dodajArtikl(new Artikl("Kolica", 1500, "8"));
        s.dodajArtikl(new Artikl("Romobil", 1000, "019"));
        assertAll("izbaci", () -> {
            assertEquals("Romobil", s.izbaciArtiklSaKodom("019").getNaziv());
        }, () -> {
            assertEquals(2, s.getBrojArtikala());
        });
    }
    @Test
    void izbaciArtiklPrazanSupermarket()  {
        Korpa s = new Korpa();
        assertThrows(IllegalAccessException.class, () -> {
            s.izbaciArtiklSaKodom("1");
        });
    }

    @Test
    void izbaciJediniArtiklIzSupermerketa() throws IllegalAccessException {
        Korpa s = new Korpa();
        s.dodajArtikl(new Artikl("Romobil", 1000, "019"));
        assertAll("izbaci", () -> {
            assertEquals("Romobil", s.izbaciArtiklSaKodom("019").getNaziv());
        }, () -> {
            assertEquals(0, s.getBrojArtikala());
        });
    }
}
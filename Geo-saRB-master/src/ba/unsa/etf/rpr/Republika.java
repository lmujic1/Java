package ba.unsa.etf.rpr;

public class Republika extends Drzava {
    public Republika(int id, String naziv, Grad glavniGrad) {
        super(id, naziv, glavniGrad);
    }

    @Override
    public String getNaziv() {
        return "Republika " + super.getNaziv();
    }

    @Override
    public String toString() {
        return "Republika " + super.toString();
    }
}

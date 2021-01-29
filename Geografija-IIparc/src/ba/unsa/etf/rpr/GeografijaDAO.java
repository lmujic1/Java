package ba.unsa.etf.rpr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GeografijaDAO {
    private static GeografijaDAO instance;
    private Connection conn;
    private PreparedStatement pripremniUpit;
    private PreparedStatement glavniGradUpit, dajDrzavuUpit, dajGradoveUpit, nadjiDrzavuUpit, obrisiDrzavuUpit, obrisiGradoveZaDrzavuUpit, dodajGradUpit, dodajDrzavuUpit, zadnjiGradIdUpit, zadnjiDrzavaIdUpit,
            izmijeniGradUpit, nadjiGradUpit, nadjiGradPoNazivu, obrisiGradUpit, dajDrzaveUpit;

    private GeografijaDAO() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            pripremniUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava, grad.zagadjenost FROM grad, drzava WHERE grad.drzava=drzava.id AND drzava.naziv=?");
        } catch (SQLException e) {
            regenerisiBazu();
            try {
                pripremniUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava, grad.zagadjenost FROM grad, drzava WHERE grad.drzava=drzava.id AND drzava.naziv=?");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        try {
            //glavniGradUpit = conn.prepareStatement("SELECT grad.id, grad.naziv, grad.broj_stanovnika, grad.drzava, grad.zagadjenost FROM grad, drzava WHERE grad.drzava = drzava.id AND drzava.naziv=?");
            dajDrzavuUpit = conn.prepareStatement("SELECT * FROM  drzava WHERE id=?");

            dajGradoveUpit = conn.prepareStatement("SELECT * FROM grad ORDER BY broj_stanovnika DESC");
            dajDrzaveUpit = conn.prepareStatement("SELECT * FROM drzava ORDER BY naziv");

            nadjiDrzavuUpit = conn.prepareStatement("SELECT * FROM drzava WHERE naziv=?");
            nadjiGradUpit = conn.prepareStatement("SELECT * FROM grad WHERE id=?");
            nadjiGradPoNazivu = conn.prepareStatement("SELECT * FROM grad WHERE naziv=?");

            obrisiDrzavuUpit = conn.prepareStatement("DELETE FROM drzava WHERE id=?");
            obrisiGradoveZaDrzavuUpit = conn.prepareStatement("DELETE FROM grad WHERE drzava=?");

            obrisiGradUpit = conn.prepareStatement("DELETE FROM grad WHERE id=?");

            dodajGradUpit = conn.prepareStatement("INSERT INTO grad VALUES(?,?,?,?,?)");
            dodajDrzavuUpit = conn.prepareStatement("INSERT INTO drzava VALUES(?,?,?)");

            zadnjiGradIdUpit = conn.prepareStatement("SELECT MAX(id)+1 FROM grad");
            zadnjiDrzavaIdUpit = conn.prepareStatement("SELECT MAX(id)+1 FROM drzava");

            izmijeniGradUpit = conn.prepareStatement("UPDATE grad SET naziv=?, broj_stanovnika=?, drzava=?, zagadjenost=? WHERE id=?");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerisiBazu() {
        Scanner ulaz = null;
        try {
            ulaz = new Scanner(new FileInputStream("baza.db.sql"));
            String sqlUpit = "";
            while (ulaz.hasNext()) {
                sqlUpit += ulaz.nextLine();
                if ( sqlUpit.charAt( sqlUpit.length()-1 ) == ';') {
                    try {
                        Statement stmt = conn.createStatement();
                        stmt.execute(sqlUpit);
                        sqlUpit = "";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            ulaz.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static GeografijaDAO getInstance() {
        if (instance == null) instance = new GeografijaDAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance == null) return;
        instance.close();
        instance = null;
    }

    private void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Grad glavniGrad(String nazivDrzave) {
        try {
            pripremniUpit.setString(1, nazivDrzave);
            ResultSet rs = pripremniUpit.executeQuery();
            if (!rs.next()) return null;
            return dajGradIzResultSeta(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Grad dajGradIzResultSeta(ResultSet rs) throws SQLException {
        Grad grad = new Grad(rs.getInt(1), rs.getString(2), rs.getInt(3), null, rs.getInt(5));
        grad.setDrzava(dajDrzavu(rs.getInt(4), grad));
        return grad;
    }

    private Drzava dajDrzavu(int idDrzave, Grad grad) {
        try {
            dajDrzavuUpit.setInt(1, idDrzave);
            ResultSet rs = dajDrzavuUpit.executeQuery();
            if (!rs.next()) return null;
            return dajDrzavuIzResultSeta(rs, grad);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Drzava dajDrzavuIzResultSeta(ResultSet rs, Grad grad) throws SQLException {
        return new Drzava(rs.getInt(1), rs.getString(2), grad);
    }

    public void obrisiDrzavu(String nazivDrzave) {
        try {
            nadjiDrzavuUpit.setString(1, nazivDrzave);
            ResultSet rs = nadjiDrzavuUpit.executeQuery();
            if (!rs.next()) return;
            Drzava drzava = dajDrzavuIzResultSeta(rs, null);

            obrisiGradoveZaDrzavuUpit.setInt(1, drzava.getId());
            obrisiGradoveZaDrzavuUpit.executeUpdate();

            obrisiDrzavuUpit.setInt(1, drzava.getId());
            obrisiDrzavuUpit.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Grad> gradovi() {
        ArrayList<Grad> gradovi = new ArrayList<>();
        try {
            ResultSet rs = dajGradoveUpit.executeQuery();
            while (rs.next()) {
                Grad grad = dajGradIzResultSeta(rs);
                gradovi.add(grad);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gradovi;
    }

    public void dodajGrad(Grad grad) {
        try {
            ResultSet rs = zadnjiGradIdUpit.executeQuery();
            int id = 1;
            if (rs.next()) id = rs.getInt(1);
            dodajGradUpit.setInt(1, id);
            dodajGradUpit.setString(2, grad.getNaziv());
            dodajGradUpit.setInt(3, grad.getBrojStanovnika());
            dodajGradUpit.setInt(4, grad.getDrzava().getId());
            dodajGradUpit.setInt(5, grad.getZagadjenost());

            dodajGradUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void dodajDrzavu(Drzava drzava) {
        try {
            ResultSet rs = zadnjiDrzavaIdUpit.executeQuery();
            int id = 1;
            if (rs.next()) id = rs.getInt(1);
            dodajDrzavuUpit.setInt(1, id);
            dodajDrzavuUpit.setString(2, drzava.getNaziv());
            dodajDrzavuUpit.setInt(3, drzava.getGlavniGrad().getId());

            dodajDrzavuUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void izmijeniGrad(Grad grad) {
        try {
            izmijeniGradUpit.setString(1, grad.getNaziv());
            izmijeniGradUpit.setInt(2, grad.getBrojStanovnika());
            izmijeniGradUpit.setInt(3, grad.getDrzava().getId());
            izmijeniGradUpit.setInt(4, grad.getZagadjenost());
            izmijeniGradUpit.setInt(5, grad.getId());

            izmijeniGradUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Drzava nadjiDrzavu(String nazivDrzave) {
        try {
            nadjiDrzavuUpit.setString(1, nazivDrzave);
            ResultSet rs = nadjiDrzavuUpit.executeQuery();
            if (!rs.next()) return null;
            return dajDrzavuIzResultSeta(rs, dajGrad(rs.getInt(3)));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Grad dajGrad(int idGrada) {
        try {
            nadjiGradUpit.setInt(1, idGrada);
            ResultSet rs = nadjiGradUpit.executeQuery();
            if (!rs.next()) return null;
            return dajGradIzResultSeta(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Grad nadjiGrad(String nazivGrada) {
        try {
            nadjiGradPoNazivu.setString(1, nazivGrada);
            ResultSet rs = nadjiGradPoNazivu.executeQuery();
            if (!rs.next()) return null;
            return dajGradIzResultSeta(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void obrisiGrad(Grad grad) {
        try {
            obrisiGradUpit.setInt(1, grad.getId());
            obrisiGradUpit.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Drzava> drzave() {
        ArrayList<Drzava> drzave = new ArrayList<>();
        try {
            ResultSet rs = dajDrzaveUpit.executeQuery();
            while (rs.next()) {
                Grad grad = dajGrad(rs.getInt(3));
                Drzava drzava = dajDrzavuIzResultSeta(rs, grad);
                drzave.add(drzava);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drzave;
    }
}
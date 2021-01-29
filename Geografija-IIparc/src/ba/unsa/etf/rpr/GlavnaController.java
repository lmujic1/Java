package ba.unsa.etf.rpr;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class GlavnaController {
    public TableView<Grad> tableViewGradovi;
    public TableColumn colGradId;
    public TableColumn colGradNaziv;
    public TableColumn colGradStanovnika;
    public TableColumn<Grad, String> colGradDrzava;
    public TableColumn colGradZagadjenost;
    private GeografijaDAO dao;
    private ObservableList<Grad> listaGradova;

    public GlavnaController() {
        dao = GeografijaDAO.getInstance();
        listaGradova = FXCollections.observableArrayList(dao.gradovi());
    }

    @FXML
    public void initialize() {
        tableViewGradovi.setItems(listaGradova);
        colGradId.setCellValueFactory(new PropertyValueFactory("id")); //nazivi kolona tabela <=> nazivi atributa u klasi grad
        colGradNaziv.setCellValueFactory(new PropertyValueFactory("naziv"));
        colGradStanovnika.setCellValueFactory(new PropertyValueFactory("brojStanovnika"));
        colGradDrzava.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDrzava().getNaziv()));
        colGradZagadjenost.setCellValueFactory(new PropertyValueFactory("zagadjenost"));
    }

    public void actionDodajGrad(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
            GradController gradController = new GradController(null, dao.drzave());
            loader.setController(gradController);
            root = loader.load();
            stage.setTitle("Grad");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();

            stage.setOnHiding(event -> {
                Grad grad = gradController.getGrad();
                if (grad != null) {
                    dao.dodajGrad(grad);
                    listaGradova.setAll(dao.gradovi());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void actionDodajDrzavu(ActionEvent actionEvent) {
        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/drzava.fxml"));
            DrzavaController drzavaController = new DrzavaController(null, dao.gradovi());
            loader.setController(drzavaController);
            root = loader.load();
            stage.setTitle("Drzava");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();

            stage.setOnHiding(event -> {
                Drzava drzava = drzavaController.getDrzava();
                if (drzava != null) {
                    dao.dodajDrzavu(drzava);
                    listaGradova.setAll(dao.gradovi());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionIzmijeniGrad(ActionEvent actionEvent) {
        Grad g = tableViewGradovi.getSelectionModel().getSelectedItem();
        if (g == null) return;

        Stage stage = new Stage();
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/grad.fxml"));
            GradController gradController = new GradController(g, dao.drzave());
            loader.setController(gradController);
            root = loader.load();
            stage.setTitle("Grad");
            stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
            stage.setResizable(false);
            stage.show();

            stage.setOnHiding(event -> {
                Grad grad = gradController.getGrad();
                if (grad != null) {
                    dao.izmijeniGrad(grad);
                    listaGradova.setAll(dao.gradovi());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void actionObrisiGrad(ActionEvent actionEvent) {
        Grad grad = tableViewGradovi.getSelectionModel().getSelectedItem();
        if (grad == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potvrda brisanja");
        alert.setHeaderText("Brisanje grada " + grad.getNaziv());
        alert.setContentText("Da li ste sigurni da zelite obrisati grad?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            dao.obrisiGrad(grad);
            listaGradova.setAll(dao.gradovi());
        }
    }

    public void actionZapisi(ActionEvent actionEvent) {
        Geografija g=new Geografija();
        g.setDrzave(dao.drzave());
        g.setGradovi(dao.gradovi());

        try {
            XMLEncoder encoder = new XMLEncoder(new FileOutputStream("geografija.xml"));
            encoder.writeObject(g);
            encoder.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void resetujBazu() {
        GeografijaDAO.removeInstance();
        File dbfile = new File("baza.db");
        dbfile.delete();
        dao = GeografijaDAO.getInstance();
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.viewControllers;

import classibiblioteca.entita.Utente;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Libro;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblio.tipologiearchivi.ArchivioLibri;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 *
 * @author Utente
 */
public class GestioneUtentiController implements Initializable {
    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colEmail;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colTitoloLibro1;
    @FXML private TableColumn<Utente, String> colTitoloLibro2;
    @FXML private TableColumn<Utente, String> colTitoloLibro3;
    @FXML private TextField txtRicerca;
    private ArchivioUtenti archivio;
    private ObservableList<Utente> utenti;
    private ArchivioPrestiti archivioPrestiti;
    private ArchivioPrestiti archivioLibri;
    private ObservableList<Prestito> prestito;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archivio = new ArchivioUtenti();
        utenti = FXCollections.observableArrayList();

        tabellaUtenti.setItems(utenti);
        configuraColonne();
        caricaUtenti();
    }

    private void configuraColonne() {
        colNome.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getNome()));

        colCognome.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getCognome()));

        colEmail.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getEmail()));

        colMatricola.setCellValueFactory(c ->
            new SimpleStringProperty(c.getValue().getMatricola()));

        // dato preso da archivioprestiti

        colTitoloLibro1.setCellValueFactory(c -> new SimpleStringProperty(getTitoloPrestitoAttivo(c.getValue(), 0))
        );
        colTitoloLibro2.setCellValueFactory(c -> new SimpleStringProperty(getTitoloPrestitoAttivo(c.getValue(), 1))
        );
        colTitoloLibro3.setCellValueFactory(c -> new SimpleStringProperty(getTitoloPrestitoAttivo(c.getValue(), 2))
        );
    }
        private String getTitoloPrestitoAttivo(Utente utente, int index) {
            Prestito precedente = null;
            Prestito corrente = null;

        for (int i = 0; i <= index; i++) {
            corrente = archivioPrestiti.getSingoloPrestitoAttivo(utente.getMatricola(), precedente);
            if (corrente == null) {
                return "";
            }
            precedente = corrente;
        }

        return corrente.getTitololibro();
    }

    @FXML
    private void handleCreaUtente(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/classibiblioteca/views/utentescheda.fxml")
        );
        Pane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Crea utente");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(tabellaUtenti.getScene().getWindow());
        dialogStage.setScene(new Scene(page));

        SchedaUtentecontroller controller = loader.getController();
        controller.setDialogStage(dialogStage);

        dialogStage.showAndWait();

        if (controller.isOkClicked()) {
            Utente nuovo = controller.getUtente();
            archivio.aggiungiUtente(nuovo);
            utenti.add(nuovo);
            }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML
    private void handleModificaUtente(ActionEvent event) {
        Utente selezionato = utenteSelezionato();
        if (selezionato == null) {
            avvisaSelezione();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/classibiblioteca/views/modificautente.fxml")
        );
        Pane page = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Modifica Utente");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(tabellaUtenti.getScene().getWindow());
        dialogStage.setScene(new Scene(page));

        ModificaUtenteController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setUtente(selezionato); // passa l'utente da modificare

        dialogStage.showAndWait();

        if (controller.isOkClicked()) {
            tabellaUtenti.refresh();
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML
    private void handleEliminaUtente(ActionEvent event) {
        Utente selezionato = utenteSelezionato();
        if (selezionato == null) {
            avvisaSelezione();
            return;
        }
        archivio.eliminaUtente(selezionato);
        utenti.remove(selezionato);
    }

    @FXML
    private void handleCercaUtente(ActionEvent event) {
        String testo = txtRicerca.getText();

        if (testo == null || testo.trim().isEmpty()) {
            caricaUtenti();
        } else {
            utenti.setAll(archivio.ricercaUtente(testo));
        }
    }

    private Utente utenteSelezionato() {
        return tabellaUtenti.getSelectionModel().getSelectedItem();
    }

    private void aggiornaVista() {
        tabellaUtenti.refresh();
    }
    private void caricaUtenti() {
        utenti.setAll(archivio.getLista());
    }

    private void avvisaSelezione() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nessuna selezione");
        alert.setContentText("Seleziona un utente dalla tabella.");
        alert.showAndWait();
    }
}
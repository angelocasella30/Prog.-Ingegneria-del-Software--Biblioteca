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
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Field;
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
    @FXML private TextField btnCercaHomeUtente;
    @FXML private Field fldcercaUtente; 
    @FXML MenuButton MenuButtonHomeOrdinaUtente;
    @FXML MenuItem Nome;
    @FXML MenuItem Cognome;
    @FXML private Button btnModificaHomeUtente,btnEliminaHomeUtente,btnListaHomeUtente;
    @FXML private MenuButton MenuButtonHomeCercaUtente;
    @FXML private MenuItem ItemMatricola,ItemCognome;
    @FXML private Button btnCerca;
    private String tipoRicerca = "matricola";//matricola impostato come predefinito
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
        btnModificaHomeUtente.setDisable(true);
        btnEliminaHomeUtente.setDisable(true);

        // Aggiungi un listener per la selezione della tabella
        tabellaUtenti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Abilita i pulsanti quando una riga è selezionata
                btnModificaHomeUtente.setDisable(false);
                btnEliminaHomeUtente.setDisable(false);
            } else {
                // Disabilita i pulsanti quando nessuna riga è selezionata
                btnModificaHomeUtente.setDisable(true);
                btnEliminaHomeUtente.setDisable(true);
            }
        });
        Nome.setOnAction(event->handleListaOrdinatiPerNome(event));
        Cognome.setOnAction(event->handleListaOrdinatiPerCognome(event));
        
        ItemMatricola.setOnAction(event -> tipoRicerca = "matricola");
        ItemCognome.setOnAction(event -> tipoRicerca = "cognome");
        btnCercaHomeUtente.setOnAction(this::handleCercaUtente);
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
    
    private void handleListaOrdinatiPerNome(ActionEvent event){
        utenti.setAll(FXCollections.observableArrayList(archivio.getUtentiOrdinatiPerNome()));
        tabellaUtenti.refresh();
    }
    private void handleListaOrdinatiPerCognome(ActionEvent event){
        utenti.setAll(FXCollections.observableArrayList(archivio.getUtentiOrdinatiPerCognome()));
        tabellaUtenti.refresh();
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
            
            handleListaUtenti();
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
            handleListaUtenti();
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    @FXML
    private void handleStoricoUtente(ActionEvent event) {
        Utente selezionato = utenteSelezionato();
        if (selezionato == null) {
            avvisaSelezione();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/classibiblioteca/views/schedamodificautente.fxml"));
            Pane page = loader.load();
            Storicoschedacontroller controller = loader.getController();
            controller.setUtente(selezionato);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Storico Utente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabellaUtenti.getScene().getWindow());
            dialogStage.setScene(new Scene(page));
            dialogStage.showAndWait();

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
        String testo = fldcercaUtente.getText();
        if (testo.isEmpty()) {
        caricaUtenti();
        } else {
            if (tipoRicerca.equals("matricola")) {
                utenti.setAll(archivio.ricercaUtentePerMatricola(testo));
            } else if (tipoRicerca.equals("cognome")) {
                utenti.setAll(archivio.ricercaUtentePerCognome(testo));
        }
    }
    tabellaUtenti.refresh();
    }
    
    @FXML
    private void handleListaUtenti() {
        String testoRicerca = fldcercaUtente.getText().trim();

        if (testoRicerca.isEmpty()) {
        utenti.setAll(archivio.getLista());
        } else {
        utenti.setAll(archivio.ricercaUtente(testoRicerca)); 
    }
    tabellaUtenti.refresh();
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
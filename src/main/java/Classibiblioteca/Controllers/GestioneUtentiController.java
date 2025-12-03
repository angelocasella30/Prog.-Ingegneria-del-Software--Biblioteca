package Classibiblioteca.Controllers;

import Classibiblioteca.Entità.Utente;
import Classibiblioteca.tipologiearchivi.ArchivioUtenti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestioneUtentiController {

    @FXML
    private TextField txtRicerca;

    @FXML
    private TableView<Utente> tableUtenti;

    @FXML
    private TableColumn<Utente, String> colMatricola;

    @FXML
    private TableColumn<Utente, String> colCognome;

    @FXML
    private TableColumn<Utente, String> colNome;

    @FXML
    private TableColumn<Utente, String> colEmail;

    private ArchivioUtenti archivioUtenti;

    public void initData(ArchivioUtenti archivio) {
        this.archivioUtenti = archivio;
    }

    @FXML
    public void initialize() {
        // TODO: Collegare colonne
    }

    @FXML
    void handleCerca(ActionEvent event) {
        // TODO: Implementare ricerca (UC-8)
    }

    @FXML
    void handleReset(ActionEvent event) {
        // TODO: Mostra tutti
    }

    @FXML
    void handleNuovo(ActionEvent event) {
        // TODO: Nuovo utente (UC-5)
    }

    @FXML
    void handleModifica(ActionEvent event) {
        // TODO: Modifica utente (UC-6)
    }

    @FXML
    void handleElimina(ActionEvent event) {
        // TODO: Elimina utente (UC-7)
    }
}
package Classibiblioteca.Controllers;

import Classibiblioteca.Archivio;
import Classibiblioteca.Bibliotecario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainContainer;

    @FXML
    private Label lblNomeBiblioteca;

    @FXML
    private Button btnLibri;

    @FXML
    private Button btnUtenti;

    @FXML
    private Button btnPrestiti;

    @FXML
    private Button btnSalva;

    // Riferimenti al Modello dati
    private Archivio archivio;
    private Bibliotecario bibliotecario;

    @FXML
    public void initialize() {
        // Qui caricheremo i dati all'avvio
    }

    @FXML
    void showGestioneLibri(ActionEvent event) {
        // TODO: Caricare la vista GestioneLibri.fxml al centro del BorderPane
    }

    @FXML
    void showGestioneUtenti(ActionEvent event) {
        // TODO: Caricare la vista GestioneUtenti.fxml al centro del BorderPane
    }

    @FXML
    void showGestionePrestiti(ActionEvent event) {
        // TODO: Caricare la vista GestionePrestiti.fxml al centro del BorderPane
    }

    @FXML
    void handleSalvaEsci(ActionEvent event) {
        // TODO: Salvare l'archivio su file e chiudere l'applicazione (UC-12)
    }
}
package Classibiblioteca.Controllers;

import Classibiblioteca.Archivio;
import Classibiblioteca.Bibliotecario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Controller principale 
 * gestisce l'interfaccia home,le schede libri,utenti e prestiti
 * l'inizializzazione degli elementi e il coordinamento 
 * con i controller secondari
 */

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

/**
 * Inizializza la finestra principale con
 * elementi e dati dell'interfaccia grafica
 */
    @FXML
    public void initialize() {
        // Qui caricheremo i dati all'avvio
    }

/**
 * Metodo showGestioneLibri gestisce il pulsante per visualizzare GestioneLibri
 * carica il file GestioneLibri.fxml
 * 
 * @param event evento generato dal pulsante
 */
    @FXML
    void showGestioneLibri(ActionEvent event) {
        // TODO: Caricare la vista GestioneLibri.fxml al centro del BorderPane
    }
/**
 * Metodo showGestioneUtenti gestisce il pulsante per visualizzare GestioneUtenti
 * carica il file GestioneUtenti.fxml
 * 
 * @param event evento generato dal pulsante
 */
    
    @FXML
    void showGestioneUtenti(ActionEvent event) {
        // TODO: Caricare la vista GestioneUtenti.fxml al centro del BorderPane
    }
/**
 * Metodo showGestionePrestiti gestisce il pulsante per visualizzare GestionePrestiti
 * carica il file GestionePrestiti.fxml
 * 
 * @param event evento generato dal pulsante
 */
    
    @FXML
    void showGestionePrestiti(ActionEvent event) {
        // TODO: Caricare la vista GestionePrestiti.fxml al centro del BorderPane
    }
/**
 * Metodo handleSalvaEsci gestisce il pulsante per esportare archivio su file 
 * e chiudere l'applicazione
 * 
 * @param event evento generato dal pulsante
 */
    
    @FXML
    void handleSalvaEsci(ActionEvent event) {
        // TODO: Salvare l'archivio su file e chiudere l'applicazione (UC-12)
    }
}
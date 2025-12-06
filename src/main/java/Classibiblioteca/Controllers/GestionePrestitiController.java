package Classibiblioteca.Controllers;

import Classibiblioteca.Entità.Prestito;
import Classibiblioteca.tipologiearchivi.ArchivioLibri;
import Classibiblioteca.tipologiearchivi.ArchivioPrestiti;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller che gestisce operazioni sui prestiti
 * come la gestione di nuovo prestito e restituzione
 */

public class GestionePrestitiController {

    @FXML
    private TableView<Prestito> tablePrestiti;

    @FXML
    private TableColumn<Prestito, String> colMatricola;

    @FXML
    private TableColumn<Prestito, String> colISBN;

    @FXML
    private TableColumn<Prestito, LocalDate> colDataInizio;

    @FXML
    private TableColumn<Prestito, LocalDate> colDataFine;

    @FXML
    private TableColumn<Prestito, String> colStato; // Colonna calcolata "IN RITARDO"

    private ArchivioPrestiti archivioPrestiti;
    private ArchivioLibri archivioLibri; // Serve per restituire le copie

/**
 * Metodo initData
 * 
 * collega archiviolibri e archivioprestiti
 */
    public void initData(ArchivioPrestiti archivioP, ArchivioLibri archivioL) {
        this.archivioPrestiti = archivioP;
        this.archivioLibri = archivioL;
    }
/**
 * Metodo initialize
 * 
 * inizializza la vista tabella con dati archivioprestiti e archiviolibri
 * 
 */
    
    @FXML
    public void initialize() {
        // TODO: Collegare colonne e impostare RowFactory per il colore rosso
    }
/**
 * Metodo handleNuovoPrestito gestisce la richiesta di prestito
 * 
 * gestisce il form prestito, e il controllo di disponibilità copie
 * e numero di libri in prestito per utente
 */
    
    @FXML
    void handleNuovoPrestito(ActionEvent event) {
        // TODO: Aprire popup nuovo prestito (UC-9)
    }
/**
 * Metodo handleRestituzione gestisce la richiesta di restituzione
 * 
 * gestisce il form restituzione, e aggiornamento dataritorno
 */
    
    @FXML
    void handleRestituzione(ActionEvent event) {
        // TODO: Gestire restituzione (UC-11)
    }
    
    @FXML
    public void handleVisualizzaPrestiti() {
        // da completare mostra la lista
    }
}
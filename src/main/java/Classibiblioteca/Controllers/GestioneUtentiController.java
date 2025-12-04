package Classibiblioteca.Controllers;

import Classibiblioteca.Entità.Utente;
import Classibiblioteca.tipologiearchivi.ArchivioUtenti;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Controller che gestisce operazioni su utenti
 * come la creazione, modifica,elimina di utenti
 */

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

/**
 * Metodo initData 
 * collega l'archivio e carica i dati
 */

    public void initData(ArchivioUtenti archivio) {
        this.archivioUtenti = archivio;
    }
/**
 * Inzializza la vista tabella utenti con collegamento di colonne
 */

    @FXML
    public void initialize() {
        // TODO: Collegare colonne
    }
/**
 * Metodo handleCerca gestisce l'evento ricerca utente
 * 
 * cerca utente in base input cognome o matricola
 * se non si trova l'utente non avvia la ricerca 
 */
    
    @FXML
    void handleCerca(ActionEvent event) {
        // TODO: Implementare ricerca (UC-8)
    }
/**
 * Metodo handleReset gestisce l'evento reset
 * 
 * gestisce l'annullamento dell'operazione 
 */
    
    @FXML
    void handleReset(ActionEvent event) {
        // TODO: Mostra tutti
    }
/**
 * Metodo handleNuovo gestisce l'evento nuovo utente
 * 
 * gestisce la creazione di nuovo utente e la verifica
 * con l'archivio confronto matricola
 */
    
    @FXML
    void handleNuovo(ActionEvent event) {
        // TODO: Nuovo utente (UC-5)
    }
/**
 * Metodo handleModifica gestisce l'evento modifica utente
 * 
 * modifca dettagli utente con vincolo di matricola
 */
    
    @FXML
    void handleModifica(ActionEvent event) {
        // TODO: Modifica utente (UC-6)
    }

/**
 * Metodo handleElimina gestisce l'evento elimina utente
 * 
 * elimina utente registrato, gestisce l'eccezione nel caso
 * in cui si presentano prestiti in corso
 */

    @FXML
    void handleElimina(ActionEvent event) {
        // TODO: Elimina utente (UC-7)
    }
}
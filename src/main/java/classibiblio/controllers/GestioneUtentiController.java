package classibiblio.controllers;

import classibiblioteca.entita.Utente;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import javafx.collections.transformation.SortedList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controller che gestisce operazioni su utenti
 * come la creazione, modifica,elimina di utenti
 */

public class GestioneUtentiController {

    @FXML
    private TextField txtRicerca;
    
    @FXML
    private TextField fldNome;
    
    @FXML
    private TextField fldCognome;
    
    @FXML
    private TextField fldEmail;
    
    @FXML
    private TextField fldMatricola;
    
    @FXML
    private TableView<Utente> tableUtenti;

    @FXML
    private Button btnCercaUtente;

    @FXML
    private TableColumn<Utente, String> clmnMatricolaUtente;

    @FXML
    private TableColumn<Utente, String> clmnCognomeUtente;

    @FXML
    private TableColumn<Utente, String> clmnNomeUtente;

    @FXML
    private TableColumn<Utente, String> clmnEmailUtente;

    private ArchivioUtenti archivioUtenti;

    private boolean validateFormUtenti() {
    String nome = fldNome.getText();
    String cognome = fldCognome.getText();
    String email = fldEmail.getText();
    String matricola = fldMatricola.getText();

    //da completare
    return true;
    }
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
        clmnMatricolaUtente.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        clmnCognomeUtente.setCellValueFactory(new PropertyValueFactory<>("cognome"));
        clmnNomeUtente.setCellValueFactory(new PropertyValueFactory<>("nome"));
        clmnEmailUtente.setCellValueFactory(new PropertyValueFactory<>("email"));
        if (tableUtenti.getColumns().isEmpty()) {
            tableUtenti.getColumns().addAll(clmnMatricolaUtente,clmnCognomeUtente,clmnNomeUtente,clmnEmailUtente);
    }
        tableUtenti.setItems(FXCollections.observableArrayList());
    }
/**
 * Metodo handleCerca gestisce l'evento ricerca utente
 * 
 * cerca utente in base input cognome o matricola
 * se non si trova l'utente non avvia la ricerca 
 */
    
    @FXML
    void handleCercaUtente(ActionEvent event) {
        if (ArchivioUtenti == null) return; 
        
        String cercamatricolaUtente = fldMatricola.getText().trim().toLowerCase();
        String cercacognomeUtente = fldCognome.getText().trim().toLowerCase();

        if (cercamatricolaUtente.isEmpty() && cercacognomeUtente.isEmpty()) {
            return;
        }
        ObservableList<Utente> listaUtenti = FXCollections.observableArrayList(ArchivioUtenti.getUtentiOrdinati());

        FilteredList<Utente> filteredList = new FilteredList<>(listaUtenti, utente -> true);

        filteredList.setPredicate(utente -> {
            boolean matricolacheckcampo = cercamatricolaUtente.isEmpty() || utente.getMatricola().toLowerCase().contains(cercamatricolaUtente);
            boolean cognomecheckcampo = cercacognomeUtente.isEmpty() || utente.getCognome().toLowerCase().contains(cercacognomeUtente);
            return matricolacheckcampo || cognomecheckcampo;
        });

        SortedList<Utente> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableUtenti.comparatorProperty());
        tableUtenti.setItems(sortedList);
       }

    }
/**
 * Metodo handleReset gestisce l'evento reset
 * 
 * gestisce l'annullamento dell'operazione 
 */
    
    @FXML
    void handleResetUtenti(ActionEvent event) {
        // TODO: Mostra tutti
    }
/**
 * Metodo handleNuovo gestisce l'evento nuovo utente
 * 
 * gestisce la creazione di nuovo utente e la verifica
 * con l'archivio confronto matricola
 */
    
    @FXML
    void handleCreaUtente(ActionEvent event) {
        schedaUser.setVisible(true);
    }
/**
 * Metodo handleModifica gestisce l'evento modifica utente
 * 
 * modifca dettagli utente con vincolo di matricola
 */
    
    @FXML
    void handleModificaUtente(ActionEvent event) {
        // TODO: Modifica utente (UC-6)
    }

/**
 * Metodo handleElimina gestisce l'evento elimina utente
 * 
 * elimina utente registrato, gestisce l'eccezione nel caso
 * in cui si presentano prestiti in corso
 */

    @FXML
    void handleEliminaUtente(ActionEvent event)
    {
        // TODO: Elimina utente (UC-7)
    }
    
    @FXML
    void handleSalvaFormUtenti(ActionEvent event) {
        String nome = fldScNomeUtente.getText().trim();
        String cognome = fldScCognomeUtente.getText().trim();
        String email = fldScEmailUtente.getText().trim();
        String matricola = fldScMatricolaUtente.getText().trim();
        if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || matricola.isEmpty()) {
            showAlert("Attenzione", "Ci sono campi vuoti");
            return;
        }
        if (archivioUtenti.existByMatricola(matricola)) {
            showAlert("Attenzione", "La matricola esiste");
            return;
        }
        Utente newUtente = new Utente(matricola, nome, cognome, email);
        archivioUtenti.aggiungiUtente(newUtente);
        tableUtenti.setItems(archivioUtenti.getUtentiOrdinati());
        schedaUser.setVisible(false);
       
    }
    
    @FXML
    public void handleVisualizzaUtenti() {
        ObservableList<Utente> utenti = FXCollections.observableArrayList(archivioUtenti.getUtentiOrdinati());
        tableUtenti.setItems(utenti);
    }
}
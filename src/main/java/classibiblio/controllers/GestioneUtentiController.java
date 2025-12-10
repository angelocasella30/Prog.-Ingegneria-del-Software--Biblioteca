package classibiblio.controllers;

import classibiblioteca.entita.Utente;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import javafx.collections.transformation.SortedList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
    private TextField fldScMatricolaUtente;
    
    @FXML
    private TextField fldScCognomeUtente;
    
    @FXML
    private TextField fldScNomeUtente;
    
    @FXML
    private TextField fldScEmailUtente;
    
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
    
    @FXML
    private Pane schedaUser;

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
    
    private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
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
         tableUtenti.setOnMouseClicked(event -> { if (event.getClickCount() == 1) { 
                Utente utenteSelezionato = tableUtenti.getSelectionModel().getSelectedItem();
                String matricola = utenteSelezionato.getMatricola();
                Utente utente = archivioUtenti.getUtenteByMatricola(matricola);
                fldMatricola.setText(utente.getMatricola());
                fldNome.setText(utente.getNome());
                fldCognome.setText(utente.getCognome());
                fldEmail.setText(utente.getEmail());
            
    };
    }
/**
 * Metodo handleCerca gestisce l'evento ricerca utente
 * 
 * cerca utente in base input cognome o matricola
 * se non si trova l'utente non avvia la ricerca 
 */
    
    @FXML
    void handleCercaUtente(ActionEvent event) {
        if (archivioUtenti == null) return; 
        
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
        fldScMatricolaUtente.clear();
        fldScCognomeUtente.clear();
        fldScNomeUtente.clear();
        fldScEmailUtente.clear();
        handleVisualizzaUtenti();
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
        Utente utenteSelezionato = tableUtenti.getSelectionModel().getSelectedItem();
        if (utenteSelezionato == null) {
            showAlert("Attenzione", "Nessun utente selezionato");
            return;
        }
        String matricola = utenteSelezionato.getMatricola();

        Utente utente = archivioUtenti.getUtenteByMatricola(matricola);
        fldMatricola.setText(utente.getMatricola());
        fldNome.setText(utente.getNome());
        fldCognome.setText(utente.getCognome());
        fldEmail.setText(utente.getEmail());
        schedaUser.setVisible(true);
    }

/**
 * Metodo handleElimina gestisce l'evento elimina utente
 * 
 * elimina utente registrato, gestisce l'eccezione nel caso
 * in cui si presentano prestiti in corso
 */

    @FXML
    void handleEliminaUtente(ActionEvent event){
        Utente utenteSelezionato = tableUtenti.getSelectionModel().getSelectedItem();

        if (utenteSelezionato == null) {
            showAlert("Attenzione", "Nessun utente selezionato");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("L'utente selezionato sarà eliminato permanentemente.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean utenteRimosso = archivioUtenti.eliminaUtente(utenteSelezionato);
            if (utenteRimosso) {
                tableUtenti.getItems().remove(utenteSelezionato);
                showAlert("Successo", "L'utente è stato eliminato.");
            } else {
                showAlert("Errore", "Ci sono prestiti in corso");
            }
        }
    });
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
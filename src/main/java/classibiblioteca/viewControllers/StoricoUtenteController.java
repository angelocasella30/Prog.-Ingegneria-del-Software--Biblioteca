package classibiblioteca.viewControllers;

import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StoricoUtenteController implements Initializable {

    @FXML
    private Label lblIntestazione;
    
    @FXML
    private TableView<Prestito> tabellaStorico;
    
    // Le colonne corrispondono esattamente a quelle del tuo screenshot
    @FXML
    private TableColumn<Prestito, String> colTitolo;
    @FXML
    private TableColumn<Prestito, String> colISBN;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataInizio;
    @FXML
    private TableColumn<Prestito, LocalDate> colDataRestituzione; // Questa è fondamentale per lo storico

    private Utente utenteSelezionato;
    
    // Questo rappresenterà i dati caricati dal tuo file
    private ArchivioPrestiti archivioDati; 
    
    //tabellaStorico.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Colleghiamo le colonne ai Getter della classe Prestito
        // Assicurati che i nomi tra virgolette siano IDENTICI ai nomi delle variabili in Prestito
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titololibro")); 
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN")); 
        colDataInizio.setCellValueFactory(new PropertyValueFactory<>("dataInizio")); // Controlla se hai usato la 'i' minuscola o maiuscola nel model
        colDataRestituzione.setCellValueFactory(new PropertyValueFactory<>("dataRestituzioneEffettiva"));
        
        // 2. Opzionale: Se la tabella è vuota mostra un messaggio personalizzato
        tabellaStorico.setPlaceholder(new Label("Nessun prestito storico trovato per questo utente."));
        
        // 3. RECUPERO DATI DAL FILE (Simulato tramite Singleton/Istanza globale)
        // Qui devi collegarti alla classe che ha caricato il file all'avvio del programma.
        // Esempio: Database.getInstance().getArchivioPrestiti();
        // Se non hai ancora il singleton, per ora creo un archivio vuoto per non dare errore:
        archivioDati = new ArchivioPrestiti(); 
        
        // TODO IMPORTANTE: Sostituisci la riga sopra con il richiamo al tuo archivio caricato da file.
        // Esempio: archivioDati = GestoreFile.caricaDati().getArchivioPrestiti();
    }    

    /**
     * Questo metodo viene chiamato dall'esterno (GestioneUtentiController)
     * Appena viene chiamato, filtra i dati dell'archivio (che provengono dal file)
     * e mostra solo quelli dell'utente selezionato.
     */
    public void initData(Utente utente) {
        this.utenteSelezionato = utente;
        
        // Imposto il titolo della finestra
        lblIntestazione.setText("Storico Prestiti: " + utente.getCognome() + " " + utente.getNome());
        
        popolaTabella();
    }

    private void popolaTabella() {
        ObservableList<Prestito> listaStorico = FXCollections.observableArrayList();
        
        // Ciclo su TUTTI i prestiti presenti nel "file" (caricato in memoria)
        for (Prestito p : archivioDati.getLista()) {
            
            // CONDIZIONE 1: La matricola deve corrispondere
            boolean stessoUtente = p.getMatricola().equalsIgnoreCase(utenteSelezionato.getMatricola());
            
            // CONDIZIONE 2: Deve essere stato restituito (Data Restituzione != null)
            // Se è null, vuol dire che il prestito è ancora attivo e non va nello storico
            boolean restituito = p.getDataRestituzioneEffettiva() != null;
            
            if (stessoUtente && restituito) {
                listaStorico.add(p);
            }
        }

        tabellaStorico.setItems(listaStorico);
    }
}
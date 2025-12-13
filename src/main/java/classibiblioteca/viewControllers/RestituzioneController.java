/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.viewControllers;

import classibiblioteca.entita.Utente;
import classibiblioteca.entita.Libro;
import classibiblioteca.entita.Prestito;
import classibiblio.tipologiearchivi.ArchivioLibri;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.time.LocalDate;

public class SchedaRestituzioneController {

    @FXML private ComboBox<Utente> RestituzioneUtentiDispBox;
    @FXML private ComboBox<Libro> RestituzioneLibriDispBox;
    @FXML private Button btnConfermaRestituzione;

    private ArchivioLibri archiviolibri;
    private ArchivioUtenti archivioUtenti;
    private ArchivioPrestiti archivioPrestiti;
    private ObservableList<Utente> listaUtenti;
    private ObservableList<Libro> listaLibri;

    private Prestito prestitoSelezionato;

    public void initialize() {
        // Inizializzazione degli archivi e delle liste
        archiviolibri = new ArchivioLibri();
        archivioUtenti = new ArchivioUtenti();
        archivioPrestiti = new ArchivioPrestiti();

        // Ottieni i libri disponibili e carica solo i libri che hanno copie disponibili
        listaLibri = FXCollections.observableArrayList(archiviolibri.getLibriDisponibili()); // Libri con copie disponibili
        RestituzioneLibriDispBox.setItems(listaLibri); // Imposta i libri nel ComboBox

        // Se un prestito è stato selezionato, precompila i ComboBox con i dati del prestito
        if (prestitoSelezionato != null) {
            RestituzioneLibriDispBox.getSelectionModel().select(prestitoSelezionato.getLibro()); // Seleziona il libro
            RestituzioneUtentiDispBox.getSelectionModel().select(prestitoSelezionato.getUtente()); // Seleziona l'utente
            // Imposta la data di restituzione
            prestitoSelezionato.setDataRestituzioneEffettiva(LocalDate.now());
        } else {
            // Se nessun prestito è selezionato, lascia vuoti i ComboBox
            RestituzioneLibriDispBox.getSelectionModel().clearSelection();
            RestituzioneUtentiDispBox.getSelectionModel().clearSelection();
        }

        // Listener per quando l'utente seleziona un libro
        RestituzioneLibriDispBox.setOnAction(event -> aggiornaUtentiDisponibili());

        // Impostiamo l'azione del bottone
        btnConfermaRestituzione.setOnAction(event -> confermaRestituzione());
    }

    // Metodo per impostare il prestito selezionato dalla tabella (se presente)
    public void setPrestito(Prestito prestito) {
        this.prestitoSelezionato = prestito;
    }

    // Metodo per filtrare gli utenti in base al libro selezionato
    private void aggiornaUtentiDisponibili() {
        Libro libroSelezionato = RestituzioneLibriDispBox.getSelectionModel().getSelectedItem();
        
        if (libroSelezionato != null) {
            // Ottieni gli utenti che hanno il libro in prestito
            listaUtenti = FXCollections.observableArrayList(archivioPrestiti.getUtentiConLibroInPrestito(libroSelezionato));
            RestituzioneUtentiDispBox.setItems(listaUtenti); // Imposta gli utenti nel ComboBox
            RestituzioneUtentiDispBox.getSelectionModel().clearSelection(); // Pulisce la selezione
        }
    }

    // Conferma la restituzione
    private void confermaRestituzione() {
        // Se un prestito è stato selezionato, aggiorna il sistema
        if (prestitoSelezionato != null) {
            // Se la data di restituzione non è già impostata, impostala come data corrente
            if (prestitoSelezionato.getDataRestituzioneEffettiva() == null) {
                prestitoSelezionato.setDataRestituzioneEffettiva(LocalDate.now());
            }

            // Aggiorna il prestito nell'archivio
            archivioPrestiti.restituzioneLibro(prestitoSelezionato.getUtente().getMatricola(), prestitoSelezionato.getLibro().getISBN());

            // Recupera il libro restituito e riattiva la copia
            Libro libroRestituito = prestitoSelezionato.getLibro();
            libroRestituito.incrementaCopieDisponibili();
            archiviolibri.aggiornaLibro(libroRestituito);

            // Log di conferma
            System.out.println("Prestito restituito per l'utente: " + prestitoSelezionato.getUtente().getEmail());
            System.out.println("Libro restituito: " + libroRestituito.getTitolo());

            // Chiudi la finestra di dialogo
            Stage stage = (Stage) btnConfermaRestituzione.getScene().getWindow();
            stage.close();

        } else {
            Libro libroSelezionato = RestituzioneLibriDispBox.getSelectionModel().getSelectedItem();
            Utente utenteSelezionato = RestituzioneUtentiDispBox.getSelectionModel().getSelectedItem();

            if (libroSelezionato != null && utenteSelezionato != null) {
                Prestito nuovoPrestito = new Prestito(utenteSelezionato.getNome(), 
                                                      utenteSelezionato.getCognome(), 
                                                      utenteSelezionato.getEmail(),
                                                      libroSelezionato.getTitolo(), 
                                                      LocalDate.now(), 
                                                      LocalDate.now());
                archivioPrestiti.aggiungiPrestito(nuovoPrestito);
                libroSelezionato.incrementaCopieDisponibili();
                archiviolibri.aggiornaLibro(libroSelezionato);

                Stage stage = (Stage) btnConfermaRestituzione.getScene().getWindow();
                stage.close();
            } else {
                System.out.println("Errore: Devi selezionare un libro e un utente.");
            }
        }
    }
}

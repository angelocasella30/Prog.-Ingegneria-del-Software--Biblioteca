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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;

public class SchedaPrestitoController {

    @FXML
    private ComboBox<Utente> PrestitoUtentiDispBox;

    @FXML
    private ComboBox<Libro> PrestitoLibriDispBox;

    @FXML
    private Button btnConfermaPrestito;

    private ArchivioLibri archiviolibri;
    private ArchivioUtenti archivioUtenti;
    private ArchivioPrestiti archivioPrestiti;
    private ObservableList<Utente> listaUtenti;
    private ObservableList<Libro> listaLibri;
    private TextField PrestitoFldScadenza;

    // Metodo di inizializzazione
    public void initialize() {
        archiviolibri = new ArchivioLibri();
        archivioUtenti = new ArchivioUtenti();
        archivioPrestiti = new ArchivioPrestiti();

        listaUtenti = FXCollections.observableArrayList(archivioPrestiti.getUtentiConMenoDiTrePrestiti());

        listaLibri = FXCollections.observableArrayList(archiviolibri.getLibriDisponibili());

        // Imposta le ComboBox
        PrestitoUtentiDispBox.setItems(listaUtenti);
        PrestitoLibriDispBox.setItems(listaLibri);
        LocalDate oggi=LocalDate.now();
        LocalDate scadenza=oggi.plusDays(15);
        PrestitoFldScadenza.setValue(scadenza);

        // Impostiamo un listener per il bottone
        btnConfermaPrestito.setOnAction(event -> confermaPrestito());
    }

    private void confermaPrestito() {
        // Otteniamo l'utente e il libro selezionato
        Utente utenteSelezionato = PrestitoUtentiDispBox.getSelectionModel().getSelectedItem();
        Libro libroSelezionato = PrestitoLibriDispBox.getSelectionModel().getSelectedItem();

        if (utenteSelezionato != null && libroSelezionato != null) {

            Prestito nuovoPrestito = new Prestito(
            utenteSelezionato.getNome(), 
            utenteSelezionato.getCognome(), 
            utenteSelezionato.getEmail(), 
            libroSelezionato.getTitolo(), 
            LocalDate.now(), 
            LocalDate.now().plusDays(15)
        );
            archivioPrestiti.aggiungiPrestito(nuovoPrestito);

            // Logica per confermare il prestito 
            System.out.println("Prestito confermato per l'utente: " + utenteSelezionato.getEmail());
            System.out.println("Libro selezionato: " + libroSelezionato.getTitolo());

            // aggiorna in archivio
            libroSelezionato.decrementaCopieDisponibili();
            archiviolibri.aggiornaLibro(libroSelezionato);

            // Chiudi la finestra di dialogo
            Stage stage = (Stage) btnConfermaPrestito.getScene().getWindow();
            stage.close();
        } else {
            System.out.println("Errore: Devi selezionare un utente e un libro.");
        }
    }

    public void setDialogStage(Stage dialogStage) {
    }
}


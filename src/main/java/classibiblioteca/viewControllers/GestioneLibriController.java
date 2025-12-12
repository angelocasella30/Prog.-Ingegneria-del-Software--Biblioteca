package classibiblioteca.viewControllers;

import classibiblioteca.entita.Libro;
import classibiblio.tipologiearchivi.ArchivioLibri;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestioneLibriController implements Initializable {

    // --- Elementi FXML ---
    @FXML private TableView<Libro> tabellaLibri;
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutori;
    @FXML private TableColumn<Libro, String> colAnno; // Visualizzeremo l'anno come stringa
    @FXML private TableColumn<Libro, String> colISBN;
    @FXML private TableColumn<Libro, String> colCopie;
    
    @FXML private TextField txtRicerca;

    // --- Dati ---
    private ArchivioLibri archivio; // La tua classe di gestione
    private ObservableList<Libro> libriData; // La lista che vede JavaFX

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Inizializziamo l'archivio (in un'app reale lo caricheremmo da file/DB)
        archivio = new ArchivioLibri();
        libriData = FXCollections.observableArrayList();
        
        // Colleghiamo la lista alla tabella
        tabellaLibri.setItems(libriData);

        // 2. Configuriamo le colonne (Come leggere i dati dall'oggetto Libro)
        colTitolo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitolo()));
        colISBN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        
        // Per gli Autori (che sono una lista), li trasformiamo in stringa
        colAutori.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.join(", ", cellData.getValue().getAutori())));
            
        // Per l'Anno (LocalDate), mostriamo solo l'anno
        colAnno.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.valueOf(cellData.getValue().getDatapubbl().getYear())));
            
        // Per le Copie (int)
        colCopie.setCellValueFactory(cellData -> 
            new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroCopie())));
    }    

    // --- AZIONI BOTTONI ---

    @FXML
    private void handleCreaLibro(ActionEvent event) {
        System.out.println("DEBUG: Ho cliccato sul bottone Aggiungi!"); // <--- AGGIUNGI QUESTO

        boolean okClicked = showBookEditDialog(null);

        if (okClicked) {
            System.out.println("DEBUG: Libro salvato!"); // <--- AGGIUNGI QUESTO
            refreshTabella(); 
        }
    }
    
    @FXML
    private void handleModificaLibro(ActionEvent event) {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            boolean okClicked = showBookEditDialog(selezionato);
            if (okClicked) {
                // Aggiorniamo l'archivio con i nuovi dati (l'oggetto è lo stesso, quindi basta refresh)
                refreshTabella();
            }
        } else {
            mostraAvviso("Nessuna selezione", "Seleziona un libro dalla tabella per modificarlo.");
        }
    }

    @FXML
    private void handleEliminaLibro(ActionEvent event) {
        Libro selezionato = tabellaLibri.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            boolean rimosso = archivio.eliminaLibro(selezionato.getISBN());
            if (rimosso) {
                libriData.remove(selezionato);
            } else {
                mostraAvviso("Impossibile Eliminare", "Il libro ha copie in prestito o non esiste.");
            }
        } else {
            mostraAvviso("Nessuna selezione", "Seleziona un libro da eliminare.");
        }
    }
    
    @FXML
    private void handleCercaLibro(ActionEvent event) {
        String testoRicerca = txtRicerca.getText();
        
        // Se la barra di ricerca è vuota, mostra tutto
        if (testoRicerca == null || testoRicerca.trim().isEmpty()) {
            libriData.setAll(archivio.getLista());
        } else {
            // Altrimenti usa il metodo di ricerca dell'archivio
            libriData.setAll(archivio.ricercaLibro(testoRicerca));
        }
    }

    // --- MENU FILTRO / ORDINAMENTO (Che ti mancavano) ---
    
    @FXML
    private void handleOrdinaPerTitolo(ActionEvent event) {
        // Usiamo il metodo del tuo ArchivioLibri
        libriData.setAll(archivio.getLibriPerTitolo());
    }

    @FXML
    private void handleOrdinaPerAutore(ActionEvent event) {
        // Usiamo il metodo del tuo ArchivioLibri
        libriData.setAll(archivio.getLibriPerAutore());
    }

    @FXML
    private void handleMostraTutti(ActionEvent event) {
        // Ricarica la lista originale
        libriData.setAll(archivio.getLista());
    }
    
    

    // --- METODI DI SUPPORTO ---

    public boolean showBookEditDialog(Libro libro) {
        try {
            FXMLLoader loader = new FXMLLoader();
            // ATTENZIONE AL PERCORSO: Verifica che sia corretto!
            loader.setLocation(getClass().getResource("/classibiblioteca/views/BookEditDialog.fxml"));
            Pane page = (Pane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Scheda Libro");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabellaLibri.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BookEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLibro(libro);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                // Se è un NUOVO libro, dobbiamo aggiungerlo all'archivio e alla lista grafica
                if (libro == null) {
                    Libro nuovoLibro = controller.getLibro();
                    archivio.aggiungiLibro(nuovoLibro);
                    libriData.add(nuovoLibro);
                }
            }
            return controller.isOkClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void refreshTabella() {
        tabellaLibri.refresh();
    }
    
    private void mostraAvviso(String titolo, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
    
}
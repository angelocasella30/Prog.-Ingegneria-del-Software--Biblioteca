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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestioneLibriController implements Initializable {

    // --- NOMI CHE HAI CONFERMATO ---
    @FXML private TextField txtRicerca;     // ID confermato da te
    @FXML private MenuButton menuFiltro;    // ID confermato da te
    
    // ATTENZIONE: Nel file FXML che mi hai mandato la tabella si chiamava "tableviewLibri".
    // Se l'hai rinominata in "tabellaLibri" lascia così, altrimenti rimetti "tableviewLibri".
    @FXML private TableView<Libro> tableviewLibri; 
    
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutori;
    @FXML private TableColumn<Libro, String> colAnno;
    @FXML private TableColumn<Libro, String> colISBN;
    @FXML private TableColumn<Libro, String> colCopie;

    private String filtroAttivo = "GENERALE";
    private ArchivioLibri archivio;
    private ObservableList<Libro> libriData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // DIAGNOSI RAPIDA (Stampiamo se li trova)
        System.out.println("Cerco txtRicerca: " + (txtRicerca != null ? "TROVATO" : "NULL"));
        System.out.println("Cerco menuFiltro: " + (menuFiltro != null ? "TROVATO" : "NULL"));
        
        archivio = new ArchivioLibri();
        libriData = FXCollections.observableArrayList();
        libriData.setAll(archivio.getLista());
        
        tableviewLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Assicurati che questo nome corrisponda all'ID della tabella nel FXML
        if (tableviewLibri != null) {
            tableviewLibri.setItems(libriData);
        } else {
            System.out.println("ATTENZIONE: tableviewLibri è NULL. Controlla il nome della tabella in Scene Builder!");
        }

        // Configurazione Menu e Ricerca
        if (menuFiltro != null && txtRicerca != null) {
            menuFiltro.setText("Filtro");
            menuFiltro.disableProperty().bind(txtRicerca.textProperty().isEmpty());
        }

        // Configurazione Colonne
        colTitolo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitolo()));
        colISBN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        colAutori.setCellValueFactory(cellData -> new SimpleStringProperty(String.join(", ", cellData.getValue().getAutori())));
        colAnno.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDatapubbl().getYear())));
        colCopie.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroCopie())));
    }    

    @FXML
    private void handleCercaLibro(ActionEvent event) {
        System.out.println("--- CLICK TASTO CERCA ---");
        
        String testo = txtRicerca.getText();
        System.out.println("Testo cercato: " + testo);
        System.out.println("Filtro attivo: " + filtroAttivo);
        
        // Se la casella è vuota
        if (testo == null || testo.trim().isEmpty()) {
            System.out.println("Casella vuota -> Resetto tutto");
            menuFiltro.setText("Filtro");
            filtroAttivo = "GENERALE";
            libriData.setAll(archivio.getLista());
            return;
        }

        String testoMinuscolo = testo.toLowerCase();
        ObservableList<Libro> risultati = FXCollections.observableArrayList();
        
        // Stampiamo quanti libri ci sono in totale
        System.out.println("Libri totali in archivio: " + archivio.getLista().size());

        switch (filtroAttivo) {
            case "TITOLO":
                System.out.println("-> Entro nel caso TITOLO");
                for (Libro l : archivio.getLista()) {
                    // Stampa di controllo per ogni libro
                    // System.out.println("Controllo libro: " + l.getTitolo()); 
                    if (l.getTitolo().toLowerCase().contains(testoMinuscolo)) {
                        risultati.add(l);
                        System.out.println(">>> TROVATO: " + l.getTitolo());
                    }
                }
                break;

            case "AUTORE":
                System.out.println("-> Entro nel caso AUTORE");
                for (Libro l : archivio.getLista()) {
                    for (String autore : l.getAutori()) {
                        if (autore.toLowerCase().contains(testoMinuscolo)) {
                            risultati.add(l);
                            break; 
                        }
                    }
                }
                break;

            default:
                System.out.println("-> Entro nel caso GENERALE (Default)");
                risultati.setAll(archivio.ricercaLibro(testo));
                break;
        }

        System.out.println("Risultati trovati: " + risultati.size());
        
        // Aggiorna la tabella
        libriData.setAll(risultati);
        System.out.println("Tabella aggiornata.");
    }

    @FXML
    private void handleOrdinaPerTitolo(ActionEvent event) {
        menuFiltro.setText("Titolo");
        filtroAttivo = "TITOLO";
    }

    @FXML
    private void handleOrdinaPerAutore(ActionEvent event) {
        menuFiltro.setText("Autore");
        filtroAttivo = "AUTORE";
    }
    
    @FXML
    private void handleMostraTutti(ActionEvent event) {
        menuFiltro.setText("Filtro");
        filtroAttivo = "GENERALE";
    }

    @FXML
    private void handleCreaLibro(ActionEvent event) {
        boolean okClicked = showBookEditDialog(null);
        if (okClicked) {
            libriData.setAll(archivio.getLista());
        }
    }
    
    @FXML
    private void handleModificaLibro(ActionEvent event) {
        if (tableviewLibri == null) return;
        Libro selezionato = tableviewLibri.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            boolean okClicked = showBookEditDialog(selezionato);
            if (okClicked) {
                tableviewLibri.refresh();
            }
        } else {
            mostraAvviso("Nessuna selezione", "Seleziona un libro da modificare.");
        }
    }

    @FXML
    private void handleEliminaLibro(ActionEvent event) {
        if (tableviewLibri == null) return;
        
        Libro selezionato = tableviewLibri.getSelectionModel().getSelectedItem();
        
        if (selezionato != null) {
            // --- 1. CHIEDIAMO CONFERMA ---
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma Eliminazione");
            alert.setHeaderText("Stai per eliminare il libro: " + selezionato.getTitolo());
            alert.setContentText("Sei sicuro di voler procedere? L'operazione non è reversibile.");

            // Mostriamo l'avviso e aspettiamo la risposta
            // Se l'utente preme OK, il risultato sarà ButtonType.OK
            if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
                
                // --- 2. PROCEDIAMO CON L'ELIMINAZIONE ---
                boolean rimosso = archivio.eliminaLibro(selezionato.getISBN());
                
                if (rimosso) {
                    libriData.remove(selezionato);
                    // Opzionale: un messaggino di successo
                    // mostraAvviso("Successo", "Libro eliminato correttamente."); 
                } else {
                    mostraAvviso("Errore", "Impossibile eliminare il libro (forse ha prestiti attivi).");
                }
            }
            
        } else {
            mostraAvviso("Nessuna selezione", "Seleziona un libro da eliminare.");
        }
    }

    public boolean showBookEditDialog(Libro libro) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/classibiblioteca/views/BookEditDialog.fxml"));
            Pane page = (Pane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(libro == null ? "Nuovo Libro" : "Modifica Libro");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            
            // Protezione nel caso la tabella sia null
            if (tableviewLibri != null && tableviewLibri.getScene() != null) {
                dialogStage.initOwner(tableviewLibri.getScene().getWindow());
            }
            
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            BookEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLibro(libro);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                if (libro == null) {
                    archivio.aggiungiLibro(controller.getLibro());
                }
            }
            return controller.isOkClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void mostraAvviso(String titolo, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}
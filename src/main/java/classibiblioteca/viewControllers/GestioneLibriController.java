package classibiblioteca.viewControllers;

import classibiblio.Archivio;
import classibiblioteca.entita.Libro;
import classibiblio.tipologiearchivi.ArchivioLibri;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
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
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestioneLibriController implements Initializable {

    @FXML private TextField txtRicerca;
    @FXML private MenuButton menuFiltro;
    @FXML private Button btnCercaLibri;
    
    @FXML private TableView<Libro> tableviewLibri;
    
    @FXML private TableColumn<Libro, String> colTitolo;
    @FXML private TableColumn<Libro, String> colAutori;
    @FXML private TableColumn<Libro, String> colAnno;
    @FXML private TableColumn<Libro, String> colISBN;
    @FXML private TableColumn<Libro, String> colCopie;
    @FXML private TableColumn<Libro, String> colCopiePrestate;


    private String filtroAttivo = null;
    private ArchivioLibri archivioLibri;
    private ObservableList<Libro> libriData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inizializzazione GestioneLibri");

        // archivioLibri verrà iniettato da setArchivio() DOPO initialize()
        // quindi qui non fare nulla, aspetta che venga iniettato

        if (archivioLibri == null) {
            System.out.println("⚠️  Archivio ancora null, sarà iniettato dopo");
            archivioLibri = new ArchivioLibri(); // fallback temporaneo
        }

        libriData = FXCollections.observableArrayList();
        libriData.setAll(archivioLibri.getLista());
        
        // Setup tabella
        if (tableviewLibri != null) {
            tableviewLibri.setItems(libriData);
            tableviewLibri.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        } else {
            System.out.println("ATTENZIONE: tableviewLibri è NULL!");
        }
        
        // Disabilita la casella di ricerca di default
        if (txtRicerca != null) {
            txtRicerca.setDisable(true);
            txtRicerca.setStyle("-fx-opacity: 0.5;");
        }

        // Configurazione Menu
        if (menuFiltro != null) {
            menuFiltro.setText("Filtro");
        }
        
        // Disabilita il pulsante cerca finché il campo di testo è vuoto
        if (btnCercaLibri != null) {
            btnCercaLibri.disableProperty().bind(txtRicerca.textProperty().isEmpty());
        }

        // Configurazione Colonne
        colTitolo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitolo()));
        colISBN.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getISBN()));
        colAutori.setCellValueFactory(cellData -> new SimpleStringProperty(String.join(", ", cellData.getValue().getAutori())));
        colAnno.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getDatapubbl().getYear())));
        colCopie.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroCopie())));
        colCopiePrestate.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getCopiePrestate())));
        
        // Crea i menu item
        crearMenuFiltri();
    }

    // Metodo per creare i menu item
    private void crearMenuFiltri() {
        MenuItem itemTitolo = new MenuItem("Titolo");
        MenuItem itemAutore = new MenuItem("Autore");
        MenuItem itemNessuno = new MenuItem("Nessuno");

        itemTitolo.setOnAction(e -> {
            filtroAttivo = "TITOLO";
            menuFiltro.setText("Titolo");
            txtRicerca.setDisable(false);
            txtRicerca.setStyle("");
            txtRicerca.clear();
        });

        itemAutore.setOnAction(e -> {
            filtroAttivo = "AUTORE";
            menuFiltro.setText("Autore");
            txtRicerca.setDisable(false);
            txtRicerca.setStyle("");
            txtRicerca.clear();
        });

        itemNessuno.setOnAction(e -> {
            filtroAttivo = null;
            menuFiltro.setText("Filtro");
            txtRicerca.setDisable(true);
            txtRicerca.setStyle("-fx-opacity: 0.5;");
            txtRicerca.clear();
            libriData.setAll(archivioLibri.getLista());
        });

        menuFiltro.getItems().setAll(itemTitolo, itemAutore, itemNessuno);
    }

    @FXML
    private void handleCercaLibro(ActionEvent event) {
        System.out.println("--- CLICK TASTO CERCA ---");

        // Se nessun filtro è stato scelto, blocca la ricerca
        if (filtroAttivo == null) {
            mostraAvviso("Filtro non selezionato", "Seleziona un filtro dal menu prima di cercare.");
            return;
        }

        String testo = txtRicerca.getText();
        System.out.println("Testo cercato: " + testo);
        System.out.println("Filtro attivo: " + filtroAttivo);

        // Se la casella è vuota
        if (testo == null || testo.trim().isEmpty()) {
            System.out.println("Casella vuota -> Mostra tutti");
            libriData.setAll(archivioLibri.getLista());
            return;
        }

        String testoMinuscolo = testo.toLowerCase();
        ObservableList<Libro> risultati = FXCollections.observableArrayList();

        System.out.println("Libri totali in archivio: " + archivioLibri.getLista().size());

        switch (filtroAttivo) {
            case "TITOLO":
                System.out.println("-> Entro nel caso TITOLO");
                for (Libro l : archivioLibri.getLista()) {
                    if (l.getTitolo().toLowerCase().contains(testoMinuscolo)) {
                        risultati.add(l);
                        System.out.println(">>> TROVATO: " + l.getTitolo());
                    }
                }
                break;

            case "AUTORE":
                System.out.println("-> Entro nel caso AUTORE");
                for (Libro l : archivioLibri.getLista()) {
                    for (String autore : l.getAutori()) {
                        if (autore.toLowerCase().contains(testoMinuscolo)) {
                            risultati.add(l);
                            break;
                        }
                    }
                }
                break;

            default:
                System.out.println("-> Entro nel caso DEFAULT");
                break;
        }

        System.out.println("Risultati trovati: " + risultati.size());
        libriData.setAll(risultati);
        System.out.println("Tabella aggiornata.");
    }

    @FXML
    private void handleCreaLibro(ActionEvent event) {
        boolean okClicked = showBookEditDialog(null);
        if (okClicked) {
            libriData.setAll(archivioLibri.getLista());
            salvaArchivio();
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
                salvaArchivio();
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma Eliminazione");
            alert.setHeaderText("Stai per eliminare il libro: " + selezionato.getTitolo());
            alert.setContentText("Sei sicuro di voler procedere? L'operazione non è reversibile.");

            if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
                boolean rimosso = archivioLibri.eliminaLibro(selezionato.getISBN());
                
                if (rimosso) {
                    libriData.remove(selezionato);
                    salvaArchivio();
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
            if (libro == null) { // creazione nuovo
                boolean inserito = archivioLibri.aggiungiLibro(controller.getLibro());
                if (!inserito) {
                    mostraAvviso("ISBN duplicato",
                            "Esiste già un libro con questo ISBN. Modifica l'ISBN oppure modifica il libro esistente.");
                    return false; // non aggiornare tabella
                }
            }
        }
        return controller.isOkClicked();

    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

    
        private Archivio archivio;
        private Path savePath;
        
        public void setArchivio(Archivio archivio) {
            this.archivio = archivio;
            if (archivio != null) {
                this.archivioLibri = archivio.getArchivioLibri();
                // ✅ RICARICARE sempre
                libriData.setAll(archivioLibri.getLista());
                tableviewLibri.refresh();
                System.out.println("✓ GestioneLibri: Archivio iniettato e tabella aggiornata");
            }
        }

        public void setSavePath(Path savePath) {
            this.savePath = savePath;
        }


    // Metodo helper per salvare l'archivio
    private void salvaArchivio() {
        try {
            Archivio archivioGlobale = new Archivio(null, archivioLibri, null);
            archivioGlobale.salvaDefault();
            System.out.println("✓ Archivio Libri salvato");
        } catch (IOException e) {
            mostraAvviso("Errore", "Salvataggio fallito: " + e.getMessage());
            System.err.println("Errore salvataggio archivio: " + e.getMessage());
        }
    }
    
    private void mostraAvviso(String titolo, String contenuto) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titolo);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}

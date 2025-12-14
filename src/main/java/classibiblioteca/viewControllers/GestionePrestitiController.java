package classibiblioteca.viewControllers;

import classibiblio.Archivio;
import classibiblioteca.entita.Libro;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestionePrestitiController implements Initializable {

    // --- FXML: TOP ---
    @FXML private TextField txtRicerca;
    @FXML private MenuButton menuFiltro;
    @FXML private Button btnCercaPrestito;

    // --- FXML: TABLE ---
    @FXML private TableView<Prestito> tabellaPrestiti;
    @FXML private TableColumn<Prestito, String> colTitolo;
    @FXML private TableColumn<Prestito, String> colEmail;
    @FXML private TableColumn<Prestito, LocalDate> colDataInizio;
    @FXML private TableColumn<Prestito, LocalDate> colDataScadenza;
    @FXML private TableColumn<Prestito, LocalDate> colDataRestituzione;
    @FXML private TableColumn<Prestito, String> colStato;

    // --- Model condiviso ---
    private Archivio archivioFacade;
    private Path savePath;

    // --- Dati per TableView ---
    private final ObservableList<Prestito> masterData = FXCollections.observableArrayList();
    private FilteredList<Prestito> filteredData;

    // Stato UI: filtro ricerca
    private enum Filtro { NESSUNO, TITOLO }
    private Filtro filtroCorrente = Filtro.NESSUNO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inizializzazione GestionePrestiti");

        tabellaPrestiti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabellaPrestiti.setPlaceholder(new Label("Nessun contenuto nella tabella"));

        // Colonne
        colTitolo.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getTitololibro()));
        colEmail.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getEmailuser()));
        colDataInizio.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getDatainizio()));
        colDataScadenza.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getDataScadenzaPrevista()));
        colDataRestituzione.setCellValueFactory(cd -> new SimpleObjectProperty<>(cd.getValue().getDataRestituzioneEffettiva()));
        colStato.setCellValueFactory(cd -> new SimpleStringProperty(cd.getValue().getStato()));

        // Evita "null" in tabella per data restituzione
        colDataRestituzione.setCellFactory(col -> new TableCell<Prestito, LocalDate>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText("");
                else setText(item.toString());
            }
        });

        // Riga rossa: in ritardo
        tabellaPrestiti.setRowFactory(tv -> new TableRow<Prestito>() {
            @Override
            protected void updateItem(Prestito item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().remove("prestito-ritardo");
                if (empty || item == null) return;

                boolean attivo = (item.getDataRestituzioneEffettiva() == null);
                boolean inRitardoOra = attivo && item.isInRitardo();
                boolean restituitoInRitardo = !attivo
                        && item.getDataRestituzioneEffettiva() != null
                        && item.getDataScadenzaPrevista() != null
                        && item.getDataRestituzioneEffettiva().isAfter(item.getDataScadenzaPrevista());

                if (inRitardoOra || restituitoInRitardo) {
                    getStyleClass().add("prestito-ritardo");
                }
            }
        });

        // Filtered + Sorted
        filteredData = new FilteredList<>(masterData, p -> true);
        SortedList<Prestito> sorted = new SortedList<>(filteredData);
        sorted.comparatorProperty().bind(tabellaPrestiti.comparatorProperty());
        tabellaPrestiti.setItems(sorted);

        // Stato iniziale
        menuFiltro.setText("Filtro");
        txtRicerca.setDisable(true);
        if (btnCercaPrestito != null) btnCercaPrestito.setDisable(true);
        filtroCorrente = Filtro.NESSUNO;
    }


    // Iniezione dal Main/Test
    public void setArchivio(Archivio archivio) {
        this.archivioFacade = archivio;
        masterData.setAll(archivio.getArchivioPrestiti().getLista());
        tabellaPrestiti.refresh();
    }

    public void setSavePath(Path savePath) {
        this.savePath = savePath;
    }

    // --- FILTRI MENU ---

    @FXML
    private void handleFiltroTitolo(ActionEvent event) {
        filtroCorrente = Filtro.TITOLO;
        menuFiltro.setText("Titolo");

        txtRicerca.setDisable(false);
        if (btnCercaPrestito != null) btnCercaPrestito.setDisable(false);

        txtRicerca.requestFocus();
    }

    @FXML
    private void handleFiltroNessuno(ActionEvent event) {
        filtroCorrente = Filtro.NESSUNO;
        menuFiltro.setText("Filtro");

        txtRicerca.clear();
        txtRicerca.setDisable(true);
        if (btnCercaPrestito != null) btnCercaPrestito.setDisable(true);

        // lista completa
        filteredData.setPredicate(null);
    }

    // --- RICERCA ---
    @FXML
    private void handleCercaPrestito(ActionEvent event) {
        if (filtroCorrente != Filtro.TITOLO) return;

        String q = txtRicerca.getText();

        filteredData.setPredicate(p -> {
            if (q == null || q.trim().isEmpty()) return true;
            String s = q.toLowerCase();
            return p.getTitololibro() != null && p.getTitololibro().toLowerCase().contains(s);
        });
    }

    // --- BOTTONI LATERALI ---

    @FXML
    private void handleVisualizzaAttivi(ActionEvent event) {
        filteredData.setPredicate(p -> p.getDataRestituzioneEffettiva() == null);
    }

   @FXML
       private void handleRestituisciLibro(ActionEvent event) {
        if (archivioFacade == null) {
            mostraErrore("Archivio non inizializzato", "Archivio non caricato.");
            return;
        }

        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Nessuna selezione", "Seleziona un prestito dalla tabella.");
            return;
        }

        if (selezionato.getDataRestituzioneEffettiva() != null) {
            mostraErrore("Prestito già restituito", "Questo prestito risulta già chiuso.");
            return;
        }

        boolean eraInRitardo = selezionato.isInRitardo();

        // 1) chiudo il prestito
        selezionato.chiudiPrestito();

        // 2) aggiorno il libro: copiePrestate--
        Libro l = archivioFacade.getArchivioLibri().getLibroByISBN(selezionato.getISBN());
        if (l != null) {
            l.restituisci();
        }

        // 3) aggiorno l'utente
        Utente u = archivioFacade.getArchivioUtenti().getUtenteByMatricola(selezionato.getMatricola());
        if (u != null) {
            u.restituisciPrestito(selezionato);
        }

        tabellaPrestiti.refresh();
        salvaSuFileSilenzioso();

        if (eraInRitardo) {
            mostraInfo("Restituzione", "Prestito restituito oltre il limite previsto.");
        } else {
            mostraInfo("Restituzione", "Prestito restituito con successo.");
        }
    }


    @FXML
    private void handleCreaPrestito(ActionEvent event) {
        if (archivioFacade == null || savePath == null) {
            mostraErrore("Archivio non inizializzato", "Archivio non caricato.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/classibiblioteca/views/SchedaPrestito.fxml"));
            Pane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Crea Prestito");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            if (tabellaPrestiti.getScene() != null) {
                dialogStage.initOwner(tabellaPrestiti.getScene().getWindow());
            }

            dialogStage.setScene(new Scene(page));

            SchedaPrestitoController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setArchivio(archivioFacade);

            dialogStage.showAndWait();

            if (!controller.isOkClicked()) return;

            Prestito p = controller.getPrestitoCreato();
            if (p == null) return;

            Utente u = archivioFacade.getArchivioUtenti().getUtenteByMatricola(p.getMatricola());
            Libro  l = archivioFacade.getArchivioLibri().getLibroByISBN(p.getISBN());

            if (u == null || l == null) {
                mostraErrore("Errore", "Utente o Libro non trovati in archivio.");
                return;
            }

            // check finali
            if (!u.puoRichiederePrestito()) {
                mostraErrore("Utente non idoneo", "Limite prestiti o email non valida.");
                return;
            }
            if (!l.presta()) {
                mostraErrore("Libro non disponibile", "Non ci sono copie disponibili.");
                return;
            }

            archivioFacade.getArchivioPrestiti().aggiungiPrestito(p);
            u.aggiungiPrestito(p);

            masterData.setAll(archivioFacade.getArchivioPrestiti().getLista());
            tabellaPrestiti.refresh();
            Archivio.salva(archivioFacade, savePath);

            mostraInfo("Prestito creato", "Prestito creato correttamente.");

        } catch (Exception ex) {
            ex.printStackTrace();
            mostraErrore("Errore", "Impossibile aprire la finestra di creazione prestito: " + ex);
        }
    }

    // --- helper ---
    private void salvaSuFileSilenzioso() {
        if (archivioFacade == null || savePath == null) return;
        try {
            Archivio.salva(archivioFacade, savePath);
        } catch (IOException ex) {
            System.out.println("Errore salvataggio: " + ex.getMessage());
        }
    }

    private void mostraErrore(String titolo, String contenuto) {
        Alert a = new Alert(AlertType.ERROR);
        a.setTitle("Errore");
        a.setHeaderText(titolo);
        a.setContentText(contenuto);
        a.showAndWait();
    }

    private void mostraInfo(String titolo, String contenuto) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle(titolo);
        a.setHeaderText(null);
        a.setContentText(contenuto);
        a.showAndWait();
    }
}
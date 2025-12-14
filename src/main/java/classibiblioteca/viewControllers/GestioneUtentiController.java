package classibiblioteca.viewControllers;

import classibiblio.Archivio;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;
import classibiblio.tipologiearchivi.ArchivioUtenti;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GestioneUtentiController implements Initializable {

    @FXML private TextField txtRicerca;
    @FXML private Button btnCercaUtente;
    @FXML private MenuButton menuFiltro;

    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colEmail;
    @FXML private TableColumn<Utente, String> colPrestitiAttivi;

    // Master Data
    private final ObservableList<Utente> masterData = FXCollections.observableArrayList();
    private FilteredList<Utente> filteredData;

    private String filtroSelezionato = null;

    private ArchivioPrestiti archivioPrestitiGlobale;
    private ArchivioUtenti archivioUtenti;

    // Cache: matricola -> property testo prestiti attivi
    private final ObservableMap<String, SimpleStringProperty> prestitiAttiviByMatricola =
            FXCollections.observableHashMap();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Inizializzazione GestioneUtenti");

        // archivioUtenti e archivioPrestitiGlobale verranno iniettati da setArchivio()
        if (archivioUtenti == null || archivioPrestitiGlobale == null) {
            System.out.println("⚠️ Archivio ancora null, sarà iniettato dopo");
            archivioUtenti = new ArchivioUtenti();
            archivioPrestitiGlobale = new ArchivioPrestiti();
        }

        masterData.addAll(archivioUtenti.getLista());

        // --- Setup Colonne base ---
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colCognome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCognome()));
        colMatricola.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricola()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        if (tabellaUtenti != null) {
            tabellaUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        // --- Setup colonna Prestiti Attivi ---
        colPrestitiAttivi.setCellValueFactory(cellData -> getPrestitiAttiviProperty(cellData.getValue()));

        colPrestitiAttivi.setCellFactory(column -> new TableCell<Utente, String>() {
            private final Text text = new Text();

            {
                setText(null);
                text.wrappingWidthProperty().bind(widthProperty().subtract(10));
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(null);
                setGraphic(null);

                if (empty || item == null || item.trim().isEmpty()) {
                    return;
                }

                text.setText(item);
                setGraphic(text);
            }
        });

        // --- Disabilita la casella di ricerca di default ---
        if (txtRicerca != null) {
            txtRicerca.setDisable(true);
            txtRicerca.setStyle("-fx-opacity: 0.5;");
        }

        // Configurazione Menu
        if (menuFiltro != null) {
            menuFiltro.setText("Filtro");
        }

        // Disabilita il pulsante cerca finché il campo di testo è vuoto
        if (btnCercaUtente != null) {
            btnCercaUtente.disableProperty().bind(txtRicerca.textProperty().isEmpty());
        }

        // --- Filtri e Dati ---
        filteredData = new FilteredList<>(masterData, p -> true);
        SortedList<Utente> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabellaUtenti.comparatorProperty());
        tabellaUtenti.setItems(sortedData);

        tabellaUtenti.getSortOrder().add(colCognome);

        // Popola cache prestiti attivi all'avvio
        refreshPrestitiAttivi();

        // Crea i menu item
        crearMenuFiltri();
    }


    // Metodo per creare i menu item
    private void crearMenuFiltri() {
        MenuItem itemCognome = new MenuItem("Cognome");
        MenuItem itemMatricola = new MenuItem("Matricola");
        MenuItem itemNessuno = new MenuItem("Nessuno");

        itemCognome.setOnAction(e -> {
            filtroSelezionato = "COGNOME";
            menuFiltro.setText("Cognome");
            txtRicerca.setDisable(false);
            txtRicerca.setStyle("");
            txtRicerca.clear();
        });

        itemMatricola.setOnAction(e -> {
            filtroSelezionato = "MATRICOLA";
            menuFiltro.setText("Matricola");
            txtRicerca.setDisable(false);
            txtRicerca.setStyle("");
            txtRicerca.clear();
        });

        itemNessuno.setOnAction(e -> {
            filtroSelezionato = null;
            menuFiltro.setText("Filtro");
            txtRicerca.setDisable(true);
            txtRicerca.setStyle("-fx-opacity: 0.5;");
            txtRicerca.clear();
            filteredData.setPredicate(u -> true);
        });

        menuFiltro.getItems().setAll(itemCognome, itemMatricola, itemNessuno);
    }

    // ---------- Cache prestiti attivi ----------
    private SimpleStringProperty getPrestitiAttiviProperty(Utente u) {
        if (u == null || u.getMatricola() == null) return new SimpleStringProperty("");
        String key = u.getMatricola().trim().toLowerCase();
        return prestitiAttiviByMatricola.computeIfAbsent(key, k -> new SimpleStringProperty(""));
    }

    public void refreshPrestitiAttivi() {
        Map<String, StringBuilder> tmp = new HashMap<>();

        List<Prestito> tuttiPrestiti = archivioPrestitiGlobale.getLista();
        for (Prestito p : tuttiPrestiti) {
            if (p == null) continue;
            if (p.getDataRestituzioneEffettiva() != null) continue;
            if (p.getMatricola() == null) continue;

            String key = p.getMatricola().trim().toLowerCase();

            tmp.computeIfAbsent(key, k -> new StringBuilder())
               .append("• ").append(p.getTitololibro())
               .append("\n  Scad: ").append(p.getDataScadenzaPrevista())
               .append("\n");
        }

        prestitiAttiviByMatricola.values().forEach(prop -> prop.set(""));

        tmp.forEach((key, sb) -> prestitiAttiviByMatricola
                .computeIfAbsent(key, k -> new SimpleStringProperty(""))
                .set(sb.toString().trim())
        );

        if (tabellaUtenti != null) {
            tabellaUtenti.refresh();
        }
    }

    // ---------- Ricerca ----------
    @FXML
    private void handleCercaUtente(ActionEvent event) {
        // Se nessun filtro è stato scelto, blocca la ricerca
        if (filtroSelezionato == null) {
            mostraErrore("Filtro non selezionato", "Seleziona un filtro dal menu prima di cercare.");
            return;
        }

        String testo = (txtRicerca.getText() == null) ? "" : txtRicerca.getText().trim();

        // Se campo vuoto: mostra tutti
        if (testo.isEmpty()) {
            filteredData.setPredicate(u -> true);
            return;
        }

        String lowerText = testo.toLowerCase();

        filteredData.setPredicate(utente -> {
            if (utente == null) return false;

            String nome = utente.getNome() == null ? "" : utente.getNome().toLowerCase();
            String cognome = utente.getCognome() == null ? "" : utente.getCognome().toLowerCase();
            String matricola = utente.getMatricola() == null ? "" : utente.getMatricola().toLowerCase();
            String email = utente.getEmail() == null ? "" : utente.getEmail().toLowerCase();

            // Se nessun filtro: ricerca generale
            if (filtroSelezionato == null) {
                return nome.contains(lowerText)
                        || cognome.contains(lowerText)
                        || matricola.contains(lowerText)
                        || email.contains(lowerText);
            }

            switch (filtroSelezionato) {
                case "COGNOME":
                    return cognome.contains(lowerText);
                case "MATRICOLA":
                    return matricola.contains(lowerText);
                default:
                    return nome.contains(lowerText)
                            || cognome.contains(lowerText)
                            || matricola.contains(lowerText)
                            || email.contains(lowerText);
            }
        });
    }

    @FXML
    private void handleCreaUtente(ActionEvent event) {
        boolean okClicked = showUserEditDialog(null);
        if (okClicked) {
            salvaArchivio();
        }
    }

    @FXML
    private void handleModificaUtente(ActionEvent event) {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Nessuna selezione", "Seleziona un utente per modificarlo.");
            return;
        }
        boolean okClicked = showUserEditDialog(selezionato);
        if (okClicked) {
            tabellaUtenti.refresh();
            tabellaUtenti.sort();
            salvaArchivio();
        }
    }

    @FXML
    private void handleEliminaUtente(ActionEvent event) {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Nessuna selezione", "Seleziona un utente per eliminarlo.");
            return;
        }

        // Chiedi conferma
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Eliminazione");
        alert.setHeaderText("Stai per eliminare l'utente: " + selezionato.getCognome() + " " + selezionato.getNome());
        alert.setContentText("Sei sicuro di voler procedere? L'operazione non è reversibile.");

        if (alert.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            masterData.remove(selezionato);
            archivioUtenti.getLista().remove(selezionato);
            System.out.println("Utente eliminato: " + selezionato.getMatricola());
            salvaArchivio();
        }
    }

   @FXML
private void handleVisualizzaStorico(ActionEvent event) {
    Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
    if (selezionato == null) {
        mostraErrore("Nessuna selezione", "Seleziona un utente.");
        return;
    }

    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/classibiblioteca/views/StoricoUtente.fxml"));
        Pane page = loader.load();

        StoricoUtenteController controller = loader.getController();
        controller.setArchivioPrestiti(archivioPrestitiGlobale);
        controller.initData(selezionato);

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Storico Prestiti Utente");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        if (tabellaUtenti.getScene() != null) {
            dialogStage.initOwner(tabellaUtenti.getScene().getWindow());
        }
        dialogStage.setScene(new Scene(page));
        dialogStage.showAndWait();

    } catch (IOException e) {
        e.printStackTrace();
        mostraErrore("Errore Caricamento", "Impossibile aprire lo storico: " + e.getMessage());
    }
}


    @FXML
    private void handleMostraTutti(ActionEvent event) {
        txtRicerca.setText("");
        filteredData.setPredicate(null);
    }

    private boolean showUserEditDialog(Utente utente) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/classibiblioteca/views/UserEditDialog.fxml"));
            Pane page = (Pane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(utente == null ? "Nuovo Utente" : "Modifica Utente");
            dialogStage.initModality(Modality.WINDOW_MODAL);

            if (tabellaUtenti.getScene() != null) {
                dialogStage.initOwner(tabellaUtenti.getScene().getWindow());
            }

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            UserEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setListaUtentiEsistenti(masterData);
            controller.setUtente(utente);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                if (utente == null) {
                    Utente nuovoUtente = controller.getUtente();
                    masterData.add(nuovoUtente);
                    archivioUtenti.getLista().add(nuovoUtente);
                    tabellaUtenti.sort();
                    tabellaUtenti.refresh();
                }
                return true;
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
            mostraErrore("Errore Caricamento", "Errore: " + e.getMessage());
            return false;
        }
    }
    
        private Archivio archivio;
        private Path savePath;

        public void setArchivio(Archivio archivio) {
        this.archivio = archivio;
        if (archivio != null) {
            this.archivioUtenti = archivio.getArchivioUtenti();
            this.archivioPrestitiGlobale = archivio.getArchivioPrestiti();

            // ✅ RICARICARE sempre per sincronizzare
            masterData.setAll(archivioUtenti.getLista());

            tabellaUtenti.refresh();
            refreshPrestitiAttivi();
            System.out.println("✓ GestioneUtenti: Archivio iniettato e tabella aggiornata");
        }
        }


        public void setSavePath(Path savePath) {
            this.savePath = savePath;
        }



    // Metodo helper per salvare l'archivio
    private void salvaArchivio() {
        try {
            Archivio archivioGlobale = new Archivio(archivioUtenti, null, archivioPrestitiGlobale);
            archivioGlobale.salvaDefault();
            System.out.println("✓ Archivio Utenti salvato");
        } catch (IOException e) {
            mostraErrore("Errore", "Salvataggio fallito: " + e.getMessage());
            System.err.println("Errore salvataggio archivio: " + e.getMessage());
        }
    }

    private void mostraErrore(String titolo, String contenuto) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(titolo);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}

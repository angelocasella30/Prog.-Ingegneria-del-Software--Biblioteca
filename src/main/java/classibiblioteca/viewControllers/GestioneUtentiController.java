package classibiblioteca.viewControllers;

import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;

import java.io.IOException;
import java.net.URL;
import java.util.List;
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

    // --- 1. MODIFICA QUI: Rimossi riferimenti a AnchorPane e aggiornati gli ID ---
    @FXML private TextField txtRicerca;     // ID nel FXML: txtRicerca
    @FXML private Button btnCercaUtente;    // ID nel FXML: btnCercaUtente
    @FXML private MenuButton menuFiltro;

    @FXML private TableView<Utente> tabellaUtenti;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TableColumn<Utente, String> colCognome;
    @FXML private TableColumn<Utente, String> colMatricola;
    @FXML private TableColumn<Utente, String> colEmail;
    @FXML private TableColumn<Utente, Utente> colPrestitiAttivi;

    // Master Data
    private ObservableList<Utente> masterData = FXCollections.observableArrayList();
    
    private FilteredList<Utente> filteredData;
    private String filtroSelezionato = null;
    private ArchivioPrestiti archivioPrestitiGlobale;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archivioPrestitiGlobale = new ArchivioPrestiti();

        // Carica qui i dati dall'archivio se necessario
        // masterData.addAll(ArchivioUtenti.getInstance().getLista());

        // --- Setup Colonne ---
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colCognome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCognome()));
        colMatricola.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMatricola()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        
        tabellaUtenti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // --- Setup Colonna Prestiti ---
        colPrestitiAttivi.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        colPrestitiAttivi.setCellFactory(column -> new TableCell<Utente, Utente>() {
            @Override
            protected void updateItem(Utente utente, boolean empty) {
                super.updateItem(utente, empty);
                if (empty || utente == null) {
                    setGraphic(null);
                    return;
                }
                List<Prestito> tuttiPrestiti = archivioPrestitiGlobale.getLista();
                StringBuilder sb = new StringBuilder();
                for (Prestito p : tuttiPrestiti) {
                    if (p.getMatricola() != null && utente.getMatricola() != null &&
                        p.getMatricola().equalsIgnoreCase(utente.getMatricola()) 
                            && p.getDataRestituzioneEffettiva() == null) {
                        sb.append("• ").append(p.getTitololibro())
                          .append("\n   Scad: ").append(p.getDataScadenzaPrevista()).append("\n");
                    }
                }
                if (sb.length() == 0) {
                    setText(null);
                } else {
                    Text textNode = new Text(sb.toString());
                    textNode.wrappingWidthProperty().bind(colPrestitiAttivi.widthProperty().subtract(10));
                    setGraphic(textNode);
                }
            }
        });

        // --- Filtri e Dati ---
        filteredData = new FilteredList<>(masterData, p -> true);
        SortedList<Utente> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabellaUtenti.comparatorProperty());
        tabellaUtenti.setItems(sortedData);

        // --- Menu Filtri ---
        MenuItem itemCognome = new MenuItem("Cognome");
        MenuItem itemMatricola = new MenuItem("Matricola");
        MenuItem itemNessuna = new MenuItem("Nessuna");

        // --- 2. MODIFICA QUI: Aggiornati i riferimenti a txtRicerca (nuovo nome) ---
        itemCognome.setOnAction(e -> {
            filtroSelezionato = "COGNOME";
            menuFiltro.setText("Filtro: Cognome");
            txtRicerca.setText("");        // Corretto
            filteredData.setPredicate(null);
        });

        itemMatricola.setOnAction(e -> {
            filtroSelezionato = "MATRICOLA";
            menuFiltro.setText("Filtro: Matricola");
            txtRicerca.setText("");        // Corretto
            filteredData.setPredicate(null);
        });
        
        itemNessuna.setOnAction(e -> {
            filtroSelezionato = null;
            menuFiltro.setText("Filtro");
            txtRicerca.setText("");        // Corretto
            filteredData.setPredicate(null);
        });

        menuFiltro.getItems().setAll(itemCognome, itemMatricola, itemNessuna);
        tabellaUtenti.getSortOrder().add(colCognome);
    }    

    @FXML
    private void handleCercaUtente(ActionEvent event) {
        // --- 3. MODIFICA QUI: Aggiornato riferimento ---
        String testo = txtRicerca.getText(); 

        if (filtroSelezionato == null) {
            mostraErrore("Filtro mancante", "Seleziona un filtro dal menu per cercare.");
            return;
        }

        filteredData.setPredicate(utente -> {
            if (testo == null || testo.isEmpty()) return true;
            String lowerText = testo.toLowerCase();

            if (filtroSelezionato.equals("COGNOME")) {
                return utente.getCognome().toLowerCase().contains(lowerText);
            } else if (filtroSelezionato.equals("MATRICOLA")) {
                return utente.getMatricola().toLowerCase().contains(lowerText);
            }
            return false;
        });
    }

    @FXML
    private void handleCreaUtente(ActionEvent event) {
        showUserEditDialog(null);
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
        }
    }

    @FXML
    private void handleEliminaUtente(ActionEvent event) {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Nessuna selezione", "Seleziona un utente per eliminarlo.");
            return;
        }
        masterData.remove(selezionato);
        System.out.println("Utente eliminato: " + selezionato.getMatricola());
    }

    @FXML
    private void handleVisualizzaStorico(ActionEvent event) {
        Utente selezionato = tabellaUtenti.getSelectionModel().getSelectedItem();
        if (selezionato == null) {
            mostraErrore("Nessuna selezione", "Seleziona un utente.");
            return;
        }
        boolean haStorico = false;
        for (Prestito p : archivioPrestitiGlobale.getLista()) {
            if (p.getMatricola().equalsIgnoreCase(selezionato.getMatricola()) && p.getDataRestituzioneEffettiva() != null) {
                haStorico = true;
                break;
            }
        }
        if (!haStorico) {
            mostraErrore("Storico Vuoto", "L'utente non ha mai restituito libri.");
            return;
        }
        System.out.println("Visualizzo storico per: " + selezionato.getCognome());
    }

    @FXML
    private void handleMostraTutti(ActionEvent event) {
        txtRicerca.setText(""); // Corretto
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

    private void mostraErrore(String titolo, String contenuto) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(titolo);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
}
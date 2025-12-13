/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.viewControllers;

/**
 *
 * @author Utente
 */
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
public class GestionePrestitiController implements Initializable {

    @FXML private TableView<Prestito> tabellaPrestiti;
    @FXML private TableColumn<Prestito, String> colEmail;
    @FXML private TableColumn<Prestito, String> colMatricola;
    @FXML private TableColumn<Prestito, String> colTitolo;
    @FXML private TableColumn<Prestito, String> colISBN;
    @FXML private TableColumn<Prestito, String> colDataInizio;
    @FXML private TableColumn<Prestito, String> colDataFine;
    @FXML private TableColumn<Prestito, String> colDataRestituzione;

    @FXML private Button btnCreaPrestito;
    @FXML private Button btnRestituisci;
    @FXML private Button btnVisualizzaPrestiti;
    @FXML private Button btnVisualizzaRestituzioni;
    @FXML private Button btnCercaHomePrestito;
    @FXML private TextField fldcercaPrestito; 
    @FXML MenuButton RicercamenuButtonPrestito;
    @FXML MenuItem Matricola;
    @FXML MenuItem ISBN;
    
    @FXML private TextField fldCercaPrestito;
    private String tipoRicerca = "matricola";
    private ArchivioPrestiti archivioPrestiti;
    private ObservableList<Prestito> prestiti;

    private Utente utenteSelezionato;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        archivioPrestiti = new ArchivioPrestiti();
        prestiti = FXCollections.observableArrayList();

        tabellaPrestiti.setItems(prestiti);
        configuraColonne();
        caricaPrestiti();
        
        Matricola.setOnAction(event -> tipoRicerca = "matricola");
        ISBN.setOnAction(event -> tipoRicerca = "ISBN");
        btnCercaHomePrestito.setOnAction(event->handleCercaPrestito(event));
    }
    // Carica i prestiti attivi nella TableView
    private void caricaPrestiti() {
        if (utenteSelezionato != null) {
            prestiti.setAll(archivioPrestiti.getPrestitiAttiviPerUtente(utenteSelezionato)); // Carica i prestiti per l'utente selezionato
     }
    }

    // Configura le colonne della TableView
    private void configuraColonne() {
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUtente().getEmail())); // Email dell'utente
        colMatricola.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUtente().getMatricola())); // Matricola dell'utente
        colTitolo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitololibro())); // Titolo del libro
        colISBN.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getISBN())); // ISBN del libro
        colDataInizio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDatainizio().toString())); // Data inizio
        colDataFine.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDataScadenzaPrevista().toString())); // Data fine
        colDataRestituzione.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getDataRestituzioneEffettiva() != null ? c.getValue().getDataRestituzioneEffettiva().toString() : "Non restituito")); // Data restituzione
    
        tabellaPrestiti.setRowFactory(tableview->{
            TableRow<Prestito>row= new TableRow<>();
            row.itemProperty().addListener((obs,oldItem,newItem)->{
                if(newItem!=null && newItem.isInRitardo()){
                    row.setStyle("-fx-background-color:red; -fx-text-fill:gray");
                }else{
                    row.setStyle("");
                }
            });
            return row;
        });
    }
   
    // Gestisce la creazione di un nuovo prestito
    @FXML
    private void handleCreaPrestito(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/classibiblioteca/views/creaprestito.fxml"));
            Pane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Crea Nuovo Prestito");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabellaPrestiti.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            SchedaPrestitiController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            tabellaPrestiti.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleCercaPrestito(ActionEvent event) {
        String testo = fldCercaPrestito.getText();
        if (testo.isEmpty()) {
            caricaPrestiti();
        } else {
        if (tipoRicerca.equals("matricola")) {
            prestiti.setAll(archivioPrestiti.ricercaPrestitoPerMatricola(testo)); // Ricerca per matricola
        } else if (tipoRicerca.equals("ISBN")) {
            prestiti.setAll(archivioPrestiti.ricercaPrestitoPerISBN(testo)); // Ricerca per ISBN
        }
    }
    tabellaPrestiti.refresh(); 
}
    // Carica solo i prestiti restituiti
    @FXML
    private void handleVisualizzaRestituzioni(ActionEvent event) {
    
        prestiti.setAll(archivioPrestiti.getPrestitiRestituiti()); // Chiama il metodo aggiunto in ArchivioPrestiti
        tabellaPrestiti.refresh(); 
}

    @FXML
    private void handleVisualizzaPrestitiOrdinatiScadenza(ActionEvent event) {
    
        prestiti.setAll(archivioPrestiti.getPrestitiOrdinatiPerScadenza()); // Chiama il metodo aggiunto in ArchivioPrestiti
        tabellaPrestiti.refresh();  
}
    // Gestisce la restituzione di un prestito
    @FXML
    private void handleRestituisci(ActionEvent event) {
        Prestito selezionato = tabellaPrestiti.getSelectionModel().getSelectedItem();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/classibiblioteca/views/restituzione_prestito.fxml"));
            Pane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Restituzione Prestito");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabellaPrestiti.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            RestituzioneController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            if (selezionato != null) {
            controller.setPrestito(selezionato); 
            }
            dialogStage.showAndWait();
            if(selezionato!=null && selezionato.getDataRestituzioneEffettiva()!= null){
                prestiti.remove(selezionato);
                tabellaPrestiti.refresh();
            }
        } catch (IOException e){
             e.printStackTrace();
    }
        
}} 
}

package classibiblio.controllers;

import classibiblioteca.entita.Libro;
import classibiblio.tipologiearchivi.ArchivioLibri;
import javafx.collections.transformation.SortedList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * Controller che gestisce operazioni su libri
 * come la creazione, modifica,elimina dei libri
 */
public class GestioneLibriController {

    @FXML
    private TextField txtRicerca;
    
    @FXML
    private TextField fldTitolo;
    
    @FXML
    private TextField fldAutori;
    
    @FXML
    private TextField fldAnno;
    
    @FXML
    private TextField fldISBN;
    
    @FXML
    private TextField fldScISBNLibro;
    
    @FXML
    private TextField fldScAnnoLibro;
    
    @FXML
    private TextField fldScAutoriLibro;
    
    @FXML
    private TextField fldScTitoloLibro;
    
    @FXML
    private TextField fldScCopieDLibro;
    
    @FXML
    private TextField fldScCopiePLibro;
    
    
    @FXML
    private TextField fldCopieDisponibile;
    
    @FXML
    private TextField fldCopiePrestate;
    
    @FXML
    private TableView<Libro> tableLibri;

    @FXML
    private TableColumn<Libro, String> colISBN;

    @FXML
    private TableColumn<Libro, String> colTitolo;

    @FXML
    private TableColumn<Libro, String> colAutori;

    @FXML
    private TableColumn<Libro, String> colAnno; // O Integer/LocalDate a seconda del tipo

    @FXML
    private TableColumn<Libro, Integer> colCopieP;
    
    @FXML
    private TableColumn<Libro, Integer> colCopieD;
    
    @FXML
    private Pane schedaLibro;

    private ArchivioLibri archivioLibri;
    
    private boolean validateFormLibri() {
    String titolo = fldTitolo.getText();
    String isbn = fldISBN.getText();

    //da completare
    return true;
    }

/**
 * Metodo initData
 * 
 * collega archivio libri 
 */
    // Metodo per ricevere i dati dal MainController
    public void initData(ArchivioLibri archivio) {
        this.archivioLibri = archivio;
    }
    private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
/**
 * Metodo initialize
 * 
 * inizializza la tabella di dati archivio libri
 */
    
    @FXML
    public void initialize() {
        // TODO: Collegare le colonne agli attributi della classe Libro
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colAutori.setCellValueFactory(new PropertyValueFactory<>("autori"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        colAnno.setCellValueFactory(new PropertyValueFactory<>("anno"));
        colCopieP.setCellValueFactory(new PropertyValueFactory<>("Copie prestate"));
        colCopieD.setCellValueFactory(new PropertyValueFactory<>("Copie disponibili"));
        if (tableLibri.getColumns().isEmpty()) {
            tableLibri.getColumns().addAll(colTitolo,colAutori,colISBN,colAnno,colCopieP,colCopieD);
    }
        tableLibri.setItems(FXCollections.observableArrayList());
         tableLibri.setOnMouseClicked(event -> { if (event.getClickCount() == 1) { 
                Libro LibroSelezionato = tableLibri.getSelectionModel().getSelectedItem();
                String ISBN = libroSelezionato.getISBN();
                Libro libro = archivioLibro.getLibroByISBN(ISBN);
                fldTitolo.setText(libro.getTitolo());
                fldAutori.setText(libro.getAutori());
                fldISBN.setText(libro.getISBN());
                fldAnno.setText(libro.getAnno());
                fldCopieP.setText(libro.getCopieP());
                fldCopieD.setText(libro.getCopieD());
            
    };
    }
/**
 * Metodo handleCerca gestisce la ricerca libro
 * 
 * gestisce la ricerca titolo,autore,isbn in ordine alfabetico
 * controllo di eccezione per libro inesistente o input errato
 */

    @FXML
    void handleCercaLibro(ActionEvent event) {
        if (archivioLibri == null) return; 
        
        String cercaTitoloLibro = fldTitolo.getText().trim().toLowerCase();
        String cercaAutoreLibro = fldAutori.getText().trim().toLowerCase();
        String cercaISBNLibro = fldISBN.getText().trim().toLowerCase();

        if (cercaTitoloLibro.isEmpty() && cercaAutoreLibro.isEmpty() && cercaISBNLibro.isEmpty()) {
            return;
        }
        ObservableList<Libro> listaLibri = FXCollections.observableArrayList(ArchivioLibri.getLista());

        FilteredList<Libro> filteredList = new FilteredList<>(listaLibri, libro -> true);
        SortedList<Libro> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableLibri.comparatorProperty());
        tableLibri.setItems(sortedList);
       }
)
    }
/**
 * Metodo handleReset gestisce l'evento annullamento dati
 * 
 * gestisce lo svuotamento di campi del form libro 
 */
    
    @FXML
    void handleResetLibro(ActionEvent event) {
        fldScTitoloLibro.clear();
        fldScAnnoLibro.clear();
        fldScAutoriLibro.clear();
        fldScCopiePLibro.clear();
        fldScCopieDLibro.clear();
        fldScISBNLibro.clear();
        handleVisualizzaLibri();
    }
/**
 * Metodo handleNuvo gestisce l'evento creazione libro
 * 
 * gestisce form nuovo con controllo dati archivio per coincidenza
 * isbn come eccezione
 */
    
    @FXML
    void handleCreaLibro(ActionEvent event) {
        schedaLibro.setVisible(true);
    }
/**
 * Metodo handleModifica gestisce l'evento modifca libro
 * 
 * gestisce la modifica del form dettagli libro
 */
    
    @FXML
    void handleModificaLibro(ActionEvent event) {
        Libro libroSelezionato = tableLibri.getSelectionModel().getSelectedItem();
        if (libroSelezionato == null) {
            showAlert("Attenzione", "Nessun libro selezionato");
            return;
        }
        String ISBN = libroSelezionato.getISBN();

        Libro libro = archivioLibri.getLibroByISBN(ISBN);
        fldScISBNLibro.setText(libro.getISBN());
        fldScTitoloLibro.setText(libro.getTitolo());
        fldScAnnoLibro.setText(libro.getAnno());
        fldScAutoriLibro.setText(libro.getAutori());
        fldScCopiePLibro.setText(libro.getCopieP());
        fldScCopieDLibro.setText(libro.getCopieD());
        schedaLibro.setVisible(true);
    }
/**
 * Metodo handleElimina gestisce l'evento eliminalibro
 * 
 * gestisce l'eliminazione di libro con attenzione di eccezione
 * per libri in prestito
 */
    
    @FXML
    void handleEliminaLibro(ActionEvent event) {
        Libro libroSelezionato = tableLibri.getSelectionModel().getSelectedItem();

        if (libroSelezionato == null) {
            showAlert("Attenzione", "Nessun libro selezionato");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("libro selezionato sarà eliminato permanentemente.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean libroRimosso = archivioLibri.eliminaLibro(libroSelezionato);
            if (libroRimosso) {
                tableLibri.getItems().remove(libroSelezionato);
                showAlert("Successo", "libro è stato eliminato.");
            } else {
                showAlert("Errore", "Ci sono prestiti in corso");
            }
        }
    });
    }
    }
    
    
    @FXML
    void handleSalvaFormLibro(ActionEvent event) {
        String titolo = fldScTitoloLibro.getText().trim();
        String autore = fldScAutoreLibro.getText().trim();
        String isbn = fldScISBNLibro.getText().trim();
        String anno = fldScAnnoLibro.getText().trim();
        String copieD = fldScCopieDLibro.getText().trim();
        String copieP = fldScCopiePLibro.getText().trim();
        if (titolo.isEmpty() || autore.isEmpty() || isbn.isEmpty() || anno.isEmpty() || copieD.isEmpty() || copieP.isEmpty()) {
            showAlert("Attenzione", "Ci sono campi vuoti");
            return;
        }
        if (archivioLibri.existByIsbn(isbn)) {
            showAlert("Attenzione", "isbn esiste");
            return;
        }
        try {
        int copieDisponibili = Integer.parseInt(copieD);  
        int copiePrestate = Integer.parseInt(copieP); 
        if (copieDisponibili < 0 || copiePrestate < 0) {
            showAlert("Attenzione", "copie non possono essere negative");
            return;
        } catch (NumberFormatException e) { 
            showAlert("Attenzione", "form non validi");
            return;
        }
        Libro libro = new Libro(titolo, autore, isbn, anno, copieD, copieP);
        archivioLibri.aggiungiLibro(newLibro);
        tableLibri.setItems(archivioLibri.getLibriByTitolo());
        schedaUser.setVisible(false);
    }
    
    @FXML
    public void handleVisualizzaLibri() {
        ObservableList<Libro> libri = FXCollections.observableArrayList(archivioLibri.getLibri());
        tableLibri.setItems(libri);
    }
}
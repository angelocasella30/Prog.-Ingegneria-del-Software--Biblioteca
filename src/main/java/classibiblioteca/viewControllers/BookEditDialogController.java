package classibiblioteca.viewControllers;

import classibiblioteca.entita.Libro;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookEditDialogController implements Initializable {

    @FXML private Label lblTitolo; 
    @FXML private TextField fldTitoloScLibro;
    @FXML private TextField fldNuovoAutore;
    @FXML private ListView<String> listAutori;
    @FXML private Button btnAggiungiAutore;
    @FXML private Button btnRimuoviAutore;
    @FXML private TextField fldAnnoScLibro;
    @FXML private TextField fldISBNScLibro;
    @FXML private TextField fldICopieDisponScLibro;
    @FXML private Button btnSalvaScLibro;

    private Stage dialogStage;
    private Libro libro;
    private boolean okClicked = false;
    private ObservableList<String> autoriList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inizializza la ListView degli autori
        autoriList = FXCollections.observableArrayList();
        listAutori.setItems(autoriList);
        
        btnSalvaScLibro.setOnAction(event -> handleSalva());
    }    

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
        autoriList.clear();

        if (libro == null) {
            // --- NUOVO LIBRO ---
            lblTitolo.setText("NUOVO LIBRO");
            fldTitoloScLibro.setText("");
            fldNuovoAutore.setText("");
            fldAnnoScLibro.setText("");
            fldISBNScLibro.setText("");
            fldICopieDisponScLibro.setText("");
        } else {
            // --- MODIFICA LIBRO ---
            lblTitolo.setText("MODIFICA LIBRO");
            fldTitoloScLibro.setText(libro.getTitolo());
            
            // Carica gli autori nella ListView
            autoriList.addAll(libro.getAutori());
            
            fldAnnoScLibro.setText(String.valueOf(libro.getDatapubbl().getYear()));
            fldISBNScLibro.setText(libro.getISBN());
            fldISBNScLibro.setDisable(true); // ISBN non si cambia
            fldICopieDisponScLibro.setText(String.valueOf(libro.getNumeroCopie()));
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    // ========== GESTIONE AUTORI ==========
    
    @FXML
    private void handleAggiungiAutore(ActionEvent event) {
        String nuovoAutore = fldNuovoAutore.getText().trim();
        
        if (nuovoAutore.isEmpty()) {
            mostraErrore("Campo vuoto", "Inserisci il nome di un autore.");
            return;
        }
        
        if (autoriList.contains(nuovoAutore)) {
            mostraErrore("Autore duplicato", "Questo autore è già nella lista.");
            return;
        }
        
        autoriList.add(nuovoAutore);
        fldNuovoAutore.clear();
        fldNuovoAutore.requestFocus();
        System.out.println("✓ Autore aggiunto: " + nuovoAutore);
    }
    
    @FXML
    private void handleRimuoviAutore(ActionEvent event) {
        int selectedIndex = listAutori.getSelectionModel().getSelectedIndex();
        
        if (selectedIndex < 0) {
            mostraErrore("Nessuna selezione", "Seleziona un autore da rimuovere.");
            return;
        }
        
        String autoreRimosso = autoriList.remove(selectedIndex);
        System.out.println("✓ Autore rimosso: " + autoreRimosso);
    }

    // ========== SALVATAGGIO ==========
    
    private void handleSalva() {
        if (isInputValid()) {
            String titolo = fldTitoloScLibro.getText();
            String isbn = fldISBNScLibro.getText();
            int copie = Integer.parseInt(fldICopieDisponScLibro.getText());
            int anno = Integer.parseInt(fldAnnoScLibro.getText());
            LocalDate dataPubb = LocalDate.of(anno, 1, 1);

            // Usa direttamente la lista degli autori
            List<String> listaAutori = new ArrayList<>(autoriList);

            if (libro == null) {
                // --- CREAZIONE NUOVO ---
                libro = new Libro(titolo, dataPubb, isbn, copie);
                libro.setAutori(listaAutori);
            } else {
                // --- AGGIORNAMENTO ESISTENTE ---
                libro.setTitolo(titolo);
                libro.setDatapubbl(dataPubb);
                libro.setNumeroCopie(copie);
                libro.setAutori(listaAutori);
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        
        if (fldTitoloScLibro.getText() == null || fldTitoloScLibro.getText().length() == 0) 
            errorMessage += "Titolo non valido!\n";
        if (fldISBNScLibro.getText() == null || fldISBNScLibro.getText().length() == 0) 
            errorMessage += "ISBN non valido!\n";
        if (autoriList.isEmpty()) 
            errorMessage += "Aggiungi almeno un autore!\n";
        
        try { 
            Integer.parseInt(fldICopieDisponScLibro.getText()); 
        } catch (NumberFormatException e) { 
            errorMessage += "Copie deve essere un numero!\n"; 
        }
        try { 
            Integer.parseInt(fldAnnoScLibro.getText()); 
        } catch (NumberFormatException e) { 
            errorMessage += "Anno deve essere un numero (es. 2020)!\n"; 
        }

        if (errorMessage.length() == 0) return true;
        else {
            mostraErrore("Campi non validi", errorMessage);
            return false;
        }
    }

    private void mostraErrore(String titolo, String contenuto) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle(titolo);
        alert.setContentText(contenuto);
        alert.showAndWait();
    }
    
    public Libro getLibro() {
        return libro;
    }
}

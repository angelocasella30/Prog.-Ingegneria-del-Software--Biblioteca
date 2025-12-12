package classibiblioteca.viewControllers;

import classibiblioteca.entita.Libro;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class BookEditDialogController implements Initializable {

    @FXML private Label lblTitolo; 
    @FXML private TextField fldTitoloScLibro;
    @FXML private TextField fldAutoreScLibro;
    @FXML private TextField fldAnnoScLibro;
    @FXML private TextField fldISBNScLibro;
    @FXML private TextField fldICopieDisponScLibro;
    @FXML private Button btnSalvaScLibro;

    private Stage dialogStage;
    private Libro libro;
    private boolean okClicked = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Collega il bottone al metodo
        btnSalvaScLibro.setOnAction(event -> handleSalva());
    }    

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;

        if (libro == null) {
            // --- NUOVO LIBRO ---
            lblTitolo.setText("NUOVO LIBRO");
            fldTitoloScLibro.setText("");
            fldAutoreScLibro.setText("");
            fldAnnoScLibro.setText("");
            fldISBNScLibro.setText("");
            fldICopieDisponScLibro.setText("");
        } else {
            // --- MODIFICA LIBRO ---
            lblTitolo.setText("MODIFICA LIBRO");
            fldTitoloScLibro.setText(libro.getTitolo());
            
            // Uniamo gli autori in una stringa per mostrarli (es: "Dante, Virgilio")
            String autoriStr = String.join(", ", libro.getAutori());
            fldAutoreScLibro.setText(autoriStr);
            
            fldAnnoScLibro.setText(String.valueOf(libro.getDatapubbl().getYear()));
            fldISBNScLibro.setText(libro.getISBN());
            fldISBNScLibro.setDisable(true); // ISBN non si cambia
            fldICopieDisponScLibro.setText(String.valueOf(libro.getNumeroCopie()));
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void handleSalva() {
        if (isInputValid()) {
            String titolo = fldTitoloScLibro.getText();
            String isbn = fldISBNScLibro.getText();
            int copie = Integer.parseInt(fldICopieDisponScLibro.getText());
            int anno = Integer.parseInt(fldAnnoScLibro.getText());
            LocalDate dataPubb = LocalDate.of(anno, 1, 1); // Creiamo la data (1 Gennaio dell'anno inserito)

            // Gestione Autori (separati da virgola)
            List<String> listaAutori = new ArrayList<>();
            String[] autoriArray = fldAutoreScLibro.getText().split(",");
            for (String a : autoriArray) {
                listaAutori.add(a.trim());
            }

            if (libro == null) {
                // --- CREAZIONE NUOVO ---
                // Usiamo il costruttore della tua classe Libro
                libro = new Libro(titolo, dataPubb, isbn, copie);
                libro.setAutori(listaAutori); // Aggiungiamo gli autori dopo
            } else {
                // --- AGGIORNAMENTO ESISTENTE ---
                libro.setTitolo(titolo);
                libro.setDatapubbl(dataPubb);
                libro.setNumeroCopie(copie);
                libro.setAutori(listaAutori);
                // ISBN non lo tocchiamo in modifica
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (fldTitoloScLibro.getText() == null || fldTitoloScLibro.getText().length() == 0) errorMessage += "Titolo non valido!\n";
        if (fldISBNScLibro.getText() == null || fldISBNScLibro.getText().length() == 0) errorMessage += "ISBN non valido!\n";
        
        try { Integer.parseInt(fldICopieDisponScLibro.getText()); } catch (NumberFormatException e) { errorMessage += "Copie deve essere un numero!\n"; }
        try { Integer.parseInt(fldAnnoScLibro.getText()); } catch (NumberFormatException e) { errorMessage += "Anno deve essere un numero (es. 2020)!\n"; }

        if (errorMessage.length() == 0) return true;
        else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Campi non validi");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    
    // Getter per recuperare il libro appena creato dal controller principale
    public Libro getLibro() {
        return libro;
    }
}
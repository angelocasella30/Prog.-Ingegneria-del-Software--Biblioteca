package classibiblioteca.viewControllers;

import classibiblioteca.entita.Utente; 
import java.net.URL;
import java.util.List; // Importante per gestire la lista
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserEditDialogController implements Initializable {

    @FXML private Label lblTitolo; 
    @FXML private TextField fldNomeScUtente;
    @FXML private TextField fldCognomeScUtente;
    @FXML private TextField fldEmailScUtente;
    @FXML private TextField fldMatricolaScUtente;
    @FXML private Button btnSalvaScUtente;

    private Stage dialogStage;
    private Utente utente;
    private boolean okClicked = false;
    
    // --- NUOVO: Lista per controllare i duplicati ---
    private List<Utente> listaUtentiEsistenti; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnSalvaScUtente.setOnAction(event -> handleSalva());
    }    

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    // --- NUOVO METODO: Riceve la lista degli utenti ---
    public void setListaUtentiEsistenti(List<Utente> lista) {
        this.listaUtentiEsistenti = lista;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;

        if (utente == null) {
            // --- NUOVO UTENTE ---
            lblTitolo.setText("NUOVO UTENTE");
            fldNomeScUtente.setText("");
            fldCognomeScUtente.setText("");
            fldEmailScUtente.setText("");
            fldMatricolaScUtente.setText("");
            fldMatricolaScUtente.setDisable(false); // Qui la matricola è scrivibile, quindi va controllata
        } else {
            // --- MODIFICA UTENTE ---
            lblTitolo.setText("MODIFICA UTENTE");
            fldNomeScUtente.setText(utente.getNome());
            fldCognomeScUtente.setText(utente.getCognome());
            fldEmailScUtente.setText(utente.getEmail());
            fldMatricolaScUtente.setText(utente.getMatricola());
            fldMatricolaScUtente.setDisable(true); // In modifica è bloccata, quindi non serve controllare duplicati
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    private void handleSalva() {
        if (isInputValid()) {
            String nome = fldNomeScUtente.getText();
            String cognome = fldCognomeScUtente.getText();
            String email = fldEmailScUtente.getText();
            String matricola = fldMatricolaScUtente.getText();

            if (utente == null) {
                utente = new Utente(nome, cognome, matricola,email); 
            } else {
                utente.setNome(nome);
                utente.setCognome(cognome);
                utente.setEmail(email);
            }

            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        
        if (fldNomeScUtente.getText() == null || fldNomeScUtente.getText().trim().isEmpty()) {
            errorMessage += "Nome non valido!\n";
        }
        if (fldCognomeScUtente.getText() == null || fldCognomeScUtente.getText().trim().isEmpty()) {
            errorMessage += "Cognome non valido!\n";
        }
        
        // Controllo Email
        String email = fldEmailScUtente.getText();
        if (email == null || email.trim().isEmpty()) {
            errorMessage += "Email mancante!\n";
        } else {
            String emailLower = email.trim().toLowerCase();
            if (!emailLower.endsWith("@studenti.uni.it") && !emailLower.endsWith("@docente.uni.it")) {
                 errorMessage += "L'email deve terminare con:\n@studenti.uni.it oppure @docente.uni.it\n";
            }
        }
        
        // --- CONTROLLO MATRICOLA E DUPLICATI ---
        String inputMatricola = fldMatricolaScUtente.getText();
        
        if (inputMatricola == null || inputMatricola.trim().isEmpty()) {
            errorMessage += "Matricola mancante!\n";
        } else {
            // Se il campo è abilitato (significa che stiamo creando un NUOVO utente), controlliamo i duplicati
            if (!fldMatricolaScUtente.isDisabled() && listaUtentiEsistenti != null) {
                for (Utente u : listaUtentiEsistenti) {
                    if (u.getMatricola().equalsIgnoreCase(inputMatricola)) {
                        errorMessage += "Errore: Esiste già un utente con matricola " + inputMatricola + "!\n";
                        break; // Trovato duplicato, esci dal ciclo
                    }
                }
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Dati non validi");
            alert.setHeaderText("Impossibile salvare");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    
    public Utente getUtente() {
        return utente;
    }
}
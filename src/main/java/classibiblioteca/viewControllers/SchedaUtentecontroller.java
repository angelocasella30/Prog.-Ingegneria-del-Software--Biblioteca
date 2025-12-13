/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.viewControllers;

import classibiblioteca.entita.Utente;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SchedaUtentecontroller {

    @FXML private TextField fldCreaNomeUtente;
    @FXML private TextField fldCreaCognomeUtente;
    @FXML private TextField fldCreaEmailUtente;
    @FXML private TextField fldCreaMatricolaUtente;
    @FXML private Button btnSalvaCreaUtente;

    private Stage dialogStage;
    private Utente newuser;
    private boolean okClicked = false;

    private ArchivioUtenti archivio;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setArchivio(ArchivioUtenti archivio) {
        this.archivio = archivio;
    }

    public Utente getUtente(){
        return newuser;
    }

    public boolean isOkClicked(){
        return okClicked;
    }

    @FXML
    private void handleSalva() {
        if (!isInputValid()) return;

        newuser = new Utente(
            fldCreaNomeUtente.getText().trim(),
            fldCreaCognomeUtente.getText().trim(),
            fldCreaEmailUtente.getText().trim(),
            fldCreaMatricolaUtente.getText().trim()
        );

        okClicked = true;
        dialogStage.close();
    }

    private boolean isInputValid() {
        StringBuilder errore = new StringBuilder();

        if (fldCreaNomeUtente.getText() == null || fldCreaNomeUtente.getText().trim().isEmpty())
            errore.append("Nome mancante\n");
        if (fldCreaCognomeUtente.getText() == null || fldCreaCognomeUtente.getText().trim().isEmpty())
            errore.append("Cognome mancante\n");
        if (fldCreaEmailUtente.getText() == null || fldCreaEmailUtente.getText().trim().isEmpty())
            errore.append("Email mancante\n");
        if (fldCreaMatricolaUtente.getText() == null || fldCreaMatricolaUtente.getText().trim().isEmpty())
            errore.append("Matricola mancante\n");

        if (errore.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Errore dati");
            alert.setHeaderText("Dati utente non validi");
            alert.setContentText(errore.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }
}

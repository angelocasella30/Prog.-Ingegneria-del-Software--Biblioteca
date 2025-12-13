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

import classibiblioteca.entita.Utente;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ModificaUtenteController {

    @FXML private TextField ModificaUtentefldNome;
    @FXML private TextField ModificaUtentefldCognome;
    @FXML private TextField ModificaUtentefldEmail;
    @FXML private Label ModificaUtenteLabel;
    @FXML private Button btnSalvaModificaUtente;

    private Stage dialogStage;
    private Utente utente;
    private boolean okClicked = false;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;

        if (utente != null) {
            ModificaUtentefldNome.setText(utente.getNome());
            ModificaUtentefldCognome.setText(utente.getCognome());
            ModificaUtentefldEmail.setText(utente.getEmail());
            ModificaUtenteLabel.setText(utente.getMatricola());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSalva() {
        if (!isInputValid()) return;
        utente.setNome(ModificaUtentefldNome.getText().trim());
        utente.setCognome(ModificaUtentefldCognome.getText().trim());
        utente.setEmail(ModificaUtentefldEmail.getText().trim());

        okClicked = true;
        dialogStage.close();
    }

    private boolean isInputValid() {
        StringBuilder errorMsg = new StringBuilder();

        if (ModificaUtentefldNome.getText() == null || ModificaUtentefldNome.getText().trim().isEmpty())
            errorMsg.append("Nome mancante\n");
        if (ModificaUtentefldCognome.getText() == null || ModificaUtentefldCognome.getText().trim().isEmpty())
            errorMsg.append("Cognome mancante\n");
        if (ModificaUtentefldEmail.getText() == null || ModificaUtentefldEmail.getText().trim().isEmpty())
            errorMsg.append("Email mancante\n");

        if (errorMsg.length() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Errore dati");
            alert.setHeaderText("Dati utente non validi");
            alert.setContentText(errorMsg.toString());
            alert.showAndWait();
            return false;
        }
        return true;
    }
}

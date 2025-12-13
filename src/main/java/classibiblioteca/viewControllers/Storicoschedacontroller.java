/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.viewControllers;

package classibiblioteca.viewControllers;

import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import java.util.List;

public class Storicoschedacontroller {

    @FXML private TableView<Prestito> tablePrestitiStorico;
    @FXML private TableColumn<Prestito, String> colTitolo;
    @FXML private TableColumn<Prestito, String> colISBN;
    @FXML private TableColumn<Prestito, String> colDataInizio;
    @FXML private TableColumn<Prestito, String> colDataFine;
    @FXML private TableColumn<Prestito, String> colDataRestituzione;
    
    @FXML private Button btnClose;
    @FXML private Label StoricoUtenteLabelMatricola;

    private Utente utenteSelezionato;
    private ArchivioPrestiti archivioPrestiti;
    
    public void setUtente(Utente utente) {
        this.utenteSelezionato = utente;
        this.archivioPrestiti=archivioPrestiti;
        caricaStorico();
        StoricoUtenteLabelMatricola.setText(utente.getMatricola());
    }
    public void initialize() {
        configuraColonne();
    }
    private void configuraColonne() {
        colTitolo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitololibro()));
        colISBN.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getISBN()));
        colDataInizio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDatainizio().toString()));
        colDataFine.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDataScadenzaPrevista().toString()));
        colDataRestituzione.setCellValueFactory(c -> new SimpleStringProperty(
            c.getValue().getDataRestituzioneEffettiva() != null ? c.getValue().getDataRestituzioneEffettiva().toString() : "Non restituito"));
    }

    private void caricaStorico() {
        if (utenteSelezionato != null) {
            List<Prestito> prestiti = archivioPrestiti.getPrestitiAttiviPerUtente(utenteSelezionato);
            tablePrestitiStorico.getItems().setAll(prestiti);  // Popola la TableView con i prestiti
        }
    }

    // Gestisce la chiusura della finestra
    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
}

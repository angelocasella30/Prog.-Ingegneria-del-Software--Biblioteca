package classibiblioteca.viewControllers;

import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;  
public class StoricoUtenteController implements Initializable {

    @FXML private Label lblIntestazione;
    @FXML private TableView<Prestito> tabellaStorico;
    @FXML private TableColumn<Prestito, String> colTitolo;
    @FXML private TableColumn<Prestito, String> colISBN;
    @FXML private TableColumn<Prestito, LocalDate> colDataInizio;
    @FXML private TableColumn<Prestito, LocalDate> colDataRestituzione;
    @FXML private TableColumn<Prestito, LocalDate> colScadenza;


    private Utente utenteSelezionato;
    private ArchivioPrestiti archivioDati; // <-- viene INIETTATO

    @Override
public void initialize(URL url, ResourceBundle rb) {
    colTitolo.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getTitololibro())
    );
    colISBN.setCellValueFactory(cd ->
        new SimpleStringProperty(cd.getValue().getISBN())
    );
    colDataInizio.setCellValueFactory(cd ->
        new SimpleObjectProperty<>(cd.getValue().getDatainizio())
    );
    colDataRestituzione.setCellValueFactory(cd ->
        new SimpleObjectProperty<>(cd.getValue().getDataRestituzioneEffettiva())
    );
    colScadenza.setCellValueFactory(
    cd -> new javafx.beans.property.SimpleObjectProperty<>(cd.getValue().getDataScadenzaPrevista())
    );

    tabellaStorico.setPlaceholder(new Label("Nessun prestito storico trovato per questo utente."));

    // ➜ Righe rosse per prestiti restituiti in ritardo
    tabellaStorico.setRowFactory(tv -> new TableRow<Prestito>() {
        @Override
        protected void updateItem(Prestito item, boolean empty) {
            super.updateItem(item, empty);

            getStyleClass().remove("prestito-ritardo");
            if (empty || item == null) return;

            boolean restituitoInRitardo =
                    item.getDataScadenzaPrevista() != null &&
                    item.getDataRestituzioneEffettiva() != null &&
                    item.getDataRestituzioneEffettiva().isAfter(item.getDataScadenzaPrevista());

            if (restituitoInRitardo) {
                getStyleClass().add("prestito-ritardo");
            }
        }
    });
}


    // setter per iniettare l’archivio vero
    public void setArchivioPrestiti(ArchivioPrestiti archivioPrestiti) {
        this.archivioDati = archivioPrestiti;
    }

    public void initData(Utente utente) {
        this.utenteSelezionato = utente;
        lblIntestazione.setText("Storico Prestiti: " + utente.getCognome() + " " + utente.getNome());
        popolaTabella();
    }

    private void popolaTabella() {
        if (archivioDati == null || utenteSelezionato == null) {
            tabellaStorico.setItems(FXCollections.observableArrayList());
            return;
        }

        ObservableList<Prestito> listaStorico = FXCollections.observableArrayList();

        for (Prestito p : archivioDati.getLista()) {
            boolean stessoUtente = p.getMatricola() != null
                    && p.getMatricola().equalsIgnoreCase(utenteSelezionato.getMatricola());
            boolean restituito = p.getDataRestituzioneEffettiva() != null;

            if (stessoUtente && restituito) {
                listaStorico.add(p);
            }
        }

        tabellaStorico.setItems(listaStorico);
    }
}

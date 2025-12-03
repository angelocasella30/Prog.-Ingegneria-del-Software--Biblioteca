package Classibiblioteca.Controllers;

import Classibiblioteca.Entità.Prestito;
import Classibiblioteca.tipologiearchivi.ArchivioLibri;
import Classibiblioteca.tipologiearchivi.ArchivioPrestiti;
import java.time.LocalDate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class GestionePrestitiController {

    @FXML
    private TableView<Prestito> tablePrestiti;

    @FXML
    private TableColumn<Prestito, String> colMatricola;

    @FXML
    private TableColumn<Prestito, String> colISBN;

    @FXML
    private TableColumn<Prestito, LocalDate> colDataInizio;

    @FXML
    private TableColumn<Prestito, LocalDate> colDataFine;

    @FXML
    private TableColumn<Prestito, String> colStato; // Colonna calcolata "IN RITARDO"

    private ArchivioPrestiti archivioPrestiti;
    private ArchivioLibri archivioLibri; // Serve per restituire le copie

    public void initData(ArchivioPrestiti archivioP, ArchivioLibri archivioL) {
        this.archivioPrestiti = archivioP;
        this.archivioLibri = archivioL;
    }

    @FXML
    public void initialize() {
        // TODO: Collegare colonne e impostare RowFactory per il colore rosso
    }

    @FXML
    void handleNuovoPrestito(ActionEvent event) {
        // TODO: Aprire popup nuovo prestito (UC-9)
    }

    @FXML
    void handleRestituzione(ActionEvent event) {
        // TODO: Gestire restituzione (UC-11)
    }
}
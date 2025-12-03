package Classibiblioteca.Controllers;

import Classibiblioteca.Entità.Libro;
import Classibiblioteca.tipologiearchivi.ArchivioLibri;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class GestioneLibriController {

    @FXML
    private TextField txtRicerca;

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
    private TableColumn<Libro, Integer> colCopie;

    private ArchivioLibri archivioLibri;

    // Metodo per ricevere i dati dal MainController
    public void initData(ArchivioLibri archivio) {
        this.archivioLibri = archivio;
    }

    @FXML
    public void initialize() {
        // TODO: Collegare le colonne agli attributi della classe Libro
    }

    @FXML
    void handleCerca(ActionEvent event) {
        // TODO: Implementare ricerca (UC-4)
    }

    @FXML
    void handleReset(ActionEvent event) {
        // TODO: Resettare filtri e mostrare tutto
    }

    @FXML
    void handleNuovo(ActionEvent event) {
        // TODO: Aprire popup nuovo libro (UC-1)
    }

    @FXML
    void handleModifica(ActionEvent event) {
        // TODO: Aprire popup modifica libro (UC-2)
    }

    @FXML
    void handleElimina(ActionEvent event) {
        // TODO: Eliminare libro selezionato (UC-3)
    }
}
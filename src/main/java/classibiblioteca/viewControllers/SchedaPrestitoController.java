package classibiblioteca.viewControllers;

import classibiblio.Archivio;
import classibiblioteca.entita.Libro;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class SchedaPrestitoController {

    @FXML private Label lblTitolo;
    @FXML private TextField fldMatricola;
    @FXML private TextField fldISBN;
    @FXML private DatePicker dpDataInizio;
    @FXML private DatePicker dpDataScadenza;

    private Stage dialogStage;
    private Archivio archivio;

    private boolean okClicked = false;
    private Prestito prestitoCreato;

    private static final int DURATA_GIORNI_DEFAULT = 30;
    private boolean scadenzaModificataManualmente = false;

    @FXML
    private void initialize() {
        dpDataInizio.setValue(LocalDate.now());
        dpDataScadenza.setValue(dpDataInizio.getValue().plusDays(DURATA_GIORNI_DEFAULT));

        dpDataInizio.valueProperty().addListener((obs, oldV, newV) -> {
            if (newV != null && !scadenzaModificataManualmente) {
                dpDataScadenza.setValue(newV.plusDays(DURATA_GIORNI_DEFAULT));
            }
        });

        dpDataScadenza.valueProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) scadenzaModificataManualmente = true;
        });

        // Facoltativo ma utile per digitare a mano senza ambiguità (yyyy-MM-dd). [web:342][web:353]
        installDateConverter(dpDataInizio, "yyyy-MM-dd");
        installDateConverter(dpDataScadenza, "yyyy-MM-dd");
    }

    private void installDateConverter(DatePicker dp, String pattern) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(pattern);
        dp.setPromptText(pattern);

        dp.setConverter(new StringConverter<LocalDate>() {
            @Override public String toString(LocalDate date) {
                return (date == null) ? "" : fmt.format(date);
            }
            @Override public LocalDate fromString(String string) {
                if (string == null || string.trim().isEmpty()) return null;
                return LocalDate.parse(string.trim(), fmt);
            }
        });
    }

    public void setDialogStage(Stage dialogStage) { this.dialogStage = dialogStage; }
    public void setArchivio(Archivio archivio) { this.archivio = archivio; }

    public boolean isOkClicked() { return okClicked; }
    public Prestito getPrestitoCreato() { return prestitoCreato; }

    @FXML
    private void handleSalva() {
        if (archivio == null) {
            mostraErrore("Archivio non inizializzato", "Archivio non caricato.");
            return;
        }

        String matricola = fldMatricola.getText() != null ? fldMatricola.getText().trim() : "";
        String isbn = fldISBN.getText() != null ? fldISBN.getText().trim() : "";
        LocalDate dataInizio = dpDataInizio.getValue();
        LocalDate scadenza = dpDataScadenza.getValue();

        if (matricola.isEmpty() || isbn.isEmpty() || dataInizio == null || scadenza == null) {
            mostraErrore("Dati mancanti", "Compila Matricola, ISBN, Data inizio e Data scadenza.");
            return;
        }

        if (scadenza.isBefore(dataInizio)) {
            mostraErrore("Date non valide", "La scadenza non può essere precedente alla data di inizio.");
            return;
        }

        Utente utente = archivio.getArchivioUtenti().getUtenteByMatricola(matricola);
        if (utente == null) {
            mostraErrore("Matricola non trovata", "Non esiste alcun utente con questa matricola.");
            return;
        }

        Libro libro = archivio.getArchivioLibri().getLibroByISBN(isbn);
        if (libro == null) {
            mostraErrore("ISBN non trovato", "Non esiste alcun libro con questo ISBN.");
            return;
        }

        if (!utente.puoRichiederePrestito()) {
            mostraErrore("Utente non idoneo",
                    "L'utente non può richiedere prestiti (email non valida o limite prestiti raggiunto).");
            return;
        }

        if (!libro.isDisponibile()) {
            mostraErrore("Libro non disponibile", "Non ci sono copie disponibili per questo libro.");
            return;
        }

        prestitoCreato = new Prestito(
                utente.getEmail(),
                utente.getMatricola(),
                libro.getTitolo(),
                libro.getISBN(),
                dataInizio,
                scadenza
        );

        okClicked = true;
        dialogStage.close();
    }

    private void mostraErrore(String titolo, String contenuto) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Errore");
        a.setHeaderText(titolo);
        a.setContentText(contenuto);
        a.showAndWait();
    }
}
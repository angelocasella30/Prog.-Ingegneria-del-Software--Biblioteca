package classibiblioteca.viewControllers;

import classibiblio.Archivio;
import classibiblio.tipologiearchivi.ArchivioLibri;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;

public class MainControllerViewController implements Initializable {

    @FXML private StackPane contentArea;

    private Archivio archivio;
    private Path savePath;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carica Archivio di default all'avvio
        try {
            archivio = Archivio.caricaDefault();
            savePath = Archivio.defaultPath();
            System.out.println("✓ Archivio caricato da: " + savePath);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Errore caricamento archivio: " + e.getMessage());
            archivio = new Archivio();
            savePath = Archivio.defaultPath();
        }

        // Carica HomePage di default all'avvio
        caricaSchermata("HomePage");
    }

    // ========== METODI DI NAVIGAZIONE ==========

    @FXML
    public void showHome(ActionEvent event) {
        caricaSchermata("HomePage");
    }

    @FXML
    public void showGestionePrestiti(ActionEvent event) {
        caricaSchermata("GestionePrestiti");
    }

    @FXML
    public void showGestioneUtenti(ActionEvent event) {
        caricaSchermata("GestioneUtenti");
    }

    @FXML
    public void showGestioneLibri(ActionEvent event) {
        caricaSchermata("GestioneLibri");
    }
    

    // ========== METODO PRINCIPALE DI CARICAMENTO SCHERMATE ==========
    private void caricaSchermata(String nomeFile) {
        try {
            String percorso = "/classibiblioteca/views/" + nomeFile + ".fxml";
            URL fileUrl = getClass().getResource(percorso);

            System.out.println("Carico schermata: " + percorso);

            if (fileUrl == null) {
                System.out.println("❌ ERRORE: Non trovo il file " + percorso);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent vista = loader.load();  // load() chiama initialize()

            // DOPO load(), inietta l'archivio
            Object controller = loader.getController();

            if (controller instanceof GestioneLibriController) {
                GestioneLibriController c = (GestioneLibriController) controller;
                c.setArchivio(archivio);
                c.setSavePath(savePath);
                System.out.println("✓ GestioneLibri: Archivio iniettato");
            } else if (controller instanceof GestioneUtentiController) {
                GestioneUtentiController c = (GestioneUtentiController) controller;
                c.setArchivio(archivio);
                c.setSavePath(savePath);
                System.out.println("✓ GestioneUtenti: Archivio iniettato");
            } else if (controller instanceof GestionePrestitiController) {
                GestionePrestitiController c = (GestionePrestitiController) controller;
                c.setArchivio(archivio);
                c.setSavePath(savePath);
                System.out.println("✓ GestionePrestiti: Archivio iniettato");
            }

            contentArea.getChildren().setAll(vista);
            System.out.println("✓ Schermata caricata: " + nomeFile);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("❌ ERRORE nel caricamento: " + nomeFile);
        }
    }



    // ========== METODI DI SALVATAGGIO ==========

    @FXML
    private void handleSalva(ActionEvent event) {
        if (archivio == null || savePath == null) {
            System.out.println("❌ ERRORE: archivio/savePath null");
            return;
        }
        try {
            Archivio.salva(archivio, savePath);
            System.out.println("✓ Archivio salvato in: " + savePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ ERRORE salvataggio: " + e.getMessage());
        }
    }
    
    // ========== METODO DI RESET ==========
     @FXML private Button btnReset;

       @FXML
        private void handleResetArchivio(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma Reset");
            alert.setHeaderText("Stai per cancellare TUTTI i dati");
            alert.setContentText("Questa operazione è irreversibile. Sei sicuro?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    // Crea un archivio vuoto
                    archivio = new Archivio(
                        new ArchivioUtenti(), 
                        new ArchivioLibri(), 
                        new ArchivioPrestiti()
                    );

                    // Salva l'archivio vuoto
                    Archivio.salva(archivio, savePath);

                    // Ricarica la schermata corrente (così i controller ricevono il nuovo archivio)
                    caricaSchermata("HomePage");

                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Reset Completato");
                    info.setContentText("Archivio ripristinato a zero.");
                    info.showAndWait();

                } catch (IOException e) {
                    Alert errore = new Alert(Alert.AlertType.ERROR);
                    errore.setTitle("Errore");
                    errore.setContentText("Reset fallito: " + e.getMessage());
                    errore.showAndWait();
                }
            }
        }

    // ========== GETTER (se necessari per accesso esterno) ==========

    public Archivio getArchivio() {
        return archivio;
    }

    public void setArchivio(Archivio archivio) {
        this.archivio = archivio;
    }

    public Path getSavePath() {
        return savePath;
    }

    public void setSavePath(Path savePath) {
        this.savePath = savePath;
    }
}

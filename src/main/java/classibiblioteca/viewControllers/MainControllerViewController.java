package classibiblioteca.viewControllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane; // IMPORTANTE: Aggiungi questo import!

public class MainControllerViewController implements Initializable {

    @FXML
    private BorderPane mainContainer;

    // NUOVO: Riferimento allo StackPane che contiene il margine
    // Assicurati che nel MainView.fxml ci sia fx:id="contentArea" nello StackPane
    @FXML
    private StackPane contentArea; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Carichiamo subito GestioneLibri all'avvio
        caricaSchermata("GestioneLibri");
    }    

    @FXML
    private void showHome(ActionEvent event) {
        // caricaSchermata("HomePage"); 
    }

    @FXML
    private void showGestionePrestiti(ActionEvent event) {
        // caricaSchermata("GestionePrestiti"); 
    }

    @FXML
    private void showGestioneUtenti(ActionEvent event) {
        // caricaSchermata("GestioneUtenti"); 
    }

    @FXML
    private void showGestioneLibri(ActionEvent event) {
        caricaSchermata("GestioneLibri");
    }
    
    // --- METODO AGGIORNATO PER MANTENERE IL MARGINE ---
    private void caricaSchermata(String nomeFile) {
        try {
            // Percorso assoluto verso la cartella views
            String percorso = "/classibiblioteca/views/" + nomeFile + ".fxml";
            
            URL fileUrl = getClass().getResource(percorso);
            
            if (fileUrl == null) {
                System.out.println("ERRORE CRITICO: Non trovo il file " + percorso);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent vista = loader.load();
            
            // --- LA MODIFICA MAGICA È QUI ---
            // Invece di sostituire tutto il centro (che cancellerebbe il margine),
            // lavoriamo DENTRO lo StackPane "contentArea".
            
            contentArea.getChildren().clear(); // Rimuove la vista precedente
            contentArea.getChildren().add(vista); // Aggiunge la nuova vista (mantenendo il margine del padre)
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento: " + nomeFile);
        }
    }
}
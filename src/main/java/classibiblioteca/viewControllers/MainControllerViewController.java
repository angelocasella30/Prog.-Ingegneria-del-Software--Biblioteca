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

public class MainControllerViewController implements Initializable {

    @FXML
    private BorderPane mainContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Appena parte il programma, carica la Home
        caricaSchermata("HomePage");
    }    

    @FXML
    private void showHome(ActionEvent event) {
        caricaSchermata("HomePage");
    }

    @FXML
    private void showGestionePrestiti(ActionEvent event) {
        caricaSchermata("GestionePrestiti"); // Assicurati di creare questo file
    }

    @FXML
    private void showGestioneUtenti(ActionEvent event) {
        caricaSchermata("GestioneUtenti"); // Assicurati di creare questo file
    }

    @FXML
    private void showGestioneLibri(ActionEvent event) {
        caricaSchermata("GestioneLibri"); // Assicurati di creare questo file
    }
    
    // --- METODO HELPER PER CARICARE LE VISTE ---
    private void caricaSchermata(String nomeFileFXML) {
        try {
            // Carica il file FXML. 
            // NOTA: Se i file sono nello stesso pacchetto di questo controller, basta il nome.
            // Se sono in una cartella diversa, serve il percorso (es: "/views/" + nomeFileFXML + ".fxml")
            URL fileUrl = getClass().getResource(nomeFileFXML + ".fxml");
            
            if (fileUrl == null) {
                System.out.println("Impossibile trovare il file: " + nomeFileFXML + ".fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fileUrl);
            Parent vista = loader.load();
            
            // Inserisce la vista caricata al centro del BorderPane principale
            mainContainer.setCenter(vista);
            
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel caricamento della vista: " + nomeFileFXML);
        }
    }
}
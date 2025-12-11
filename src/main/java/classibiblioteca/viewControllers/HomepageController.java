/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.viewControllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import 

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class HomepageController implements Initializable {

    @FXML
    private Button btnHomePage;
    @FXML
    private Button btnLibriPage;
    @FXML
    private Button btnUtentiPage;
    @FXML
    private Button btnPrestitiPage;
    @FXML
    private Button btnEsporta;
    @FXML
    private StackPane stackpaneHome;
    @FXML
    private StackPane stackpanePrestiti;
    @FXML
    private TextField fldMatricolaPrestiti;
    @FXML
    private TextField fldISBNPrestiti;
    @FXML
    private Button btnCercaPrestiti;
    @FXML
    private Button btnCreaPrestiti;
    @FXML
    private Button BtnRestituisciPrestiti;
    @FXML
    private Button BtnListaRestituiti
    @FXML
    private Button BtnListaPrestiti;
    @FXML
    private TableView<Prestito> tableviewPrestiti;
    @FXML
    private StackPane stackpaneUtenti;
    @FXML
    private TextField fldCercaMatricolaUtente;
    @FXML
    private TextField fldCercaCognomeUtente;
    @FXML
    private Button btnCercaUtente;
    @FXML
    private Button btnCreaUtente;
    @FXML
    private Button btnModificaUtente;
    @FXML
    private Button btnEliminaUtente;
    @FXML
    private Button btnListaUtente;
    @FXML
    private TableView<Utente> tableviewUtenti;
    @FXML
    private StackPane stackpaneLibri;
    @FXML
    private TextField fldCercaISBNLibro;
    @FXML
    private TextField fldCercaAutoreLibro;
    @FXML
    private TextField fldCercaTitoloLibro;
    @FXML
    private Button btnCercaLibro;
    @FXML
    private Button btnAggiungiLibro;
    @FXML
    private Button btnModificaLibro;
    @FXML
    private Button btnEliminaLibro;
    @FXML
    private Button btnListaLibri;
    @FXML
    private TableView<Libro> tableviewLibri;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stackpaneHome.setVisible(true);
        stackpaneLibri.setVisible(false);
        stackpanePrestiti.setVisible(false);
        stackpaneUtenti.setVisible(false);
    }    
    
    @FXML
    private void handleHomePage() {
        stackpaneHome.setVisible(true);
        stackpaneLibri.setVisible(false);
        stackpanePrestiti.setVisible(false);
        stackpaneUtenti.setVisible(false);
    }

    @FXML
    private void handleLibriPage() {
        stackpaneHome.setVisible(false);
        stackpaneLibri.setVisible(true);
        stackpanePrestiti.setVisible(false);
        stackpaneUtenti.setVisible(false);
}

    @FXML
    private void handleUtentiPage() {
        stackpaneHome.setVisible(false);
        stackpaneLibri.setVisible(false);
        stackpanePrestiti.setVisible(false);
        stackpaneUtenti.setVisible(true);
    }

    @FXML
    private void handlePrestitiPage() {
        stackpaneHome.setVisible(false);
        stackpaneLibri.setVisible(false);
        stackpanePrestiti.setVisible(true);
        stackpaneUtenti.setVisible(false);
    }
}

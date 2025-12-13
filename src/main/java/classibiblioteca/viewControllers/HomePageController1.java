/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.viewControllers;

import classibiblioteca.views.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import classibiblioteca.entita.Libro;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;
/**
 * FXML Controller class
 *
 * @author Utente
 */
public class HomepageController implements Initializable {

    @FXML
    private Button btnHomePage,btnLibriPage,btnUtentiPage,btnPrestitiPage,btnEsporta;
    @FXML
    private StackPane stackpaneHome,stackpaneUtenti,stackpaneLibri,stackpanePrestiti;
    @FXML
    private TextField fldCercaTitoloLibro;
    @FXML
    private Button btnCercaHomeLibro;
    @FXML
    private MenuButton MenuButtonSelezionaHomeLibro,MenuButtonOrdinaHomeLibro,MenuButtonHomeCercaUtente,MenuButtonHomeOrdinaUtente,MenuButtonTipoCercaPrestiti,MenuButtonHomeTIpoPrestiti;
    @FXML
    private Button btnAggiungiHomeLibro,btnModificaHomeLibro,btnEliminaHomeLibro,btnListaHomeLibri;
    @FXML
    private VBox HomeVboxVisLibro,HomeVboxUtenti;
    @FXML
    private TableView<Libro> tableviewLibri,tableviewUtenti,tableviewPrestiti;
    @FXML
    private TextField fldCercaHomeUtente,fldCercaHomePrestiti;
    @FXML
    private Button btnCercaHomeUtente,btnAggiungHomeUtente,btnModificaHomeUtente;
    @FXML
    private Button btnEliminaHomeUtente,btnListaHomeUtente,btnStoricoHomeUtente;
    @FXML
    private Button btnCercaHomePrestiti,btnAggiungiHomePrestiti,btnRestituisciHomePrestiti,btnListaHomePrestiti;
    
    private final String BLACK = "-fx-background-color: #555555; -fx-text-fill: white;";
    private final String GRAY = "-fx-background-color: #cccccc; -fx-text-fill: black;";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stackpaneHome.setVisible(true);
        stackpaneLibri.setVisible(false);
        stackpanePrestiti.setVisible(false);
        stackpaneUtenti.setVisible(false);
        
        btnHomePage.setStyle(BLACK);
        btnUtentiPage.setStyle(GRAY);
        btnLibriPage.setStyle(GRAY);
        btnPrestitiPage.setStyle(GRAY);
        
    }    

    @FXML
    private void handleButtonPage(ActionEvent e) {
        Button clicked = (Button) e.getSource();
        stackpaneHome.setVisible(clicked == btnHomePage);
        stackpaneLibri.setVisible(clicked == btnLibriPage);
        stackpanePrestiti.setVisible(clicked == btnPrestitiPage);
        stackpaneUtenti.setVisible(clicked == btnUtentiPage);

        btnHomePage.setStyle(clicked==btnHomePage ? BLACK : GRAY);
        btnLibriPage.setStyle(clicked==btnLibriPage ? BLACK : GRAY);
        btnPrestitiPage.setStyle(clicked==btnPrestitiPage ? BLACK : GRAY);
        btnUtentiPage.setStyle(clicked==btnUtentiPage ? BLACK : GRAY);
        
    }    
    
}

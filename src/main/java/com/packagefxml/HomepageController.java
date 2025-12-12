/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.packagefxml;

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
    private StackPane stackpaneLibri;
    @FXML
    private TextField fldCercaTitoloLibro;
    @FXML
    private Button btnCercaHomeLibro;
    @FXML
    private MenuButton MenuButtonSelezionaHomeLibro;
    @FXML
    private Button btnAggiungiHomeLibro;
    @FXML
    private Button btnModificaHomeLibro;
    @FXML
    private VBox HomeVboxVisLibro;
    @FXML
    private Button btnEliminaHomeLibro;
    @FXML
    private MenuButton MenuButtonOrdinaHomeLibro;
    @FXML
    private Button btnListaHomeLibri;
    @FXML
    private TableView<?> tableviewLibri;
    @FXML
    private StackPane stackpaneUtenti;
    @FXML
    private MenuButton MenuButtonHomeCercaUtente;
    @FXML
    private TextField fldCercaHomeUtente;
    @FXML
    private Button btnCercaHomeUtente;
    @FXML
    private Button btnAggiungHomeUtente;
    @FXML
    private Button btnModificaHomeUtente;
    @FXML
    private VBox HomeVboxUtenti;
    @FXML
    private Button btnEliminaHomeUtente;
    @FXML
    private MenuButton MenuButtonHomeOrdinaUtente;
    @FXML
    private Button btnListaHomeUtente;
    @FXML
    private Button btnStoricoHomeUtente;
    @FXML
    private TableView<?> tableviewUtenti;
    @FXML
    private StackPane stackpanePrestiti;
    @FXML
    private TextField fldCercaHomePrestiti;
    @FXML
    private Button btnCercaHomePrestiti;
    @FXML
    private MenuButton MenuButtonTipoCercaPrestiti;
    @FXML
    private Button btnAggiungiHomePrestiti;
    @FXML
    private Button btnRestituisciHomePrestiti;
    @FXML
    private MenuButton MenuButtonHomeTIpoPrestiti;
    @FXML
    private Button btnListaHomePrestiti;
    @FXML
    private TableView<?> tableviewPrestiti;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleHomePage(ActionEvent event) {
    }

    @FXML
    private void handleLibriPage(ActionEvent event) {
    }

    @FXML
    private void handleUtentiPage(ActionEvent event) {
    }

    @FXML
    private void HandlePrestitiPage(ActionEvent event) {
    }
    
}

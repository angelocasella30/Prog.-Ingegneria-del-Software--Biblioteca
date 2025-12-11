/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.packagefxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class HomepageController implements Initializable {

    @FXML
    private Button btnEsporta;
    @FXML
    private Tab tabLIBRI;
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
    private TableView<?> tableviewLibri;
    @FXML
    private TableColumn<?, ?> clmnTitoloLibro;
    @FXML
    private TableColumn<?, ?> clmnAutoreLibro;
    @FXML
    private TableColumn<?, ?> clmnAnnoLibro;
    @FXML
    private TableColumn<?, ?> clmnISBNLibro;
    @FXML
    private TableColumn<?, ?> clmnDispLibro;
    @FXML
    private TableColumn<?, ?> clmnPrestLibro;
    @FXML
    private Tab tabUTENTI;
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
    private TableView<?> tableviewUtenti;
    @FXML
    private TableColumn<?, ?> clmnNomeUtente;
    @FXML
    private TableColumn<?, ?> clmnCognomeUtente;
    @FXML
    private TableColumn<?, ?> clmnMatricolaUtente;
    @FXML
    private TableColumn<?, ?> clmnEmailUtente;
    @FXML
    private TableColumn<?, ?> clmnLibro1Utente;
    @FXML
    private TableColumn<?, ?> clmnLibro2Utente;
    @FXML
    private TableColumn<?, ?> clmnLibro3Utente;
    @FXML
    private Tab tabPrestiti;
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
    private Button BtnListaRestituiti;
    @FXML
    private Button BtnListaPrestiti;
    @FXML
    private TableView<?> tableviewPrestiti;
    @FXML
    private TableColumn<?, ?> clmnMatricolaPrestiti;
    @FXML
    private TableColumn<?, ?> clmnISBNPrestiti;
    @FXML
    private TableColumn<?, ?> clmnTitoloPrestiti;
    @FXML
    private TableColumn<?, ?> clmnInizioPrestiti;
    @FXML
    private TableColumn<?, ?> clmnFinePrestiti;
    @FXML
    private TableColumn<?, ?> clmnRitardoPrestiti;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

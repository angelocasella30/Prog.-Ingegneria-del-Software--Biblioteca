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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class SchedalibroController implements Initializable {

    @FXML
    private Pane SchedaCreaLibro;
    @FXML
    private Label StoricoUtenteLabelMatricola;
    @FXML
    private TableColumn<?, ?> StoricoUtenteClmTitolo;
    @FXML
    private TableColumn<?, ?> StoricoUtenteClmISBN;
    @FXML
    private TableColumn<?, ?> StoricoUtenteClmInizio;
    @FXML
    private TableColumn<?, ?> StoricoUtenteClmScadenza;
    @FXML
    private TableColumn<?, ?> StoricoUtenteClmRestituzione;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

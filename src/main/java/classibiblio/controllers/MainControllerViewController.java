/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblio.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author jiren
 */
public class MainControllerViewController implements Initializable {

    @FXML
    private BorderPane mainContainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void showHome(ActionEvent event) {
    }

    @FXML
    private void showGestionePrestiti(ActionEvent event) {
    }

    @FXML
    private void showGestioneUtenti(ActionEvent event) {
    }

    @FXML
    private void showGestioneLibri(ActionEvent event) {
    }
    
}

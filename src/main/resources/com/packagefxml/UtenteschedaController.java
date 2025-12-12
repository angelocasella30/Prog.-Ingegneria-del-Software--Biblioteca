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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Utente
 */
public class UtenteschedaController implements Initializable {

    @FXML
    private ComboBox<?> RestituzioneBoxLibro;
    @FXML
    private ComboBox<?> RestituzioneBoxUtenti;
    @FXML
    private Label RestituzioneInizioLabel;
    @FXML
    private Label RestituzioneConsegnaLabel;
    @FXML
    private Button btnRestituzioneSalva;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

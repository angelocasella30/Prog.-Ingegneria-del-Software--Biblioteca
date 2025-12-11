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
    private TextField fldTitoloScLibro;
    @FXML
    private TextField fldAutoreScLibro;
    @FXML
    private TextField fldAnnoScLibro;
    @FXML
    private TextField fldISBNScLibro;
    @FXML
    private TextField fldICopieDisponScLibro;
    @FXML
    private TextField fldICopiePrestScLibro;
    @FXML
    private Button btnSalvaScLibro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}

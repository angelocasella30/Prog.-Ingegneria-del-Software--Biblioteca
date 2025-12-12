/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gestionebiblio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestSoloLibri extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // ATTENZIONE: Qui devi mettere il percorso ESATTO del tuo file GestioneLibri.fxml
        // Se è in: src/main/resources/classibiblioteca/views/GestioneLibri.fxml
        // Allora scrivi:
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/classibiblioteca/views/GestioneLibri.fxml"));
        
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        
        // (Opzionale) Carica il CSS se non lo hai già collegato in Scene Builder
        // scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());

        stage.setTitle("TEST AREA - Gestione Libri");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

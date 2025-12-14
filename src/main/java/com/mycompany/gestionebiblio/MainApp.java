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

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // CARICAMENTO DEL FILE FXML PRINCIPALE (La cornice con i menu)
        // Assicurati che il percorso sia esatto. Se hai seguito le istruzioni precedenti, è qui:
        String fxmlPath = "/classibiblioteca/views/MainControllerView.fxml";
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        
        // Controllo di sicurezza se non trova il file
        if (loader.getLocation() == null) {
            System.err.println("ERRORE FATALE: Non trovo il file FXML in " + fxmlPath);
            System.exit(1);
        }

        Parent root = loader.load();
        
        // Creazione della scena
        Scene scene = new Scene(root);
        
        // Configurazione della finestra
        stage.setTitle("Gestionale Biblioteca");
        stage.setScene(scene);
        
        // IMPORTANTE: Avviamo a tutto schermo per godere del layout che abbiamo sistemato
        stage.setMaximized(true); 
        
        stage.show();
    }

    /**
     * Il metodo main() viene ignorato nelle applicazioni JavaFX moderne se lanciate correttamente,
     * ma serve come fallback per l'avvio standard (es. da NetBeans/IDE).
     */
    public static void main(String[] args) {
        launch(args);
    }
}




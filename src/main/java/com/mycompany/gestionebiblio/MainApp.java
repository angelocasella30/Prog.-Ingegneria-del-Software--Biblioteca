package com.mycompany.gestionebiblio;

import classibiblio.Archivio;
import classibiblioteca.viewControllers.MainControllerViewController;
import java.net.URL;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Percorso FXML principale
        String fxmlPath = "/classibiblioteca/views/MainControllerView.fxml";

        URL location = getClass().getResource(fxmlPath);
        if (location == null) {
            System.err.println("❌ ERRORE FATALE: Non trovo il file FXML in: " + fxmlPath);
            System.err.println("Controlla che sia in src/main/resources/classibiblioteca/views/ e che il nome sia corretto.");
            System.exit(1);
        }

        System.out.println("✓ File FXML trovato: " + fxmlPath);

        // Carica il FXML
        FXMLLoader loader = new FXMLLoader(location);
        Parent root = loader.load();

        // Recupera il controller principale
        MainControllerViewController mainController = loader.getController();

        // Carica o crea l'Archivio
        final Path savePath = Archivio.defaultPath();

        Archivio archivioDaUsare;
        try {
            archivioDaUsare = Archivio.caricaDefault();
            System.out.println("✓ Archivio caricato da: " + savePath);
        } catch (Exception e) {
            System.out.println("⚠ Archivio non trovato, creane uno nuovo");
            archivioDaUsare = new Archivio();
        }

        final Archivio archivio = archivioDaUsare;


        // Inietta l'Archivio nel controller principale
        mainController.setArchivio(archivio);
        mainController.setSavePath(savePath);

        // Crea la Scene e mostra
        Scene scene = new Scene(root);
        stage.setTitle("Gestionale Biblioteca");
        stage.setScene(scene);
        stage.setMaximized(true);

        // Salva l'archivio alla chiusura (opzionale ma consigliato)
        stage.setOnCloseRequest(event -> {
            try {
                Archivio.salva(archivio, savePath);
                System.out.println("✓ Archivio salvato alla chiusura");
            } catch (Exception e) {
                System.err.println("❌ Errore salvataggio finale: " + e.getMessage());
            }
        });

        stage.show();
        System.out.println("✓ Applicazione avviata");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
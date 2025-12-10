package classibiblio.tipologiearchivi;

import classibiblio.entita.Prestito;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Prestito
 * @implements serializable
 * 
 * gestisce aggiuntaprestito e restituzione in archivioprestiti
 */

public class ArchivioPrestiti implements Serializable {
    
    
    private  List<Prestito> listprestiti;
    
    public ArchivioPrestiti() {
        this.listprestiti = new ArrayList<>();
    }
    
    // --- Gestione Lista ---
    
    public void aggiungiPrestito(Prestito p)
    {
    }
    
    // UC-12: Gestione Restituzione
    public boolean restituzioneLibro(String matricola, String ISBN) {
        // Da implementare: trova prestito, chiudilo, rimuovilo o spostalo nello storico
        return false;
    }
    
    // --- Metodi per l'Interfaccia Grafica ---
    
    // Requisito UI-1.4: Visualizzazione ordinata per data scadenza
    public List<Prestito> getPrestitiOrdinatiPerScadenza() {
        // Da implementare: ordinamento cronologico
        return listprestiti;
    }
    
    // Metodo utile per il controller (opzionale)
    public List<Prestito> getPrestitiScaduti() {
        // Da implementare: filtra solo quelli con ritardo
        return new ArrayList<>();
    }
    
    public List<Prestito> getLista() {
        return listprestiti;
    }
}
package Classibiblioteca.tipologiearchivi;

import Classibiblioteca.Entità.Utente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe ArchivioUtenti 
 * @implements serializable
 * 
 * gestisce aggiunta,elimina,ricerca e modifica in archivio utenti
 */

public class ArchivioUtenti implements Serializable {
    
    
    private List<Utente> listutenti;
    
    public ArchivioUtenti() {
        this.listutenti = new ArrayList<>();
    }
    
    // --- Gestione Lista ---
    
    public void aggiungiUtente(Utente x) {
        // Da implementare: controllo matricola univoca
        listutenti.add(x);
    }
    
    public boolean eliminaUtente(Utente x) {
        // Da implementare
        return false;
    }
    
    public boolean existByMatricola(String matricola) {
    // confronto se esiste  in archivio utente
    }
    
    public Utente getUtenteByMatricola(String matricola) {
        // Da implementare
        return null;
    }
    public boolean aggiornaUtenti(String nome,String cognome) {
    // da completare
    return false;
    }
    // --- Metodi per la Ricerca (UC-8) ---
    
    public List<Utente> ricercaUtente(String keyword) {
        // Da implementare (cognome, matricola)
        return new ArrayList<>();
    }
    
    // --- Metodi per l'Interfaccia Grafica ---
    
    // Requisito UI-1.3
    public List<Utente> getUtentiOrdinati() {
        // Da implementare: ordinamento per Cognome poi Nome
        return listutenti;
    }
    
    public List<Utente> getLista() {
        return listutenti;
    }
}
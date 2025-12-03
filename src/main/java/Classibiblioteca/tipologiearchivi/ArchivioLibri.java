package Classibiblioteca.tipologiearchivi;

import Classibiblioteca.Entità.Libro;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ArchivioLibri implements Serializable {
    
    
    private List<Libro> listlibro;
    
    public ArchivioLibri() {
        this.listlibro = new ArrayList<>();
    }
    
    // --- Gestione Lista ---
    
    public void aggiungiLibro(Libro x) {
        // Da implementare: controllo duplicati ISBN
        listlibro.add(x);
    }
    
    public boolean eliminaLibro(String ISBN) {
        // Da implementare
        return false;
    }
    
    // Per la modifica, restituisci il libro e lo modifichi nel controller, oppure passi i dati qui
    public Libro getLibroByISBN(String ISBN) {
        // Da implementare
        return null;
    }
    
    // --- Metodi per la Ricerca (UC-4) ---
    
    public List<Libro> ricercaLibro(String keyword) {
        // Da implementare (ricerca per titolo, autore, ISBN)
        return new ArrayList<>(); 
    }
    
    // --- Metodi per l'Interfaccia Grafica (Ordinamento) ---
    
    // Requisito UI-1.1
    public List<Libro> getLibriPerTitolo() {
        // Da implementare: return lista ordinata per titolo
        return listlibro; 
    }

    // Requisito UI-1.2
    public List<Libro> getLibriPerAutore() {
        // Da implementare: return lista ordinata per autore
        return listlibro;
    }
    
    public List<Libro> getLista() {
        return listlibro;
    }
}
package Classibiblioteca.Entità;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Libro 
 * @implements serializable
 * 
 * rappresenta libro con dettagli, get e set di data
 */
public class Libro implements Serializable {
    

    private String titolo;
    private List<String> autore;
    private LocalDate datapubbl;
    private final String ISBN;
    private int numerocopie;
    private int copieprestate;

    public Libro(String titolo, LocalDate datapubbl, String ISBN, int numerocopie) {
        this.titolo = titolo;
        this.autore = new ArrayList<>();
        this.datapubbl = datapubbl;
        this.ISBN = ISBN;
        this.numerocopie = numerocopie;
        this.copieprestate=0;
    }

    // --- Getter e Setter ---

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public List<String> getAutore() {
        return autore;
    }

    public void setAutore(List<String> autore) {
        this.autore = autore;
    }

    public LocalDate getDatapubbl() {
        return datapubbl;
    }

    public void setDatapubbl(LocalDate datapubbl) {
        this.datapubbl = datapubbl;
    }

    public String getISBN() {
        return ISBN;
    }

    public int getNumerocopie() {
        return numerocopie;
    }

    public void setNumerocopie(int numerocopie) {
        this.numerocopie = numerocopie;
    }

    // --- Metodi di Logica (Prototipi) ---
    
    // Prototipo: verifica se ci sono copie disponibili
    public boolean isDisponibile() {
        return false; // Da implementare
    }
    
    // Prototipo: verifica se ci sono delle copie prestate
    public boolean isPrestato()
    {
        return this.copieprestate!=0;
    }
/**
 * @override
 * @return restituisce stringa libro con titolo, autore, anno, isbn e copie
 */
    
    @Override
    public String toString() {
        return "Libro{" + "titolo=" + titolo + ", autore=" + autore + ", datapubbl=" + datapubbl + ", ISBN=" + ISBN + ", numerocopie=" + numerocopie + '}';
    }
}
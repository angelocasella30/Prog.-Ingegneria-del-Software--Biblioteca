package classibiblioteca.entita;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Libro.
 * <p>
 * Rappresenta un libro della biblioteca con i relativi
 * dati descrittivi e lo stato delle copie disponibili
 * e prestate.
 * </p>
 *
 * La classe implementa {@link Serializable} per consentire
 * la persistenza dei dati.
 */
public class Libro implements Serializable {
    

    private static final long serialVersionUID = 1L; //Serve per salvare il file e non avere problemi

    private String titolo;
    private List<String> autori; // Ho rinominato in plurale per chiarezza
    private LocalDate datapubbl;
    private final String ISBN;
    private int numeroCopie;     // Copie totali possedute dalla biblioteca
    private int copiePrestate;   // Copie attualmente fuori

    public Libro(String titolo, LocalDate datapubbl, String ISBN, int numeroCopie) {
        this.titolo = titolo;
        this.autori = new ArrayList<>();
        this.datapubbl = datapubbl;
        this.ISBN = ISBN;
        this.numeroCopie = numeroCopie;
        this.copiePrestate = 0;
    }

    // --- Getter e Setter ---

    public String getTitolo()
    { 
        return titolo;
    }
    public void setTitolo(String titolo) 
    {
        this.titolo = titolo; 
    }

    public List<String> getAutori() 
    {
        return autori; 
    }
    
    // Metodo helper per aggiungere un singolo autore
    public void aggiungiAutore(String autore)
    {
        if (autore != null && !autore.isEmpty()) {
            this.autori.add(autore);
        }
    }

    public void setAutori(List<String> autori)
    {
        this.autori = autori; 
    }

    public LocalDate getDatapubbl() 
    {
        return datapubbl; 
    }
    public void setDatapubbl(LocalDate datapubbl)
    {
        this.datapubbl = datapubbl; 
    }

    public String getISBN()
    { 
        return ISBN; 
    }

    public int getNumeroCopie() 
    { 
        return numeroCopie; 
    }
    
    // Se modifichi le copie totali, non puoi scendere sotto quelle già prestate
    public void setNumeroCopie(int numeroCopie)
    {
        if (numeroCopie >= copiePrestate) 
        {
            this.numeroCopie = numeroCopie;
        }
    }
    
    public int getCopiePrestate() 
    { 
        return copiePrestate;
    }

    // --- Metodi di Logica ---

    /**
     * Verifica se c'è almeno una copia fisica disponibile per il prestito.
     */
    public boolean isDisponibile() 
    {
        return copiePrestate < numeroCopie;
    }
    
    /**
     * Incrementa il contatore delle copie prestate (se possibile).
     * @return true se l'operazione ha successo, false se non ci sono copie.
     */
    public boolean presta() 
    {
        if (isDisponibile()) {
            copiePrestate++;
            return true;
        }
        return false;
    }

/**
 * Registra la restituzione di una copia del libro.
 *
 * @return true se la restituzione è avvenuta,
 *         false se non risultano copie prestate
 */

    public boolean restituisci() 
    {
        if (copiePrestate > 0) {
            copiePrestate--;
            return true;
        }
        return false;
    }
    
/**
 * Restituisce il primo autore del libro.
 *
 * @return nome del primo autore oppure stringa vuota
 */

    public String getPrimoAutore() 
    {
    return (autori != null && !autori.isEmpty()) ? autori.get(0) : "";
        
    }
    
/**
 * Verifica se si presenta almeno un prestito attivo
 *
 * @return true se esistono prestiti attivi, false altrimenti
 */
    public boolean isPrestato() 
    {
        return copiePrestate > 0;
    }
    
    // --- Equals, HashCode e ToString ---
/**
 * Confronta due libri tramite ISBN
 *
 * @param x oggetto da confrontare
 * @return true se i due libri hanno lo stesso ISBN
 */

    @Override
    public boolean equals(Object x) 
    {
        if (this == x) return true;
        if (x == null || getClass() != x.getClass()) return false;
        Libro libro = (Libro) x;
        return this.ISBN.equals(libro.ISBN);
    }
/**
 * Calcola l'hash code del libro in base all'ISBN.
 *
 * @return valore hash del libro
 */

    @Override
    public int hashCode() 
    {
        return this.ISBN != null ? this.ISBN.hashCode() : 0;  
    }
/**
 * Restituisce un' output testuale del libro.
 *
 * @return stringa descrittiva del libro
 */

    @Override
    public String toString()
    {
        // Formattazione più leggibile per l'output
        return titolo + " (ISBN: " + ISBN + ") - Disp: " + (numeroCopie - copiePrestate) + "/" + numeroCopie;
    }
}
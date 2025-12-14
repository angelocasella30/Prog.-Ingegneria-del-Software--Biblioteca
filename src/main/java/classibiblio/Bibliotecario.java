package classibiblio;

import java.io.Serializable;

/**
 * Rappresenta il Bibliotecario, ovvero l'attore (stakeholder)
 * che interagisce con il sistema di gestione della biblioteca.
 * <p>
 * Il bibliotecario possiede un riferimento all'archivio della biblioteca
 * che utilizza per gestire libri, utenti e prestiti.
 * </p>
 */

public class Bibliotecario implements Serializable {
    

    private final String nomeBiblioteca;
    private final Archivio archiviobiblioteca;

    /**
     * Costruttore della classe Bibliotecario.
     *
     * @param nomeBiblioteca nome della biblioteca gestita
     * @param archivioBiblioteca archivio contenente i dati della biblioteca
     */   
    public Bibliotecario(String nomeBiblioteca, Archivio archiviobiblioteca) {
        this.nomeBiblioteca = nomeBiblioteca;
        this.archiviobiblioteca = archiviobiblioteca;
    }
    /**
     * Restituisce il nome della biblioteca.
     *
     * @return nome della biblioteca
     */

    public String getNomeBiblioteca() {
        return nomeBiblioteca;
    }
    /**
     * Restituisce l'archivio associato alla biblioteca.
     *
     * @return archivio della biblioteca
     */

    public Archivio getArchivio() {
        return archiviobiblioteca;
    }
}
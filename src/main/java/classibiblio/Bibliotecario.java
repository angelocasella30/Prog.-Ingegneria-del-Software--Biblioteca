package classibiblio;

import java.io.Serializable;

/**
 * Rappresenta l'attore (Stakeholder) che interagisce con il sistema.
 * Mantiene il riferimento all'Archivio corrente.
 */
public class Bibliotecario implements Serializable {
    

    private final String nomeBiblioteca;
    private final Archivio archiviobiblioteca;
    
    public Bibliotecario(String nomeBiblioteca, Archivio archiviobiblioteca) {
        this.nomeBiblioteca = nomeBiblioteca;
        this.archiviobiblioteca = archiviobiblioteca;
    }

    public String getNomeBiblioteca() {
        return nomeBiblioteca;
    }

    public Archivio getArchivio() {
        return archiviobiblioteca;
    }
}
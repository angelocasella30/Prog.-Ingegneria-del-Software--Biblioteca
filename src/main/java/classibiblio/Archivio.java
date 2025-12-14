package classibiblio;

import classibiblio.tipologiearchivi.ArchivioLibri;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import java.io.*;

/**
 * Classe Facade che aggrega tutti gli archivi del sistema.
 * <p>
 * Fornisce un punto di accesso unico ai dati della biblioteca
 * (utenti, libri e prestiti) e gestisce la persistenza
 * dell'intero stato dell'applicazione su file.
 * </p>
 */
public class Archivio implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Archivio degli utenti */
    private ArchivioUtenti archivioUtenti;

    /** Archivio dei libri */
    private ArchivioLibri archivioLibri;

    /** Archivio dei prestiti */
    private ArchivioPrestiti archivioPrestiti;

    /**
     * Costruttore di default.
     * <p>
     * Inizializza archivi vuoti ed è utilizzato
     * al primo avvio dell'applicazione.
     * </p>
     */
    public Archivio() {
        this.archivioUtenti = new ArchivioUtenti();
        this.archivioLibri = new ArchivioLibri();
        this.archivioPrestiti = new ArchivioPrestiti();
    }

    /**
     * Costruttore parametrizzato.
     * <p>
     * Utilizzato principalmente per test o per iniezione
     * di archivi già esistenti.
     * </p>
     *
     * @param archivioUtenti archivio utenti
     * @param archivioLibri archivio libri
     * @param archivioPrestiti archivio prestiti
     */
    public Archivio(ArchivioUtenti archivioUtenti,
                    ArchivioLibri archivioLibri,
                    ArchivioPrestiti archivioPrestiti) {
        this.archivioUtenti = archivioUtenti;
        this.archivioLibri = archivioLibri;
        this.archivioPrestiti = archivioPrestiti;
    }

    /**
     * Restituisce l'archivio utenti.
     *
     * @return archivio utenti
     */
    public ArchivioUtenti getArchivioUtenti() {
        return archivioUtenti;
    }

    /**
     * Restituisce l'archivio libri.
     *
     * @return archivio libri
     */
    public ArchivioLibri getArchivioLibri() {
        return archivioLibri;
    }

    /**
     * Restituisce l'archivio prestiti.
     *
     * @return archivio prestiti
     */
    public ArchivioPrestiti getArchivioPrestiti() {
        return archivioPrestiti;
    }

    /**
     * Salva lo stato completo dell'archivio su file binario.
     *
     * @param archivio archivio da salvare
     * @param filepath percorso del file
     * @throws IOException in caso di errori di I/O
     */
    public static void salvaArchivio(Archivio archivio, String filepath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(archivio);
        }
    }

    /**
     * Carica lo stato dell'archivio da file binario.
     *
     * @param filepath percorso del file
     * @return archivio ricostruito
     * @throws IOException in caso di errori di I/O
     * @throws ClassNotFoundException se la classe non è compatibile
     */
    public static Archivio caricaArchivio(String filepath)
            throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filepath))) {
            return (Archivio) in.readObject();
        }
    }
}

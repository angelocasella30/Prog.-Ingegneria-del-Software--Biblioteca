package Classibiblioteca;

import Classibiblioteca.tipologiearchivi.ArchivioLibri;
import Classibiblioteca.tipologiearchivi.ArchivioPrestiti;
import Classibiblioteca.tipologiearchivi.ArchivioUtenti;
import java.io.*;

/**
 * Classe Facade che aggrega tutti gli archivi del sistema.
 * Gestisce la persistenza dei dati su file (DF-1.4).
 */
public class Archivio implements Serializable {

    private ArchivioUtenti listautenti;
    private ArchivioLibri listalibri;
    private ArchivioPrestiti listaprestiti;

    // Costruttore vuoto: Inizializza archivi vuoti (per il primo avvio)
    public Archivio() {
        this.listautenti = new ArchivioUtenti();
        this.listalibri = new ArchivioLibri();
        this.listaprestiti = new ArchivioPrestiti();
    } //decidere se metterlo

    // Costruttore con parametri (utile per test o iniezioni)
    public Archivio(ArchivioUtenti listautenti, ArchivioLibri listalibri, ArchivioPrestiti listaprestiti) {
        this.listautenti = listautenti;
        this.listalibri = listalibri;
        this.listaprestiti = listaprestiti;
    }

    // --- Getters per accedere ai sotto-archivi ---
    // I Controller useranno questi metodi per manipolare i dati specifici

    public ArchivioUtenti getArchivioUtenti() {
        return listautenti;
    }

    public ArchivioLibri getArchivioLibri() {
        return listalibri;
    }

    public ArchivioPrestiti getArchivioPrestiti() {
        return listaprestiti;
    }

    // --- Gestione Persistenza (Salvataggio/Caricamento) ---

    /**
     * Salva l'intero stato dell'archivio su un file binario.
     * @param archivio L'oggetto da salvare.
     * @param filepath Il percorso del file.
     * @throws IOException Se ci sono errori di scrittura su disco.
     */
    public static void salvaArchivio(Archivio archivio, String filepath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filepath))) {
            out.writeObject(archivio);
        }
    }

    /**
     * Carica lo stato dell'archivio da un file binario.
     * @param filepath Il percorso del file da leggere.
     * @return L'oggetto Archivio ricostruito.
     * @throws IOException Se il file non esiste o non è leggibile.
     * @throws ClassNotFoundException Se la classe nel file non corrisponde al codice.
     */
    public static Archivio caricaArchivio(String filepath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filepath))) {
            return (Archivio) in.readObject();
        }
    }
}
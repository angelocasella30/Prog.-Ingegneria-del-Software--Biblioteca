package classibiblio;

import classibiblio.tipologiearchivi.ArchivioLibri;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblio.tipologiearchivi.ArchivioUtenti;

import java.io.*;
import java.nio.file.*;
import static java.nio.file.StandardCopyOption.*;

public class Archivio implements Serializable {

    private static final long serialVersionUID = 1L;

    // Percorso di default: <working dir>/data/archivio.dat
    public static final String DEFAULT_PATH = "data/archivio.dat";

    private ArchivioUtenti archivioUtenti;
    private ArchivioLibri archivioLibri;
    private ArchivioPrestiti archivioPrestiti;

    public Archivio() {
        this.archivioUtenti = new ArchivioUtenti();
        this.archivioLibri = new ArchivioLibri();
        this.archivioPrestiti = new ArchivioPrestiti();
    }

    public Archivio(ArchivioUtenti archivioUtenti, ArchivioLibri archivioLibri, ArchivioPrestiti archivioPrestiti) {
        this.archivioUtenti = archivioUtenti != null ? archivioUtenti : new ArchivioUtenti();
        this.archivioLibri = archivioLibri != null ? archivioLibri : new ArchivioLibri();
        this.archivioPrestiti = archivioPrestiti != null ? archivioPrestiti : new ArchivioPrestiti();
    }

    public ArchivioUtenti getArchivioUtenti() { return archivioUtenti; }
    public ArchivioLibri getArchivioLibri() { return archivioLibri; }
    public ArchivioPrestiti getArchivioPrestiti() { return archivioPrestiti; }

    // -------- Persistenza --------

    public static Path defaultPath() {
        return Paths.get(System.getProperty("user.dir"), DEFAULT_PATH);
    }

    public static Archivio caricaDefault() throws IOException, ClassNotFoundException {
        return carica(defaultPath());
    }

    public static Archivio carica(Path path) throws IOException, ClassNotFoundException {
        if (path == null) throw new IllegalArgumentException("Path nullo");
        if (Files.notExists(path)) return new Archivio(); // primo avvio

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            Object obj = in.readObject();
            return (Archivio) obj;
        }
    }

    public void salvaDefault() throws IOException {
        salva(this, defaultPath());
    }

    public static void salva(Archivio archivio, Path path) throws IOException {
        if (archivio == null) throw new IllegalArgumentException("Archivio nullo");
        if (path == null) throw new IllegalArgumentException("Path nullo");

        Files.createDirectories(path.getParent());

        Path tmp = Paths.get(path.toString() + ".tmp");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(tmp))) {
            out.writeObject(archivio);
        }

        try {
            Files.move(tmp, path, REPLACE_EXISTING, ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException ex) {
            Files.move(tmp, path, REPLACE_EXISTING);
        }
    }
}
package classibiblio;

import classibiblio.tipologiearchivi.ArchivioLibri;
import classibiblio.tipologiearchivi.ArchivioPrestiti;
import classibiblio.tipologiearchivi.ArchivioUtenti;
import classibiblioteca.entita.Libro;
import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

/**
 * Test per la classe Archivio.
 * <p>
 * Verifica:
 * - Inizializzazione e getter dei tre archivi
 * - Salvataggio su file (serializzazione)
 * - Caricamento da file (deserializzazione)
 * - Gestione errori (path nullo, file inesistente, ecc.)
 * - Integrazione: ciclo completo prestito-restituzione con persistenza
 * </p>
 */
public class ArchivioTest {

    private Archivio archivio;
    private Path tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        archivio = new Archivio();
        // Crea un file temporaneo per i test di persistenza
        tempFile = Files.createTempFile("test_archivio", ".dat");
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Pulisci il file temporaneo
        if (tempFile != null && Files.exists(tempFile)) {
            Files.deleteIfExists(tempFile);
        }
    }

    /*
     * TEST 1: Inizializzazione e Getter
     * Metodi testati:
     * - Costruttore predefinito
     * - getArchivioUtenti()
     * - getArchivioLibri()
     * - getArchivioPrestiti()
     */
    @Test
    public void testInizilaizazioneEGetter() {
        System.out.println("Test 1: Inizializzazione e Getter");

        // Verifica che gli archivi siano inizializzati
        assertNotNull(archivio.getArchivioUtenti(), "ArchivioUtenti non deve essere null");
        assertNotNull(archivio.getArchivioLibri(), "ArchivioLibri non deve essere null");
        assertNotNull(archivio.getArchivioPrestiti(), "ArchivioPrestiti non deve essere null");

        // Verifica che siano vuoti all'inizio
        assertEquals(0, archivio.getArchivioUtenti().getLista().size(), "ArchivioUtenti deve essere vuoto");
        assertEquals(0, archivio.getArchivioLibri().getLista().size(), "ArchivioLibri deve essere vuoto");
        assertEquals(0, archivio.getArchivioPrestiti().getLista().size(), "ArchivioPrestiti deve essere vuoto");
    }

    /*
     * TEST 2: Costruttore con Parametri
     * Metodi testati:
     * - Costruttore(archivioUtenti, archivioLibri, archivioPrestiti)
     * - Null-safety: se un parametro è null, viene sostituito con una nuova istanza
     */
    @Test
    public void testCostruttoreConParametri() {
        System.out.println("Test 2: Costruttore con Parametri (Null Safe)");

        ArchivioUtenti archivioUtentiCustom = new ArchivioUtenti();
        ArchivioLibri archivioLibriCustom = new ArchivioLibri();
        ArchivioPrestiti archivioPrestitiCustom = new ArchivioPrestiti();

        // Crea Archivio con istanze custom
        Archivio archivioCustom = new Archivio(archivioUtentiCustom, archivioLibriCustom, archivioPrestitiCustom);

        assertNotNull(archivioCustom.getArchivioUtenti());
        assertNotNull(archivioCustom.getArchivioLibri());
        assertNotNull(archivioCustom.getArchivioPrestiti());

        // Crea Archivio con null (deve creare nuove istanze)
        Archivio archivioConNull = new Archivio(null, null, null);

        assertNotNull(archivioConNull.getArchivioUtenti(), "Deve creare nuova istanza se null");
        assertNotNull(archivioConNull.getArchivioLibri(), "Deve creare nuova istanza se null");
        assertNotNull(archivioConNull.getArchivioPrestiti(), "Deve creare nuova istanza se null");
    }

    /*
     * TEST 3: Salvataggio e Caricamento Base
     * Metodi testati:
     * - Archivio.salva(archivio, path)
     * - Archivio.carica(path)
     */
    @Test
    public void testSalvataggioECaricamento() throws IOException, ClassNotFoundException {
        System.out.println("Test 3: Salvataggio e Caricamento Base");

        // Aggiungi dati all'archivio
        Utente utente = new Utente("Angelo", "Casella", "000000", "angelo@test.com");
        archivio.getArchivioUtenti().aggiungiUtente(utente);

        Libro libro = new Libro("Java Programming", LocalDate.of(2020, 1, 1), "ISBN-001", 5);
        libro.aggiungiAutore("Joshua Bloch");
        archivio.getArchivioLibri().aggiungiLibro(libro);

        // Salva l'archivio
        Archivio.salva(archivio, tempFile);
        assertTrue(Files.exists(tempFile), "Il file deve essere stato creato");

        // Carica l'archivio
        Archivio caricato = Archivio.carica(tempFile);

        // Verifica che i dati siano stati ripristinati
        assertEquals(1, caricato.getArchivioUtenti().getLista().size(), "1 utente deve essere caricato");
        assertEquals("Angelo", caricato.getArchivioUtenti().getUtenteByMatricola("000000").getNome());

        assertEquals(1, caricato.getArchivioLibri().getLista().size(), "1 libro deve essere caricato");
        assertEquals("ISBN-001", caricato.getArchivioLibri().getLibroByISBN("ISBN-001").getISBN());
    }

    /**
     * TEST 4: Caricamento File Inesistente
     * Metodi testati:
     * - Archivio.carica(path) quando il file non esiste
     * Comportamento atteso: ritorna un nuovo Archivio() vuoto
     */
    @Test
    public void testCaricamentoFileInesistente() throws IOException, ClassNotFoundException {
        System.out.println("Test 4: Caricamento File Inesistente");

        Path pathInesistente = Files.createTempFile("inesistente_", ".dat");
        Files.deleteIfExists(pathInesistente); // Elimina il file

        Archivio caricato = Archivio.carica(pathInesistente);

        assertNotNull(caricato, "Deve ritornare un nuovo Archivio");
        assertEquals(0, caricato.getArchivioUtenti().getLista().size(), "Deve essere vuoto");
        assertEquals(0, caricato.getArchivioLibri().getLista().size(), "Deve essere vuoto");
        assertEquals(0, caricato.getArchivioPrestiti().getLista().size(), "Deve essere vuoto");
    }


    /**
     * TEST 5: Gestione Errori
     * Metodi testati:
     * - Archivio.salva() con path null  
     * - Archivio.carica() con path null  
     */
    @Test
    public void testGestioneErroriPathNull() {
        System.out.println("Test 5: Gestione Errori - Path Null");

        // Test salva con archivio null
        assertThrows(IllegalArgumentException.class, () -> {
            Archivio.salva(null, tempFile);
        }, "Deve lanciare IllegalArgumentException se archivio è null");

        // Test salva con path null
        assertThrows(IllegalArgumentException.class, () -> {
            Archivio.salva(archivio, null);
        }, "Deve lanciare IllegalArgumentException se path è null");

        // Test carica con path null
        assertThrows(IllegalArgumentException.class, () -> {
            Archivio.carica(null);
        }, "Deve lanciare IllegalArgumentException se path è null");
    }


    /*
     * TEST 6: Path Predefinito
     * Metodi testati:
     * - Archivio.defaultPath()
     */
    @Test
    public void testDefaultPath() {
        System.out.println("Test 6: Path Predefinito");

        Path defaultPath = Archivio.defaultPath();

        assertNotNull(defaultPath, "defaultPath non deve essere null");
        assertTrue(defaultPath.toString().contains("archivio.dat"), "Deve contenere 'archivio.dat'");
        assertTrue(defaultPath.toString().contains("data"), "Deve contenere la cartella 'data'");
    }


    /*
     * TEST 7: TEST D'INTEGRAZIONE - Ciclo Completo Prestito-Restituzione
     * 
     * Verifica che dopo il ciclo prestito-restituzione:
     * 1. I dati siano salvati correttamente
     * 2. Dopo il caricamento, lo stato sia coerente
     */
    @Test
    public void testIntegrazioneCicloCompletoPrestito() throws IOException, ClassNotFoundException {
        System.out.println("Test 7: Integrazione - Ciclo Completo Prestito-Restituzione");

        // Creazione dati
        Utente utente = new Utente("Mario", "Rossi", "MAT-100", "mario@studenti.uni.it");
        archivio.getArchivioUtenti().aggiungiUtente(utente);

        Libro libro = new Libro("Effective Java", LocalDate.of(2020, 1, 1), "ISBN-EJ", 3);
        libro.aggiungiAutore("Joshua Bloch");
        archivio.getArchivioLibri().aggiungiLibro(libro);

        // Creazione Prestito
        LocalDate oggi = LocalDate.now();
        Prestito prestito = new Prestito(
            utente.getEmail(),
            utente.getMatricola(),
            libro.getTitolo(),
            libro.getISBN(),
            oggi,
            oggi.plusDays(30)
        );

        archivio.getArchivioPrestiti().aggiungiPrestito(prestito);
        libro.presta();

        // Verifica pre salvataggio
        assertEquals(1, libro.getCopiePrestate(), "1 copia deve essere prestata");
        assertEquals(1, archivio.getArchivioPrestiti().getPrestitiAttivi().size(), "1 prestito attivo");
        assertEquals("In Corso", prestito.getStato(), "Stato deve essere In Corso");

        // Salvataggio
        Archivio.salva(archivio, tempFile);
        assertTrue(Files.exists(tempFile), "Il file deve essere stato creato");

        // Caricamento
        Archivio caricato = Archivio.carica(tempFile);

        // Verifica post caricamento
        assertEquals(1, caricato.getArchivioUtenti().getLista().size(), "Utente deve essere caricato");
        assertEquals(1, caricato.getArchivioLibri().getLista().size(), "Libro deve essere caricato");
        assertEquals(1, caricato.getArchivioPrestiti().getLista().size(), "Prestito deve essere caricato");

        Libro libroCaricato = caricato.getArchivioLibri().getLibroByISBN("ISBN-EJ");
        assertEquals(1, libroCaricato.getCopiePrestate(), "Le copie prestate devono essere mantenunte");

        Prestito prestitoCaricato = caricato.getArchivioPrestiti().getLista().get(0);
        assertEquals("In Corso", prestitoCaricato.getStato(), "Stato prestito deve essere mantentuto");

        // Restituzione
        caricato.getArchivioPrestiti().restituzioneLibro("MAT-100", "ISBN-EJ");
        libroCaricato.restituisci();

        // ===== FASE 8: Verifica post-restituzione =====
        assertEquals(0, libroCaricato.getCopiePrestate(), "Nessuna copia deve essere prestata");
        assertEquals(0, caricato.getArchivioPrestiti().getPrestitiAttivi().size(), "Nessun prestito attivo");
        assertEquals(1, caricato.getArchivioPrestiti().getPrestitiStorico().size(), "1 prestito in storico");
        assertEquals("Concluso", prestitoCaricato.getStato(), "Stato deve essere Concluso");

        // ===== FASE 9: Salvataggio stato finale e ricaricamento =====
        Archivio.salva(caricato, tempFile);
        Archivio ricaricato = Archivio.carica(tempFile);

        // Verifica finale
        assertEquals(0, ricaricato.getArchivioLibri().getLibroByISBN("ISBN-EJ").getCopiePrestate(), 
                     "Lo stato finale deve essere persistito");
        assertEquals(1, ricaricato.getArchivioPrestiti().getPrestitiStorico().size(), 
                     "Lo storico deve essere persistito");
    }

    /**
     * TEST 8: Gestione Multipli Utenti, Libri e Prestiti
     * Verifica che la serializzazione funzioni correttamente
     * quando ci sono multipli elementi in ogni archivio
     */
    @Test
    public void testSalvataggioMultipliElementi() throws IOException, ClassNotFoundException {
        System.out.println("Test 8: Salvataggio Multipli Elementi");

        // Aggiungi 3 utenti
        for (int i = 0; i < 3; i++) {
            Utente u = new Utente("Nome" + i, "Cognome" + i, "MAT-" + i, "email" + i + "@test.com");
            archivio.getArchivioUtenti().aggiungiUtente(u);
        }

        // Aggiungi 3 libri
        for (int i = 0; i < 3; i++) {
            Libro l = new Libro("Titolo " + i, LocalDate.now(), "ISBN-" + i, 5);
            l.aggiungiAutore("Autore " + i);
            archivio.getArchivioLibri().aggiungiLibro(l);
        }

        // Aggiungi 3 prestiti
        for (int i = 0; i < 3; i++) {
            Prestito p = new Prestito(
                "email" + i + "@test.com",
                "MAT-" + i,
                "Titolo " + i,
                "ISBN-" + i,
                LocalDate.now(),
                LocalDate.now().plusDays(30)
            );
            archivio.getArchivioPrestiti().aggiungiPrestito(p);
        }

        // Salva e ricarica
        Archivio.salva(archivio, tempFile);
        Archivio caricato = Archivio.carica(tempFile);

        // Verifica
        assertEquals(3, caricato.getArchivioUtenti().getLista().size(), "3 utenti devono essere caricati");
        assertEquals(3, caricato.getArchivioLibri().getLista().size(), "3 libri devono essere caricati");
        assertEquals(3, caricato.getArchivioPrestiti().getLista().size(), "3 prestiti devono essere caricati");
    }

    /**
     * TEST 9: Caricamento Default
     * Metodi testati:
     * - Archivio.caricaDefault() quando il file non esiste
     */
    @Test
    public void testCaricamentoDefault() throws IOException, ClassNotFoundException {
        System.out.println("Test 9: Caricamento Default");

        // Se il file di default non esiste, caricaDefault() deve ritornare un nuovo Archivio
        Archivio caricato = Archivio.caricaDefault();

        assertNotNull(caricato, "caricaDefault deve ritornare un Archivio non null");
        assertNotNull(caricato.getArchivioUtenti());
        assertNotNull(caricato.getArchivioLibri());
        assertNotNull(caricato.getArchivioPrestiti());
    }

    /**
     * TEST 10: Verifica Serializzazione della Classe
     * Verifica che Archivio implementi Serializable correttamente
     */
    @Test
    public void testSerializableInterface() {
        System.out.println("Test 10: Verifica Interfaccia Serializable");

        // Verifica che Archivio implementi Serializable
        assertTrue(archivio instanceof java.io.Serializable, "Archivio deve implementare Serializable");

        // Verifica che gli archivi interni implementino Serializable
        assertTrue(archivio.getArchivioUtenti() instanceof java.io.Serializable, 
                   "ArchivioUtenti deve implementare Serializable");
        assertTrue(archivio.getArchivioLibri() instanceof java.io.Serializable, 
                   "ArchivioLibri deve implementare Serializable");
        assertTrue(archivio.getArchivioPrestiti() instanceof java.io.Serializable, 
                   "ArchivioPrestiti deve implementare Serializable");
    }
}

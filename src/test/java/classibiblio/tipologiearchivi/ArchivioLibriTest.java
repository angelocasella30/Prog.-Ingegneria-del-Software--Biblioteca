package classibiblio.tipologiearchivi;

import classibiblioteca.entita.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

/*
 * Test per la classe ArchivioLibri.
 */
public class ArchivioLibriTest 
{

    private ArchivioLibri archivio;
    private Libro libro1; // Java (Gosling)
    private Libro libro2; // Algoritmi (Cormen)
    private Libro libro3; // Paperone (Disney)

    @BeforeEach 
    public void setUp()
    {
        archivio = new ArchivioLibri();
        
        // Inizializzo i libri con i dati per i test
        
        // Libro 1: Titolo "Java", Autore "Gosling James"
        libro1 = new Libro("Java Programming", LocalDate.of(2020, 1, 1), "ISBN-001", 5);
        libro1.aggiungiAutore("Gosling James");
        libro1.aggiungiAutore("Altro Autore");

        // Libro 2: Titolo "Algoritmi", Autore "Cormen Thomas"
        libro2 = new Libro("Algoritmi Avanzati", LocalDate.of(2018, 5, 20), "ISBN-002", 3);
        libro2.aggiungiAutore("Cormen Thomas");

        // Libro 3: Titolo "Zio Paperone", Autore "Disney Walt"
        libro3 = new Libro("Zio Paperone", LocalDate.of(1990, 12, 5), "ISBN-003", 10);
        libro3.aggiungiAutore("Disney Walt");
    }

    /*
     * TEST 1: Inserimento e Controllo Duplicati
     * Metodi testati: 
     * - aggiungiLibro()
     * - existByIsbn()
     * - getLista()
     */
    @Test
    public void testAggiuntaEVerifica() {
        System.out.println("Test 1: Inserimento e Controllo Duplicati ISBN");

        // 1. Verifico che all'inizio non esista l'ISBN
        assertFalse(archivio.existByIsbn("ISBN-001"), "L'ISBN non dovrebbe esistere già");
        
        // 2. Aggiungo il primo libro
        archivio.aggiungiLibro(libro1);
        
        // 3. Verifico che ora esista
        assertTrue(archivio.existByIsbn("ISBN-001"), "L'ISBN dovrebbe esistere dopo l'aggiunta");
        assertEquals(1, archivio.getLista().size(), "La lista deve contenere 1 libro");

        // 4. Provo ad aggiungere un duplicato di ISBN
        Libro cloneLibro1 = new Libro("Titolo Fake", LocalDate.now(), "ISBN-001", 1);
        cloneLibro1.aggiungiAutore("Autore X");
        
        archivio.aggiungiLibro(cloneLibro1); // Dovrebbe stampare errore e non aggiungere
        
        // 5. La lista deve rimanere di 1 elemento
        assertEquals(1, archivio.getLista().size(), "Non deve essere possibile aggiungere duplicati");
    }

    /*
     * TEST 2: Eliminazione e Blocco Prestiti
     * Metodi testati:
     * - eliminaLibro()
     * - libro.presta() (Logica di dominio)
     */
    @Test
    public void testEliminazioneConVincoli() {
        System.out.println("Test 2: Eliminazione e Vincolo Prestiti");

        archivio.aggiungiLibro(libro1);
        archivio.aggiungiLibro(libro2);
        
        // CASO A: Eliminazione corretta (Nessuna copia prestata)
        // Elimino libro1 che non ha prestiti
        boolean esitoEliminazione = archivio.eliminaLibro("ISBN-001");
        assertTrue(esitoEliminazione, "L'eliminazione deve restituire true se non ci sono prestiti");
        assertNull(archivio.getLibroByISBN("ISBN-001"), "Il libro non deve più esistere");

        // CASO B: Eliminazione bloccata (Copie in prestito)
        // Simulo il prestito sul libro2
        libro2.presta(); 
        assertTrue(libro2.isPrestato(), "Il libro deve risultare prestato");

        // Provo a eliminare libro2
        boolean esitoFallito = archivio.eliminaLibro("ISBN-002");
        assertFalse(esitoFallito, "L'eliminazione deve fallire se ci sono copie in prestito");
        assertNotNull(archivio.getLibroByISBN("ISBN-002"), "Il libro deve rimanere nell'archivio");
    }

    /*
     * TEST 3: Ricerca Libro (Keyword)
     * Metodi testati:
     * - ricercaLibro()
     */
    @Test
    public void testRicerca() 
    {
        System.out.println("Test 3: Ricerca Libro (Per Titolo o Autore)");

        archivio.aggiungiLibro(libro1); // Gosling
        archivio.aggiungiLibro(libro2); // Cormen
        
        // 1. Ricerca del titolo ("algo") -> Deve trovare Algoritmi
        List<Libro> risultatiTitolo = archivio.ricercaLibro("algoritmi");
        assertEquals(1, risultatiTitolo.size());
        assertEquals("ISBN-002", risultatiTitolo.get(0).getISBN());

        // 2. Ricerca  dell'autore ("gosling") -> Deve trovare Java
        List<Libro> risultatiAutore = archivio.ricercaLibro("gosling");
        assertEquals(1, risultatiAutore.size());
        assertEquals("ISBN-001", risultatiAutore.get(0).getISBN());

        // 3. Ricerca fallita
        List<Libro> risultatiVuoti = archivio.ricercaLibro("inesistente");
        assertEquals(0, risultatiVuoti.size());
    }

    /*
     * TEST 4: Ordinamento per Primo Autore
     * Metodi testati:
     * - getLibriPerAutore()
     */
    @Test
    public void testOrdinamentoAutore() {
        System.out.println("Test 4: Ordinamento per Primo Autore");

        // Inserisco in ordine sparso
        archivio.aggiungiLibro(libro1); // Gosling (G)
        archivio.aggiungiLibro(libro2); // Cormen (C)
        archivio.aggiungiLibro(libro3); // Disney (D)
        
        // Lista attesa ordinata: 
        // 1. Cormen (C)
        // 2. Disney (D)
        // 3. Gosling (G)
        
        List<Libro> ordinata = archivio.getLibriPerAutore();
        
        assertEquals("Cormen Thomas", ordinata.get(0).getPrimoAutore());
        assertEquals("Disney Walt", ordinata.get(1).getPrimoAutore());
        assertEquals("Gosling James", ordinata.get(2).getPrimoAutore());
        
        // Verifico che la lista originale sia intatta (ordine di inserimento: G, C, D)
        assertEquals("Gosling James", archivio.getLista().get(0).getPrimoAutore());
    }

    /*
     * TEST 5: Ordinamento per Titolo
     * Metodi testati:
     * - getLibriPerTitolo()
     */
    @Test
    public void testOrdinamentoTitolo() {
        System.out.println("Test 5: Ordinamento per Titolo");

        archivio.aggiungiLibro(libro1); // "Java..."
        archivio.aggiungiLibro(libro2); // "Algoritmi..."
        archivio.aggiungiLibro(libro3); // "Zio Paperone..."
        
        // Lista attesa ordinata:
        // 1. Algoritmi
        // 2. Java
        // 3. Zio Paperone

        List<Libro> ordinata = archivio.getLibriPerTitolo();
        
        assertEquals("Algoritmi Avanzati", ordinata.get(0).getTitolo());
        assertEquals("Java Programming", ordinata.get(1).getTitolo());
        assertEquals("Zio Paperone", ordinata.get(2).getTitolo());
        // 1. Ricerca per parte del titolo
    }
}
package classibiblio.entita;

import classibiblioteca.entita.Libro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe Libro.
 * Verifica la gestione delle copie, i dati anagrafici e l'identità del libro.
 */
public class LibroTest 
{

    private Libro libro;

    // Eseguito prima di OGNI test
    @BeforeEach
    public void setUp() 
    {
        // 
        libro = new Libro("Il Signore degli Anelli", LocalDate.of(1954, 7, 29),"0000-0001", 3);
    }

    /*
     * TEST 1: Creazione e Stato Iniziale (Getters)
     * Metodi testati:
     * - Costruttore
     * - getTitolo(), getISBN(), getDatapubbl()
     * - getNumeroCopie(), getCopiePrestate(), getAutori()
     * - isDisponibile(), isPrestato()
     */
    @Test
    public void testStatoIniziale() 
    {
        System.out.println("Test 1: Creazione e Getters");

        assertEquals("Il Signore degli Anelli", libro.getTitolo());
        assertEquals("0000-0001", libro.getISBN());
        assertEquals(3, libro.getNumeroCopie());
        assertEquals(0, libro.getCopiePrestate());
        
        // Verifica altri campi
        assertNotNull(libro.getAutori());
        assertTrue(libro.getAutori().isEmpty(), "La lista autori deve nascere vuota");
        assertTrue(libro.isDisponibile(), "Deve essere disponibile appena creato");
        assertFalse(libro.isPrestato(), "Non deve risultare prestato all'inizio");
    }

    /**
     * TEST 2: Modifica Dati (Setters)
     * Metodi testati:
     * - setTitolo(), setDatapubbl()
     * - aggiungiAutore(), setAutori()
     * - setNumeroCopie() (logica base)
     */
    @Test
    public void testSetters() {
        System.out.println("Test 2: Modifica Dati (Harry Potter)");

        // Trasformiamo il libro in Harry Potter
        libro.setTitolo("Harry Potter e la Pietra Filosofale");
        libro.setDatapubbl(LocalDate.of(1997, 6, 26));
        
        // Test gestione Autori
        libro.aggiungiAutore("J.K. Rowling");
        
        // Verifica modifiche
        assertEquals("Harry Potter e la Pietra Filosofale", libro.getTitolo());
        assertEquals(1, libro.getAutori().size());
        assertEquals("J.K. Rowling", libro.getAutori().get(0));
        
        // Test modifica copie totali
        libro.setNumeroCopie(10);
        assertEquals(10, libro.getNumeroCopie());
    }

    /*
     * TEST 3: Logica Business - Ciclo Prestito
     * Metodi testati:
     * - presta()
     * - restituisci()
     * - isDisponibile()
     * - getCopiePrestate()
     */
    @Test
    public void testCicloPrestito() 
    {
        System.out.println("Test 3: Ciclo Prestito e Restituzione");

        // Il libro ha 3 copie totali
        
        // 1. Primo prestito
        boolean esito1 = libro.presta();
        assertTrue(esito1, "Il prestito deve riuscire");
        assertEquals(1, libro.getCopiePrestate());
        assertTrue(libro.isDisponibile(), "Con 1/3 copie fuori, deve essere ancora disponibile");

        // 2. Secondo prestito
        libro.presta();
        assertEquals(2, libro.getCopiePrestate());

        // 3. Terzo prestito (Esaurimento copie)
        libro.presta();
        assertEquals(3, libro.getCopiePrestate());
        assertFalse(libro.isDisponibile(), "Con 3/3 copie fuori, non deve essere più disponibile");

        // 4. Tentativo quarto prestito (Fallimento)
        boolean esito4 = libro.presta();
        assertFalse(esito4, "Non puoi prestare se le copie sono finite");
        assertEquals(3, libro.getCopiePrestate(), "Il contatore non deve andare oltre il limite");

        // 5. Restituzione
        boolean esitoRestituzione = libro.restituisci();
        assertTrue(esitoRestituzione, "La restituzione deve riuscire");
        assertEquals(2, libro.getCopiePrestate(), "Le copie prestate devono scendere a 2");
        assertTrue(libro.isDisponibile(), "Ora deve essere di nuovo disponibile");
    }

    /*
     * TEST 4: Logica Business - Limiti Restituzione e Modifica Copie
     * Metodi testati:
     * - restituisci() (caso limite)
     * - setNumeroCopie() (validazione logica)
     */
    @Test
    public void testLimitiLogici() 
    {
        System.out.println("Test 4: Limiti Restituzione e Setter Copie");

        // Caso A: Restituire libro mai prestato
        boolean esito = libro.restituisci();
        assertFalse(esito, "Non puoi restituire un libro che ha 0 copie prestate");
        assertEquals(0, libro.getCopiePrestate(), "Non deve andare in negativo");

        // Caso B: Validazione setNumeroCopie
        // Prendo in prestito 2 copie
        libro.presta();
        libro.presta(); // Ora copiePrestate = 2
        
        // Provo a ridurre le copie totali a 1 (impossibile perché ne ho 2 fuori)
        libro.setNumeroCopie(1);
        assertEquals(3, libro.getNumeroCopie(), "Non deve permettere di ridurre le copie totali sotto al numero di quelle prestate");
        
        // Provo ad aumentarle a 5 (possibile)
        libro.setNumeroCopie(5);
        assertEquals(5, libro.getNumeroCopie());
    }

    /**
     * TEST 5: Identità (Equals e HashCode)
     * Metodi testati:
     * - equals()
     * - hashCode()
     */
    @Test
    public void testEqualsHashCode() 
    {
        System.out.println("Test 5: Equals e HashCode (ISBN)");

        // Libro 1: Signore degli Anelli (ISBN A)
        Libro l1 = new Libro("Il Signore degli Anelli", LocalDate.now(), "ISBN-AAA", 5);
        
        // Libro 2: Harry Potter (MA con stesso ISBN di l1 -> per il sistema sono uguali)
        Libro l2 = new Libro("Harry Potter", LocalDate.now(), "ISBN-AAA", 2);
        
        // Libro 3: Harry Potter (ISBN diverso)
        Libro l3 = new Libro("Harry Potter", LocalDate.now(), "ISBN-BBB", 5);

        // Test Equals
        assertEquals(l1, l2, "Due libri con stesso ISBN devono essere uguali (anche se titolo diverso)");
        assertNotEquals(l2, l3, "Due libri con ISBN diverso devono essere diversi");

        // Test HashCode
        assertEquals(l1.hashCode(), l2.hashCode(), "Se due oggetti sono equals, devono avere stesso hashCode");
    }

    /*
     * TEST 6: Rappresentazione Stringa
     * Metodi testati:
     * - toString()
     */
    @Test
    public void testToString() 
    {
        System.out.println("Test 6: ToString");

        String descrizione = libro.toString();
        
        assertNotNull(descrizione);
        // Verifico che contenga i dati essenziali
        assertTrue(descrizione.contains("Il Signore degli Anelli")); // Titolo
        assertTrue(descrizione.contains("0000-0001"));             // ISBN
        assertTrue(descrizione.contains("3"));                     // Numero copie
    }
}
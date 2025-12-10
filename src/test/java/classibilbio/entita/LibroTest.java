package classibilbio.entita;

import classibiblio.entita.Libro;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe Libro.
 * Verifica la gestione delle copie e l'identità del libro.
 */
public class LibroTest 
{

    public LibroTest() 
    {
    }

    /**
     * Test fondamentale: Verifica che il prestito scali le copie correttamente.
     */
    @Test
    public void testDisponibilitaCopie() 
    {
        System.out.println("Test Disponibilità e Prestito");
        
        // 1. Creo un libro con 2 copie totali
        Libro libro = new Libro("Il signore degli anelli", LocalDate.now(), "888-153", 2);
        
        // All'inizio deve essere disponibile
        assertTrue(libro.isDisponibile(), "Appena creato deve essere disponibile");
        assertEquals(0, libro.getCopiePrestate()); // Nessuna copia fuori
        
        // 2.Prendo in prestito la prima copia
        boolean esito1 = libro.presta();
        
        // 3. Controllo
        assertTrue(esito1, "Il prestito deve andare a buon fine");
        assertEquals(1, libro.getCopiePrestate());
        assertTrue(libro.isDisponibile(), "Con 1 su 2 prestate, è ancora disponibile");

        // Prendo la seconda copia
        boolean esito2 = libro.presta();
        assertTrue(esito2);
        assertEquals(2, libro.getCopiePrestate());
        assertFalse(libro.isDisponibile(), "Con 2 su 2 prestate, NON deve essere disponibile");

        // Provo a prendere la terza copia (non deve riuscirci)
        boolean esito3 = libro.presta();
        assertFalse(esito3, "Non deve prestare se le copie sono finite");
        assertEquals(2, libro.getCopiePrestate(), "Non possono essere prestate più copie di quelle disponibili");
    }

    /**
     * Test della restituzione.
     */
    @Test
    public void testRestituzione() 
    {
        System.out.println("Test Restituzione");
        
        Libro libro = new Libro("Il piccolo principe", LocalDate.now(), "999-000", 1);
        libro.presta(); // Ora 1/1 (Esaurito)
        assertFalse(libro.isDisponibile());
        
        libro.restituisci(); // Torna 0/1 (Disponibile)
        assertTrue(libro.isDisponibile());
        assertEquals(0, libro.getCopiePrestate());
        
        // Provo a restituire un libro mai prestato (non deve andare sotto zero)
        libro.restituisci();
        assertEquals(0, libro.getCopiePrestate(), "Le copie prestate non possono essere meno di 0");
    }
    
    /**
     * Test dell'identità (Equals basato su ISBN).
     */
    @Test
    public void testUguaglianzaIsbn() 
    {
        System.out.println("Test Equals (ISBN)");
        
        Libro l1 = new Libro("Titolo A", LocalDate.now(), "ISBN-UNICO", 5);
        Libro l2 = new Libro("Titolo Diverso", LocalDate.now(), "ISBN-UNICO", 2); // Stesso ISBN
        Libro l3 = new Libro("Titolo A", LocalDate.now(), "ISBN-DIVERSO", 5);
        
        // Devono essere uguali se l'ISBN è uguale, anche se il resto cambia
        assertEquals(l1, l2, "Due libri con stesso ISBN devono essere considerati uguali");
        
        // Devono essere diversi se l'ISBN cambia
        assertNotEquals(l1, l3, "Due libri con ISBN diverso devono essere diversi");
    }
}
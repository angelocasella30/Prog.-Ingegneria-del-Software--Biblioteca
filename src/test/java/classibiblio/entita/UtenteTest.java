package classibiblio.entita;

import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe Utente.
 * Verifica le regole di business (prestiti,storico) e i metodi di accesso.
 */
public class UtenteTest 
{

    private Utente u;

    // Eseguito prima di OGNI test per avere un ambiente pulito
    @BeforeEach
    public void setUp() 
    {
        // Inizializzo l'utente principale: Angelo Casella, Matricola 000001
        u = new Utente("Angelo", "Casella", "000001", "angelo.casella@studenti.uni.it");
    }

    /*
     * TEST 1: Creazione e Stato Iniziale (Getters)
     * Metodi testati:
     * - Costruttore Utente()
     * - getNome(), getCognome(), getMatricola(), getEmail()
     * - getPrestitiattivi(), getStoricoprestiti()
     */
    @Test
    public void testStatoIniziale() 
    {
        System.out.println("Test 1: Creazione Utente e Getters");
        
        // Verifica dei dati
        assertEquals("Angelo", u.getNome());
        assertEquals("Casella", u.getCognome());
        assertEquals("000001", u.getMatricola());
        assertEquals("angelo.casella@studenti.uni.it", u.getEmail());
        
        // Verifica  liste (non devono essere null, ma vuote)
        assertNotNull(u.getPrestitiattivi());
        assertTrue(u.getPrestitiattivi().isEmpty(), "La lista prestiti attivi deve essere vuota all'inizio");
        
        assertNotNull(u.getStoricoprestiti());
        assertTrue(u.getStoricoprestiti().isEmpty(), "Lo storico deve essere vuoto all'inizio");
        
        // Un nuovo utente deve poter chiedere prestiti
        assertTrue(u.puoRichiederePrestito());
    }

    /**
     * TEST 2: Modifica Dati (Setters)
     * Metodi testati:
     * - setNome(), setCognome() , setEmail()
     */
    @Test
    public void testSetters() 
    {
        System.out.println("Test 2: Modifica Dati (Setters)");
        
        // Cambio i valori trasformando l'utente in "Matteo Adinolfi"
        u.setNome("Matteo");
        u.setCognome("Adinolfi");
        u.setEmail("matteo.adinolfi@studenti.uni.it");
        
        // Verifico che siano cambiati correttamenti
        assertEquals("Matteo", u.getNome());
        assertEquals("Adinolfi", u.getCognome());
        assertEquals("matteo.adinolfi@studenti.uni.it", u.getEmail());
    }

    /*
     * TEST 3: Logica Business - Limite Prestiti
     * Metodi testati:
     * - aggiungiPrestito()
     * - puoRichiederePrestito()
     */
    @Test
    public void testLimitePrestiti() 
    {
        System.out.println("Test 3: Limite Max 3 Prestiti");
        
        // Creiamo date fittizie
        LocalDate oggi = LocalDate.now();
        
        // Creiamo 3 prestiti fittizi per Angelo Casella
        Prestito p1 = new Prestito(u.getEmail(), u.getMatricola(), "Libro1", "ISBN1", oggi, oggi.plusDays(30));
        Prestito p2 = new Prestito(u.getEmail(), u.getMatricola(), "Libro2", "ISBN2", oggi, oggi.plusDays(30));
        Prestito p3 = new Prestito(u.getEmail(), u.getMatricola(), "Libro3", "ISBN3", oggi, oggi.plusDays(30));
        
        // 1. Aggiungo primo prestito
        u.aggiungiPrestito(p1);
        assertTrue(u.puoRichiederePrestito(), "Con 1 prestito attivo deve poterne chiedere ancora");
        
        // 2. Aggiungo secondo prestito
        u.aggiungiPrestito(p2);
        assertTrue(u.puoRichiederePrestito(), "Con 2 prestiti attivi deve poterne chiedere ancora");
        
        // 3. Aggiungo terzo prestito (Limite raggiunto)
        u.aggiungiPrestito(p3);
        assertFalse(u.puoRichiederePrestito(), "Con 3 prestiti attivi NON deve poterne chiedere altri");
    }

    /*
     * TEST 4: Ciclo di Vita Prestito (Restituzione e Storico)
     * Metodi testati:
     * - restituisciPrestito()
     * - verificaPrestitiAttivi()
     * - getStoricoprestiti()
     */
    @Test
    public void testFlussoRestituzione()
    {
        System.out.println("Test 4: Restituzione e Storico");
        
        Prestito p = new Prestito(u.getEmail(), u.getMatricola(), "LibroTest", "ISBN-T", LocalDate.now(), LocalDate.now());
        
        // FASE 1: Prestito in corso
        u.aggiungiPrestito(p);
        assertTrue(u.verificaPrestitiAttivi(), "Dovrebbe risultare che ci sono prestiti attivi");
        assertFalse(u.getPrestitiattivi().isEmpty());
        
        // FASE 2: Restituzione
        u.restituisciPrestito(p);
        
        // Verifiche post-restituzione
        assertTrue(u.getPrestitiattivi().isEmpty(), "La lista attivi deve essere vuota dopo la restituzione");
        assertFalse(u.verificaPrestitiAttivi(), "Non dovrebbero esserci prestiti attivi");
        
        // Verifica Storico
        assertEquals(1, u.getStoricoprestiti().size(), "Lo storico deve contenere 1 elemento");
        assertEquals(p, u.getStoricoprestiti().get(0), "L'elemento nello storico deve essere quello restituito");
    }

    /*
     * TEST 5: Uguaglianza tra Utenti
     * Metodi testati:
     * - equals()
     */
    @Test
    public void testEquals() 
    {
        System.out.println("Test 5: Equals (basato su Matricola)");
        
        // Angelo Casella, 000001
        Utente u1 = new Utente("Angelo", "Casella", "000001", "a@a.it");
        // Clone di Angelo Casella, 000001
        Utente u2 = new Utente("Angelo", "Casella", "000001", "a@a.it"); 
        // Matteo Adinolfi, 000002
        Utente u3 = new Utente("Matteo", "Adinolfi", "000002", "b@b.it"); 
        
        assertEquals(u1, u2, "Due utenti con stessa matricola devono essere uguali");
        assertNotEquals(u1, u3, "Due utenti con matricola diversa devono essere diversi");
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
        
        String descrizione = u.toString();
        
        assertNotNull(descrizione, "Il toString non deve essere null");
        // Verifica che contenga almeno i dati fondamentali
        assertTrue(descrizione.contains("Angelo"), "Deve contenere il nome");
        assertTrue(descrizione.contains("Casella"), "Deve contenere il cognome");
        assertTrue(descrizione.contains("000001"), "Deve contenere la matricola");
    }
}
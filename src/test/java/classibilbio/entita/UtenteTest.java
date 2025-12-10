package classibilbio.entita;

import classibiblio.entita.Prestito;
import classibiblio.entita.Utente;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe Utente.
 * Verifica le regole sui prestiti (max 3) e la gestione dello storico.
 */
public class UtenteTest 
{

    public UtenteTest() 
    {
    }

    /**
     * Test Creazione: Verifica che le liste nascano vuote.
     */
    @Test
    public void testStatoIniziale() 
    {
        System.out.println("Test Creazione Utente");
        Utente u = new Utente("Mario", "Rossi", "12345", "mario@studenti.uni.it");
        
        assertEquals("Mario", u.getNome());
        assertNotNull(u.getPrestitiattivi());
        assertTrue(u.getPrestitiattivi().isEmpty(), "La lista prestiti attivi deve essere vuota all'inizio");
        assertTrue(u.getStoricoprestiti().isEmpty(), "Lo storico deve essere vuoto all'inizio");
        assertTrue(u.puoRichiederePrestito(), "Un nuovo utente deve poter chiedere prestiti");
    }

    /**
     * Test Fondamentale: Verifica il limite massimo di 3 prestiti.
     */
    @Test
    public void testLimitePrestiti() 
    {
        System.out.println("Test Limite 3 Prestiti");
        Utente u = new Utente("Luigi", "Verdi", "000007", "luigi@studenti.uni.it");
        
        // Creiamo dei prestiti "finti" per riempire la lista
        LocalDate oggi = LocalDate.now();
        Prestito p1 = new Prestito(u.getEmail(), u.getMatricola(), "Libro1", "ISBN1", oggi, oggi.plusDays(30));
        Prestito p2 = new Prestito(u.getEmail(), u.getMatricola(), "Libro2", "ISBN2", oggi, oggi.plusDays(30));
        Prestito p3 = new Prestito(u.getEmail(), u.getMatricola(), "Libro3", "ISBN3", oggi, oggi.plusDays(30));
        
        // Aggiungo 1 prestito
        u.aggiungiPrestito(p1);
        assertTrue(u.puoRichiederePrestito(), "Con 1 prestito deve poterne chiedere ancora");
        
        // Aggiungo 2 prestiti
        u.aggiungiPrestito(p2);
        assertTrue(u.puoRichiederePrestito(), "Con 2 prestiti deve poterne chiedere ancora");
        
        // Aggiungo 3 prestiti (LIMITE RAGGIUNTO)
        u.aggiungiPrestito(p3);
        assertFalse(u.puoRichiederePrestito(), "Con 3 prestiti NON deve poterne chiedere un altro");
        
    }

    /**
     * Test Restituzione: Verifica lo spostamento da Attivi a Storico.
     */
    @Test
    public void testFlussoRestituzione() 
    {
        System.out.println("Test Flusso Restituzione");
        Utente u = new Utente("Anna", "Neri", "000009", "anna@studenti.uni.it");
        Prestito p = new Prestito(u.getEmail(), u.getMatricola(), "Libro", "ISBN", LocalDate.now(), LocalDate.now());
        
        // 1. Prendo in prestito
        u.aggiungiPrestito(p);
        assertTrue(u.verificaPrestitiAttivi());
        
        // 2. Restituisco
        u.restituisciPrestito(p);
        
        // Controlli
        assertTrue(u.getPrestitiattivi().isEmpty(), "La lista attivi deve essere vuota");
        assertEquals(1, u.getStoricoprestiti().size(), "Lo storico deve contenere 1 elemento");
        assertEquals(p, u.getStoricoprestiti().get(0), "L'elemento nello storico deve essere quello restituito");
    }

    /*
     * Test Equals: Verifica che due utenti siano uguali se hanno la stessa matricola.
     */
    @Test
    public void testUguaglianzaMatricola() 
    {
        System.out.println("Test Equals (Matricola)");
        Utente u1 = new Utente("Mario", "Rossi", "000100", "a@studenti.uni.it");
        Utente u2 = new Utente("Mario", "Rossi", "000100", "a@studenti.uni.it"); // Stessa matricola
        Utente u3 = new Utente("Luigi", "Verdi", "000999", "b@studenti.uni.it"); // Diversa matricola
        
        assertEquals(u1, u2, "Gli utenti con la stessa matricola devono essere uguali");
        assertNotEquals(u1, u3, "Gli utenti con una matricola diversa devono essere diversi");
    }
    /*
    Test per validità dominio email:
    Verifica che l
    */
    @Test
    public void testValiditaEmailDoppioDominio() {
        System.out.println("Test Validità Email (Studenti e Docenti)");
        
        // 1. Caso Studente Valido
        Utente studente = new Utente("Mario", "Rossi", "S01", "mario@studenti.uni.it");
        assertTrue(studente.puoRichiederePrestito(), "Email @studenti.uni.it deve essere accettata");

        // 2. Caso Docente Valido
        Utente docente = new Utente("Prof", "Bianchi", "D01", "prof@docente.uni.it");
        assertTrue(docente.puoRichiederePrestito(), "Email @docente.uni.it deve essere accettata");

        // 3. Caso Utente Esterno (Invalido)
        Utente esterno = new Utente("Hacker", "Evil", "X99", "hacker@gmail.com");
        assertFalse(esterno.puoRichiederePrestito(), "Email @gmail.com NON deve essere accettata");
        
        // 4. Caso Email Errata o Parziale
        Utente errato = new Utente("Test", "Test", "X00", "mario@uni.it"); // Manca "studenti" o "docente"
        assertFalse(errato.puoRichiederePrestito(), "Email parziale non valida deve essere rifiutata");
    }
}
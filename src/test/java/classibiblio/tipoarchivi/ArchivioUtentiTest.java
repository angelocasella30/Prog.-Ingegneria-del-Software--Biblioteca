package classibiblio.tipoarchivi; // Ho corretto il package (era tiporchivi)

import classibiblio.entita.Utente;
// Assicurati che l'import di ArchivioUtenti sia corretto in base alla tua struttura
import classibiblio.tipologiearchivi.ArchivioUtenti; 

import org.junit.jupiter.api.BeforeEach; // <--- FONDAMENTALE per JUnit 5
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class ArchivioUtentiTest {

    private ArchivioUtenti archivio;
    private Utente u1;
    private Utente u2;
    private Utente u3;

    // L'annotazione @BeforeEach dice a JUnit: 
    // "Esegui questo metodo prima di OGNI singolo @Test qui sotto"
    @BeforeEach 
    public void setUp() {
        archivio = new ArchivioUtenti();
        
        // Inizializzo gli utenti di test
        u1 = new Utente("Mario", "Rossi", "100", "mario@test.com");
        u2 = new Utente("Luca", "Bianchi", "200", "luca@test.com");
        u3 = new Utente("Anna", "Verdi", "300", "anna@test.com");
    }

    /**
     * TEST 1: Inserimento e Controllo Esistenza
     * Metodi testati: 
     * - aggiungiUtente()
     * - existByMatricola()
     * - getLista()
     */
    @Test
    public void testAggiuntaEVerifica() {
        // Verifico che all'inizio non esista
        assertFalse(archivio.existByMatricola("100"), "La matricola non dovrebbe esistere all'inizio");
        
        // Aggiungo
        archivio.aggiungiUtente(u1);
        
        // Verifico che ora esista
        assertTrue(archivio.existByMatricola("100"), "La matricola dovrebbe esistere dopo l'aggiunta");
        
        // Verifico che la lista contenga effettivamente 1 elemento
        assertEquals(1, archivio.getLista().size(), "La lista deve contenere 1 elemento");
    }

    /**
     * TEST 2: Unicità della Matricola
     * Metodi testati:
     * - aggiungiUtente() (ramo else)
     */
    @Test
    public void testMatricolaDuplicata() {
        archivio.aggiungiUtente(u1); // Inserisco matricola "100"
        
        // Creo un altro utente con STESSA matricola "100" ma nome diverso
        Utente clone = new Utente("Luigi", "Neri", "100", "clone@test.com");
        
        archivio.aggiungiUtente(clone); // Provo ad aggiungerlo
        
        // La lista deve rimanere di 1 elemento
        assertEquals(1, archivio.getLista().size(), "Non deve essere possibile aggiungere duplicati");
        
        // Controllo che l'utente presente sia ancora l'originale (Mario Rossi)
        Utente salvato = archivio.getUtenteByMatricola("100");
        assertEquals("Mario", salvato.getNome(), "Il nome deve rimanere quello originale");
    }

    /**
     * TEST 3: Recupero e Eliminazione
     * Metodi testati:
     * - getUtenteByMatricola()
     * - eliminaUtente()
     */
    @Test
    public void testRecuperoEdEliminazione() {
        archivio.aggiungiUtente(u1);
        
        // Test Recupero (Caso positivo)
        Utente trovato = archivio.getUtenteByMatricola("100");
        assertNotNull(trovato, "L'utente deve essere trovato");
        assertEquals("Rossi", trovato.getCognome());
        
        // Test Recupero (Caso negativo - matricola inesistente)
        Utente nonTrovato = archivio.getUtenteByMatricola("999");
        assertNull(nonTrovato, "Se la matricola non c'è, deve restituire null");
        
        // Test Eliminazione
        boolean esitoEliminazione = archivio.eliminaUtente(u1);
        assertTrue(esitoEliminazione, "L'eliminazione deve restituire true");
        assertEquals(0, archivio.getLista().size(), "La lista deve essere vuota dopo eliminazione");
    }

    /**
     * TEST 4: Aggiornamento Dati
     * Metodi testati:
     * - aggiornaUtente()
     */
    @Test
    public void testAggiornamentoUtente() {
        archivio.aggiungiUtente(u1); // Mario Rossi, 100
        
        // Provo ad aggiornare una matricola che NON esiste
        boolean esitoFallito = archivio.aggiornaUtente("999", "X", "Y", "Z");
        assertFalse(esitoFallito, "Aggiornamento su matricola inesistente deve fallire");
        
        // Aggiorno l'utente esistente (Matricola 100)
        boolean esitoOk = archivio.aggiornaUtente("100", "MarioJunior", "RossiUpdated", "nuova@email.it");
        assertTrue(esitoOk, "Aggiornamento deve riuscire");
        
        // Verifico che i campi siano cambiati davvero
        Utente uAggiornato = archivio.getUtenteByMatricola("100");
        assertEquals("MarioJunior", uAggiornato.getNome());
        assertEquals("RossiUpdated", uAggiornato.getCognome());
        assertEquals("nuova@email.it", uAggiornato.getEmail());
    }

    /**
     * TEST 5: Ricerca (Caso d'uso UC-8)
     * Metodi testati:
     * - ricercaUtente()
     */
    @Test
    public void testRicerca() {
        archivio.aggiungiUtente(u1); // Rossi
        archivio.aggiungiUtente(u2); // Bianchi
        archivio.aggiungiUtente(u3); // Verdi
        
        // 1. Ricerca per Cognome esatto
        List<Utente> risultati = archivio.ricercaUtente("rossi");
        assertEquals(1, risultati.size(), "Deve trovare 1 utente");
        assertEquals("Mario", risultati.get(0).getNome());
        
        // 2. Ricerca per Matricola esatta
        risultati = archivio.ricercaUtente("200");
        assertEquals(1, risultati.size(), "Deve trovare matricola 200");
        assertEquals("Luca", risultati.get(0).getNome());
        
        // 3. Ricerca che non produce risultati
        risultati = archivio.ricercaUtente("Viola");
        assertEquals(0, risultati.size(), "Non deve trovare nessuno");
        
        // 4. Ricerca parziale (NON deve trovare nulla con equals)
        risultati = archivio.ricercaUtente("Ros");
        assertEquals(0, risultati.size(), "Ricerca parziale non supportata");
    }

    /**
     * TEST 6: Ordinamento (Requisito UI-1.3)
     * Metodi testati:
     * - getUtentiOrdinati()
     */
    @Test
    public void testOrdinamento() {
        // Inserisco in ordine sparso
        archivio.aggiungiUtente(u3); // Verdi
        archivio.aggiungiUtente(u1); // Rossi
        archivio.aggiungiUtente(u2); // Bianchi
        
        Utente u4 = new Utente("Antonio", "Bianchi", "400", "antonio@test.com");
        archivio.aggiungiUtente(u4);
        
        List<Utente> ordinata = archivio.getUtentiOrdinati();
        
        assertEquals("Antonio", ordinata.get(0).getNome()); // Bianchi Antonio
        assertEquals("Luca", ordinata.get(1).getNome());    // Bianchi Luca
        assertEquals("Rossi", ordinata.get(2).getCognome());
        assertEquals("Verdi", ordinata.get(3).getCognome());
        
        // Verifico che la lista originale NON sia stata modificata
        assertEquals("Verdi", archivio.getLista().get(0).getCognome());
    }
}
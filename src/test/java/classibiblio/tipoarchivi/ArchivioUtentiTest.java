package classibiblio.tipoarchivi;

import classibiblioteca.entita.Utente;

import classibiblio.tipologiearchivi.ArchivioUtenti;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/*
 * Test per la classe ArchivioUtenti.
 * Verifica la gestione della lista, la ricerca e l'ordinamento con i nuovi dati.
 */
public class ArchivioUtentiTest {

    private ArchivioUtenti archivio;
    private Utente u1;
    private Utente u2;
    private Utente u3;

    @BeforeEach 
    public void setUp() {
        archivio = new ArchivioUtenti();
        
        // Inizializzo gli utenti con i nuovi dati richiesti
        // Matricole progressive: 000000, 000001, 000002
        u1 = new Utente("Angelo", "Casella", "000000", "angelo.casella@test.com");
        u2 = new Utente("Matteo", "Adinolfi", "000001", "matteo.adinolfi@test.com");
        u3 = new Utente("Luisa", "Dong", "000002", "luisa.dong@test.com");
    }

    /*
     * TEST 1: Inserimento e Controllo Esistenza
     * Metodi testati: 
     * - aggiungiUtente()
     * - existByMatricola()
     * - getLista()
     */
    @Test
    public void testAggiuntaEVerifica() {
        System.out.println("Test 1: Inserimento e Controllo Esistenza");

        // Verifico che all'inizio non esista la matricola 000000
        assertFalse(archivio.existByMatricola("000000"), "La matricola non dovrebbe esistere all'inizio");
        
        // Aggiungo Angelo Casella
        archivio.aggiungiUtente(u1);
        
        // Verifico che ora esista
        assertTrue(archivio.existByMatricola("000000"), "La matricola dovrebbe esistere dopo l'aggiunta");
        
        // Verifico che la lista contenga effettivamente 1 elemento
        assertEquals(1, archivio.getLista().size(), "La lista deve contenere 1 elemento");
    }

    /*
     * TEST 2: Unicità della Matricola
     * Metodi testati:
     * - aggiungiUtente() (ramo else)
     */
    @Test
    public void testMatricolaDuplicata() {
        System.out.println("Test 2: Unicità della Matricola");

        archivio.aggiungiUtente(u1); // Inserisco Angelo (000000)
        
        // Creo un altro utente con STESSA matricola "000000" ma nome diverso
        Utente clone = new Utente("Utente", "Fake", "000000", "fake@test.com");
        
        archivio.aggiungiUtente(clone); // Provo ad aggiungerlo
        
        // La lista deve rimanere di 1 elemento
        assertEquals(1, archivio.getLista().size(), "Non deve essere possibile aggiungere duplicati");
        
        // Controllo che l'utente presente sia ancora l'originale (Angelo)
        Utente salvato = archivio.getUtenteByMatricola("000000");
        assertEquals("Angelo", salvato.getNome(), "Il nome deve rimanere quello originale");
    }

    /*
     * TEST 3: Recupero e Eliminazione
     * Metodi testati:
     * - getUtenteByMatricola()
     * - eliminaUtente()
     */
    @Test
    public void testRecuperoEdEliminazione() {
        System.out.println("Test 3: Recupero e Eliminazione");

        archivio.aggiungiUtente(u1);
        
        // Test Recupero (Caso positivo)
        Utente trovato = archivio.getUtenteByMatricola("000000");
        assertNotNull(trovato, "L'utente deve essere trovato");
        assertEquals("Casella", trovato.getCognome());
        
        // Test Recupero (Caso negativo - matricola inesistente)
        Utente nonTrovato = archivio.getUtenteByMatricola("999999");
        assertNull(nonTrovato, "Se la matricola non c'è, deve restituire null");
        
        // Test Eliminazione
        boolean esitoEliminazione = archivio.eliminaUtente(u1);
        assertTrue(esitoEliminazione, "L'eliminazione deve restituire true");
        assertEquals(0, archivio.getLista().size(), "La lista deve essere vuota dopo eliminazione");
    }

    /*
     * TEST 4: Aggiornamento Dati
     * Metodi testati:
     * - aggiornaUtente()
     */
    @Test
    public void testAggiornamentoUtente() {
        System.out.println("Test 4: Aggiornamento Dati Utente");

        archivio.aggiungiUtente(u1); // Angelo Casella, 000000
        
        // Provo ad aggiornare una matricola che NON esiste
        boolean esitoFallito = archivio.aggiornaUtente("999999", "X", "Y", "Z");
        assertFalse(esitoFallito, "Aggiornamento su matricola inesistente deve fallire");
        
        // Aggiorno l'utente esistente (Matricola 000000)
        boolean esitoOk = archivio.aggiornaUtente("000000", "AngeloJunior", "CasellaUpdated", "nuova@email.it");
        assertTrue(esitoOk, "Aggiornamento deve riuscire");
        
        // Verifico che i campi siano cambiati davvero
        Utente uAggiornato = archivio.getUtenteByMatricola("000000");
        assertEquals("AngeloJunior", uAggiornato.getNome());
        assertEquals("CasellaUpdated", uAggiornato.getCognome());
        assertEquals("nuova@email.it", uAggiornato.getEmail());
    }

    /*
     * TEST 5: Ricerca (Caso d'uso UC-8)
     * Metodi testati:
     * - ricercaUtente()
     */
    @Test
    public void testRicerca() {
        System.out.println("Test 5: Ricerca (Caso d'uso UC-8)");

        archivio.aggiungiUtente(u1); // Casella
        archivio.aggiungiUtente(u2); // Adinolfi
        archivio.aggiungiUtente(u3); // Dong
        
        // 1. Ricerca per Cognome esatto (Case Insensitive)
        // Cerco "casella" -> Angelo
        List<Utente> risultati = archivio.ricercaUtente("casella");
        assertEquals(1, risultati.size(), "Deve trovare 1 utente");
        assertEquals("Angelo", risultati.get(0).getNome());
        
        // 2. Ricerca per Matricola esatta
        // Cerco "000001" -> Matteo
        risultati = archivio.ricercaUtente("000001");
        assertEquals(1, risultati.size(), "Deve trovare matricola 000001");
        assertEquals("Matteo", risultati.get(0).getNome());
        
        // 3. Ricerca che non produce risultati
        risultati = archivio.ricercaUtente("Viola");
        assertEquals(0, risultati.size(), "Non deve trovare nessuno");
        
        // 4. Ricerca parziale (NON deve trovare nulla con equals)
        risultati = archivio.ricercaUtente("Cas");
        assertEquals(0, risultati.size(), "Ricerca parziale non supportata");
    }

    /*
     * TEST 6: Ordinamento (Requisito UI-1.3)
     * Metodi testati:
     * - getUtentiOrdinati()
     */
    @Test
    public void testOrdinamento() {
        System.out.println("Test 6: Ordinamento (Requisito UI-1.3)");

        // Inserisco in ordine sparso
        archivio.aggiungiUtente(u3); // Dong (D)
        archivio.aggiungiUtente(u1); // Casella (C)
        archivio.aggiungiUtente(u2); // Adinolfi(A)
        
        // Aggiungo un altro Casella per testare l'ordinamento secondario per Nome
        // Giovanni Casella (C)
        Utente u4 = new Utente("Giovanni", "Casella", "000003", "antonio@test.com");
        archivio.aggiungiUtente(u4);
        
        // Lista attesa ordinata: 
        // 1. Adinolfi Matteo (Cognome A)
        // 2. Casella Angelo   (Cognome C, Nome A)
        // 3. Casella Giovanni (Cognome C, Nome G)
        // 4. Dong Luisa       (Cognome D)
        
        List<Utente> ordinata = archivio.getUtentiOrdinati();
        
        assertEquals("Adinolfi", ordinata.get(0).getCognome()); 
        assertEquals("Angelo", ordinata.get(1).getNome());
        assertEquals("Giovanni", ordinata.get(2).getNome());
        assertEquals("Dong", ordinata.get(3).getCognome());
        
        // Verifico che la lista originale NON sia stata modificata
        // L'ordine di inserimento era: Dong, Casella, Adinolfi, Casella
        assertEquals("Dong", archivio.getLista().get(0).getCognome());
    }
}
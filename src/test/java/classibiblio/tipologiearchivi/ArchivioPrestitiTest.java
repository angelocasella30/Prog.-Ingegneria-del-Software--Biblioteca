package classibiblio.tipologiearchivi;

import classibiblioteca.entita.Prestito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

/*
 * Test per la classe ArchivioPrestiti.
 */
public class ArchivioPrestitiTest {

    private ArchivioPrestiti archivio;
    private Prestito pNormale;
    private Prestito pScaduto;
    private Prestito pLungoTermine;

    @BeforeEach 
    public void setUp() {
        archivio = new ArchivioPrestiti();
        
        LocalDate oggi = LocalDate.now();

        // 1. Prestito NORMALE (Iniziato oggi, scade tra 10 giorni)
        pNormale = new Prestito("user@test.com", "MAT-001", "Java Programming", "ISBN-111", 
                                oggi, 
                                oggi.plusDays(10)); 

        // 2. Prestito SCADUTO (Iniziato 20 giorni fa, scaduto ieri)
        // Questo serve per testare il metodo isInRitardo()
        pScaduto = new Prestito("user2@test.com", "MAT-002", "Storia Romana", "ISBN-222", 
                                oggi.minusDays(20), 
                                oggi.minusDays(1)); 

        // 3. Prestito LUNGO TERMINE (Iniziato oggi, scade tra 30 giorni)
        pLungoTermine = new Prestito("user3@test.com", "MAT-003", "Harry Potter", "ISBN-333", 
                                     oggi, 
                                     oggi.plusDays(30));
    }

    /*
     * TEST 1: Inserimento e Controllo Duplicati
     * Metodi testati: 
     * - aggiungiPrestito()
     * - getLista()
     */
    @Test
    public void testAggiunta() {
        System.out.println("Test 1: Inserimento e Controllo Duplicati");

        // Aggiungo un prestito valido
        archivio.aggiungiPrestito(pNormale);
        assertEquals(1, archivio.getLista().size(), "La lista deve contenere 1 prestito");

        // Provo ad aggiungere lo STESSO prestito (stessa matricola, isbn e data inizio)
        // Nota: Il metodo equals in Prestito controlla questi campi
        archivio.aggiungiPrestito(pNormale);
        
        assertEquals(1, archivio.getLista().size(), "Non deve aggiungere duplicati dello stesso prestito");
    }

    /*
     * TEST 2: Restituzione Libro (Chiusura Prestito)
     * Metodi testati:
     * - restituzioneLibro()
     * - Prestito.chiudiPrestito()
     */
    @Test
    public void testRestituzione() {
        System.out.println("Test 2: Restituzione Libro (Chiusura)");

        archivio.aggiungiPrestito(pNormale);

        // Verifico che all'inizio sia "In Corso" (Data restituzione null)
        assertNull(pNormale.getDataRestituzioneEffettiva());
        assertEquals("In Corso", pNormale.getStato());

        // Eseguo la restituzione
        boolean esito = archivio.restituzioneLibro("MAT-001", "ISBN-111");
        
        assertTrue(esito, "La restituzione deve andare a buon fine");
        
        // Verifico che NON sia stato eliminato dalla lista (Storico), ma solo chiuso
        assertEquals(1, archivio.getLista().size(), "Il prestito deve rimanere nello storico");
        assertNotNull(pNormale.getDataRestituzioneEffettiva(), "La data restituzione non deve essere null");
        assertEquals("Concluso", pNormale.getStato());
    }

    /*
     * TEST 3: Rilevamento Prestiti Scaduti (Ritardi)
     * Metodi testati:
     * - getPrestitiScaduti()
     * - Prestito.isInRitardo()
     */
    @Test
    public void testPrestitiScaduti() {
        System.out.println("Test 3: Rilevamento Prestiti Scaduti");

        archivio.aggiungiPrestito(pNormale); // Scade tra 10 giorni (OK)
        archivio.aggiungiPrestito(pScaduto); // Scaduto ieri (RITARDO)

        List<Prestito> listaScaduti = archivio.getPrestitiScaduti();

        // Deve trovare solo quello scaduto
        assertEquals(1, listaScaduti.size(), "Deve esserci solo 1 prestito scaduto");
        assertEquals("MAT-002", listaScaduti.get(0).getMatricola());
        assertEquals("IN RITARDO", listaScaduti.get(0).getStato());
    }

    /*
     * TEST 4: Ordinamento per Scadenza
     * Metodi testati:
     * - getPrestitiOrdinatiPerScadenza()
     */
    @Test
    public void testOrdinamentoScadenza() {
        System.out.println("Test 4: Ordinamento per Data Scadenza");

        // Inserisco in ordine sparso
        archivio.aggiungiPrestito(pLungoTermine); // Scade tra 30 gg
        archivio.aggiungiPrestito(pScaduto);      // Scaduto ieri (-1 gg)
        archivio.aggiungiPrestito(pNormale);      // Scade tra 10 gg

        List<Prestito> ordinati = archivio.getPrestitiOrdinatiPerScadenza();

        // Ordine atteso (dal più vecchio al più futuro):
        // 1. pScaduto (Ieri)
        // 2. pNormale (Tra 10gg)
        // 3. pLungoTermine (Tra 30gg)

        assertEquals("MAT-002", ordinati.get(0).getMatricola()); // pScaduto
        assertEquals("MAT-001", ordinati.get(1).getMatricola()); // pNormale
        assertEquals("MAT-003", ordinati.get(2).getMatricola()); // pLungoTermine
    }
}
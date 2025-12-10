package classibiblio.entita;

import classibiblioteca.entita.Prestito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Test per la classe Prestito.
 */
public class PrestitoTest {

    private Prestito prestito;
    private LocalDate oggi;
    private LocalDate scadenzaStandard;

    // Eseguito prima di ogni test
    @BeforeEach
    public void setUp() {
        oggi = LocalDate.now();
        scadenzaStandard = oggi.plusDays(30);
        
        // Inizializzo un prestito standard: Angelo Casella (Studente)
        prestito = new Prestito(
            "angelo.casella@studenti.uni.it", // Dominio corretto
            "000000", 
            "Il Signore degli Anelli", 
            "ISBN-123", 
            oggi, 
            scadenzaStandard
        );
    }

    /*
     * TEST 1: Creazione e Stato Iniziale (Getters)
     * Metodi testati:
     * - Costruttore
     * - getEmailuser(), getMatricola(), getTitololibro(), getISBN()
     * - getDatainizio(), getDataScadenzaPrevista()
     * - getDataRestituzioneEffettiva() (che deve essere null)
     * - getStato() (In Corso)
     */
    @Test
    public void testStatoIniziale() {
        System.out.println("Test 1: Creazione e Stato Iniziale");
        
        // Verifica dati anagrafici
        assertEquals("angelo.casella@studenti.uni.it", prestito.getEmailuser());
        assertEquals("000000", prestito.getMatricola());
        assertEquals("Il Signore degli Anelli", prestito.getTitololibro());
        assertEquals("ISBN-123", prestito.getISBN());
        
        // Verifica date
        assertEquals(oggi, prestito.getDatainizio());
        assertEquals(scadenzaStandard, prestito.getDataScadenzaPrevista());
        
        // Verifica stato iniziale (Prestito appena creato)
        assertNull(prestito.getDataRestituzioneEffettiva(), "Appena creato, la data restituzione deve essere null");
        assertFalse(prestito.isInRitardo(), "Un prestito appena creato non può essere in ritardo");
        assertEquals("In Corso", prestito.getStato(), "Lo stato iniziale deve essere 'In Corso'");
    }

    /*
     * TEST 2: Modifica Dati (Setters)
     * Metodi testati:
     * - setEmailuser(), setMatricola()
     * - setTitololibro(), setISBN()
     * - setDatainizio(), setDataScadenzaPrevista()
     */
    @Test
    public void testSetters() {
        System.out.println("Test 2: Modifica Dati (Setters)");
        
        // Modifico i campi simulando un passaggio a un Docente
        prestito.setEmailuser("matteo.adinolfi@docente.uni.it"); // Testo il dominio docente
        prestito.setTitololibro("Harry Potter");
        
        LocalDate nuovaData = oggi.plusDays(1);
        LocalDate nuovaScadenza = scadenzaStandard.plusDays(10);
        
        prestito.setDatainizio(nuovaData);
        prestito.setDataScadenzaPrevista(nuovaScadenza);
        
        // Verifico i cambiamenti - metto anche le get della matricola e dell'isbn per testarli
        assertEquals("matteo.adinolfi@docente.uni.it", prestito.getEmailuser());
        assertEquals("000000", prestito.getMatricola());
        assertEquals("Harry Potter", prestito.getTitololibro());
        assertEquals("ISBN-123", prestito.getISBN());
        assertEquals(nuovaData, prestito.getDatainizio());
        assertEquals(nuovaScadenza, prestito.getDataScadenzaPrevista());
    }

    /*
     * TEST 3: Logica Business - Chiusura Prestito
     * Metodi testati:
     * - chiudiPrestito()
     * - getDataRestituzioneEffettiva()
     * - getStato() (Concluso)
     */
    @Test
    public void testChiusuraPrestito() {
        System.out.println("Test 3: Chiusura Prestito (Lifecycle)");
        
        // Simulo la restituzione del libro
        prestito.chiudiPrestito();
        
        // Verifiche
        assertNotNull(prestito.getDataRestituzioneEffettiva(), "Dopo la chiusura, la data non deve essere null");
        assertEquals(LocalDate.now(), prestito.getDataRestituzioneEffettiva(), "La data di restituzione deve essere oggi");
        
        assertEquals("Concluso", prestito.getStato(), "Lo stato deve diventare 'Concluso'");
        assertFalse(prestito.isInRitardo(), "Un prestito concluso non è in ritardo");
    }

    /*
     * TEST 4: Logica Business - Gestione Ritardi
     * Metodi testati:
     * - isInRitardo()
     * - getStato() (IN RITARDO)
     */
    @Test
    public void testGestioneRitardo() {
        System.out.println("Test 4: Gestione Ritardi");
        
        // Creo una situazione di ritardo: prestito scaduto IERI
        LocalDate inizioPassato = LocalDate.now().minusDays(40);
        LocalDate scadenzaPassata = LocalDate.now().minusDays(1); // Scaduto ieri
        
        Prestito prestitoScaduto = new Prestito(
            "ritardatario@studenti.uni.it", // Email valida
            "000999", "Manuale Java", "ISBN-0001", 
            inizioPassato, scadenzaPassata
        );
        
        // 1. Verifica Ritardo Attivo
        assertTrue(prestitoScaduto.isInRitardo(), "Deve essere in ritardo se oggi > scadenza e non restituito");
        assertEquals("IN RITARDO", prestitoScaduto.getStato());
        
        // 2. Verifica dopo restituzione in ritardo
        prestitoScaduto.chiudiPrestito();
        
        assertFalse(prestitoScaduto.isInRitardo(), "Una volta restituito, il flag ritardo si spegne");
        assertEquals("Concluso", prestitoScaduto.getStato(), "Stato finale Concluso");
    }

    /*
     * TEST 5: Identità (Equals e HashCode)
     * Metodi testati:
     * - equals()
     * - hashCode()
     */
    @Test
    public void testEqualsHashCode() {
        System.out.println("Test 5: Identità (Equals/HashCode)");
        
        // La chiave di unicità è: Matricola + ISBN + DataInizio
        
        // p1 è identico a 'prestito' (creato nel setUp)
        Prestito p1 = new Prestito(
            "altro.studente@studenti.uni.it", // Email diversa (non conta per equals)
            "000000",                         // Stessa Matricola
            "Altro Titolo",                   // Titolo diverso (non conta)
            "ISBN-123",                       // Stesso ISBN
            oggi,                             // Stessa Data Inizio
            scadenzaStandard
        );
        
        // p2 ha data diversa
        Prestito p2 = new Prestito(
            "angelo.casella@studenti.uni.it", 
            "000000", "T", "ISBN-123", 
            LocalDate.now().minusDays(1), 
            scadenzaStandard
        );
        
        // Test Equals
        assertEquals(prestito, p1, "Devono essere uguali se Matricola, ISBN e DataInizio coincidono");
        assertNotEquals(prestito, p2, "Devono essere diversi se cambia la data inizio");
        
        // Test HashCode
        assertEquals(prestito.hashCode(), p1.hashCode(), "HashCode deve coincidere se equals è true");
    }
    

    /*
     * TEST 6: Rappresentazione Stringa
     * Metodi testati:
     * - toString()
     */
    @Test
    public void testToString() {
        System.out.println("Test 6: ToString");
        
        String descrizione = prestito.toString();
        
        assertNotNull(descrizione);
        // Verifico che contenga le info chiave
        assertTrue(descrizione.contains("000000"), "Deve contenere la matricola");
        assertTrue(descrizione.contains("ISBN-123"), "Deve contenere l'ISBN");
        assertTrue(descrizione.contains("In Corso"), "Deve contenere lo stato");
    }
}
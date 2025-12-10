package classibilbio.entita;

import classibiblio.entita.Prestito;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test per la classe Prestito.
 * Verifica la gestione delle date, degli stati e dei ritardi.
 */
public class PrestitoTest 
{

    public PrestitoTest() 
    {
    }

    /**
     * Test Creazione: Verifica che un prestito nasca pulito e "In Corso".
     */
    @Test
    public void testCreazioneEStatoIniziale() 
    {
        System.out.println("Test Creazione Prestito");
        
        LocalDate oggi = LocalDate.now();
        LocalDate scadenza = oggi.plusDays(30);
        
        // Creo un prestito valido
        Prestito p = new Prestito("user@studenti.uni.it", "000001", "IT", "ISBN-111", oggi, scadenza);
        
        // Verifiche
        assertEquals("In Corso", p.getStato(), "Lo stato iniziale deve essere In Corso");
        assertNull(p.getDataRestituzioneEffettiva(), "La data di restituzione deve essere null all'inizio");
        assertFalse(p.isInRitardo(), "Un prestito appena creato non può essere in ritardo");
        assertEquals("000001", p.getMatricola());
        assertEquals("ISBN-111", p.getISBN());
    }

    /**
     * Test Chiusura: Verifica che la restituzione funzioni.
     */
    @Test
    public void testChiusuraPrestito() 
    {
        System.out.println("Test Chiusura Prestito");
        
        Prestito p = new Prestito("u", "m", "t", "i", LocalDate.now(), LocalDate.now().plusDays(30));
        
        // Simulo l'azione di restituzione
        p.chiudiPrestito();
        
        // Verifiche
        assertNotNull(p.getDataRestituzioneEffettiva(), "Dopo la chiusura, la data restituzione non può essere null");
        assertEquals(LocalDate.now(), p.getDataRestituzioneEffettiva(), "La data di restituzione deve essere oggi");
        assertEquals("Concluso", p.getStato(), "Lo stato deve diventare Concluso");
        assertFalse(p.isInRitardo(), "Un prestito concluso non è mai in ritardo (anche se consegnato dopo)");
    }

    /**
     * Test Ritardo: Verifica che il sistema rilevi i ritardi.
     */
    @Test
    public void testRilevamentoRitardo() 
    {
        System.out.println("Test Rilevamento Ritardo");
        
        // Esempio: prestito scaduto IERI
        LocalDate inizio = LocalDate.now().minusDays(31); // Iniziato un mese fa
        LocalDate scadenza = LocalDate.now().minusDays(1); // Scaduto ieri
        
        Prestito pScaduto = new Prestito("u", "m", "t", "i", inizio, scadenza);
        
        // Deve essere in ritardo
        assertTrue(pScaduto.isInRitardo(), "Deve segnalare ritardo se oggi > scadenza");
        assertEquals("IN RITARDO", pScaduto.getStato());
        
        // Ma se lo chiudo oggi
        pScaduto.chiudiPrestito();
        
        // non deve più essere considerato "in ritardo" come stato attivo (è Concluso)
        assertEquals("Concluso", pScaduto.getStato());
    }

    /**
     * Test Equals: Per ritrovare il prestito nelle lista.
     */
    @Test
    public void testEquals() 
    {
        System.out.println("Test Equals (Chiave composta)");
        
        LocalDate data = LocalDate.of(2023, 1, 1);
        
        Prestito p1 = new Prestito("u", "1", "T", "ISBN1", data, data.plusDays(30));
        Prestito p2 = new Prestito("u", "1", "T", "ISBN1", data, data.plusDays(30)); // Gemello
        Prestito p3 = new Prestito("u", "2", "T", "ISBN1", data, data.plusDays(30)); // Diversa Matricola
        
        assertEquals(p1, p2, "Stessi dati chiave = Stesso prestito");
        assertNotEquals(p1, p3, "Dati diversi = Prestito diverso");
    }
}
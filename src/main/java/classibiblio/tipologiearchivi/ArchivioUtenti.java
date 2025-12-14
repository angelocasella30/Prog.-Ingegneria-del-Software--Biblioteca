package classibiblio.tipologiearchivi;

import classibiblioteca.entita.Utente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe ArchivioUtenti.
 * <p>
 * Gestisce l'archivio degli utenti della biblioteca,
 * consentendo operazioni di aggiunta, elimina,
 * modifica, ricerca e visualizzazione ordinata degli utenti
 * </p>
 *
 * La classe implementa {@link Serializable} per consentire
 * la persistenza dei dati.
 */


public class ArchivioUtenti implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private final List<Utente> listutenti;
    
    public ArchivioUtenti() {
        this.listutenti = new ArrayList<>();
    }
    
    // --- Gestione Lista ---
/**
 * Aggiunge un utente all'archivio.
 * <p>
 * L'utente viene inserito solo se la matricola
 * non risulta già presente nell'archivio.
 * </p>
 *
 * @param x utente da aggiungere
 */
    
    public void aggiungiUtente(Utente x) {
        // Aggiunta di un utente, controllo matricola univoca
        if(!existByMatricola(x.getMatricola()))
            listutenti.add(x);
        else
        {
            System.out.println("Erorre: Utente con matricola"+x.getMatricola()+"già all'interno della lista");
        }
    }
/**
 * Elimina un utente dall'archivio.
 *
 * @param x utente da eliminare
 * @return true se l'utente è stato rimosso, false altrimenti
 */    

    public boolean eliminaUtente(Utente x) {
        // Elimina un utente all'interno della lista
        return listutenti.remove(x);
    }
/**
 * Verifica se esiste un utente con la matricola specificata.
 *
 * @param matricola matricola da verificare
 * @return true se l'utente esiste, false altrimenti
 */   

    public boolean existByMatricola(String matricola)
    {
    // confronto se esiste  in archivio utente
        for(Utente u:listutenti)
            if(u.getMatricola().equals(matricola))
                return true;
        return false;
    }
/**
 * Restituisce un utente cercandolo tramite matricola.
 *
 * @param matricola matricola dell'utente
 * @return l'utente trovato oppure null se non esiste
 */    

    public Utente getUtenteByMatricola(String matricola) {
        // Cerco l'utente tramite matricola
        for(Utente u:listutenti)
            if(u.getMatricola().equals(matricola))
                return u;
        System.out.println("Nessun utente trovato con la matricola data");
        return null; 
    }
/**
 * Aggiorna i dati di un utente esistente
 *
 * @param matricola matricola dell'utente da aggiornare
 * @param nome nome aggiornato
 * @param cognome cognome aggiornato
 * @param email email aggiornato
 * @return true se l'aggiornamento è avvenuto, false altrimenti
 */
    public boolean aggiornaUtente(String matricola,String nome,String cognome,String email) 
    {
        Utente u = getUtenteByMatricola(matricola);
        if(u!=null)
        {
            u.setNome(nome);
            u.setCognome(cognome);
            u.setEmail(email);
            return true;
            
        }
        return false;
    }
/**
 * Ricerca utenti in base a cognome o matricola
 *
 * @param campocerca valore da cercare
 * @return lista degli utenti che corrispondono ai criteri
 */
    // --- Metodi per la Ricerca (UC-8) ---
    
    public List<Utente> ricercaUtente(String campocerca) 
    {
     // --- Metodo 2: Per il Caso d'Uso 4.8 (Ricerca Grafica) ---
        List<Utente> risultati = new ArrayList<>();
        
        for (Utente u : listutenti) {
            // UC 4.8: Cerca per Cognome O Matricola
            // Usiamo equalsIgnoreCase per il controllo preciso ma senza distinzione maiuscole/minuscole
            boolean matchCognome = u.getCognome().equalsIgnoreCase(campocerca);
            boolean matchMatricola = u.getMatricola().equalsIgnoreCase(campocerca);

            if (matchCognome || matchMatricola) {
                risultati.add(u);
            }
        }
        
        return risultati;
    }
    
    // --- Metodi per l'Interfaccia Grafica ---
/**
 * Restituisce la lista degli utenti ordinata per cognome e poi per nome
 *
 * @return lista ordinata degli utenti
 */
    
    // Requisito UI-1.3
    
    public List<Utente> getUtentiOrdinati() {
        List<Utente> ordinata = new ArrayList<>(listutenti);
  
        ordinata.sort(Comparator.comparing(Utente::getCognome, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Utente::getNome, String.CASE_INSENSITIVE_ORDER));
        return ordinata;
    }
    
/**
 * Restituisce la lista completa degli utenti presenti nell'archivio utenti
 *
 * @return lista degli utenti
 */

    public List<Utente> getLista() {
        return listutenti;
    }
}
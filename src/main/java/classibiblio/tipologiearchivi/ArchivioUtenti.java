package classibiblio.tipologiearchivi;

import classibiblioteca.entita.Utente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
/**
 * Classe ArchivioUtenti 
 * @implements serializable
 * 
 * gestisce aggiunta,elimina,ricerca e modifica in archivio utenti
 */

public class ArchivioUtenti implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private final List<Utente> listutenti;
    
    public ArchivioUtenti() {
        this.listutenti = new ArrayList<>();
    }
    
    // --- Gestione Lista ---
    
    public void aggiungiUtente(Utente x) {
        // Aggiunta di un utente, controllo matricola univoca
        if(!existByMatricola(x.getMatricola()))
            listutenti.add(x);
        else
        {
            System.out.println("Erorre: Utente con matricola"+x.getMatricola()+"già all'interno della lista");
        }
    }
    
    public boolean eliminaUtente(Utente x) {
        // Elimina un utente all'interno della lista
        return listutenti.remove(x);
    }
    
    public boolean existByMatricola(String matricola)
    {
    // confronto se esiste  in archivio utente
        for(Utente u:listutenti)
            if(u.getMatricola().equals(matricola))
                return true;
        return false;
    }
    
    public Utente getUtenteByMatricola(String matricola) {
        // Cerco l'utente tramite matricola
        for(Utente u:listutenti)
            if(u.getMatricola().equals(matricola))
                return u;
        System.out.println("Nessun utente trovato con la matricola data");
        return null; 
    }
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
    
    // Requisito UI-1.3
    
    public List<Utente> getUtentiOrdinatiPerCognome() {
        List<Utente> ordinata = new ArrayList<>(listutenti);
  
        ordinata.sort(Comparator.comparing(Utente::getCognome, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Utente::getNome, String.CASE_INSENSITIVE_ORDER));
        return ordinata;
    }
    public List<Utente> getUtentiOrdinatiPerNome() {
        List<Utente> ordinata = new ArrayList<>(listutenti);
        ordinata.sort(Comparator.comparing(Utente::getNome, String.CASE_INSENSITIVE_ORDER)
                            .thenComparing(Utente::getCognome, String.CASE_INSENSITIVE_ORDER));
    return ordinata;
    }


    
    public List<Utente> getLista() {
        return listutenti;
    }
}
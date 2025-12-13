package classibiblio.tipologiearchivi;

import classibiblioteca.entita.Prestito;
import classibiblioteca.entita.Utente;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe ArchivioPrestiti
 * @implements serializable
 * * gestisce aggiunta prestito, restituzione e ricerche in archivio
 */
public class ArchivioPrestiti implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Prestito> listprestiti;

    public ArchivioPrestiti() {
        this.listprestiti = new ArrayList<>();
    }

    // --- Gestione Lista ---

    public void aggiungiPrestito(Prestito p) {
        // 1. Validazione base: oggetto nullo
        if (p == null) {
            System.out.println("Errore: Impossibile aggiungere un prestito nullo.");
            return;
        }

        // 2. Validazione campi obbligatori
        if (p.getMatricola() == null || p.getMatricola().isEmpty() ||
            p.getISBN() == null || p.getISBN().isEmpty() ||
            p.getDatainizio() == null || p.getDataScadenzaPrevista() == null) {
            System.out.println("Errore: Dati prestito incompleti (Matricola, ISBN o Date mancanti).");
            return;
        }

        // 3. Controllo duplicati (Opzionale ma consigliato)
        // Evitiamo di inserire lo stesso identico prestito due volte
        if (listprestiti.contains(p)) {
            System.out.println("Attenzione: Questo prestito è già registrato in archivio.");
            return;
        }

        // Tutto ok, aggiungo
        listprestiti.add(p);
    }

    // UC-12: Gestione Restituzione
    public boolean restituzioneLibro(String matricola, String ISBN) {
        if (matricola == null || ISBN == null) return false;

        // Cerco il prestito ATTIVO per questo utente e questo libro
        for (Prestito p : listprestiti) {
            boolean stessoUtente = p.getMatricola().equalsIgnoreCase(matricola);
            boolean stessoLibro = p.getISBN().equalsIgnoreCase(ISBN);
            boolean isAperto = (p.getDataRestituzioneEffettiva() == null);

            if (stessoUtente && stessoLibro && isAperto) {
                // Trovato! Chiudo il prestito (imposta la data di oggi)
                p.chiudiPrestito();
                return true; // Restituzione avvenuta con successo
            }
        }
        
        System.out.println("Errore: Nessun prestito attivo trovato per Matricola " + matricola + " e ISBN " + ISBN);
        return false;
    }

    // --- Metodi per l'Interfaccia Grafica ---

    // Requisito UI-1.4: Visualizzazione ordinata per data scadenza
    public List<Prestito> getPrestitiOrdinatiPerScadenza() {
        // Creo una copia per non modificare l'ordine della lista originale
        List<Prestito> ordinata = new ArrayList<>(listprestiti);
        
        // Ordino usando la data di scadenza prevista
        ordinata.sort(Comparator.comparing(Prestito::getDataScadenzaPrevista));
        
        return ordinata;
    }

    // Metodo utile per il controller: ottieni solo quelli in ritardo
    public List<Prestito> getPrestitiScaduti() {
        List<Prestito> scaduti = new ArrayList<>();
        
        for (Prestito p : listprestiti) {
            if (p.isInRitardo()) {
                scaduti.add(p);
            }
        }
        return scaduti;
    }
    
    
    // Metodo per cercare i prestiti per matricola
    public List<Prestito> ricercaPrestitoPerMatricola(String matricola) {
        List<Prestito> risultati = new ArrayList<>();
    
    for (Prestito p : listprestiti) {
        if (p.getMatricola().contains(matricola)) {  
            risultati.add(p);
        }
    }
    
    return risultati;
}

    // Metodo per cercare i prestiti per ISBN
    public List<Prestito> ricercaPrestitoPerISBN(String isbn) {
        List<Prestito> risultati = new ArrayList<>();
    
    for (Prestito p : listprestiti) {
        if (p.getISBN().contains(isbn)) {
            risultati.add(p);
        }
    }
    
    return risultati;
}

    // Metodo per vedere solo i prestiti ATTIVI (non ancora restituiti)
    public List<Prestito> getPrestitiAttivi() {
        List<Prestito> attivi = new ArrayList<>();
        for (Prestito p : listprestiti) {
            if (p.getDataRestituzioneEffettiva() == null) {
                attivi.add(p);
            }
        }
        return attivi;
    }
    //metodo per ottenere la lista prestitiattivi per utente
    public List<Prestito> getPrestitiAttiviPerUtente(Utente utente) {
        List<Prestito> prestitiAttiviUtente = new ArrayList<>();
        
        for (Prestito p : listprestiti) {
            // Verifica se il prestito è attivo per l'utente specifico (controlla matricola)
            if (p.getMatricola().equalsIgnoreCase(utente.getMatricola()) && p.getDataRestituzioneEffettiva() == null) {
                prestitiAttiviUtente.add(p);
            }
        }
        
        return prestitiAttiviUtente;
    }
    //metodo per ottenere il singolo prestito attivo
    public Prestito getSingoloPrestitoAttivo(String matricola, Prestito precedente) {
    boolean trovatoPrecedente = (precedente == null); 
    for (Prestito p : listprestiti) {
        if (p.getMatricola().equals(matricola) && p.getDataRestituzioneEffettiva() == null) {
            if (trovatoPrecedente) {
                return p;
            }
            if (p.equals(precedente)) {
                trovatoPrecedente = true;
            }
        }
    }
    return null;
}

    public List<Prestito> getLista() {
        return listprestiti;
    }
}
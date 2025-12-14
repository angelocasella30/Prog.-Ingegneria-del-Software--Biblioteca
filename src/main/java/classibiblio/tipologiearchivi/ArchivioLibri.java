package classibiblio.tipologiearchivi;

import classibiblioteca.entita.Libro;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe ArchivioLibri
 * <p>
 * Gestisce gli elementi libri del sistema 
 * Gestisce le operazioni come aggiunta, elimina, ricerca, modifica
 * ed visualizzazione ordinata dei libri
 * </p>
 * La classe implementa {@link Serializable} per garantire la persistenza
 * dei dati durante il funzionamento dell'applicazione
 * 
 */
public class ArchivioLibri implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Libro> listlibro;

    public ArchivioLibri() {
        this.listlibro = new ArrayList<>();
    }

/**
 * Aggiunge un libro all'archivio.
 * <p>
 * L'inserimento avviene solo se:
 * <ul>
 *   <li>i campi obbligatori sono non vuoti</li>
 *   <li>non esiste già un libro con lo stesso ISBN</li>
 * </ul>
 * </p>
 *
 * @param x libro da aggiungere all'archivio
 * @return 
 */

    public boolean aggiungiLibro(Libro x) {
       if (x.getTitolo() == null || x.getTitolo().trim().isEmpty() ||
           x.getISBN() == null || x.getISBN().trim().isEmpty() ||
           x.getAutori() == null || x.getAutori().isEmpty() ||
           x.getDatapubbl() == null) 
       {
           System.out.println("Errore: Impossibile salvare un libro con campi vuoti.");
           return false;
       }

       if (!existByIsbn(x.getISBN())) {
           listlibro.add(x);
           return true;
       } else {
           System.out.println("Errore: Libro con ISBN " + x.getISBN() + " già presente.");
           return false;
       }
   }


/**
 * Elimina un libro dall'archivio tramite ISBN.
 * <p>
 * L'eliminazione è bloccata se il libro ha copie attive in prestito.
 * </p>
 *
 * @param ISBN codice ISBN del libro da eliminare
 * @return true se il libro è stato eliminato, false altrimenti
 */
    public boolean eliminaLibro(String ISBN) 
    {
        Libro daEliminare = getLibroByISBN(ISBN);
        if (daEliminare != null) {
            
            // CONTROLLO AGGIUNTO: Se ci sono copie prestate, blocco l'eliminazione
            if (daEliminare.getCopiePrestate() > 0) {
                System.out.println("Attenzione: Impossibile eliminare il libro (ISBN: " + ISBN + "). Ci sono copie in prestito.");
                return false; 
            }
            
            return listlibro.remove(daEliminare);
        }
        return false;
    }
/**
 * Verifica l'esistenza di un libro tramite ISBN.
 *
 * @param ISBN codice ISBN da verificare se esistente
 * @return true se il libro esiste nell'archivio, false altrimenti
 */

    public boolean existByIsbn(String ISBN) 
    {
        if (ISBN == null) return false;
        for(Libro l : listlibro) {
            if (l.getISBN().equalsIgnoreCase(ISBN))
            {
                return true;
            }
        }
        return false;
    }
/**
 * Aggiorna i dati di un libro esistente.
 *
 * @param ISBN ISBN del libro da aggiornare
 * @param nuovoTitolo titolo modificato 
 * @param nuoviAutori lista aggiornata degli autori
 * @param nuovaData data di pubblicazione aggiornata
 * @param nuoveCopie numero di copie aggiornato
 * @return true se l'aggiornamento è avvenuto con successo, false altrimenti
 */
    public boolean aggiornaLibro(String ISBN, String nuovoTitolo, List<String> nuoviAutori, LocalDate nuovaData, int nuoveCopie) 
    {
        // 1 Controllo che i nuovi dati non siano vuoti
        if (nuovoTitolo == null || nuovoTitolo.trim().isEmpty() ||
            nuoviAutori == null || nuoviAutori.isEmpty() ||
            nuovaData == null) 
        {
            return false; 
        }

        Libro l = getLibroByISBN(ISBN);
        if (l != null) 
        {
            l.setTitolo(nuovoTitolo);
            l.setAutori(nuoviAutori);
            l.setDatapubbl(nuovaData);
            l.setNumeroCopie(nuoveCopie);
            return true;
        }
        return false;
    }

    public Libro getLibroByISBN(String ISBN)
    {
        if (ISBN == null) return null;
        for (Libro l : listlibro) 
        {
            if (l.getISBN().equalsIgnoreCase(ISBN)) 
            {
                return l;
            }
        }
        return null;
    }

    // --- Metodi per la Ricerca (UC-4) ---
/**
 * Ricerca libri nell'archivio tramite parola chiave.
 * <p>
 * La ricerca viene effettuata su:
 * titolo, ISBN e autori.
 * </p>
 *
 * @param keyword parola chiave di ricerca
 * @return lista dei libri che corrispondono ai criteri
 */

    public List<Libro> ricercaLibro(String keyword)
    {
        List<Libro> risultati = new ArrayList<>();
        
        if (keyword == null || keyword.isEmpty()) 
        {
            return risultati;
        }

        String cerca = keyword.toLowerCase();

        for (Libro l : listlibro) 
        {
            boolean matchTitolo = l.getTitolo().toLowerCase().contains(cerca);
            boolean matchISBN = l.getISBN().toLowerCase().contains(cerca);
            
            boolean matchAutore = false;
            if (l.getAutori() != null) 
            {
                for (String autore : l.getAutori()) 
                {
                    if (autore.toLowerCase().contains(cerca)) 
                    {
                        matchAutore = true;
                        break;
                    }
                }
            }

            if (matchTitolo || matchISBN || matchAutore) 
            {
                risultati.add(l);
            }
        }
        return risultati; 
    }

    // --- Metodi per l'Interfaccia Grafica (Ordinamento) ---
/**
 * Restituisce la lista dei libri ordinata per titolo.
 *
 * @return lista dei libri ordinata per titolo
 */

    public List<Libro> getLibriPerTitolo() 
    {
        List<Libro> ordinata = new ArrayList<>(listlibro);
        ordinata.sort(Comparator.comparing(Libro::getTitolo, String.CASE_INSENSITIVE_ORDER));
        return ordinata; 
    }
/**
 * Restituisce la lista dei libri ordinata per autore.
 *
 * @return lista dei libri ordinata per autore
 */
    public List<Libro> getLibriPerAutore() 
    {
      List<Libro> ordinata = new ArrayList<>(listlibro);
        ordinata.sort(Comparator.comparing(Libro::getPrimoAutore, String.CASE_INSENSITIVE_ORDER));
        return ordinata;
    }
  
/*
 * Restituisce la lista completa dei libri presenti nell'archivio.
 *
 * @return lista dei libri
 */
    public List<Libro> getLista() 
    {
        return listlibro;
    }
}
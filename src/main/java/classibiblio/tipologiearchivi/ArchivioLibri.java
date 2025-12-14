package classibiblio.tipologiearchivi;

import classibiblioteca.entita.Libro;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Classe ArchivioLibri
 * @implements serializable
 * * gestisce aggiunta, elimina, crea e modifica libro in archivio
 */
public class ArchivioLibri implements Serializable {

    private static final long serialVersionUID = 1L;
    private final List<Libro> listlibro;

    public ArchivioLibri() {
        this.listlibro = new ArrayList<>();
    }

    // --- Gestione Lista ---

    public void aggiungiLibro(Libro x)
    {
        // 1. Controllo campi vuoti
        if (x.getTitolo() == null || x.getTitolo().trim().isEmpty() ||
            x.getISBN() == null || x.getISBN().trim().isEmpty() ||
            x.getAutori() == null || x.getAutori().isEmpty() ||
            x.getDatapubbl() == null) 
        {
            System.out.println("Errore: Impossibile salvare un libro con campi vuoti.");
            return; 
        }

        // 2. Controllo duplicati ISBN
        if (!existByIsbn(x.getISBN())) 
        {
            listlibro.add(x);
        } else {
            System.out.println("Errore: Libro con ISBN " + x.getISBN() + " già presente.");
        }
    }

    // --- MODIFICA QUI ---
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
    // --------------------

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

    public List<Libro> getLibriPerTitolo() 
    {
        List<Libro> ordinata = new ArrayList<>(listlibro);
        ordinata.sort(Comparator.comparing(Libro::getTitolo, String.CASE_INSENSITIVE_ORDER));
        return ordinata; 
    }

    public List<Libro> getLibriPerAutore() 
    {
      List<Libro> ordinata = new ArrayList<>(listlibro);
        ordinata.sort(Comparator.comparing(Libro::getPrimoAutore, String.CASE_INSENSITIVE_ORDER));
        return ordinata;
    }
    /*
    public List<Libro> getLibriDisponibili() {
        List<Libro> libriDisponibiliList = new ArrayList<>();

        // Aggiungi i libri che hanno almeno una copia disponibile
        for (Libro libro : listlibro) {
            if (libro.getNumeroCopie() > 0) {
                libriDisponibiliList.add(libro);
            }
        }

        return libriDisponibiliList;
    }
    public void aggiornaLibro(Libro libro) {
        for (int i = 0; i < listlibro.size(); i++) {
            if (listlibro.get(i).getTitolo().equalsIgnoreCase(libro.getTitolo())) {
                listlibro.set(i, libro);  // Sostituisce il libro aggiornato nell'archivio
                System.out.println("Libro aggiornato: " + libro.getTitolo());
                return;
            }
        }
    }
    public class ArchivioLibri {
    private List<Libro> libriDisponibili;
}
*/
    public List<Libro> getLista() 
    {
        return listlibro;
    }
}
package classibiblioteca.entita;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe Prestito.
 * <p>
 * Rappresenta un prestito di libro a un utente esistente della biblioteca.
 * Registra le informazioni relative all'utente, al libro,
 * alla data di inizio, alla data di scadenza prevista e
 * all'eventuale data di restituzione.
 * </p>
 *
 * La classe implementa {@link Serializable} per consentire
 * la persistenza dei dati.
 */

public class Prestito implements Serializable {
    //private Utente utente;  
    private static final long serialVersionUID = 1L;
    private String emailuser;
    private String matricola;
    private String titololibro;
    private String ISBN;
    
    private LocalDate dataInizio;
    private LocalDate dataScadenzaPrevista;      
    private LocalDate dataRestituzioneEffettiva; 

    public Prestito(String emailuser, String matricola, String titololibro, String ISBN, LocalDate datainizio, LocalDate dataScadenzaPrevista) {
        this.emailuser = emailuser;
        this.matricola = matricola;
        this.titololibro = titololibro;
        this.ISBN = ISBN;
        this.dataInizio = datainizio;
        this.dataScadenzaPrevista = dataScadenzaPrevista;
        this.dataRestituzioneEffettiva = null; 
    }

    // --- Getter e Setter ---

    public String getEmailuser() {
        return emailuser;
    }

    public void setEmailuser(String emailuser) {
        this.emailuser = emailuser;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getTitololibro() {
        return titololibro;
    }

    public void setTitololibro(String titololibro) {
        this.titololibro = titololibro;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public LocalDate getDatainizio() {
        return dataInizio;
    }

    public void setDatainizio(LocalDate datainizio) {
        this.dataInizio = datainizio;
    }

    public LocalDate getDataScadenzaPrevista() {
        return dataScadenzaPrevista;
    }

    public void setDataScadenzaPrevista(LocalDate dataScadenzaPrevista) {
        this.dataScadenzaPrevista = dataScadenzaPrevista;
    }

    public LocalDate getDataRestituzioneEffettiva() {
        return dataRestituzioneEffettiva;
    }
    public void setDataRestituzioneEffettiva(LocalDate dataRestituzione) {
    this.dataRestituzioneEffettiva = dataRestituzione;
    }

    /*public Utente getUtente() {
        return utente;
    }*/
/**
 * Restituisce lo stato del prestito.
 * <p>
 * Lo stato può essere:
 * <ul>
 *   <li>"In Corso"</li>
 *   <li>"IN RITARDO"</li>
 *   <li>"Concluso"</li>
 * </ul>
 * </p>
 *
 * @return stato del prestito
 */

    // --- Metodo per la UI (Stato) ---
    // Questo serve alla TableView per la colonna "Stato"
    public String getStato() {
        if (dataRestituzioneEffettiva != null) {
            return "Concluso";
        }
        if (isInRitardo()) {
            return "IN RITARDO";
        }
        return "In Corso";
    }

    // --- Metodi di Logica  ---
    /*
    Verifica se il prestito è in ritardo oppure no. Il primo controllo
    viene fatto per evitare il caso in cui il prestito sia già stato concluso
    */
/**
 * Verifica se il prestito è in ritardo rispetto alla scadenza prevista.
 *
 * @return true se il prestito è in ritardo, false altrimenti
 */

    public boolean isInRitardo() 
    {
        if (dataRestituzioneEffettiva != null) 
        {
            return false; // Se l'ha restituito, non è in ritardo
        }
        return LocalDate.now().isAfter(dataScadenzaPrevista);    }
    
      /*
      Chiude il prestito impostando la data di restituzione a oggi.
     */
/**
 * Chiude il prestito impostando la data
 * di restituzione effettiva alla data odierna.
 */

    public void chiudiPrestito() 
    {
        this.dataRestituzioneEffettiva = LocalDate.now();
    }    

    // --- Metodi per la ricerca ---

       /*
    Un prestito è uguale se il suo campo matricola isbn e data di inizio sono uguali contemporaneamente
    */
/**
 * Confronta due prestiti
 * <p>
 * Due prestiti sono considerati uguali se appartengono allo stesso utente tramite
 * matricola e lo stesso libro tramite ISBN  e hanno la stessa data di inizio.
 * </p>
 *
 * @param o oggetto da confrontare
 * @return true se i prestiti sono uguali
 */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestito prestito = (Prestito) o;
        // Un prestito è unico per la combinazione: Utente + Libro + Data Inizio
        // (Perché lo stesso utente può prendere lo stesso libro in date diverse)
        return this.matricola.equals(prestito.matricola) &&
               this.ISBN.equals(prestito.ISBN) &&
               this.dataInizio.equals(prestito.dataInizio);
    }
/**
 * Calcola l'hash code del prestito sulla base
 * di matricola, ISBN e data di inizio.
 *
 * @return valore hash del prestito
 */
    @Override
    public int hashCode() {
        return Objects.hash(matricola, ISBN, dataInizio);
    }
/**
 * Restituisce un'output testuale del prestito.
 *
 * @return stringa descrittiva del prestito
 */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
 
        sb.append("Prestito {");
        sb.append("Matricola='").append(matricola).append('\'');
        sb.append(", ISBN='").append(ISBN).append('\'');
        sb.append(", DataInizio=").append(dataInizio);
        sb.append(", Scadenza=").append(dataScadenzaPrevista);
        sb.append(", Stato=").append(getStato());
        sb.append('}');
         //Modificarla agigungendo gli spazi a capo credo
        return sb.toString();
    }
}
package Classibiblioteca.Entità;

import java.io.Serializable;
import java.time.LocalDate;

public class Prestito implements Serializable {


    private String emailuser;
    private String matricola;
    private String titololibro;
    private String ISBN;
    
    private LocalDate datainizio;
    private LocalDate dataScadenzaPrevista;      
    private LocalDate dataRestituzioneEffettiva; 

    public Prestito(String emailuser, String matricola, String titololibro, String ISBN, LocalDate datainizio, LocalDate dataScadenzaPrevista) {
        this.emailuser = emailuser;
        this.matricola = matricola;
        this.titololibro = titololibro;
        this.ISBN = ISBN;
        this.datainizio = datainizio;
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
        return datainizio;
    }

    public void setDatainizio(LocalDate datainizio) {
        this.datainizio = datainizio;
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

    public void setDataRestituzioneEffettiva(LocalDate dataRestituzioneEffettiva) {
        this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
    }

    // --- Metodo per la UI (Stato) ---
    // Questo serve alla TableView per la colonna "Stato"
    public String getStato() {
        if (dataRestituzioneEffettiva != null) {
            return "Concluso";
        }
        if (LocalDate.now().isAfter(dataScadenzaPrevista)) {
            return "IN RITARDO";
        }
        return "In Corso";
    }

    // --- Metodi di Logica (Prototipi) ---

    public boolean isInRitardo() {
        return false; // Da implementare meglio se serve logica complessa
    }

    public void chiudiPrestito() {
        // Da implementare
    }

    @Override
    public String toString() {
        return "Prestito{" + "matricola=" + matricola + ", ISBN=" + ISBN + ", scadenza=" + dataScadenzaPrevista + '}';
    }
}
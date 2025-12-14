/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classibiblioteca.entita;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe Utente.
 * <p>
 * Rappresenta l'utente della biblioteca.
 * L'utente è identificato univocamente dalla matricola.
 * </p>
 *
 * La classe implementa {@link Serializable}
 * per consentire la persistenza dei dati.
 */
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L; // Serve per il salvataggio dei file
    private static final String DOMINIO_STUDENTI = "@studenti.uni.it";
    private static final String DOMINIO_DOCENTI = "@docente.uni.it";
    private static final int MAX_PRESTITI = 3; // Regola di business per validità prestito
    private String nome;
    private String cognome;
    private final String matricola;
    private String email;
    
    private final List<Prestito> prestitiAttivi;
    private final List<Prestito> storicoPrestiti;

    public Utente(String nome, String cognome, String matricola, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.email = email;
        this.prestitiAttivi = new ArrayList<>();
        this.storicoPrestiti = new ArrayList<>();
    }

    // --- Getter e Setter ---

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getMatricola() {
        return matricola;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Prestito> getPrestitiattivi() {
        return prestitiAttivi;
    }

    public List<Prestito> getStoricoprestiti() {
        return storicoPrestiti;
    }

    // --- Metodi di Logica ---

/**
 * Verifica se l'utente può richiedere un nuovo prestito.
 * <p>
 * Le condizioni sono:
 * <ul>
 *   <li>utente ha meno di 3 prestiti attivi</li>
 *   <li>email valida (studente o docente)</li>
 * </ul>
 * </p>
 *
 * @return true se l'utente può richiedere un prestito
 */
    public boolean puoRichiederePrestito() {
        return prestitiAttivi.size()<MAX_PRESTITI && isEmailValida() ; //modificare diagramma delle classi
    }

    //Vincolo FC 1.4

/**
 * Verifica se l'email dell'utente è valida.
 * <p>
 * Un'email è valida se appartiene al dominio
 * studenti o docenti.
 * </p>
 *
 * @return true se l'email è valida
 */
    public boolean isEmailValida()
    {
       return this.email.endsWith(DOMINIO_STUDENTI) || this.email.endsWith(DOMINIO_DOCENTI);
    }
/**
 * Verifica se l'utente ha prestiti attivi
 *
 * @return true se esistono prestiti attivi
 */
    public boolean verificaPrestitiAttivi()
    {
        return !prestitiAttivi.isEmpty();
    }
    /**
     * Aggiunge un nuovo prestito alla lista dei prestiti attivi.
     * @param p
     */
    public void aggiungiPrestito(Prestito p) {
        if (p != null) {
            this.prestitiAttivi.add(p);
        }
    }

    /**
     * Sposta un prestito dagli attivi allo storico (restituzione).
     * @param p
     */
    public void restituisciPrestito(Prestito p) {
        if (prestitiAttivi.remove(p)) {
            storicoPrestiti.add(p); /*Decidere se vogliamo metterla o meno, potremmo aggiungerla direttamente nell'aggiungi*/
        }
    }

    // --- Metodi Fondamentali per Collezioni e Ricerca ---
/**
 * Confronta due utenti.
 * Due utenti sono uguali se hanno la stessa matricola.
 *
 * @param x oggetto da confrontare
 * @return true se gli utenti sono uguali
 */

    @Override
    public boolean equals(Object x) {
        if (this == x) return true;
        if (x == null || getClass() != x.getClass()) 
            return false;
        Utente user = (Utente) x;
        // Due utenti sono uguali se hanno la stessa matricola
        return this.matricola.equals(user.matricola);
    }
/**
 * Calcola l'hash code utente
 *
 * @return hashcode utente, altrimenti 0 se falsa
 */

    @Override
    public int hashCode() 
    {
        return this.matricola != null ? this.matricola.hashCode() : 0;
    }

/**
 * Restituisce una rappresentazione testuale dell'utente,
 * utile per il debugging e la visualizzazione.
 *
 * @return stringa descrittiva dell'utente
 */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Utente:\nnome=").append(nome);
        sb.append(", cognome=").append(cognome);
        sb.append(", matricola=").append(matricola);
        sb.append(", email=").append(email);
        sb.append(", prestitiAttivi=").append(prestitiAttivi);
        sb.append('}');
        return sb.toString();
    }
    
}

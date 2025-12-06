/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classibiblioteca.Entità;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Classe Utente 
 * @implements serializable
 * 
 * rappresenta utente con dettagli, get e set di data
 */
public class Utente implements Serializable {

    private String nome;
    private String cognome;
    private final String matricola;
    private String email;
    
    private List<Prestito> prestitiattivi;
    private final List<Prestito> storicoprestiti;

    public Utente(String nome, String cognome, String matricola, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.email = email;
        this.prestitiattivi = new ArrayList<>();
        this.storicoprestiti = new ArrayList<>();
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
        return prestitiattivi;
    }

    public void setPrestitiattivi(List<Prestito> prestitiattivi) {
        this.prestitiattivi = prestitiattivi;
    }

    public List<Prestito> getStoricoprestiti() {
        return storicoprestiti;
    }

    // --- Metodi di Logica (Prototipi) ---

    // Prototipo: controlla se può prendere altri prestiti (Max 3)
    public boolean puoRichiederePrestito() {
        return false; // Da implementare
    }
    
    public boolean verificaPrestitiAttivi()
    {
        return this.prestitiattivi==null;
    }
/**
 * @override
 * @return restituisce stringa utente con nome,cognome,matricola,email
 */
    
    @Override
    public String toString() {
        return "Utente{" + "nome=" + nome + ", cognome=" + cognome + ", matricola=" + matricola + ", email=" + email + '}';
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classibiblioteca.Entità;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author centr
 */
public class Utente 
{
    private String nome;
    private String cognome;
    private final String matricola;
    private String email;
    private List <Prestito> prestitiattivi;
    private final List<Prestito> storicoprestiti;

    public Utente(String nome, String cognome, String matricola, String email)
    {
        this.nome = nome;
        this.cognome = cognome;
        this.matricola = matricola;
        this.email = email;   
        prestitiattivi = new ArrayList<> ();
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() 
    {
    }
    

    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classibiblioteca.tipologiearchivi;

import Classibiblioteca.Entità.Utente;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author centr
 */
public class ArchivioUtenti
{
    private List<Utente> listutenti;
    
    public ArchivioUtenti()
    {
        listutenti = new ArrayList<>();
    }
    
    public void aggiungiUtente(Utente x)
    {
        listutenti.add(x);
    }
    
    public Utente eliminaUtente()
    {
    }
    
    public void modificaUtente(String nome,String cognome,String email)
    {
        
    }
    
    public Utente ricercaUtente(String campo)
    {
        
    }
    
     public int hashCode()
    {
        
    }    
    public int equals()
    {
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classibiblioteca.tipologiearchivi;

import Classibiblioteca.Entità.Libro;
import Classibiblioteca.Entità.Utente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author centr
 */
public class ArchivioLibri
{
    private List<Libro> listlibro;
    
    public ArchivioLibri()
    {
        listlibro = new ArrayList<>();
    }
    
    
    public int hashCode()
    {
        
    }    
    public int equals()
    {
        
    }
    public void aggiungiLibro(Libro x)
    {
        listlibro.add(x);
    }
    
    public Libro eliminaLibro()
    {
    }
//decidere cosa fare con il campo autori nella modifica    
    public void modificaLibro(String titolo,LocalDate annopubbl,int numerocopie)
    {
        
    }
    
    public Libro ricercaLibro(String campo)
    {
        
    }
     
    
    
}

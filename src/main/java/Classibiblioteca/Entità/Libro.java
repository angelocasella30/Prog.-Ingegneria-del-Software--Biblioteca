/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classibiblioteca.Entità;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author centr
 */
public class Libro 
{
    private String titolo;
    private List <String> autore;
    private LocalDate datapubbl;
    private final String ISBN;
    public static int numerocopie;

    public Libro(String titolo, LocalDate datapubbl, String ISBN,int numerocopie)
    {
        this.titolo = titolo;
        autore = new ArrayList<>();
        this.datapubbl = datapubbl;
        this.ISBN = ISBN;
        this.numerocopie=numerocopie;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public List<String> getAutore() {
        return autore;
    }

    public void setAutore(List<String> autore) {
        this.autore = autore;
    }

    public LocalDate getDatapubbl() {
        return datapubbl;
    }

    public void setDatapubbl(LocalDate datapubbl) {
        this.datapubbl = datapubbl;
    }

    public static int getNumerocopie() {
        return numerocopie;
    }

    public static void setNumerocopie(int numerocopie) {
        Libro.numerocopie = numerocopie;
    }

    @Override
    public String toString() {
        return "Libro{" + "titolo=" + titolo + ", autore=" + autore + ", datapubbl=" + datapubbl + ", ISBN=" + ISBN + '}';
    }
    
    
}

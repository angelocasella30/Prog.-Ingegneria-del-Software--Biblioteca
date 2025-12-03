/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classibiblioteca.Entità;

/**
 *
 * @author centr
 */
public class Prestito
{
    private String emailuser;
    private String matricola;
    private String titololibro;
    private int ISBN;
    private LocalDate datainizio;
    private LocalDate datarest;

    public Prestito(String emailuser, String matricola, String titololibro, int ISBN, LocalDate datainizio, LocalDate datarest) {
        this.emailuser = emailuser;
        this.matricola = matricola;
        this.titololibro = titololibro;
        this.ISBN = ISBN;
        this.datainizio = datainizio;
        this.datarest = datarest;
    }

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

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public LocalDate getDatainizio() {
        return datainizio;
    }

    public void setDatainizio(LocalDate datainizio) {
        this.datainizio = datainizio;
    }

    public LocalDate getDatarest() {
        return datarest;
    }

    public void setDatarest(LocalDate datarest) {
        this.datarest = datarest;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prestito{emailuser=").append(emailuser);
        sb.append(", matricola=").append(matricola);
        sb.append(", titololibro=").append(titololibro);
        sb.append(", ISBN=").append(ISBN);
        sb.append(", datainizio=").append(datainizio);
        sb.append(", datarest=").append(datarest);
        sb.append('}');
        return sb.toString();
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

/**
 *
 * @author Matteo Calosci
 */
public class Gestore {
    
    //Oggetto utilizzato per la lettura da tastiera
    Scanner tastiera;
    //Nome 
    String autore;
    //Ultimo messaggio ricevuto
    String ultimo;
    //Attributo che rappresenta la disponibilità del c/s
    Boolean disponibilita;

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getAutore() {
        return this.autore;
    }

    public void setDisponibilita(Boolean disp) {
        this.disponibilita = disp;
    }

    public Boolean getDisponibilita() {
        return this.disponibilita;
    }

}

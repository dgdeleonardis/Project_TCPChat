/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matteo Calosci
 */
public class Gestore {

    //Oggetto utilizzato per la lettura da tastiera
    private Scanner tastiera;
    //Ultimo messaggio ricevuto
    private String ultimo;
    //Attributo che rappresenta la disponibilità del c/s
    private boolean disponibilita;
    //Stream per la comunicazione
    private final DataInputStream in;
    private final DataOutputStream out;
    //Nome autore
    private String autore;
    //Nome entità
    private final String NOME;
    //Colore
    private final String COLORE;
    //Determina se l'host è connesso
    private boolean connesso;
    //Imposto il codice ansi per il colore di reset
    public static final String RESET = "\u001B[0m";
    //Il costruttore Gestore consente di creare un oggetto di tipo Gestore e di inizializzare
    //Gli stream associati, il nome dell'entità a cui è applciato (CLIENT o SERVER) il colore 
    //che caratterizza l'entità a cui è applicato e lo stato (connesso inizialmente)
    public Gestore(DataInputStream in, DataOutputStream out, String nome, String colore, boolean connesso) {
        this.tastiera = new Scanner(System.in);
        this.in = in;
        this.out = out;
        this.autore = "Ancora non definito! Scrivi \"autore\" per impostare un nome!";
        this.connesso = connesso;
        this.NOME = nome;
        this.COLORE = colore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getAutore() {
        return this.autore;
    }

    public void setDisponibilita(Boolean disp) {
        this.disponibilita = disp;
    }

    public boolean getDisponibilita() {
        return this.disponibilita;
    }
    
    public boolean getConnesso(){
        return this.connesso;
    }

    public void leggi() {
        try {
            String letta = in.readUTF();
            System.out.println(this.COLORE + " >>> " + this.RESET + letta);
            this.ultimo = letta;
        } catch (IOException ex) {
            System.err.println("Impossibile leggere");
        }
    }

    public void scrivi(String messaggio) {
        try {
            this.out.writeUTF(messaggio);
        } catch (IOException ex) {
            System.err.println("Impossibile scrivere");
        }
    }
    
    public void chiudi(){
        
    }
    
    public void menu() {
        String letta = this.tastiera.nextLine();
        letta = this.RESET + letta.toLowerCase();
        switch (letta) {
            case "smile":
                this.scrivi("=)");
            break;

            case "autore":
                System.out.println("\tNome attuale: " + this.COLORE + this.getAutore() + this.RESET);
                System.out.println("\tVuoi cambiarlo ? (si/no)");
                String scelta = this.tastiera.nextLine().toLowerCase();
                if (scelta.equals("si")) {
                    System.out.println("\tInserisci il nuovo nome");
                    this.setAutore(this.tastiera.nextLine());
                }
            break;

            case "end":
                this.chiudi();
            break;
                
            default:
                this.scrivi(letta);
            break;
        }
    }

}

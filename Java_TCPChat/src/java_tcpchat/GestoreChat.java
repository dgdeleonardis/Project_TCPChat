/*
 * To change this license header, choose License Headers inDataStream Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template inDataStream the editor.
 */
package java_tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Classe che modella GestoreChat. Ha la funzione di offrire all'entità Host le
 * operazioni e gli attributi per la gestione di una chat punto-punto.
 *
 * @author Matteo Calosci (commenti di Diego De Leonardis)
 */
public class GestoreChat {

    //Stream per la comunicazione
    private final DataInputStream inDataStream;
    private final DataOutputStream outDataStream;
    //Scanner per lettura da tastiera
    private final Scanner tastiera;

    //Nome dell'entità a cui è associato il gestore
    private String autore;
    //Colore dell'output dell'entità a cui è associato il gestore
    private final String COLORE;
    //Ultimo messaggio ricevuto
    private String ultimoMessaggio;

    //Determina se l'host è connesso
    private boolean connesso;
    //Determina la disponibilità dell'entità a cui è associato il gestore di ricevere messaggi 
    private boolean disponibilita;
    //Valore per il reset del colore di output
    public static final String RESET = "\u001B[0m";

    /**
     * Costruttore della classe che consente di creare un oggetto di tipo
     * GestoreChat e di inizializzare gli stream di I/O per lo scambio dei
     * messaggi, il colore e l'autore che caratterizza l'entità a cui è
     * applicato e lo stato e la disponibilità di questo.
     *
     * @param in stream di input dell'entità a cui è associata l'istanza
     * @param out stream di output dell'entità a cui è associata l'istanza
     * @param nome nome dell'entità a cui è associata l'istanza
     * @param colore colore dell'output da terminale dell'entità a cui è
     * associata l'istanza
     */
    public GestoreChat(DataInputStream in, DataOutputStream out, String nome, String colore) {
        this.inDataStream = in;
        this.outDataStream = out;
        this.tastiera = new Scanner(System.in);
        this.autore = nome;
        this.COLORE = colore;
        this.connesso = true;
        this.disponibilita = true;
    }

    /**
     * Metodo che ha come funzione l'interpretazione dell'input dell'utente
     */
    public void menu() {
        String inputUtente = this.tastiera.nextLine();
        inputUtente = this.RESET + inputUtente.toLowerCase();
        switch (inputUtente) {
            case "smile":
                this.scrivi("=)");
                break;

            case "autore":
                System.out.println("\tNome attuale: " + this.COLORE + this.getAutore() + this.RESET);
                System.out.print("\tVuoi cambiarlo ? (s/n): ");
                String scelta = this.tastiera.nextLine().toLowerCase();
                if (scelta.equals("s")) {
                    System.out.print("\tInserisci un nuovo nome: ");
                    this.setAutore(this.tastiera.nextLine());
                }
                break;

            case "end":
                this.chiudi();
                break;

            default:
                this.scrivi(inputUtente);
                break;
        }
    }

    /**
     * 
     */
    public void leggi() {
        try {
            String letta = inDataStream.readUTF();
            System.out.println(this.COLORE + " >>> " + this.RESET + letta);
            this.ultimoMessaggio = letta;
        } catch (IOException ex) {
            System.err.println("Impossibile leggere");
        }
    }

    public void scrivi(String messaggio) {
        try {
            this.outDataStream.writeUTF(messaggio);
        } catch (IOException ex) {
            System.err.println("Impossibile scrivere");
        }
    }

    public void chiudi() {

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

    public String getCOLORE() {
        return COLORE;
    }

    public boolean getConnesso() {
        return this.connesso;
    }

}

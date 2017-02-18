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
        //Nel momento in cui verrà digitato un comando speciale da parte di una delle due entità
        //sarà comunque necessario inviare un messaggio vuoto all'entità "destinatario"
        //tramite l'istruzione this.scrivi("");
        System.out.print("Scrivi pure qualcosa : ");
        String inputUtente = this.tastiera.nextLine();
        inputUtente = inputUtente.toLowerCase();
        switch (inputUtente) {
            case "$smile":
                this.scrivi("=)");
                break;

            case "$autore":
                System.out.println("\tNome attuale: " + this.COLORE + this.getAutore() + GestoreChat.RESET);
                System.out.print("\tVuoi cambiarlo ? (si/no): ");
                String scelta = this.tastiera.nextLine().toLowerCase();
                if (scelta.equals("si")) {
                    System.out.print("\tInserisci un nuovo nome: ");
                    this.setAutore(this.tastiera.nextLine());
                    //Informo il destinatario di aver cambiato nome
                    this.scrivi(" <---- Guarda ! Ho appena cambiato il mio nome !");
                }else{
                    this.scrivi("");
                }
                
                break;

            case "$end":
                this.chiudi();
                this.scrivi("");
                break;

            case "$disponibile":
                System.out.println("\tStato attuale: " + this.disponibilita);
                System.out.print("\tVuoi cambiare lo stato dell'entità " + this.autore + " ? (si/no): ");
                if (this.tastiera.nextLine().toLowerCase().equals("si")){
                    this.setDisponibilita(!this.disponibilita);
                    while(!this.disponibilita){
                        System.out.print("\tSto dormendo! digita \"$sveglia\" per svegliarmi\n\t");
                        String sveglia = this.tastiera.nextLine();
                        if(sveglia.toLowerCase().equals("$sveglia")){
                            this.setDisponibilita(true);
                            //Informo il destinatario di essere stato offline per un po'
                            this.scrivi("Scusa ... Mi ero assentato un po' !");
                        }
                        else{
                            System.out.print("\tContinuo a dormire allora ;)\n");
                        }
                    }
                }
                else{
                    this.scrivi("");
                }
                break;

            case "$ultimo":
                System.out.println(this.ultimoMessaggio);
                this.scrivi("");
                break;

            case "$help":
                this.menuToString();
                this.scrivi("");
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
            String datiTemp[] = letta.split("§");
            //Se la riga letta è nulla la ignoro
            if (!(datiTemp[0].equals(""))) {
                System.out.println(datiTemp[1] + datiTemp[2] + " >>> " + GestoreChat.RESET + datiTemp[0]);
                this.ultimoMessaggio = datiTemp[0];
            }

        } catch (IOException ex) {
            System.err.println("Impossibile leggere");
        }
    }

    public void scrivi(String messaggio) {
        try {
            //La stringa inviata contiene anche una sorta di header che contiene il colore identificativo
            //dell'entità mittente e il relativo nome entrambi separati dal carattere §
            this.outDataStream.writeUTF(messaggio + "§" + this.COLORE + "§" + this.autore);
            System.out.println(this.COLORE + "Sono in attesa di un messaggio ..." + GestoreChat.RESET);
        } catch (IOException ex) {
            System.err.println("Impossibile scrivere");
        }
    }

    public void chiudi() {
        try {
            this.outDataStream.close();
            this.inDataStream.close();
            this.connesso = false;
        } catch (IOException ex) {
            System.err.println("Impossibile chiudere la comunicazione");
        }

    }

    public void menuToString() {
        System.out.print("\tMenu comandi speciali\n"
                + "\t$autore (Cambia il nome dell'autore)\n"
                + "\t$smile (Invia uno smile)\n"
                + "\t$ultimo (Stampa l'ultimo messaggio ricevuto)\n"
                + "\t$disponibile (Imposta la disponibilità dell'host)\n"
                + "\t$end (Chiudi la comunicazione)\n"
                + "\t$help (Stampa il menu)\n");
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
        return this.COLORE;
    }

    public boolean getConnesso() {
        return this.connesso;
    }

}

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
    //Determina la disponibilità di ricevere messaggi dell'entità a cui è associato il gestore
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
    public void scriviMessaggio() {
        //Input da parte dell'utente
        System.out.print(this.COLORE + ">>> ");
        String inputUtente = this.tastiera.nextLine().toLowerCase();
        //Analisi dell'input dell'utente
        switch (inputUtente) {
            case "$autore":
                System.out.println(this.COLORE + ">>> Nome attuale: " + this.autore);
                System.out.print(this.COLORE + ">>> Vuoi cambiarlo? (sì/no)\n" + this.COLORE + ">>> ");
                inputUtente = this.tastiera.nextLine().toLowerCase();
                if (inputUtente.equals("sì") || inputUtente.equals("si")) {
                    String temp = this.autore;
                    System.out.print(this.COLORE + ">>> Inserisci un nuovo nome: ");
                    this.autore = this.tastiera.nextLine();
                    System.out.println(this.COLORE + ">>> Modifica effettuata.");
                    //Informo il destinatario di aver cambiato nome
                    this.inviaMessaggio(temp + " ha cambiato nome in " + this.autore);
                } else {
                    System.out.println(this.COLORE + ">>> Operazione annullata.");
                    this.scriviMessaggio();
                }
                break;

            case "$disponibile":
                System.out.println(this.COLORE + ">>> Stato attuale: " + this.disponibilita);
                System.out.print(this.COLORE + ">>> Vuoi cambiare lo stato dell'entità " + this.autore + " ? (sì/no)\n" + this.COLORE + ">>> ");
                inputUtente = this.tastiera.nextLine().toLowerCase();
                if (inputUtente.equals("sì") || inputUtente.equals("si")) {
                    this.disponibilita = !this.disponibilita;
                    if (this.disponibilita) {
                        this.inviaMessaggio(this.autore + " adesso è disponibile e può ricevere i tuoi messaggi.");
                    } else {
                        this.inviaMessaggio(this.autore + " adesso non è disponibil per ricevere i tuoi messaggi.");
                    }
                    //this.scriviMessaggio();
                }
                break;

            case "$smile":
                this.inviaMessaggio("=)");
                break;

            case "$ultimo":
                this.inviaMessaggio(this.ultimoMessaggio);
                break;

            case "$help":
                this.stampaMenu();
                this.scriviMessaggio();
                break;

            case "$end":
                this.inviaMessaggio("/end/");
                this.chiudi();
                System.out.println(this.COLORE + ">>> Connesione chiusa con successo.");
                break;

            default:
                this.inviaMessaggio(inputUtente);
                break;
        }
    }

    /**
     * Metodo che ha come funzione la lettura di nuovi messaggi ricevuti sul
     * flusso di input
     */
    public void leggiMessaggio() {
        try {
            String[] messaggio = inDataStream.readUTF().split("§");
            if (this.disponibilita) { 
                if ((messaggio[0].replace("\n", "")).equals("/end/")) {
                    System.out.println(messaggio[1] + messaggio[2] + " >>> Richiesta di chiusura della connessione...");
                    this.chiudi();
                    System.out.println(this.COLORE + " >>> Connessione chiusa con successo.");
                } else {
                    System.out.println(messaggio[1] + messaggio[2] + " >>> " + messaggio[0]);
                    this.ultimoMessaggio = messaggio[0];
                }
            } else {
                //this.inviaMessaggio("Host non disponibile per ricevere messaggi.");
            }
        } catch (IOException ex) {
            if(this.connesso) {
                System.err.println(">>> Impossibile leggere il messaggio.");
            }
        }
    }

    /**
     * Metodo che ha la funzione di inviare al destinatario un messaggio
     * attraverso lo stream di output a cui è associata la connessione
     *
     * @param messaggio stringa da inviare al destinatario
     */
    public void inviaMessaggio(String messaggio) {
        try {
            /**
             * La stringa inviata al mittente contiene rispettivamente il
             * messaggio, il colore identificativo dell'entità sorgente e il
             * relativo nome, entrambi separati dal carattere §
             */
            this.outDataStream.writeUTF(messaggio + "§" + this.COLORE + "§" + this.autore);
            //System.out.println(this.COLORE + ">>> Sono in attesa di un messaggio ..." + GestoreChat.RESET);
        } catch (IOException ex) {
            System.err.println(">>> Impossibile inviare il messaggio.");
        }
    }

    /**
     * Metodo che ha la funzione di chiudere i flussi di input e output
     * associati alla connessione
     */
    public void chiudi() {
        try {
            this.outDataStream.close();
            this.inDataStream.close();
            this.connesso = false;
        } catch (IOException ex) {
            System.err.println(">>> Impossibile chiudere la comunicazione.");
        }

    }

    /**
     * Metodo che ha la funzione di stampare il menu.
     */
    public void stampaMenu() {
        System.out.print(this.COLORE + ">>> Menu dei comandi speciali: \n"
                + this.COLORE + ">>>\t $autore (Cambia il nome dell'autore)\n"
                + this.COLORE + ">>>\t $disponibile (Imposta la disponibilità dell'host)\n"
                + this.COLORE + ">>>\t $smile (Invia uno smile)\n"
                + this.COLORE + ">>>\t $ultimo (Stampa l'ultimo messaggio ricevuto)\n"
                + this.COLORE + ">>>\t $help (Stampa il menu)\n"
                + this.COLORE + ">>>\t $end (Chiudi la comunicazione)\n");
    }

    /**
     * Metodo getter che ritorna il valore dell'attributo connesso.
     *
     * @return attributo connesso
     */
    public boolean getConnesso() {
        return this.connesso;
    }

    /**
     * Metodo getter di autore.
     *
     * @return attributo autore
     */
    public String getAutore() {
        return autore;
    }

    /**
     * Metodo getter di COLORE.
     *
     * @return attributo COLORE
     */
    public String getCOLORE() {
        return COLORE;
    }
}

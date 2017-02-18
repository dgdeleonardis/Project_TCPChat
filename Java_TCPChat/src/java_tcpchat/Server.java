/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Classe che estende Host. Modella l'entità del Server all'interno di
 * un'architettura Client/Server. Rispetto alla classe madre sono stati aggiunti
 * gli attributi e i metodi caratteristici di un'applicativo lato Server per
 * l'instaurazione di una connessione secondo le regole del protocollo TCP.
 *
 * @author Calosci Matteo (commenti di Diego De Leonardis)
 */
public class Server extends Host {
    //Oggetto di tipo ServerSocket necessario per l'apertura della connessione.
    ServerSocket serverSocket;

    /**
     * Costruttore della classe che va ad istanziare l'oggetto, andando a
     * valorizzare il solo attributo porta. Si richiama il costruttore della
     * superclasse.
     *
     * @param porta valore della porta dove il server sarà in ascolto
     */
    public Server(int porta) {
        super(porta);
    }

    /**
     * Metodo per l'apertura di una connessione da parte dell'entità Server
     * sulla porta prefissata.
     *
     * @param autoreDefault nome di default dell'entità.
     * @param coloreTerminaleDefault colore di default sul terminale
     * dell'entità.
     */
    public void attendi(String autoreDefault, String coloreTerminaleDefault) {
        try {
            //Il Server si mette in ascolto sulla porta prefissata attendendo possibili richieste
            this.serverSocket = new ServerSocket(this.porta);
            System.out.println("Server in ascolto sull'indirizzo IP " + InetAddress.getLocalHost() + " : " + porta);
            this.connectionSocket = this.serverSocket.accept();
            //Creazione di un gestore nel caso di instaurazione di una connessione
            this.gestore = new GestoreChat(new DataInputStream(this.connectionSocket.getInputStream()), new DataOutputStream(this.connectionSocket.getOutputStream()), autoreDefault, coloreTerminaleDefault);
            System.out.println("Connessione effettuata con successo.\n" + this.gestore.getCOLORE() + "Ciao io sono il server!" + "\u001B[0m");
            this.gestore.menuToString();
        } catch (IOException e) {
            System.err.println("ERRORE: è possibile che non sia riusciti ad\n"
                    + "\tIstanziare l'oggetto ServerSocket;\n"
                    + "\tInstaurare una connessione;\n");
        }
    }

    /**
     * Metodo statico per l'avvio della classe.
     *
     * @param args argomenti da linea di comando
     */
    public static void main(String[] args){
        /**
         * Variabili locali ausiliari per l'inizializzazione della classe
         * gestore. Commento per Matteo: da verificare l'utilità e la
         * correttezza.
         */
        String autoreDefault = "Server";
        String coloreTerminaleDefault = "\u001B[32m";

        Server s = new Server(2000);
        while (true) {
            s.attendi(autoreDefault, coloreTerminaleDefault);
            while (s.gestore.getConnesso()) {
                s.gestore.leggi();
                s.gestore.menu();
            }
        }
    }
}
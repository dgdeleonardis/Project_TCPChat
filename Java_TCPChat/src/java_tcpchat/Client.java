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
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Classe che estende Host. Modella l'entità del Client all'interno di
 * un'architettura Client/Server. Rispetto alla classe madre sono stati aggiunti
 * i metodi caratteristici di un'applicativo lato Client per la richiesta ed
 * instaurazione di una connessione secondo le regole del protocollo TCP.
 *
 * @author Calosci Matteo (commento di Diego De Leonardis)
 */
public class Client extends Host {

    /**
     * Costruttore della classe che va ad istanziare l'oggetto, andando a
     * valorizzare il solo attributo porta. Si richiama il costruttore della
     * superclasse.
     *
     * @param porta valore della porta dove il client richiederà la connessione
     */
    public Client(int porta) {
        super(porta);
    }

    /**
     * Metodo per la richiesta ed instaurazione di una connessione da parte
     * dell'entità ad un server, sulla porta ed indirizzo IP prefissati
     *
     * @param autoreDefault nome di default dell'entità.
     * @param coloreTerminaleDefault colore di default sul terminale
     */
    public void connetti(String autoreDefault, String coloreTerminaleDefault) {
        try {
            //Il Client richiede sulla porta ed indirizzo IP prefissato l'instaurazione di una connessione
            this.connectionSocket = new Socket(InetAddress.getLocalHost(), this.porta);
            //Creazione di un gestore nel caso di instaurazione di una connessione
            this.gestore = new GestoreChat(new DataInputStream(this.connectionSocket.getInputStream()), new DataOutputStream(this.connectionSocket.getOutputStream()), autoreDefault, coloreTerminaleDefault);
            System.out.println("Connessione stabilita con il server " + InetAddress.getLocalHost() + ":" + porta + "\n");
            System.out.println(this.gestore.getCOLORE() + "Ciao io sono il client!");
        } catch (UnknownHostException e) {
            System.out.println("ERRORE: Host sconosciuto");
        } catch (IOException e) {
            System.out.println("ERRORE: impossibile avviare la comunicazione");
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Variabili locali ausiliari per l'inizializzazione della classe
         * gestore. Commento per Matteo: da verificare l'utilità e la
         * correttezza.
         */
        String autoreDefault = "Client";
        String coloreTerminaleDefault = "\u001B[35m";

        Client c = new Client(2000);
        c.connetti(autoreDefault, coloreTerminaleDefault);
        while (c.gestore.getConnesso()) {
            c.gestore.menu();
            c.gestore.leggi();
        }
    }
}

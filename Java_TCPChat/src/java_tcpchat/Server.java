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
     * valorizzare i soli attributi porta e serverSocket. Si richiama il
     * costruttore della superclasse.
     *
     * @param porta valore della porta dove il server sarà in ascolto
     */
    public Server(int porta) {
        super(porta);
        try {
            this.serverSocket = new ServerSocket(this.porta);
            System.out.println("Server " + InetAddress.getLocalHost() + " in ascolto sulla porta " + porta);
        } catch (IOException e) {
            System.err.println("ERRORE: impossibile istanziare l'oggetto serverSocket della classe Server.");
        }
    }

    /**
     * Metodo per l'instaurazione di una connessione da parte dell'entità Server
     * sulla porta prefissata.
     *
     * @param autoreDefault nome di default dell'entità.
     * @param coloreTerminaleDefault colore di default sul terminale
     * dell'entità.
     */
    public void attendi(String autoreDefault, String coloreTerminaleDefault) {
        try {
            //Il Server attende una possibile richiesta da parte di un client di connessione
            this.connectionSocket = this.serverSocket.accept();
            //Creazione di un gestore nel caso di instaurazione di una connessione
            this.gestore = new GestoreChat(new DataInputStream(this.connectionSocket.getInputStream()), new DataOutputStream(this.connectionSocket.getOutputStream()), autoreDefault, coloreTerminaleDefault);
            System.out.println("Connessione instaurata con successo.\n" + this.gestore.getCOLORE() + "Ciao, io sono " + this.gestore.getAutore() + "!" + GestoreChat.RESET);
            this.gestore.menuToString();
        } catch (IOException e) {
            System.err.println("ERRORE: impossibile instaurare una connessione.");
        }
    }

    /**
     * Metodo statico per l'avvio della classe.
     *
     * @param args argomenti da linea di comando
     */
    public static void main(String[] args) {
        //Definizione di alcuni parametri di default
        String autoreDefault = "Server";
        String coloreTerminaleDefault = "\u001B[32m";
        int porta = 2000;
        boolean loopFlag = true;

        Server s = new Server(porta);
        while (loopFlag) {
            s.attendi(autoreDefault, coloreTerminaleDefault);
            //Svolgimento della chat fino a che non si è più connessi con l'altra entità
            s.gestore.leggiMessaggio();
            while (s.gestore.getConnesso()) {
                s.gestore.scriviMessaggio();
                s.gestore.leggiMessaggio();
            }
            try {
                s.connectionSocket.close();
            } catch (IOException ex) {
                System.err.println("ERRORE: impossibile chiudere la connessione");
            }
        }
    }
}

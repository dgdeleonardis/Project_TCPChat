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
 *
 * @author Calosci Matteo
 */
public class Client extends Host {
    /**
     *
     */
    public Client(int porta) {
        super(porta);
    }

    /**
     *
     */
    public void connetti() {
        try {
            /*
             * Tramite questa istruzione è possibile ottenere l'indirizzo ip
             * della macchina su cui è in esecuzione l'applicazione.
             * L'indirizzo ip ottenuto sarà lo stesso sia per il client che per il server
             */
            //Viene effettuata la connessione con il server
            this.connectionSocket = new Socket(InetAddress.getLocalHost(), this.porta);
            //Creazione del gestore
            this.gestore = new GestoreChat(new DataInputStream(this.connectionSocket.getInputStream()), new DataOutputStream(this.connectionSocket.getOutputStream()), this.gestore.getAutore() , this.gestore.getCOLORE(),true);
            System.out.println("Connessione stabilita con il server " + InetAddress.getLocalHost() + ":" + porta + "\n");
            System.out.println(this.gestore.getCOLORE() + "Ciao io sono il client!");
            
            
        } catch (UnknownHostException e) {
            System.out.println("Host sconosciuto");
        } catch (IOException e) {
            System.out.println("Errore durante l'avvio della comunicazione");
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Client c = new Client(2000);
        c.connetti();
        while (c.gestore.getConnesso()) {
            c.gestore.menu();
            c.gestore.leggi();
        }
        
    }

}

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
    
    private final static String COLORE = "\u001B[35m";
    //Nome identificativo dell'oggetto
    //Viene passato al costruttore di gestore per inizializzare il contenuto dell'attributo autore.
    private final String NOME = "Client";

    public Client() {
        
    }

    public void connetti() {
        try {
            /*
             * Tramite questa istruzione è possibile ottenere l'indirizzo ip
             * della macchina su cui è in esecuzione l'applicazione.
             * L'indirizzo ip ottenuto sarà lo stesso sia per il client che per il server
             */
            //Viene effettuata la connessione con il server
            this.client = new Socket(InetAddress.getLocalHost(), this.port);
            System.out.println("Connessione stabilita con il server " + InetAddress.getLocalHost() + ":" + port + "\n");
            System.out.println(this.COLORE + "Ciao io sono il client!");
            //Creazione del gestore
            this.g = new Gestore(new DataInputStream(this.client.getInputStream()), new DataOutputStream(this.client.getOutputStream()), this.NOME , this.COLORE,true);
            
        } catch (UnknownHostException e) {
            System.out.println("Host sconosciuto");
        } catch (IOException e) {
            System.out.println("Errore durante l'avvio della comunicazione");
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.connetti();
        while (c.g.getConnesso()) {
            c.g.menu();
            c.g.leggi();
        }
        
    }

}

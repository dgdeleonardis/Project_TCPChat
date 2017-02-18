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
 *
 * @author Calosci Matteo
 */
public class Server extends Host {

    //Oggetto di tipo ServerSocket che permette di stabilire la connessione 
    ServerSocket serverSocket;
    //Colore
    private final static String COLORE= "\u001B[34m";
    //Nome identificativo dell'oggetto
    //Viene passato al costruttore di gestore per inizializzare il contenuto dell'attributo autore.
    private final String NOME = "Server";
    
    public Server(int porta) {
         super(porta);
    }
    
    public void attendi() {
        try {
            //Creo il server sulla porta 2000
            this.serverSocket = new ServerSocket(this.porta);
                System.out.println("Server in ascolto su " + InetAddress.getLocalHost() + ":" + porta);
                //Metto in attesa di richieste il processo server sulla porta 2000.
                //Nel momento in cui venissero effettuate richieste, il metodo accept()
                //restituirà un oggetto Socket connesso con il client richiedente
                this.connectionSocket = this.serverSocket.accept();
                System.out.println("Connessione effettuata con successo.\n");
                System.out.println(this.COLORE + "Ciao io sono il server!" + "\u001B[0m");
                //Creazione del gestore
                this.gestore= new GestoreChat(new DataInputStream(this.connectionSocket.getInputStream()),new DataOutputStream(this.connectionSocket.getOutputStream()),this.NOME,this.COLORE,true);        
        } catch (IOException e) {
            System.err.println("Impossibile leggere dallo stream");
        }        
    }
    
    
    public static void main(String[] args) {
        Server s = new Server(2000); 
        while(true){ 
            s.attendi();
            while(s.gestore.getConnesso()){
                s.gestore.leggi();
                s.gestore.menu(); 
            }
            
        }
    }
}

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
import java.util.Scanner;

/**
 *
 * @author Calosci Matteo
 */
public class Server extends Host {

    //Oggetto di tipo ServerSocket che permette di stabilire la connessione 
    ServerSocket serverSocket;
    
    public Server(Gestore g) {
        this.g = g;
        this.s = new Scanner(System.in);
        this.connesso = false;
    }
    
    public void attendi() {
        try {
            //Creo il server sulla porta 2000
            this.serverSocket = new ServerSocket(this.port);
            while (true) {
                System.out.println("Server in ascolto su " + InetAddress.getLocalHost() + ":" + port);
                //Metto in attesa di richieste il processo server sulla porta 2000.
                //Nel momento in cui venissero effettuate richieste, il metodo accept()
                //restituirà un oggetto Socket connesso con il client richiedente
                this.client = this.serverSocket.accept();
                System.out.println("Connessione effettuata con successo.\n");
                this.connesso = true;
                //Si istanziano gli stream per la comunicazione 
                this.in = new DataInputStream(this.client.getInputStream());
                this.out = new DataOutputStream(this.client.getOutputStream());
                while (this.connesso) {
                    System.out.println("Il client ha scritto: " + this.leggi());
                    this.scrivi(s.nextLine());
                }
            }
        } catch (IOException e) {
            System.err.println("Impossibile leggere dallo stream");
        }        
    }
    
    public String leggi() {
        try {
            return in.readUTF();
        } catch (IOException ex) {
            System.err.println("Impossibile leggere dallo stream");
            return null;
        }
    }
    
    public void scrivi(String messaggio) {
        try {
            messaggio = messaggio.toLowerCase();
            switch (messaggio) {
                case "non disponibile":
                    this.g.disponibilita = false;
                    break;
                
                case "disponibile":
                    this.g.disponibilita = true;
                    break;
                
                case "autore":
                    System.out.println("\tScegli il nome!");
                    this.g.setAutore(this.s.nextLine());
                    break;
                
                case "smile":
                    this.out.writeUTF("=)");
                    break;
                
                case "end":
                    this.connesso = false;
                    break;
                
                default:
                    
                    out.writeUTF(messaggio);
                
            }
            
        } catch (IOException ex) {
            System.err.println("Impossibile scrivere sullo stream");
        }
    }
}

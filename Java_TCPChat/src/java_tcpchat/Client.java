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
import java.util.Scanner;

/**
 *
 * @author Calosci Matteo
 */
public class Client extends Host {

    public Client(Gestore g) {
        this.g = g;
        this.s = new Scanner(System.in);
        this.connesso = false;
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
            this.connesso = true;
            //Creazione del canale comunicativo
            this.in = new DataInputStream(this.client.getInputStream());
            this.out = new DataOutputStream(this.client.getOutputStream());
            while (this.connesso) {
                this.scrivi(s.nextLine());
                System.out.println("Il server ha scritto: " + this.leggi());
            }

        } catch (UnknownHostException e) {
            System.out.println("Host sconosciuto");
        } catch (IOException e) {
            System.out.println("Errore durante l'avvio della comunicazione");
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
                    connesso = false;
                    break;

                default:

                    out.writeUTF(messaggio);

            }

        } catch (IOException ex) {
            System.err.println("Impossibile scrivere sullo stream");
        }
    }

}

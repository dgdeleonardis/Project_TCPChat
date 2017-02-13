/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_tcpchat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Calosci Matteo
 */
public abstract class Host {
    
    //Gestore dell'host
    Gestore g;
    //Stream per la comunicazione
    DataInputStream in;
    DataOutputStream out;
    //Oggetto adibito alla lettura da tastiera
    Scanner s;
    //Oggetto di tipo Socket che permette di stabilire una connessione tra c/s
    Socket client;
    //Porta su cui è in ascolto il server e su cui il client farà richiests
    int port = 2000;
    //Determina se l'host è connesso (true) oppure no (false)
    Boolean connesso;
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_tcpchat;

import java.net.Socket;

/**
 *
 * @author Calosci Matteo
 */
public abstract class Host {
    
    //Gestore dell'host
    Gestore g;
    //Oggetto di tipo Socket che permette di stabilire una connessione tra c/s
    Socket client;
    //Porta su cui è in ascolto il server e su cui il client farà richiests
    int port = 2000;
    
}

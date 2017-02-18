/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java_tcpchat;

import java.net.Socket;

/**
 * Classe astratta che va a modellare il concetto di host all'interno di
 * un'architettura Client/Server. E' definita esclusivamente da un oggetto di
 tipo GestoreChat (per la gestione della chat) e dalla coppia socket - porta per
 l'instaurazione di una connessione stabile secondo le regole del protocollo
 TCP.
 *
 * @author Calosci Matteo (commenti di Diego De Leonardis)
 */
public abstract class Host {
    protected GestoreChat gestore;
    protected Socket connectionSocket;
    protected int porta;
    
    public Host(int porta) {
        this.porta = porta;
    }
}

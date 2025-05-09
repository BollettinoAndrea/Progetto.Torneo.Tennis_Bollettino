package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTennis {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);
        Partita partita = new Partita();
        ArrayList<GestoreClient> clientConnessi = new ArrayList<>();

        System.out.println("Server in ascolto...");

        while (true) {
            Socket socketClient = serverSocket.accept();
            GestoreClient gestore = new GestoreClient(socketClient, partita, clientConnessi);
            clientConnessi.add(gestore);
            gestore.start();
        }
    }
}
package client;

import common.Message;
import common.Message.Dati;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientTennis {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 12345);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Thread per ricevere i messaggi dal server
            Thread ricevitore = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = in.readLine()) != null) {
                        Message msg = Message.fromJson(linea);
                        if (msg.getTipo().equals("aggiorna_punteggio")) {
                            System.out.println("Punteggio aggiornato: " + msg.getDati().getPunteggio());
                            if (msg.getDati().getMessaggio() != null) {
                                System.out.println("Messaggio: " + msg.getDati().getMessaggio());
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Errore durante la lettura dei dati: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Errore generico nel ricevitore: " + e.getMessage());
                }
            });
            ricevitore.start();

            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("1. Aggiungi punto al giocatore 0");
                System.out.println("2. Aggiungi punto al giocatore 1");
                System.out.println("3. Visualizza punteggio");

                int scelta = scanner.nextInt();
                switch (scelta) {
                    case 1:
                        out.println(new Message("aggiungi_punto", new Dati() {{
                            setGiocatore(0);
                        }}).toJson());
                        break;
                    case 2:
                        out.println(new Message("aggiungi_punto", new Dati() {{
                            setGiocatore(1);
                        }}).toJson());
                        break;
                    case 3:
                        out.println(new Message("richiedi_punteggio", new Dati()).toJson());
                        break;
                    default:
                        System.out.println("Scelta non valida.");
                }
            }
        } catch (IOException e) {
            System.out.println("Errore di connessione o I/O: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore generico nel client: " + e.getMessage());
        }
    }
}
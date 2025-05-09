package server;

import common.Message;
import common.Message.Dati;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class GestoreClient extends Thread {
    private Socket socket;
    private Partita partita;
    private ArrayList<GestoreClient> clientConnessi;
    private BufferedReader in;
    private PrintWriter out;

    public GestoreClient(Socket socket, Partita partita, ArrayList<GestoreClient> clientConnessi) throws IOException {
        this.socket = socket;
        this.partita = partita;
        this.clientConnessi = clientConnessi;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String input = in.readLine();
                if (input == null) break; // la connessione è chiusa

                // Deserializza il messaggio
                try {
                    Message msg = Message.fromJson(input);
                    switch (msg.getTipo()) {
                        case "aggiungi_punto":
                            int giocatore = msg.getDati().getGiocatore();
                            partita.punto(giocatore);
                            inviaAGruppo("Punto assegnato al giocatore " + giocatore);
                            break;
                        case "richiedi_punteggio":
                            inviaPunteggio();
                            break;
                        default:
                            System.out.println("Tipo di messaggio non riconosciuto: " + msg.getTipo());
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("Errore nel processamento del messaggio: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Errore durante la lettura del flusso: " + e.getMessage());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                System.out.println("Errore durante la chiusura del socket: " + e.getMessage());
            }
        }
    }

    private void inviaPunteggio() throws Exception {
        Dati dati = new Dati();
        dati.setPunteggio(partita.getPunteggio());
        Message msg = new Message("aggiorna_punteggio", dati);
        out.println(msg.toJson());
    }

    private void inviaAGruppo(String messaggio) throws Exception {
        Dati dati = new Dati();
        dati.setPunteggio(partita.getPunteggio());
        dati.setMessaggio(messaggio);
        Message msg = new Message("aggiorna_punteggio", dati);
        for (GestoreClient gc : clientConnessi) {
            try {
                gc.out.println(msg.toJson());
            } catch (Exception e) {
                System.out.println("Errore nell'invio del messaggio al client: " + e.getMessage());
            }
        }
    }
}
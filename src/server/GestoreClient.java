package server;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static final ObjectMapper mapper = new ObjectMapper();

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
            String input;
            while ((input = in.readLine()) != null) {
                Message msg = mapper.readValue(input, Message.class);

                switch (msg.getTipo()) {
                    case "imposta_nomi" -> {
                        String nome1 = msg.getNomi().get(0);
                        String nome2 = msg.getNomi().get(1);
                        partita.setNomiGiocatori(nome1, nome2);
                        inviaAGruppo("Nomi dei giocatori impostati: " + nome1 + " vs " + nome2);
                    }

                    case "aggiungi_punto" -> {
                        int giocatore = msg.getDati().getGiocatore();
                        partita.punto(giocatore);

                        if (partita.partitaFinita()) {
                            String vincitore = partita.getNomeGiocatore(partita.getVincitore());
                            inviaAGruppo("HA VINTO " + vincitore + "!");
                        } else {
                            inviaAGruppo("Punto assegnato a " + partita.getNomeGiocatore(giocatore));
                        }
                    }

                    case "richiedi_punteggio" -> inviaPunteggio();
                }
            }
        } catch (IOException e) {
            System.out.println("Errore: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void inviaPunteggio() throws IOException {
        Dati dati = new Dati();
        dati.setPunteggio(partita.getPunteggio());
        Message msg = new Message("aggiorna_punteggio", dati);
        out.println(mapper.writeValueAsString(msg));
    }

    private void inviaAGruppo(String messaggio) throws IOException {
        Dati dati = new Dati();
        dati.setPunteggio(partita.getPunteggio());
        dati.setMessaggio(messaggio);
        Message msg = new Message("aggiorna_punteggio", dati);
        String json = mapper.writeValueAsString(msg);

        System.out.println("[SERVER] " + partita.getPunteggio());
        System.out.println("[SERVER] " + messaggio);

        for (GestoreClient gc : clientConnessi) {
            gc.out.println(json);
        }
    }
}
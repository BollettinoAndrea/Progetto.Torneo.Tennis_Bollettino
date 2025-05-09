package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.Message;
import common.Message.Dati;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class ClientTennis {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ObjectMapper mapper = new ObjectMapper();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il nome del giocatore 1: ");
        String nome1 = scanner.nextLine();
        System.out.print("Inserisci il nome del giocatore 2: ");
        String nome2 = scanner.nextLine();

        Message msgNomi = new Message();
        msgNomi.setTipo("imposta_nomi");
        msgNomi.setNomi(Arrays.asList(nome1, nome2));
        out.println(mapper.writeValueAsString(msgNomi));

        Thread ricevitore = new Thread(() -> {
            try {
                String linea;
                while ((linea = in.readLine()) != null) {
                    Message msg = mapper.readValue(linea, Message.class);
                    if ("aggiorna_punteggio".equals(msg.getTipo())) {
                        System.out.println("Punteggio aggiornato: " + msg.getDati().getPunteggio());
                        if (msg.getDati().getMessaggio() != null)
                            System.out.println("Messaggio: " + msg.getDati().getMessaggio());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        ricevitore.start();

        while (true) {
            System.out.println("1. Aggiungi punto a: " + nome1 + ".");
            System.out.println("2. Aggiungi punto a: " + nome2 + ".");
            System.out.println("3. Visualizza punteggio");

            int scelta = scanner.nextInt();
            Dati dati = new Dati();
            Message msg;

            switch (scelta) {
                case 1 -> {
                    dati.setGiocatore(0);
                    msg = new Message("aggiungi_punto", dati);
                    out.println(mapper.writeValueAsString(msg));
                }
                case 2 -> {
                    dati.setGiocatore(1);
                    msg = new Message("aggiungi_punto", dati);
                    out.println(mapper.writeValueAsString(msg));
                }
                case 3 -> {
                    msg = new Message("richiedi_punteggio", new Dati());
                    out.println(mapper.writeValueAsString(msg));
                }
            }
        }
    }
}
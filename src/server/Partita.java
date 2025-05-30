package server;

public class Partita {
    private String[] nomiGiocatori = {"Giocatore 0", "Giocatore 1"};
    private int[] game = {0, 0};
    private int[] set = {0, 0};
    private int[] punti = {0, 0};
    public String[] etichettePunti = {"0", "15", "30", "40", "AD"};
    private static final int SET_PER_VITTORIA = 2;

    public synchronized void setNomiGiocatori(String nome1, String nome2) {
        nomiGiocatori[0] = nome1;
        nomiGiocatori[1] = nome2;
    }

    public synchronized void punto(int giocatore) {
        if (partitaFinita()) return; // Non si può segnare dopo la fine

        int avversario = 1 - giocatore;
        if (punti[giocatore] == 3 && punti[avversario] < 3) {
            giocoVinto(giocatore);
        } else if (punti[giocatore] == 3 && punti[avversario] == 3) {
            punti[giocatore] = 4;
        } else if (punti[giocatore] == 4) {
            giocoVinto(giocatore);
        } else if (punti[giocatore] == 3 && punti[avversario] == 4) {
            punti[avversario] = 3;
        } else {
            punti[giocatore]++;
        }
    }

    private void giocoVinto(int giocatore) {
        game[giocatore]++;
        punti[0] = 0;
        punti[1] = 0;
        if (game[giocatore] >= 6 && game[giocatore] - game[1 - giocatore] >= 2) {
            set[giocatore]++;
            game[0] = 0;
            game[1] = 0;
        }
    }

    public synchronized String getPunteggio() {
        return "Set: " + set[0] + "-" + set[1] +
                " | Game: " + game[0] + "-" + game[1] +
                " | Punti: " + etichettePunti[punti[0]] + "-" + etichettePunti[punti[1]];
    }

    public String getNomeGiocatore(int indice) {
        return nomiGiocatori[indice];
    }

    public synchronized boolean haVinto(int giocatore) {
        return set[giocatore] == SET_PER_VITTORIA;
    }

    public synchronized boolean partitaFinita() {
        return haVinto(0) || haVinto(1);
    }

    public synchronized int getVincitore() {
        if (haVinto(0)) return 0;
        if (haVinto(1)) return 1;
        return -1;
    }
}
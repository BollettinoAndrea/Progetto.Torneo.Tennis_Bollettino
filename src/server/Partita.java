package server;

public class Partita {
    private String[] nomiGiocatori = {"Giocatore 0", "Giocatore 1"};
    private int[] giochi = {0, 0};
    private int[] set = {0, 0};
    private int[] punti = {0, 0};
    public String[] etichettePunti = {"0", "15", "30", "40", "VANTAGGIO"};

    public synchronized void setNomiGiocatori(String nome1, String nome2) {
        nomiGiocatori[0] = nome1;
        nomiGiocatori[1] = nome2;
    }

    public synchronized void punto(int giocatore) {
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
        giochi[giocatore]++;
        punti[0] = 0;
        punti[1] = 0;
        if (giochi[giocatore] >= 6 && giochi[giocatore] - giochi[1 - giocatore] >= 2) {
            set[giocatore]++;
            giochi[0] = 0;
            giochi[1] = 0;
        }
    }

    public synchronized String getPunteggio() {
        return "Set: " + set[0] + "-" + set[1] +
                " | Giochi: " + giochi[0] + "-" + giochi[1] +
                " | Punti: " + etichettePunti[punti[0]] + "-" + etichettePunti[punti[1]];
    }

    public String getNomeGiocatore(int indice) {
        return nomiGiocatori[indice];
    }
}
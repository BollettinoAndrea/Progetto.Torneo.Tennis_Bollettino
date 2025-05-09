package common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private String tipo;
    private Dati dati;
    private List<String> nomi; // Lista dei nomi dei giocatori

    public Message() {
    }

    public Message(String tipo, Dati dati) {
        this.tipo = tipo;
        this.dati = dati;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Dati getDati() {
        return dati;
    }

    public void setDati(Dati dati) {
        this.dati = dati;
    }

    public List<String> getNomi() {
        return nomi;
    }

    public void setNomi(List<String> nomi) {
        this.nomi = nomi;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Dati {
        private Integer giocatore;
        private String punteggio;
        private String messaggio;

        public Dati() {
        }

        public Integer getGiocatore() {
            return giocatore;
        }

        public void setGiocatore(Integer giocatore) {
            this.giocatore = giocatore;
        }

        public String getPunteggio() {
            return punteggio;
        }

        public void setPunteggio(String punteggio) {
            this.punteggio = punteggio;
        }

        public String getMessaggio() {
            return messaggio;
        }

        public void setMessaggio(String messaggio) {
            this.messaggio = messaggio;
        }
    }
}
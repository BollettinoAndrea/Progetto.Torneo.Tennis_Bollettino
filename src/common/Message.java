package common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Message {

    @JsonProperty("tipo")
    private String tipo;

    @JsonProperty("dati")
    private Dati dati;

    // Costruttore
    public Message() {}

    public Message(String tipo, Dati dati) {
        this.tipo = tipo;
        this.dati = dati;
    }

    // Getter e Setter
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

    // Metodo per serializzare a JSON
    public String toJson() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    // Metodo per deserializzare da JSON
    public static Message fromJson(String jsonStr) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonStr, Message.class);
    }

    // Classe interna per i dati
    public static class Dati {
        @JsonProperty("giocatore")
        private int giocatore;
        @JsonProperty("punteggio")
        private String punteggio;
        @JsonProperty("messaggio")
        private String messaggio;

        // Getter e Setter
        public int getGiocatore() {
            return giocatore;
        }

        public void setGiocatore(int giocatore) {
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
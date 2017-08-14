package giovannicornachini.macknotas.br.Entidades;

/**
 * Created by GiovanniCornachini on 08/05/15.
 */
public class Faltas {
    String faltasAtuais;
    String faltasPermitidas;
    String faltasPorcentagem;
    String faltasUltima;

    public Faltas() {
    }

    public Faltas(String faltasAtuais, String faltasPermitidas, String faltasPorcentagem, String faltasUltima) {
        this.faltasAtuais = faltasAtuais;
        this.faltasPermitidas = faltasPermitidas;
        this.faltasPorcentagem = faltasPorcentagem;
        this.faltasUltima = faltasUltima;
    }

    public String getFaltasAtuais() {
        return faltasAtuais;
    }

    public void setFaltasAtuais(String faltasAtuais) {
        this.faltasAtuais = faltasAtuais;
    }

    public String getFaltasPermitidas() {
        return faltasPermitidas;
    }

    public void setFaltasPermitidas(String faltasPermitidas) {
        this.faltasPermitidas = faltasPermitidas;
    }

    public String getFaltasPorcentagem() {
        return faltasPorcentagem;
    }

    public void setFaltasPorcentagem(String faltasPorcentagem) {
        this.faltasPorcentagem = faltasPorcentagem;
    }

    public String getFaltasUltima() {
        return faltasUltima;
    }

    public void setFaltasUltima(String faltasUltima) {
        this.faltasUltima = faltasUltima;
    }
}

package giovannicornachini.macknotas.br.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiovanniCornachini on 19/04/15.
 */
public class Materia {
    String nome;
    String formula;
    List<String> notas;

    public Materia(String nome, String formula, ArrayList<String> notas) {
        this.nome = nome;
        this.formula = formula;
        this.notas = notas;
    }

    public Materia() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public List<String> getNotas() {
        return notas;
    }

    public void setNotas(List<String> notas) {
        this.notas = notas;
    }
}

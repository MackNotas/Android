package giovannicornachini.macknotas.br.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiovanniCornachini on 06/06/15.
 */
public class Horario {

    List<String> horas;
    List<List<String>> materias;


    public Horario() {
        this.horas = new ArrayList<>();
        this.materias = new ArrayList<>();
    }


    public List<String> getHoras() {
        return horas;
    }

    public void setHoras(List<String> horas) {
        this.horas = horas;
    }

    public List<List<String>> getMaterias() {
        return materias;
    }

    public void setMaterias(List<List<String>> materias) {
        this.materias = materias;
    }
}

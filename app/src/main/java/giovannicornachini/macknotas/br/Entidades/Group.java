package giovannicornachini.macknotas.br.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiovanniCornachini on 19/04/15.
 */
public class Group {

    public String string;
    public final List<String> children = new ArrayList<String>();
    public List listMateria;
    public List listfaltas;

    public Group(String string) {
        this.string = string;
    }



}

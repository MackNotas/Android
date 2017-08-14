package giovannicornachini.macknotas.br.Entidades;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiovanniCornachini on 02/08/15.
 */
public class GroupAtvComplem {
    public String string;
    public final List<String> children = new ArrayList<String>();
    public List listAtvComplemDeferidas;

    public GroupAtvComplem(String string) {
        this.string = string;
    }
}

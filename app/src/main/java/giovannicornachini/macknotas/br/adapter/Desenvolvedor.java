package giovannicornachini.macknotas.br.adapter;

import java.util.ArrayList;

/**
 * Created by GiovanniCornachini on 17/05/15.
 */
public class Desenvolvedor {

    public String nome;

    public Desenvolvedor(String nome) {
        this.nome = nome;
    }

    public static ArrayList<Desenvolvedor> getDesenvolvedores() {
        ArrayList<Desenvolvedor> desenvolverdor = new ArrayList<Desenvolvedor>();
        desenvolverdor.add(new Desenvolvedor("Caio"));
        desenvolverdor.add(new Desenvolvedor("Marla"));
        desenvolverdor.add(new Desenvolvedor("Sarah"));
        return desenvolverdor;
    }
}

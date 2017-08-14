package giovannicornachini.macknotas.br.adapter;

import java.util.ArrayList;

/**
 * Created by GiovanniCornachini on 16/05/15.
 */
public class SwitchAndDescription {

    public String descricao;
    public boolean status;

    public SwitchAndDescription(String descricao,boolean status) {
        this.descricao = descricao;
        this.status = status;
    }


    //inutilizado
    public static ArrayList<SwitchAndDescription> getSwitchAndDescription() {
        ArrayList<SwitchAndDescription> switchAndDescription = new ArrayList<>();
        switchAndDescription.add(new SwitchAndDescription("Mostrar a nota na notificação",false));
        switchAndDescription.add(new SwitchAndDescription("Receber uma vez a notificação da nota",false));
        return switchAndDescription;
    }
}

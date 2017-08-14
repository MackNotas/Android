package giovannicornachini.macknotas.br.adapter;

/**
 * Created by GiovanniCornachini on 14/05/15.
 */
import java.util.ArrayList;

public class StatusServidor {
    public String name;
    public String status;

    public StatusServidor(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public static ArrayList<StatusServidor> getStatusServidor() {
        ArrayList<StatusServidor> statusServidor = new ArrayList<StatusServidor>();
        statusServidor.add(new StatusServidor("T.I.A.", "OFF"));
        statusServidor.add(new StatusServidor("MackNotas", "OFF"));
        statusServidor.add(new StatusServidor("Push", "OFF"));
        return statusServidor;
    }
}

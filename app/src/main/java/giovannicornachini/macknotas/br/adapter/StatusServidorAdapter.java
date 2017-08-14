package giovannicornachini.macknotas.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import giovannicornachini.macknotas.br.R;

/**
 * Created by GiovanniCornachini on 14/06/15.
 */
public class StatusServidorAdapter extends ArrayAdapter<StatusServidor> {

    public StatusServidorAdapter(Context context, ArrayList<StatusServidor> statusServidores) {
        super(context, 0, statusServidores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StatusServidor statusServidor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_status_servidor, parent, false);
        }
        // Lookup view for data population
        TextView nomeServidor = (TextView) convertView.findViewById(R.id.text1);
        nomeServidor.setText(statusServidor.name);

        View status = convertView.findViewById(R.id.statusServidor);
        if(statusServidor.status.equals("OFF"))
            status.setBackgroundResource(R.drawable.circle_red);
        else
            status.setBackgroundResource(R.drawable.circle_green);

        // Return the completed view to render on screen
        return convertView;
    }
}
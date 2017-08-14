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
 * Created by GiovanniCornachini on 17/05/15.
 */
public class CustomDesenvolvedorAdapter extends ArrayAdapter<Desenvolvedor> {

    public CustomDesenvolvedorAdapter(Context context, ArrayList<Desenvolvedor> desenvolvedor) {
        super(context, 0, desenvolvedor);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Desenvolvedor desenvolvedor = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_desenvolvedor, parent, false);
        }
        // Lookup view for data population
        TextView nomeDesenvolvedor = (TextView) convertView.findViewById(R.id.text1);
        // Populate the data into the template view using the data object
        nomeDesenvolvedor.setText(desenvolvedor.nome);
        // Return the completed view to render on screen
        return convertView;
    }
}

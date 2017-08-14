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
 * Created by GiovanniCornachini on 06/06/15.
 */
public class CustomHorarioAdapter extends ArrayAdapter<HorarioAdapter> {

    public CustomHorarioAdapter(Context context, ArrayList<HorarioAdapter> horarioAdapters) {
        super(context, 0, horarioAdapters);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final HorarioAdapter horarioAdapter = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_horario, parent, false);
        }

        // Lookup view for data population
        final TextView horario = (TextView) convertView.findViewById(R.id.txtHorario);
        final TextView materia = (TextView) convertView.findViewById(R.id.txtMateria);
        // Populate the data into the template view using the data object
        horario.setText(horarioAdapter.horario);
        materia.setText(horarioAdapter.materia);


        // Return the completed view to render on screen
        return convertView;
    }




}

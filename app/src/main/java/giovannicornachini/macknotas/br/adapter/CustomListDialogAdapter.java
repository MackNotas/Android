package giovannicornachini.macknotas.br.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import giovannicornachini.macknotas.br.R;

/**
 * Created by GiovanniCornachini on 28/06/15.
 */
public class CustomListDialogAdapter extends ArrayAdapter<ListDialogAdapter> {

    public CustomListDialogAdapter(Context context, ArrayList<ListDialogAdapter> user) {
        super(context, 0, user);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ListDialogAdapter user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialoglist_ajustes, parent, false);
        }
        // Lookup view for data population
        TextView firstLine = (TextView) convertView.findViewById(R.id.text1);
        firstLine.setText(user.firstLine);
        TextView secondLine = (TextView) convertView.findViewById(R.id.text2);
        secondLine.setText(user.secondLine);


        // Return the completed view to render on screen
        return convertView;
    }
}

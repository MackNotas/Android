package giovannicornachini.macknotas.br.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;

import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.dao.AjustesDAO;
import giovannicornachini.macknotas.br.dao.SwitchDAO;

/**
 * Created by GiovanniCornachini on 16/05/15.
 */
public class CustomSwitchAndDescriptionAdapter extends ArrayAdapter<SwitchAndDescription> {

    public CustomSwitchAndDescriptionAdapter(Context context, ArrayList<SwitchAndDescription> switchAndDescriptions) {
        super(context, 0, switchAndDescriptions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        SwitchAndDescription switchAndDescription = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_switch, parent, false);
        }
        // Lookup view for data population
        final Switch switch1 = (Switch) convertView.findViewById(R.id.switch1);
        TextView text1 = (TextView) convertView.findViewById(R.id.text1);

        // Populate the data into the template view using the data object
        switch1.setText(switchAndDescription.descricao);
        switch1.setChecked(switchAndDescription.status);

        if(switch1.getText().equals("Convidar para receber notificação")) {
            switch1.setVisibility(View.INVISIBLE);
            text1.setText(switchAndDescription.descricao);
        }else if(switch1.getText().equals("Como posso ativar as notificações?")){
            switch1.setVisibility(View.INVISIBLE);
            text1.setText(switchAndDescription.descricao);
            text1.setTextColor(Color.RED);
        }else{
            text1.setVisibility(View.INVISIBLE);

        }


        //verifica se tem push disponivel
        AjustesDAO ajustesDAO = new AjustesDAO();
        if(!ajustesDAO.hasPush()){
            switch1.setEnabled(false);
            switch1.setClickable(true);
            switch1.setFocusableInTouchMode(false);

        } else {


            switch1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SwitchDAO dao = new SwitchDAO();
                    if (switch1.getText().equals("Mostrar a nota na notificação")) {
                        dao.setShowNotaStatus(switch1.isChecked());
                    } else {
                        dao.setPushOnlyOnce(switch1.isChecked());
                    }

                }
            });
        }



        // Return the completed view to render on screen
        return convertView;
    }


}

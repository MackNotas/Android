package giovannicornachini.macknotas.br.CalendarTest;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import giovannicornachini.macknotas.br.Entidades.Calendario;
import giovannicornachini.macknotas.br.R;

public class CalendarSectionedAdapter extends SectionedBaseAdapter {


    int sizeSection;
    int sizeItems;
    List<String> mes;
    List<List<Calendario>> listCalendario;

    public CalendarSectionedAdapter(List<String> mes, List<List<Calendario>> listCalendario) {
        this.mes = mes;
        this.listCalendario = listCalendario;
    }



    @Override
    public Object getItem(int section, int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int section, int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSectionCount() {
        return listCalendario.size();
    }

    @Override
    public int getCountForSection(int section) {
        return listCalendario.get(section).size();
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.item_calendario, null);
        } else {
            layout = (LinearLayout) convertView;
        }

        TextView dia = (TextView) layout.findViewById(R.id.textItem);
        TextView diaSemana = (TextView) layout.findViewById(R.id.txt_diaSem);
        TextView materia = (TextView) layout.findViewById(R.id.txtNomeMateria);
        TextView tipoProva = (TextView) layout.findViewById(R.id.txtTipoProva);

        dia.setText(listCalendario.get(section).get(position).getDia());
        diaSemana.setText(listCalendario.get(section).get(position).getDiaSemana());
        materia.setText(listCalendario.get(section).get(position).getMateria());
        tipoProva.setText(listCalendario.get(section).get(position).getTipoProva());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateInString = listCalendario.get(section).get(position).getData()+"/"+Calendar.getInstance().get(Calendar.YEAR);


        Date date = null;
        try {
            date = formatter.parse(dateInString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date.compareTo(new Date())<0){
            LinearLayout rectangle = ((LinearLayout) layout.findViewById(R.id.backgroundCalendar));

            rectangle.getBackground().setAlpha(128);
            dia.setTextColor(Color.argb(100, 255, 0, 0));
            diaSemana.setTextColor(Color.argb(100, 255, 0, 0));

        }


        return layout;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_calendario, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        ((TextView) layout.findViewById(R.id.textItem)).setText(this.mes.get(section));
        return layout;
    }

}

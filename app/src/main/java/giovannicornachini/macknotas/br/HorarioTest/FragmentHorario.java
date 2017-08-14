package giovannicornachini.macknotas.br.HorarioTest;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import giovannicornachini.macknotas.br.Entidades.Horario;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.adapter.CustomHorarioAdapter;
import giovannicornachini.macknotas.br.adapter.HorarioAdapter;

/**
 * Created by giovannicornachini on 17/04/16.
 */
public class FragmentHorario extends Fragment {

    Horario horario;
    int positionFragment = 0;

    public FragmentHorario() {
    }

    public FragmentHorario(int positionFragment, Horario horario) {
        this.horario= horario;
        this.positionFragment = positionFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_horario_fragments, container, false);

        //Horarios e matérias
        ArrayList<HorarioAdapter> arrayOfHorarios = new ArrayList<>();
        for(int i=0; i<horario.getMaterias().get(positionFragment).size();i++) {
            arrayOfHorarios.add(new HorarioAdapter(horario.getMaterias().get(positionFragment).get(i), horario.getHoras().get(i)));
        }
        // Create the adapter to convert the array to views
        CustomHorarioAdapter adapterInformacoes = new CustomHorarioAdapter(getActivity(), arrayOfHorarios);
        // Attach the adapter to a ListView
        ListView listViewInformacoes = (ListView) rootView.findViewById(R.id.informacoes);
        listViewInformacoes.setAdapter(adapterInformacoes);


        return rootView;
    }
}

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
 * A dummy fragment representing a section of the app, but that simply
 * displays dummy text.
 */
public class DummySectionFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";

    Horario horario= new Horario();
    View rootView;
    CustomHorarioAdapter adapterInformacoes;
    ListView listViewInformacoes;

    public DummySectionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.tab_horario_fragments, container, false);

        int positionFragment = getArguments().getInt(ARG_SECTION_NUMBER)-1;

        //Horarios e matérias
        ArrayList<HorarioAdapter> arrayOfHorarios = new ArrayList<>();
        for(int i=0; i<horario.getMaterias().get(positionFragment).size();i++) {
            arrayOfHorarios.add(new HorarioAdapter(horario.getMaterias().get(positionFragment).get(i), horario.getHoras().get(i)));
        }
        // Create the adapter to convert the array to views
        adapterInformacoes = new CustomHorarioAdapter(getActivity(), arrayOfHorarios);
        // Attach the adapter to a ListView
        listViewInformacoes = (ListView) rootView.findViewById(R.id.informacoes);
        listViewInformacoes.setAdapter(adapterInformacoes);



        return rootView;
    }

    public void setHorario(Horario horario){
        this.horario = horario;
    }


}

package giovannicornachini.macknotas.br.util;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import giovannicornachini.macknotas.br.Entidades.AtvDeferidas;
import giovannicornachini.macknotas.br.Entidades.Faltas;
import giovannicornachini.macknotas.br.Entidades.Group;
import giovannicornachini.macknotas.br.Entidades.GroupAtvComplem;
import giovannicornachini.macknotas.br.Entidades.Materia;
import giovannicornachini.macknotas.br.R;

/**
 * Created by GiovanniCornachini on 02/08/15.
 */
public class AtvComplemExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<GroupAtvComplem> groups;
    public LayoutInflater inflater;
    public Activity activity;
    private Faltas faltas;
    private int contador;

    public AtvComplemExpandableListAdapter(Activity act, SparseArray<GroupAtvComplem> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
        contador = 0;
    }



    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }


    //setar valores de notas
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details_atv_comp, null);
        }

        //setar atvDeferidas
        List<AtvDeferidas> listAtvDeferidas = groups.get(0).listAtvComplemDeferidas;

        ((TextView) convertView.findViewById(R.id.dataLabel)).setText(listAtvDeferidas.get(groupPosition).getData());
        ((TextView) convertView.findViewById(R.id.tipoAtvLabel)).setText(listAtvDeferidas.get(groupPosition).getTipo());
        ((TextView) convertView.findViewById(R.id.modalidadeLabel)).setText(listAtvDeferidas.get(groupPosition).getModalidade());
        ((TextView) convertView.findViewById(R.id.anoSemLabel)).setText(listAtvDeferidas.get(groupPosition).getAnoSemestre());
        ((TextView) convertView.findViewById(R.id.horasLabel)).setText(listAtvDeferidas.get(groupPosition).getHoras());



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, groups.get(groupPosition).string, Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        GroupAtvComplem group = (GroupAtvComplem) getGroup(groupPosition);
        ((TextView) convertView).setText(group.string);


        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}

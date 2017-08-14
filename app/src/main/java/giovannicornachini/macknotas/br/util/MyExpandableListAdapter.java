package giovannicornachini.macknotas.br.util;

import android.app.Activity;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import giovannicornachini.macknotas.br.Entidades.Faltas;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.Entidades.Group;
import giovannicornachini.macknotas.br.Entidades.Materia;

/**
 * Created by GiovanniCornachini on 19/04/15.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<Group> groups;
    public LayoutInflater inflater;
    public Activity activity;
    private Faltas faltas;
    private int contador;


    public MyExpandableListAdapter(Activity act, SparseArray<Group> groups) {
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
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }

        //setar notas
        List<Materia> listMateria = groups.get(0).listMateria;

        for (int j = 0; j < 15; j++) {
            if(listMateria.get(groupPosition).getNotas().get(j).equals("")){
                notaVazia(j,convertView);
            }else{
                notaNaoVazia(j,convertView,listMateria.get(groupPosition).getNotas().get(j));
            }
        }

        //setarFaltas
        if(groups.get(0).listfaltas!=null && groups.get(0).listfaltas.size()>0) {
            List<Faltas> listfaltas = groups.get(0).listfaltas;
            setFaltas(groupPosition, convertView, listfaltas);
        }else{
            setFaltasVazia(groupPosition, convertView);
        }

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

        String materia;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((TextView) convertView).setText(group.string);


        verificaMedia(convertView, groupPosition);

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





    //Metodo para setar faltas
    public void setFaltas(int posicaoNota, View convertView,List<Faltas> listfaltas){
        TextView faltasAtuais = (TextView) convertView.findViewById(R.id.faltasAtuais);
        TextView faltasPermitidas = (TextView) convertView.findViewById(R.id.faltasPermitidas);
        TextView faltasPorcentagem = (TextView) convertView.findViewById(R.id.faltasPorcentagem);
        TextView faltasUltima = (TextView) convertView.findViewById(R.id.faltasUltima);

        faltasAtuais.setText(listfaltas.get(posicaoNota).getFaltasAtuais());
        faltasPermitidas.setText(listfaltas.get(posicaoNota).getFaltasPermitidas());
        faltasPorcentagem.setText("("+listfaltas.get(posicaoNota).getFaltasPorcentagem()+"%)");

        if (listfaltas.get(posicaoNota).getFaltasUltima().equals("00/00/0000"))
            faltasUltima.setText("Nunca");
        else
            faltasUltima.setText(listfaltas.get(posicaoNota).getFaltasUltima());

        if(listfaltas.get(posicaoNota).getFaltasPorcentagem().isEmpty()){
            faltasAtuais.setText("Não foi possível obter suas faltas");

            faltasPermitidas.setText("");
            faltasPorcentagem.setText("");
            ((TextView) convertView.findViewById(R.id.textView3)).setText("");
            ((TextView) convertView.findViewById(R.id.textView7)).setText("");
            ((TextView) convertView.findViewById(R.id.textView5)).setText("");

        }else if(Double.parseDouble(listfaltas.get(posicaoNota).getFaltasPorcentagem())<=15.0){
            faltasPorcentagem.setTextColor(Color.rgb(0,200,0));
            faltasAtuais.setTextColor(Color.rgb(0,200,0));
        }else if(Double.parseDouble(listfaltas.get(posicaoNota).getFaltasPorcentagem())<25.0){
            faltasPorcentagem.setTextColor(Color.rgb(255,105,0));
            faltasAtuais.setTextColor(Color.rgb(255,105,0));
        }else{
            faltasPorcentagem.setTextColor(Color.RED);
            faltasAtuais.setTextColor(Color.RED);
        }



    }

    public void setFaltasVazia(int posicaoNota, View convertView){
        TextView faltasAtuais = (TextView) convertView.findViewById(R.id.faltasAtuais);
        TextView faltasPermitidas = (TextView) convertView.findViewById(R.id.faltasPermitidas);
        TextView faltasPorcentagem = (TextView) convertView.findViewById(R.id.faltasPorcentagem);
        TextView faltasUltima = (TextView) convertView.findViewById(R.id.faltasUltima);

        faltasAtuais.setText("");
        faltasPermitidas.setText("");
        faltasPorcentagem.setText("(Não encontrado)");
        faltasAtuais.setText("");

        faltasUltima.setText("Não encontrado");
    }





    public void notaVazia(int posicaoNota, View convertView){

        TextView nota,notaLabel;

        switch (posicaoNota){
            case 0:
                nota = (TextView) convertView.findViewById(R.id.notaA);
                notaLabel = (TextView) convertView.findViewById(R.id.notaALabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 1:
                nota = (TextView) convertView.findViewById(R.id.notaB);
                notaLabel = (TextView) convertView.findViewById(R.id.notaBLabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 2:
                nota = (TextView) convertView.findViewById(R.id.notaC);
                notaLabel = (TextView) convertView.findViewById(R.id.notaCLabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 3:
                nota = (TextView) convertView.findViewById(R.id.notaD);
                notaLabel = (TextView) convertView.findViewById(R.id.notaDLabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 4:
                nota = (TextView) convertView.findViewById(R.id.notaE);
                notaLabel = (TextView) convertView.findViewById(R.id.notaELabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 5:
                nota = (TextView) convertView.findViewById(R.id.notaF);
                notaLabel = (TextView) convertView.findViewById(R.id.notaFLabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 6:
                nota = (TextView) convertView.findViewById(R.id.notaG);
                notaLabel = (TextView) convertView.findViewById(R.id.notaGLabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 7:
                nota = (TextView) convertView.findViewById(R.id.notaH);
                notaLabel = (TextView) convertView.findViewById(R.id.notaHLabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 8:
                nota = (TextView) convertView.findViewById(R.id.notaI);
                notaLabel = (TextView) convertView.findViewById(R.id.notaILabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 9:
                nota = (TextView) convertView.findViewById(R.id.notaJ);
                notaLabel = (TextView) convertView.findViewById(R.id.notaJLabel);
                nota.setVisibility(View.GONE);
                notaLabel.setVisibility(View.GONE);
                break;
            case 10:
                nota = (TextView) convertView.findViewById(R.id.notaSub);
                notaLabel = (TextView) convertView.findViewById(R.id.notaSubLabel);
                notaLabel.setText("");
                break;
            case 11:
                nota = (TextView) convertView.findViewById(R.id.notaPart);
                notaLabel = (TextView) convertView.findViewById(R.id.notaPartLabel);
                notaLabel.setText("");
                break;
            case 12:
                nota = (TextView) convertView.findViewById(R.id.notaMI);
                notaLabel = (TextView) convertView.findViewById(R.id.notaMILabel);
                notaLabel.setText("");
                break;
            case 13:
                nota = (TextView) convertView.findViewById(R.id.notaPF);
                notaLabel = (TextView) convertView.findViewById(R.id.notaPFLabel);
                notaLabel.setText("");
                break;
            case 14:
                nota = (TextView) convertView.findViewById(R.id.notaMF);
                notaLabel = (TextView) convertView.findViewById(R.id.notaMFLabel);
                notaLabel.setText("");
                break;
        }

    }

    public void notaNaoVazia(int posicaoNota, View convertView, String valor){
        ExpandableListView lstNotas;
        TextView nota,notaLabel;



        switch (posicaoNota){
            case 0:
                nota = (TextView) convertView.findViewById(R.id.notaA);
                notaLabel = (TextView) convertView.findViewById(R.id.notaALabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 1:
                nota = (TextView) convertView.findViewById(R.id.notaB);
                notaLabel = (TextView) convertView.findViewById(R.id.notaBLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 2:
                nota = (TextView) convertView.findViewById(R.id.notaC);
                notaLabel = (TextView) convertView.findViewById(R.id.notaCLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 3:
                nota = (TextView) convertView.findViewById(R.id.notaD);
                notaLabel = (TextView) convertView.findViewById(R.id.notaDLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 4:
                nota = (TextView) convertView.findViewById(R.id.notaE);
                notaLabel = (TextView) convertView.findViewById(R.id.notaELabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 5:
                nota = (TextView) convertView.findViewById(R.id.notaF);
                notaLabel = (TextView) convertView.findViewById(R.id.notaFLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 6:
                nota = (TextView) convertView.findViewById(R.id.notaG);
                notaLabel = (TextView) convertView.findViewById(R.id.notaGLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 7:
                nota = (TextView) convertView.findViewById(R.id.notaH);
                notaLabel = (TextView) convertView.findViewById(R.id.notaHLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 8:
                nota = (TextView) convertView.findViewById(R.id.notaI);
                notaLabel = (TextView) convertView.findViewById(R.id.notaILabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 9:
                nota = (TextView) convertView.findViewById(R.id.notaJ);
                notaLabel = (TextView) convertView.findViewById(R.id.notaJLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 10:
                nota = (TextView) convertView.findViewById(R.id.notaSub);
                notaLabel = (TextView) convertView.findViewById(R.id.notaSubLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 11:
                nota = (TextView) convertView.findViewById(R.id.notaPart);
                notaLabel = (TextView) convertView.findViewById(R.id.notaPartLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 12:
                nota = (TextView) convertView.findViewById(R.id.notaMI);
                notaLabel = (TextView) convertView.findViewById(R.id.notaMILabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 13:
                nota = (TextView) convertView.findViewById(R.id.notaPF);
                notaLabel = (TextView) convertView.findViewById(R.id.notaPFLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
            case 14:
                nota = (TextView) convertView.findViewById(R.id.notaMF);
                notaLabel = (TextView) convertView.findViewById(R.id.notaMFLabel);
                notaLabel.setText(valor);
                nota.setVisibility(View.VISIBLE);
                notaLabel.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void verificaMedia(View convertView, int groupPosition) {
        List<Materia> listMateria = groups.get(0).listMateria;
        if(contador<(listMateria.size()*2) && listMateria.size() > 1) {
            if (Double.parseDouble(listMateria.get(groupPosition).getNotas().get(14).toString()) >= 6.0) {
                TextView text = (TextView) convertView.findViewById(R.id.nomeMateria);
                text.setTextColor(Color.rgb(0, 200, 0));
            }
            contador++;
        }else{
            TextView text = (TextView) convertView.findViewById(R.id.nomeMateria);
            text.setTextColor(Color.BLACK);
        }

    }

}
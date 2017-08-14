package giovannicornachini.macknotas.br.Fragments.Graduacao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import giovannicornachini.macknotas.br.ActMain;
import giovannicornachini.macknotas.br.Entidades.AtvDeferidas;
import giovannicornachini.macknotas.br.Entidades.AtvTotalHoras;
import giovannicornachini.macknotas.br.Entidades.Faltas;
import giovannicornachini.macknotas.br.Entidades.Group;
import giovannicornachini.macknotas.br.Entidades.GroupAtvComplem;
import giovannicornachini.macknotas.br.Entidades.Horario;
import giovannicornachini.macknotas.br.Entidades.Materia;
import giovannicornachini.macknotas.br.HorarioTest.SectionsPagerAdapter;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.dao.AtvComplemDAO;
import giovannicornachini.macknotas.br.dao.HorarioDAO;
import giovannicornachini.macknotas.br.util.AtvComplemExpandableListAdapter;
import giovannicornachini.macknotas.br.util.MyExpandableListAdapter;

/**
 * Created by GiovanniCornachini on 02/08/15.
 */
public class AtvComplemTab extends Fragment {

    private static final String ARG_TEXT = "ARG_TEXT";
    private View view;
    private FragmentActivity myContext;
    private AtvComplemDAO atvComplemDAO = new AtvComplemDAO();
    List<AtvDeferidas> listAtvDeferidas;
    AtvTotalHoras atvTotalHoras;
    private ExpandableListView expandableListView;
    String response = "";
    String responseLocal = "";

    // more efficient than HashMap for mapping integers to objects
    AtvComplemExpandableListAdapter adpDados;
    SparseArray<GroupAtvComplem> groups = new SparseArray<GroupAtvComplem>();

    public static AtvComplemTab newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        AtvComplemTab sampleFragment = new AtvComplemTab();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        myContext.setTitle("Ativ. Complementares");

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.tab_atv_complementares, container, false);

        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_atv_complem);

        Bundle bundle = getActivity().getIntent().getExtras();
        final String login = bundle.getString("login");
        final String senha = bundle.getString("senha");
        final String unidade = bundle.getString("unidade");

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (listAtvDeferidas == null || atvTotalHoras == null) {
                    //obter dados locais
                    responseLocal = atvComplemDAO.getJson();
                    if (!responseLocal.isEmpty() && response.isEmpty()) {
                        listAtvDeferidas = atvComplemDAO.getAtvComplemDeferidas(responseLocal);
                        atvTotalHoras = atvComplemDAO.getTotalHoras(responseLocal);
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(atvTotalHoras!=null) {
                            createLists();
                        }
                    }
                });
            }
        }).start();


        if(isOnline()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (response.isEmpty()) {
                        //obter dados webservice
                        response = atvComplemDAO.getResponse(login,senha, unidade);
                    }
                    listAtvDeferidas = atvComplemDAO.getAtvComplemDeferidas(response);
                    atvTotalHoras = atvComplemDAO.getTotalHoras(response);

                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //criar listas
                                if (!response.isEmpty()) {
                                    createLists();
                                }

                            }
                        });
                    }catch(Exception e){

                    }
                }
            }).start();
        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
            alertDialog.setTitle("Ops, deu ruim!");
            alertDialog.setMessage("Por favor, verifique sua conexão com a internet ou tente mais tarde.");
            alertDialog.setButton("Fechar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // funções aqui
                }
            });
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.show();
        }


        return view;
    }

    public void createLists(){
        //criando ExpandableListView
        createData(listAtvDeferidas);
        adpDados = new AtvComplemExpandableListAdapter(getActivity(),
                groups);
        expandableListView.setAdapter(adpDados);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    expandableListView.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });
        setListViewHeight(expandableListView);


        //Atividades deferidas
        ListView listView = (ListView) view.findViewById(R.id.list_resumo_horas);
        List<String> values = new ArrayList();
        values.add("Ativ. de Ensino   "+ atvTotalHoras.getEnsino());
        values.add("Ativ. de Pesquisa "+atvTotalHoras.getPesquisa());
        values.add("Ativ. de Extensão "+atvTotalHoras.getExtensao());
        values.add("Excedentes        "+atvTotalHoras.getExcedentes());
        values.add("Total de Horas    "+atvTotalHoras.getTotal());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                return view;
            }
        };
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
    }

    public void createData(List<AtvDeferidas> atvDeferidasList) {

        for (int j = 0; j < atvDeferidasList.size(); j++) {
            GroupAtvComplem group = new GroupAtvComplem(atvDeferidasList.get(j).getAssunto());
            group.listAtvComplemDeferidas = listAtvDeferidas;
            group.children.add(
                    ""
            );
            groups.append(j, group);
        }


    }

    @Override
    public void onAttach(Activity activity) {
        myContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_ajustes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_deslogar) {
            Intent it = new Intent(getActivity(), ActMain.class);
            startActivity(it);
            ParseUser.logOut();
            getActivity().finish();


        }

        return super.onOptionsItemSelected(item);
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    //Metodo para ajustar a altura dinamicamente da ExpandableListView e listview
    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}

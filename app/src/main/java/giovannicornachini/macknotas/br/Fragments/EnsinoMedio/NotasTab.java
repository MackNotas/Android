package giovannicornachini.macknotas.br.Fragments.EnsinoMedio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import giovannicornachini.macknotas.br.ActMain;
import giovannicornachini.macknotas.br.Entidades.Faltas;
import giovannicornachini.macknotas.br.Entidades.Group;
import giovannicornachini.macknotas.br.Entidades.Materia;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.adapter.EnsinoMedio.NotasExpandableListAdapter;
import giovannicornachini.macknotas.br.dao.FaltasDAO;
import giovannicornachini.macknotas.br.dao.LoginDAO;
import giovannicornachini.macknotas.br.dao.NotasDAO;


public class NotasTab extends Fragment{

    private String login;
    private String senha;
    private String unidade;
    private Button btnAtualizarNotas;
    private ExpandableListView lstNotas;
    private List<Materia> listMateria;
    private List<String> listNome;
    private List<Faltas> listFaltas;
    NotasExpandableListAdapter adpDados;
    NotasDAO notasDAO = new NotasDAO();
    FaltasDAO faltasDAO = new FaltasDAO();

    // more efficient than HashMap for mapping integers to objects
    SparseArray<Group> groups = new SparseArray<Group>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);



        //Pega dados da main view
        Bundle bundle = getActivity().getIntent().getExtras();

        if(bundle!=null && bundle.containsKey("login")) {

            login = bundle.getString("login");
            senha = bundle.getString("senha");
            unidade = bundle.getString("unidade");
            Log.d("MackNotas Notas", "Login: " + login);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (isOnline()) {

                        listMateria = new ArrayList();
                        listMateria = notasDAO.buscarNotas(login, senha, unidade);

                        if (listMateria.size() > 0) {
                            listFaltas = faltasDAO.getFaltas(login, senha);
                        }else{
                            listFaltas = faltasDAO.faltasVazia(listMateria.size());
                        }

                        listNome = new ArrayList<String>();
                        for (int i = 0; i < listMateria.size(); i++) {
                            listNome.add(listMateria.get(i).getNome());
                        }


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (listNome.size() <= 0 || !isOnline()) {
                                    ifErrorGetLocalNotas();
                                } else {
                                    handler.sendEmptyMessage(0);


                                    new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Notas Atualizadas!")
                                            .showCancelButton(false)
                                            .show();

                                }
                                LoginDAO loginDAO = new LoginDAO();
                                loginDAO.getPessoalData(login, senha, unidade);

                            }
                        });
                    } else {
                        ifErrorGetLocalNotas();
                    }

                }
            }).start();

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.tab_notas, container, false);
        setHasOptionsMenu(true);

        lstNotas = (ExpandableListView) v.findViewById(R.id.lstNotas);


        if(listNome!=null){
            handler.sendEmptyMessage(0);
        }


        return v;
    }




    public void createData(List<String> listNome,List<Materia> listmateria,List<Faltas> listFaltas) {

        for (int j = 0; j < listNome.size(); j++) {
            Group group = new Group(listNome.get(j));
            group.listMateria = listmateria;
            group.listfaltas = listFaltas;
            group.children.add(
                    ""
            );
            groups.append(j, group);
        }


    }


    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch( msg.what ){
                case 0:
                    createData(listNome, listMateria,listFaltas);
                    //lstNotas = (ExpandableListView) getActivity().findViewById(R.id.lstNotas);
                    adpDados = new NotasExpandableListAdapter(getActivity(),
                            groups);
                    lstNotas.setAdapter(adpDados);

                    lstNotas.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        int previousItem = -1;

                        @Override
                        public void onGroupExpand(int groupPosition) {
                            if(groupPosition != previousItem )
                                lstNotas.collapseGroup(previousItem );
                            previousItem = groupPosition;
                        }
                    });

                    break;

            }
        }
    };



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
    }





    public void ifErrorGetLocalNotas(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("NotasPin");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(final List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    if(parseObjects.size()>0) {


                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                listMateria = new ArrayList<Materia>();


                                JSONArray jsonarray = null;
                                try {
                                    jsonarray = new JSONArray(parseObjects.get(parseObjects.size() - 1).get("nota").toString());
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                JSONArray notasJson;
                                Materia materia;
                                for(int i=0; i<jsonarray.length(); i++){
                                    try {
                                        JSONObject obj = jsonarray.getJSONObject(i);

                                        materia = new Materia();
                                        //materia.setFormula(obj.getString("formulas"));
                                        materia.setNome(obj.getString("nome"));
                                        notasJson = obj.getJSONArray("notas");


                                        List<String> list = new ArrayList<>();
                                        for (int j = 0; j < notasJson.length(); j++) {
                                            list.add(notasJson.getString(j));
                                        }

                                        materia.setNotas(list);


                                        listMateria.add(materia);
                                    }catch (Exception e){

                                    }
                                }


                                listNome = new ArrayList<String>();
                                for(int i=0;i<listMateria.size();i++){
                                    listNome.add(listMateria.get(i).getNome());
                                }



                                // here you check the value of getActivity() and break up if needed
                                if(getActivity() == null)
                                    return;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //if(!adpDados.isEmpty()){
                                        //adpDados.clear();
                                        //}

                                        if (listNome.size() <= 0) {
                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Ops, deu ruim!")
                                                    .setContentText("Não há nenhuma nota salva!\n" +
                                                            "Por favor tente novamente mais tarde.")
                                                    .show();

                                        } else {
                                            handler.sendEmptyMessage(0);

                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Ops, deu ruim!")
                                                    .setContentText("Por favor, verifique sua conexão com a internet ou tente mais tarde.")
                                                    .show();

                                        }
                                    }
                                });
                            }
                        }).start();



                    }else{

                    }

                } else {
                    Log.d("MackNotas","Nenhum item de notas foi encontrado no pin");
                }
            }
        });
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }



    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_deslogar){
            Intent it = new Intent(getActivity(), ActMain.class);
            startActivity(it);
            ParseUser.logOut();
            getActivity().finish();



        }else if(id == R.id.act_atualizarNotas){
            Toast.makeText(getActivity().getApplicationContext(),
                    "Atualizando Notas...", Toast.LENGTH_LONG).show();

            item.setEnabled(false);
            item.setVisible(false);

            new Thread(new Runnable() {
                @Override
                public void run() {

                    listMateria = new ArrayList();

                    listMateria = notasDAO.buscarNotas(login, senha, unidade);
                    if(listMateria.size()>0) {
                        listFaltas = faltasDAO.getFaltas(login, senha);
                    }else{
                        listFaltas = faltasDAO.faltasVazia(listMateria.size());
                    }

                    listNome = new ArrayList<String>();
                    for (int i = 0; i < listMateria.size(); i++) {
                        listNome.add(listMateria.get(i).getNome());
                    }


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //if(!adpDados.isEmpty()){
                            //adpDados.clear();
                            //}

                            if (listNome.size() <= 0) {
                                ifErrorGetLocalNotas();
                            } else {
                                handler.sendEmptyMessage(0);


                                SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                dialog.setTitleText("Notas Atualizadas!");
                                dialog.showCancelButton(false);
                                dialog.show();
                                Log.d("MackNotas", "Notas Atualizadas");

                            }
                            item.setEnabled(true);
                            item.setVisible(true);
                        }
                    });

                }
            }).start();

        }

        return super.onOptionsItemSelected(item);
    }


}

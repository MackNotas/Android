package giovannicornachini.macknotas.br.Fragments.Graduacao;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import giovannicornachini.macknotas.br.ActMain;
import giovannicornachini.macknotas.br.CalendarTest.CalendarSectionedAdapter;
import giovannicornachini.macknotas.br.CalendarTest.PinnedHeaderListView;
import giovannicornachini.macknotas.br.Entidades.Calendario;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.dao.CalendarioDAO;

/**
 * Created by GiovanniCornachini on 31/05/15.
 */
public class CalendarioTab extends Fragment implements View.OnClickListener {

    private static final String ARG_TEXT = "ARG_TEXT";
    CalendarioDAO calendarioDAO = new CalendarioDAO();
    List<List<Calendario>> listCalendario = new ArrayList<>();
    List<String> mes;
    String login;
    String senha;
    String unidade;
    View v;
    PinnedHeaderListView listView;
    CalendarSectionedAdapter sectionedAdapter;
//    SweetAlertDialog pDialog;
    String response = "";
    String responseLocal = "";

    public static CalendarioTab newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        CalendarioTab sampleFragment = new CalendarioTab();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("Calendário");

//        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
//        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.button_red_mack));
//        pDialog.setTitleText("Carregando...");
//        pDialog.setCancelable(false);
//        pDialog.show();

        if (listCalendario.size() > 0 && v != null) {
            listView = (PinnedHeaderListView) v.findViewById(R.id.list);
            listView.setSelection(getMesAtual(listCalendario));

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        v = inflater.inflate(R.layout.tab_calendario, container, false);

        listView = (PinnedHeaderListView) v.findViewById(R.id.list);

        mes = gerarMes();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(response.isEmpty()) {
                            responseLocal = calendarioDAO.getJson();
                        if (!responseLocal.isEmpty()) {
                            listCalendario = calendarioDAO.getCalendario(responseLocal);


                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sectionedAdapter = new CalendarSectionedAdapter(mes, listCalendario);
                                    listView.setAdapter(sectionedAdapter);
                                    listView.setSelection(getMesAtual(listCalendario));


                                }
                            });
                        }
                    }
                }
            }).start();


        if (isOnline()) {
            if(response.isEmpty()) {
                Bundle bundle = getActivity().getIntent().getExtras();
                login = bundle.getString("login");
                senha = bundle.getString("senha");
                unidade = bundle.getString("unidade");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        response = calendarioDAO.getResponse(login, senha, unidade);
                        listCalendario = calendarioDAO.getCalendario(response);

                        if(getActivity()!=null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (listCalendario.size() > 0 || !isOnline()) {
                                        if (sectionedAdapter == null)
                                            sectionedAdapter = new CalendarSectionedAdapter(mes, listCalendario);
                                        listView.setAdapter(sectionedAdapter);
                                        listView.setSelection(getMesAtual(listCalendario));
                                        Toast.makeText(getActivity().getApplicationContext(),
                                                "Calendário Atualizado!", Toast.LENGTH_LONG).show();
                                    } else {

                                        if (getActivity() == null) {
                                            return;
                                        }

                                        Context context = getActivity().getApplicationContext();
                                        CharSequence text = "Verifique sua conexão ou tente mais tarde.";
                                        int duration = Toast.LENGTH_SHORT;

                                        Toast toast = Toast.makeText(context, text, duration);
                                        toast.show();
                                    }

                                }
                            });
                        }

                    }
                }).start();
            }else{
                sectionedAdapter = new CalendarSectionedAdapter(mes, listCalendario);
                listView.setAdapter(sectionedAdapter);
                listView.setSelection(getMesAtual(listCalendario));

            }
        } else {

            if (listCalendario.size()>0) {
                sectionedAdapter = new CalendarSectionedAdapter(mes, listCalendario);
                listView.setAdapter(sectionedAdapter);
                listView.setSelection(getMesAtual(listCalendario));
            }

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

        listView.setDivider(null);
        listView.setDividerHeight(0);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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


    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity().getApplicationContext(), "Item: " + v.getTag(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_ajustes, menu);
    }


    public int getMesAtual(List<List<Calendario>> listCalendario) {

        int mes = Integer.parseInt(new SimpleDateFormat("dd/MM/yyyy").format(new Date()).toString().substring(3, 5));
        int total = 0;
        for (int i = 0; i < mes - 1; i++) {
            total += listCalendario.get(i).size();
        }

        return total + mes - 1;

    }


    public List<String> gerarMes() {
        List<String> mes = new ArrayList<String>();
        mes.add("Janeiro");
        mes.add("Fevereiro");
        mes.add("Março");
        mes.add("Abril");
        mes.add("Maio");
        mes.add("Junho");
        mes.add("Julho");
        mes.add("Agosto");
        mes.add("Setembro");
        mes.add("Outubro");
        mes.add("Novembro");
        mes.add("Dezembro");

        return mes;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}

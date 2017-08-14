package giovannicornachini.macknotas.br.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import giovannicornachini.macknotas.br.ActMain;
import giovannicornachini.macknotas.br.CalendarTest.PinnedHeaderListView;
import giovannicornachini.macknotas.br.ConvidarAmigoActivity;
import giovannicornachini.macknotas.br.FaleComOMackNotas;
import giovannicornachini.macknotas.br.InformacaoPessoal;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.SobreActivity;
import giovannicornachini.macknotas.br.WebViewActivity;
import giovannicornachini.macknotas.br.adapter.CustomInformacoesAdapter;
import giovannicornachini.macknotas.br.adapter.CustomListDialogAdapter;
import giovannicornachini.macknotas.br.adapter.CustomSwitchAndDescriptionAdapter;
import giovannicornachini.macknotas.br.adapter.Informacoes;
import giovannicornachini.macknotas.br.adapter.ListDialogAdapter;
import giovannicornachini.macknotas.br.adapter.StatusServidor;
import giovannicornachini.macknotas.br.adapter.StatusServidorAdapter;
import giovannicornachini.macknotas.br.adapter.SwitchAndDescription;
import giovannicornachini.macknotas.br.dao.AjustesDAO;
import giovannicornachini.macknotas.br.dao.SwitchDAO;

/**
 * Created by GiovanniCornachini on 02/05/15.
 */
public class AjustesTab extends Fragment {
    private static final String ARG_TEXT = "ARG_TEXT";

    TextView tiaMackenzie;
    String statusMackNotas = "OFF";
    String statusTIA = "OFF";
    String statusPush = "OFF";
    ArrayList<StatusServidor> arrayOfServidores;
    AjustesDAO ajustesDAO = new AjustesDAO();

    View v;
    CustomSwitchAndDescriptionAdapter adapter;

    public static AjustesTab newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        AjustesTab sampleFragment = new AjustesTab();
        sampleFragment.setArguments(args);


        return sampleFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        getActivity().setTitle("Ajustes");

        if(isOnline()) {

            //Verifica TIA
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean status = ajustesDAO.verificaTIA();
                    Log.d("VERIFICA TIA", "FIM");

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status) {
                                    statusTIA = "ON";
                                    arrayOfServidores.set(0, new StatusServidor("T.I.A.", statusTIA));
                                    if (getActivity() != null) {
                                        StatusServidorAdapter adapterStatusServidor = new StatusServidorAdapter(getActivity(), arrayOfServidores);
                                        // Attach the adapter to a ListView
                                        ListView listStatusServer = (ListView) v.findViewById(R.id.listServers);
                                        listStatusServer.setAdapter(adapterStatusServidor);
                                        setListViewHeightBasedOnChildren(listStatusServer);
                                    }
                                }
                            }
                        });
                    }

                }
            }).start();

            //Verifica MackNotas
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean status = ajustesDAO.verificaMackNotas();
                    Log.d("Mack Notas", "" + status);

                    if (getActivity() !=null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status) {
                                    statusMackNotas = "ON";
                                    arrayOfServidores.set(1, new StatusServidor("MackNotas", statusMackNotas));

                                    if (getActivity() == null) {
                                        return;
                                    }

                                    StatusServidorAdapter adapterStatusServidor = new StatusServidorAdapter(getActivity(), arrayOfServidores);
                                    // Attach the adapter to a ListView
                                    ListView listStatusServer = (ListView) v.findViewById(R.id.listServers);
                                    listStatusServer.setAdapter(adapterStatusServidor);
                                    setListViewHeightBasedOnChildren(listStatusServer);
                                } else {

                                }
                            }
                        });
                    }
                }
            }).start();


            //Verifica Push
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final boolean status = ajustesDAO.verificaPush();
                    Log.d("Push", "" + status);

                    if(getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status) {
                                    statusPush = "ON";
                                    arrayOfServidores.set(2, new StatusServidor("Push", statusPush));
                                    StatusServidorAdapter adapterStatusServidor = new StatusServidorAdapter(getActivity(), arrayOfServidores);
                                    // Attach the adapter to a ListView
                                    ListView listStatusServer = (ListView) v.findViewById(R.id.listServers);
                                    listStatusServer.setAdapter(adapterStatusServidor);
                                    setListViewHeightBasedOnChildren(listStatusServer);
                                } else {

                                }
                            }
                        });
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


    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.tab_ajustes, container, false);

        //Metodo da list de convidar amigos
        hasPushThread();

        //T.I.A. Conectado
        tiaMackenzie = (TextView) v.findViewById(R.id.edt_conectadoTIA);
        tiaMackenzie.setText("TIA Conectado "+ParseUser.getCurrentUser().getUsername().toString());
        tiaMackenzie.setEnabled(false);

        //Servidores
        arrayOfServidores = new ArrayList<>();
        arrayOfServidores.add(new StatusServidor("T.I.A.",statusTIA));
        arrayOfServidores.add(new StatusServidor("MackNotas",statusMackNotas));
        arrayOfServidores.add(new StatusServidor("Push",statusPush));
        // Create the adapter to convert the array to views
        StatusServidorAdapter adapterStatusServidor = new StatusServidorAdapter(getActivity(), arrayOfServidores);
        // Attach the adapter to a ListView
        ListView listStatusServer = (ListView) v.findViewById(R.id.listServers);
        listStatusServer.setAdapter(adapterStatusServidor);
        setListViewHeightBasedOnChildren(listStatusServer);




        //Criando Switchs
        SwitchDAO switchDAO = new SwitchDAO();
        // Construct the data source
        ArrayList<SwitchAndDescription> switchAndDescription = new ArrayList<>();
        switchAndDescription.add(new SwitchAndDescription("Mostrar a nota na notificação", switchDAO.getShowNotaStatus()));
        switchAndDescription.add(new SwitchAndDescription("Receber apenas uma vez a notificação", switchDAO.getPushOnlyOnce()));
        switchAndDescription.add(new SwitchAndDescription("Como posso ativar as notificações?", false));
        ArrayList<SwitchAndDescription> arrayOfSwitchAndDescription = switchAndDescription;
        // Create the adapter to convert the array to views
        adapter = new CustomSwitchAndDescriptionAdapter(getActivity(), arrayOfSwitchAndDescription);
        // Attach the adapter to a ListView
        final ListView listViewSwitch = (ListView) v.findViewById(R.id.switchs);
        listViewSwitch.setAdapter(adapter);
        setListViewHeightBasedOnChildrenInformacoes(listViewSwitch);
        listViewSwitch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Como ativar as notificações?");
                    alertDialog.setMessage("Atualmente o nosso sistema de envio de notificação está em fase de testes e restrito a um " +
                            "pequeno número de usuários.\n\nVocê pode, no entanto, ser convidado por algum usuário tester que possua a " +
                            "função ativa.\n\nDe tempos em tempos faremos divulgações em nossa página do Facebook convidando interessados" +
                            "em participar.\nFique ligado!");
                    alertDialog.setButton("Fechar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // funções aqui
                        }
                    });
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.show();
                }
            }
        });




        //Criando DialogList
        // Construct the data source
        ArrayList<ListDialogAdapter> listDialogAdapters = new ArrayList<>();
        listDialogAdapters.add(new ListDialogAdapter("Relatar um problema","Informe se algo não está funcionando corretamente"));
        listDialogAdapters.add(new ListDialogAdapter("Sugestões","Dê sugestões para que possamos melhorar o macknotas"));
        listDialogAdapters.add(new ListDialogAdapter("Dúvidas e outros assuntos","Tire suas dúvidas ou fale sobre qualquer assunto"));
        ArrayList<ListDialogAdapter> arrayListDialogAdapter = listDialogAdapters;
        // Create the adapter to convert the array to views
        CustomListDialogAdapter adapterDialog = new CustomListDialogAdapter(getActivity(), arrayListDialogAdapter);

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                getActivity());
        builderSingle.setIcon(R.drawable.ic_ajustes_red);
        builderSingle.setTitle("Fale com o MackNotas:");
        builderSingle.setNegativeButton("Fechar", null);
        builderSingle.setAdapter(adapterDialog,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), FaleComOMackNotas.class);
                        i.putExtra("idFeedBack", which);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().getApplicationContext().startActivity(i);
                    }
                });



        //INFORMAÇÕES
        final ArrayList<Informacoes> informacoes = new ArrayList<>();
        informacoes.add(new Informacoes("Desempenho Pessoal"));
        informacoes.add(new Informacoes("Fale com o MackNotas"));
        informacoes.add(new Informacoes("Avisos e novidades"));
        informacoes.add(new Informacoes("Sobre o MackNotas"));
        ArrayList<Informacoes> arrayOfInformacoes = informacoes;
        // Create the adapter to convert the array to views
        CustomInformacoesAdapter adapterInformacoes = new CustomInformacoesAdapter(getActivity(), arrayOfInformacoes);
        // Attach the adapter to a ListView
        ListView listViewInformacoes = (ListView) v.findViewById(R.id.informacoes);
        listViewInformacoes.setAdapter(adapterInformacoes);
        listViewInformacoes.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (informacoes.get(position).nome) {
                    case "Fale com o MackNotas":
                        if (isOnline()) {
                            builderSingle.show();
                        } else {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Ops, deu ruim!")
                                    .setContentText("Por favor, verifique sua conexão com a internet ou tente mais tarde.")
                                    .show();
                        }
                        break;
                    case "Avisos e novidades":
                        Intent i = new Intent(getActivity(), WebViewActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //getActivity().getApplicationContext().startActivity(i);

                        try {
                            getActivity().getApplicationContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
                            i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/641625832641666"));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().getApplicationContext().startActivity(i);
                        } catch (Exception e) {
                            i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MackNotas"));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().getApplicationContext().startActivity(i);

                        }

                        break;
                    case "Sobre o MackNotas":
                        i = new Intent(getActivity(), SobreActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().getApplicationContext().startActivity(i);
                        break;
                    case "Desempenho Pessoal":
                        if (isOnline()) {
                            Bundle bundle = getActivity().getIntent().getExtras();
                            i = new Intent(getActivity(), InformacaoPessoal.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("login", bundle.getString("login"));
                            i.putExtra("senha", bundle.getString("senha"));
                            i.putExtra("unidade", bundle.getString("unidade"));
                            getActivity().getApplicationContext().startActivity(i);
                        } else {
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Ops, deu ruim!")
                                    .setContentText("Por favor, verifique sua conexão com a internet ou tente mais tarde.")
                                    .show();
                        }
                        break;

                }
            }
        });
        setListViewHeightBasedOnChildren(listViewInformacoes);



        Log.d("FIM ON CREATE", "FIM");



        return v;
    }


    public void hasPushThread(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                boolean hasPush = false;

                try {
                    hasPush = ParseUser.getCurrentUser().fetch().getBoolean("hasPush");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final boolean finalHasPush = hasPush;

                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(finalHasPush) {
                            //Criando Switchs
                            SwitchDAO switchDAO = new SwitchDAO();
                            // Construct the data source
                            ArrayList<SwitchAndDescription> switchAndDescription = new ArrayList<>();
                            switchAndDescription.add(new SwitchAndDescription("Mostrar a nota na notificação", switchDAO.getShowNotaStatus()));
                            switchAndDescription.add(new SwitchAndDescription("Receber apenas uma vez a notificação", switchDAO.getPushOnlyOnce()));
                            switchAndDescription.add(new SwitchAndDescription("Convidar para receber notificação", false));
                            ArrayList<SwitchAndDescription> arrayOfSwitchAndDescription = switchAndDescription;
                            // Create the adapter to convert the array to views
                            adapter = new CustomSwitchAndDescriptionAdapter(getActivity(), arrayOfSwitchAndDescription);
                            // Attach the adapter to a ListView
                            final ListView listViewSwitch = (ListView) v.findViewById(R.id.switchs);
                            listViewSwitch.setAdapter(adapter);
                            setListViewHeightBasedOnChildrenInformacoes(listViewSwitch);
                            listViewSwitch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 2) {
                                        Intent i = new Intent(getActivity(), ConvidarAmigoActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getActivity().startActivity(i);
                                    }
                                }
                            });
                        }else{
                            //Criando Switchs
                            SwitchDAO switchDAO = new SwitchDAO();
                            // Construct the data source
                            ArrayList<SwitchAndDescription> switchAndDescription = new ArrayList<>();
                            switchAndDescription.add(new SwitchAndDescription("Mostrar a nota na notificação", switchDAO.getShowNotaStatus()));
                            switchAndDescription.add(new SwitchAndDescription("Receber apenas uma vez a notificação", switchDAO.getPushOnlyOnce()));
                            switchAndDescription.add(new SwitchAndDescription("Como posso ativar as notificações?", false));
                            ArrayList<SwitchAndDescription> arrayOfSwitchAndDescription = switchAndDescription;
                            // Create the adapter to convert the array to views
                            adapter = new CustomSwitchAndDescriptionAdapter(getActivity(), arrayOfSwitchAndDescription);
                            // Attach the adapter to a ListView
                            final ListView listViewSwitch = (ListView) v.findViewById(R.id.switchs);
                            listViewSwitch.setAdapter(adapter);
                            setListViewHeightBasedOnChildrenInformacoes(listViewSwitch);
                            listViewSwitch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 2) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog.setTitle("Como ativar as notificações?");
                                        alertDialog.setMessage("Atualmente o nosso sistema de envio de notificação está em fase de testes e restrito a um " +
                                                "pequeno número de usuários.\n\nVocê pode, no entanto, ser convidado por algum usuário tester que possua a " +
                                                "função ativa.\n\nDe tempos em tempos faremos divulgações em nossa página do Facebook convidando interessados " +
                                                "em participar.\nFique ligado!");
                                        alertDialog.setButton("Fechar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // funções aqui
                                            }
                                        });
                                        alertDialog.setIcon(R.mipmap.ic_launcher);
                                        alertDialog.show();
                                    }
                                }
                            });

                        }



                    }
                });




            }
        }).start();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_fragment_ajustes, menu);
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


    //Adaptação do método anterior para o switch
    public static void setListViewHeightBasedOnChildrenInformacoes(ListView listView) {
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
        Double number = 1.5;
        params.height = (int) ((totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)))*number);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}

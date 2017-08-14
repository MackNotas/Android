package giovannicornachini.macknotas.br.Fragments.Graduacao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import java.util.Calendar;

import giovannicornachini.macknotas.br.ActMain;
import giovannicornachini.macknotas.br.Entidades.Horario;
import giovannicornachini.macknotas.br.HorarioTest.SectionsPagerAdapter;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.dao.HorarioDAO;

/**
 * Created by GiovanniCornachini on 06/06/15.
 */
public class HorarioTab extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View view;
    private FragmentActivity myContext;
    private HorarioDAO horarioDAO = new HorarioDAO();
    private static final String ARG_TEXT = "ARG_TEXT";
    Horario horario;
    String response = "";
    String responseLocal = "";

    public static HorarioTab newInstance(String text) {
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);

        HorarioTab sampleFragment = new HorarioTab();
        sampleFragment.setArguments(args);

        return sampleFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        myContext.setTitle("Horários");

    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.tab_horario, container, false);

        //get Local JSON
        new Thread(new Runnable() {
            @Override
            public void run() {

                responseLocal = horarioDAO.getJson();
                if(!responseLocal.isEmpty()) {
                    horario = horarioDAO.getHorario(responseLocal);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(horario!=null && view!=null ) {
                                if(mViewPager == null){
                                    mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), horario);
                                    mViewPager = (ViewPager) myContext.findViewById(R.id.pager);
                                    mViewPager.setAdapter(mSectionsPagerAdapter);
                                    mViewPager.setCurrentItem(getCurrentDay());
                                }
                            }

                        }
                    });
                }
            }
        }).start();




        Bundle bundle = getActivity().getIntent().getExtras();
        final String login = bundle.getString("login");
        final String senha = bundle.getString("senha");
        final String unidade = bundle.getString("unidade");

        if(isOnline()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (response.isEmpty())
                        response = horarioDAO.getResponse(login, senha, unidade);
                    horario = horarioDAO.getHorario(response);


                        if(getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (response.isEmpty() && responseLocal.isEmpty()) {
                                        horario = horarioDAO.getHorarioVazio();

                                        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), horario);
                                        mViewPager = (ViewPager) view.findViewById(R.id.pager);
                                        mViewPager.setAdapter(mSectionsPagerAdapter);
                                        mViewPager.setCurrentItem(getCurrentDay());
                                    }

                                    if (horario == null || horario.getHoras().size() < 1) {
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


        return view;
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

    public int getCurrentDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return 0;

            case Calendar.TUESDAY:
                return 1;

            case Calendar.WEDNESDAY:
                return 2;

            case Calendar.THURSDAY:
                return 3;

            case Calendar.FRIDAY:
                return 4;

            case Calendar.SATURDAY:
                return 5;

            default:
                return 0;
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}

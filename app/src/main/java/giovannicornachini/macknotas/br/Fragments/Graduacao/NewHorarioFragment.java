package giovannicornachini.macknotas.br.Fragments.Graduacao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import giovannicornachini.macknotas.br.Entidades.Horario;
import giovannicornachini.macknotas.br.HorarioTest.FragmentHorario;
import giovannicornachini.macknotas.br.HorarioTest.SectionsPagerAdapter;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.dao.HorarioDAO;

/**
 * Created by giovannicornachini on 17/04/16.
 */
public class NewHorarioFragment extends Fragment {

    private FragmentTabHost mTabHost;
    View v;

    public NewHorarioFragment(){

    }

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private HorarioDAO horarioDAO = new HorarioDAO();
    Horario horario;

    String responseLocal = "";
    String response= "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((ActionBarActivity)getActivity()).setTitle("Horários");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.v = inflater.inflate(R.layout.tab_horario_new, container, false);
        setHasOptionsMenu(true);

        // TAB's
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);

        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        ((ActionBarActivity)getActivity()).getSupportActionBar().setElevation(0);

        Bundle bundle = getActivity().getIntent().getExtras();
        final String login = bundle.getString("login");
        final String senha = bundle.getString("senha");
        final String unidade = bundle.getString("unidade");

        // GET DATA
        //get Local JSON
        new Thread(new Runnable() {
            @Override
            public void run() {

                responseLocal = horarioDAO.getJson();
                if(responseLocal.isEmpty()) {


                }else{
                    horario = horarioDAO.getHorario(responseLocal);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(horario!=null && v!=null ) {
                                setupViewPager(viewPager);
                                tabLayout.setupWithViewPager(viewPager);
                            }

                        }
                    });

                }
            }
        }).start();

        //get Online JSON
        if(isOnline()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (response.isEmpty())
                        response = horarioDAO.getResponse(login, senha, unidade);
                    horario = horarioDAO.getHorario(response);

                    if(getActivity() == null){
                        return;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setupViewPager(viewPager);
                            tabLayout.setupWithViewPager(viewPager);

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


        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new FragmentHorario(0, this.horario), "Seg");
        adapter.addFragment(new FragmentHorario(1, this.horario), "Ter");
        adapter.addFragment(new FragmentHorario(2, this.horario), "Qua");
        adapter.addFragment(new FragmentHorario(3, this.horario), "Qui");
        adapter.addFragment(new FragmentHorario(4, this.horario), "Sex");
        adapter.addFragment(new FragmentHorario(5, this.horario), "Sáb");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getCurrentDay());
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}

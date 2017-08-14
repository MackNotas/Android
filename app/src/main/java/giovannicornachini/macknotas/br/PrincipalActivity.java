package giovannicornachini.macknotas.br;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarFragment;

import giovannicornachini.macknotas.br.Fragments.AjustesTab;
import giovannicornachini.macknotas.br.Fragments.Graduacao.AtvComplemTab;
import giovannicornachini.macknotas.br.Fragments.Graduacao.CalendarioTab;
import giovannicornachini.macknotas.br.Fragments.Graduacao.NewHorarioFragment;
import giovannicornachini.macknotas.br.Fragments.Graduacao.NotasTab;


public class PrincipalActivity extends ActionBarActivity {
    private FragmentTabHost mTabHost;
    private BottomBar mBottomBar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        String unidade= "";
        unidade = bundle.getString("unidade");
        if(unidade == null){
            ParseUser.logOut();
            finish();
        }

        /*
        Criando Tabs e setando suas referencias
         */
        ParseUser parseUser = ParseUser.getCurrentUser();
        String curso = parseUser.getString("curso");
        Log.d("curso", curso);

        setContentView(R.layout.activity_principal);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setFragmentItems(getSupportFragmentManager(), R.id.fragmentContainer,
                new BottomBarFragment(NotasTab.newInstance("Content for recents."), R.drawable.ic_book_default, "Matérias"),
                new BottomBarFragment(new NewHorarioFragment(), R.drawable.ic_horario_default, "Horários"),
                new BottomBarFragment(AtvComplemTab.newInstance("Content for nearby stuff."), R.drawable.ic_atv_complementares_default, "Ativ. Complem."),
                new BottomBarFragment(CalendarioTab.newInstance("Content for friends."), R.drawable.ic_calendar_default, "Calendário"),
                new BottomBarFragment(AjustesTab.newInstance("Content for food."), R.drawable.ic_ajustes_default, "Ajustes")
        );

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, "#E53935");
        mBottomBar.mapColorForTab(1, "#E53935");
        mBottomBar.mapColorForTab(2, "#E53935");
        mBottomBar.mapColorForTab(3, "#E53935");
        mBottomBar.mapColorForTab(4, "#E53935");

        ///SETANDO COR DO MENU
        try {
            android.support.v7.app.ActionBar bar = getSupportActionBar(); // or MainActivity.getInstance().getActionBar()
            bar.setBackgroundDrawable(new ColorDrawable(0xffbb0505));
            bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
            bar.setDisplayShowTitleEnabled(true);

        }catch (Exception e){

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_act_notas, menu);
        return true;
    }

    @Override
    public void onBackPressed(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }


    private View getTabIndicator(Context context, int title, int icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        ImageView iv = (ImageView) view.findViewById(R.id.imageView);
        iv.setImageResource(icon);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(title);
        return view;
    }



}

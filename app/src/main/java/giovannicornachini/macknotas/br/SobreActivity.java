package giovannicornachini.macknotas.br;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import giovannicornachini.macknotas.br.BuildConfig;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.adapter.CustomDesenvolvedorAdapter;
import giovannicornachini.macknotas.br.adapter.Desenvolvedor;


public class SobreActivity extends ActionBarActivity {
    TextView versao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        //Cor Menu
        try {
            android.support.v7.app.ActionBar bar = getSupportActionBar(); // or MainActivity.getInstance().getActionBar()
            bar.setBackgroundDrawable(new ColorDrawable(0xffbb0505));
            bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
            bar.setDisplayShowTitleEnabled(true);
        }catch (Exception e){

        }


        versao = (TextView) findViewById(R.id.txtVersao);
        versao.setText(BuildConfig.VERSION_NAME);


        //Criando list

        //INFORMAÇÕES

        final ArrayList<Desenvolvedor> arrayOfDevelopers = new ArrayList<>();
        arrayOfDevelopers.add(new Desenvolvedor("Caio Remedio"));
        arrayOfDevelopers.add(new Desenvolvedor("Giovanni Cornachini"));
        arrayOfDevelopers.add(new Desenvolvedor("Vinicius Augusto"));
        // Create the adapter to convert the array to views
        CustomDesenvolvedorAdapter adapterDevelopers = new CustomDesenvolvedorAdapter(getApplicationContext(), arrayOfDevelopers);
        // Attach the adapter to a ListView
        ListView listViewInformacoes = (ListView) findViewById(R.id.listDevelopers);
        listViewInformacoes.setAdapter(adapterDevelopers);

        listViewInformacoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("MackNotas",""+position);

                switch (position){
                    case 0:
                        //String uri = "facebook://facebook.com/info?user=100002830007263";
                        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(getOpenFacebookIntent(getApplicationContext(),"100002830007263"));
                        break;
                    case 1:
                        //uri = "facebook://facebook.com/info?user=1791263332";
                        //intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(getOpenFacebookIntent(getApplicationContext(),"1791263332"));
                        break;
//                    case 2:
//                        //uri = "facebook://facebook.com/info?user=100003052692932";
//                        //intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                        startActivity(getOpenFacebookIntent(getApplicationContext(),"100003052692932"));
//                        break;
                }
            }
        });





    }

    public static Intent getOpenFacebookIntent(Context context, String id) {
        try{
            // open in Facebook app
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            Log.d("MackNotas", id);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/"+id));
        } catch (Exception e) {
            // open in browser
            Log.d("MackNotas", id);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"+id));
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.act_fechar){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

package giovannicornachini.macknotas.br;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.dao.ConvidarAmigoDAO;


public class ConvidarAmigoActivity extends ActionBarActivity {

    TextView edtTiaAmigo;
    Button btnEnviarConvite;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convidar_amigo);
        try {
            android.support.v7.app.ActionBar bar = getSupportActionBar(); // or MainActivity.getInstance().getActionBar()
            bar.setBackgroundDrawable(new ColorDrawable(0xffbb0505));
            bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
            bar.setDisplayShowTitleEnabled(true);
        }catch (Exception e){

        }

        edtTiaAmigo = (TextView) findViewById(R.id.edtTiaAmigo);
        btnEnviarConvite = (Button) findViewById(R.id.btnEnviarConvite);
        list = (ListView) findViewById(R.id.listView);

        btnEnviarConvite.setEnabled(false);
        btnEnviarConvite.setBackgroundColor(getResources().getColor(R.color.button_red_block));

        final List<String> values = new ArrayList<>();
        values.add("Você não possui convites");
        values.add("Você ainda não convidou ninguém :(");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        list.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list);

        if (isOnline()) {
            final ConvidarAmigoDAO convidarAmigoDAO = new ConvidarAmigoDAO();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final int invites = convidarAmigoDAO.getInvitesDisponiveis();

                    ConvidarAmigoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (invites > 0) {
                                values.set(0, "Você possui " + invites + " convites!");
                                list.setAdapter(adapter);
                                setListViewHeightBasedOnChildren(list);
                                btnEnviarConvite.setEnabled(true);
                                btnEnviarConvite.setBackgroundColor(getResources().getColor(R.color.button_red_mack));
                            } else {
                                //erro get
                            }
                        }
                    });


                }
            }).start();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    final List<String> invited = convidarAmigoDAO.getUsersInvited();

                    ConvidarAmigoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (invited.size() > 0 && !invited.get(0).isEmpty()) {
                                values.remove(1);
                                for (int i = 0; i < invited.size(); i++) {
                                    values.add(i + 1, invited.get(i));
                                }
                                list.setAdapter(adapter);
                                setListViewHeightBasedOnChildren(list);
                            }
                        }
                    });
                }

            }).start();

            btnEnviarConvite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnEnviarConvite.setEnabled(false);
                    btnEnviarConvite.setBackgroundColor(getResources().getColor(R.color.button_red_block));
                    btnEnviarConvite.setText("Aguarde...");

                    Map<String, String> parametrosCloudCode = new HashMap<String, String>();
                    parametrosCloudCode.put("tia", edtTiaAmigo.getText().toString());
                    ParseCloud.callFunctionInBackground("inviteUserToPush", parametrosCloudCode,
                            new FunctionCallback<ParseObject>() {
                                @Override
                                public void done(final ParseObject object, com.parse.ParseException e) {
                                    if (e == null) {
                                        values.add(edtTiaAmigo.getText().toString());
                                        list.setAdapter(adapter);
                                        setListViewHeightBasedOnChildren(list);
                                        AlertDialog alertDialog = new AlertDialog.Builder(ConvidarAmigoActivity.this).create();
                                        alertDialog.setTitle("SUCESSO!");
                                        alertDialog.setMessage("Usuário convidado já está possibilitado de " +
                                                "receber notificações de notas!");
                                        alertDialog.setButton("Fechar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                        alertDialog.setIcon(R.mipmap.ic_launcher);
                                        alertDialog.show();
                                    } else {
                                        Log.d("MackNotas", "ERROR: " + e.getMessage());
                                        AlertDialog alertDialog = new AlertDialog.Builder(ConvidarAmigoActivity.this).create();
                                        alertDialog.setTitle("Ops, deu ruim!");
                                        alertDialog.setMessage(e.getMessage());
                                        alertDialog.setButton("Fechar", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        alertDialog.setIcon(R.mipmap.ic_launcher);
                                        alertDialog.show();

                                    }
                                    btnEnviarConvite.setEnabled(true);
                                    btnEnviarConvite.setBackgroundColor(getResources().getColor(R.color.button_red_mack));
                                    btnEnviarConvite.setText("ENVIAR CONVITE");
                                }
                            });
                }
            });

        }else{
            AlertDialog alertDialog = new AlertDialog.Builder(ConvidarAmigoActivity.this).create();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_convidar_amigo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.act_fechar){
            finish();
        }


        return super.onOptionsItemSelected(item);
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) ConvidarAmigoActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

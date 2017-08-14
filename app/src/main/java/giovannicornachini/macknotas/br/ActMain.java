package giovannicornachini.macknotas.br;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import giovannicornachini.macknotas.br.R;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import giovannicornachini.macknotas.br.dao.LoginDAO;


public class ActMain extends ActionBarActivity implements View.OnClickListener {

    private Button btnConectar;
    private EditText edtTia;
    private EditText edtPassword;
    private TextView logoMack;
    private Spinner spinner;
    String login;
    String senha;
    String unidade;
    private String resposta = "";

    ///Classes
    LoginDAO loginDAO = new LoginDAO();
    Intent it;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/Bitter-Bold.otf");
        logoMack = (TextView) findViewById(R.id.lbl_m);
        logoMack.setTypeface(type);

        edtTia = (EditText) findViewById(R.id.edtTia);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnConectar = (Button) findViewById(R.id.btnConectar);


        //IMPORTANTE
        btnConectar.setOnClickListener(this);


        //Spinner
        List<String> j = new ArrayList();
        j.add("São Paulo");
        j.add("Tamboré");
        j.add("Brasília");
        j.add("Campinas");

        spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, j);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //VERIFICA SE ESTA LOGADO, SE ESTIVER REDIRECIONA PARA A ACTNOTAS
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Log.d("MackNotas", "User Logado: " + currentUser.getUsername());
            it = new Intent(this, PrincipalActivity.class);
            it.putExtra("login", currentUser.getUsername());
            senha = loginDAO.readPasswordFromFile(ActMain.this);
            it.putExtra("senha", senha);
            unidade = loginDAO.readUnidadeFromFile(ActMain.this);
            it.putExtra("unidade", unidade);
            startActivity(it);
        }
    }

    @Override
    public void onClick(View v) {

        if (isOnline()) {

            it = new Intent(this, PrincipalActivity.class);

            btnConectar.setText("Aguarde...");
            btnConectar.setEnabled(false);
            Log.d("MackNotas", "Botao Conectar Acionado");

            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.button_red_mack));
            pDialog.setTitleText("Verificando...");
            pDialog.setCancelable(false);
            pDialog.show();

            if (ParseUser.getCurrentUser() != null) {
                ParseUser.logOut();
            }


            Log.d("MackNotas", "Antes Thread");
            login = edtTia.getText().toString();
            senha = edtPassword.getText().toString();


            Runnable r = new Runnable() {
                @Override
                public void run() {
                    resposta = "";
                    unidade = getUnidade();
                    loginDAO.realizarLogin(login, senha, unidade, ActMain.this);

                    Log.d("MackNotas", "AntesRunOn");
                    while (resposta.equals("")) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("MackNotas", "Dentro");

                            if (resposta.equals("true")) {
                                pDialog.cancel();
                                it.putExtra("login", login);
                                loginDAO.writePasswordToFile(senha, ActMain.this);
                                loginDAO.writeUnidadeToFile(unidade, ActMain.this);
                                it.putExtra("senha", senha);
                                it.putExtra("unidade", unidade);
                                startActivity(it);

                            } else {
                                pDialog.cancel();
                                new SweetAlertDialog(ActMain.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ops, deu ruim!")
                                        .setContentText("Seu TIA/senha estão incorretos ou o TIA está offline.\n" +
                                                "Por favor tente mais tarde.")
                                        .setConfirmText("Fechar")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.cancel();
                                            }
                                        })
                                        .show();
                            }

                            btnConectar.setText(R.string.lbl_entrar);
                            btnConectar.setEnabled(true);
                        }
                    });
                }
            };

            Thread t = new Thread(r);
            t.start();

        } else {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Ops, deu ruim!")
                    .setContentText("Por favor, verifique sua conexão com a internet ou tente mais tarde.")
                    .show();
        }


    }

    public String getUnidade(){
        String selectedItem = spinner.getSelectedItem().toString();

        switch(selectedItem){
            case "São Paulo":
                return "001";
            case "Tamboré":
                return "002";
            case "Brasília":
                return "003";
            case "Campinas":
                return "001";
            case "Recife":
                return "001";
            case "Rio de Janeiro":
                return "006";
            case "AEJA":
                return "010";
            case "UATU":
                return "011";
            default:
                return "001";
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_act_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

package giovannicornachini.macknotas.br;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import giovannicornachini.macknotas.br.dao.FaleComOMackNotasDAO;


public class FaleComOMackNotas extends ActionBarActivity {
    EditText edtNomeCompleto;
    EditText edtEmail;
    EditText edtMensagem;
    Button btnEnviar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fale_com_omack_notas);
        try {
            android.support.v7.app.ActionBar bar = getSupportActionBar(); // or MainActivity.getInstance().getActionBar()
            bar.setBackgroundDrawable(new ColorDrawable(0xffbb0505));
            bar.setDisplayShowTitleEnabled(false);  // required to force redraw, without, gray color
            bar.setDisplayShowTitleEnabled(true);
        }catch (Exception e){

        }

        edtNomeCompleto = (EditText) findViewById(R.id.edtNomeCompleto);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtMensagem = (EditText) findViewById(R.id.edtMensagem);
        btnEnviar = (Button) findViewById(R.id.btnEnviar);


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEnviar.setEnabled(false);
                if(edtNomeCompleto.getText().toString().isEmpty() ||
                        edtEmail.getText().toString().isEmpty() ||
                        edtMensagem.getText().toString().isEmpty()){
                    AlertDialog alertDialog = new AlertDialog.Builder(FaleComOMackNotas.this)
                            .create();
                    alertDialog.setTitle("Ops, deu ruim!");
                    alertDialog.setMessage("Por favor, preencha todos os campos.");
                    alertDialog.setButton("Fechar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // funções aqui
                        }
                    });
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.show();

                }else{
                    FaleComOMackNotasDAO faleComOMackNotasDAO = new FaleComOMackNotasDAO();
                    Bundle bundle = FaleComOMackNotas.this.getIntent().getExtras();
                    int id = bundle.getInt("idFeedBack");
                    faleComOMackNotasDAO.enviarFeedBack(id,edtMensagem.getText().toString(),
                            edtNomeCompleto.getText().toString(),edtEmail.getText().toString(),
                            FaleComOMackNotas.this);
                }
                btnEnviar.setEnabled(true);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fale_com_omack_notas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }
}

package giovannicornachini.macknotas.br.dao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import giovannicornachini.macknotas.br.FaleComOMackNotas;
import giovannicornachini.macknotas.br.R;

/**
 * Created by GiovanniCornachini on 28/06/15.
 */
public class FaleComOMackNotasDAO {


    public void enviarFeedBack(int id, String mensagem,String nomeCompleto, String email, final Context context){
        String versionName = "";
        try {
            versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ParseObject feedback = new ParseObject("Feedback");
        feedback.put("email", email);
        feedback.put("mensagem", mensagem);
        feedback.put("nomeCompleto", nomeCompleto);
        feedback.put("TIA", ParseUser.getCurrentUser().getUsername().toString());
        feedback.put("appVersion", versionName);
        feedback.put("modeloCelular", android.os.Build.MODEL);
        feedback.put("osVersion", String.valueOf(Build.VERSION.SDK_INT));
        feedback.put("plataforma", "Android");
        feedback.put("tipo", id);

        feedback.saveInBackground(new SaveCallback() {

            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Enviado!");
                    alertDialog.setMessage("Feedback enviado com sucesso");
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // funções aqui
                            ((FaleComOMackNotas)context).finish();
                        }
                    });
                    alertDialog.setIcon(R.mipmap.ic_launcher);
                    alertDialog.show();


                } else {

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setTitle("Ops, deu ruim!");
                    alertDialog.setMessage("Não foi possível enviar o Feedback.\nPor favor, verifique sua conexão com a internet\n");
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

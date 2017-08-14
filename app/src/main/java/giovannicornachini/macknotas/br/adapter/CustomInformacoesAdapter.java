package giovannicornachini.macknotas.br.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import giovannicornachini.macknotas.br.ConvidarAmigoActivity;
import giovannicornachini.macknotas.br.R;
import giovannicornachini.macknotas.br.SobreActivity;
import giovannicornachini.macknotas.br.WebViewActivity;

/**
 * Created by GiovanniCornachini on 16/05/15.
 */
public class CustomInformacoesAdapter extends ArrayAdapter<Informacoes>{

        public CustomInformacoesAdapter(Context context, ArrayList<Informacoes> informacoeses) {
            super(context, 0, informacoeses);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final Informacoes informacoes = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_informacoes, parent, false);
            }


            // Lookup view for data population
            final TextView descricaoInformacao = (TextView) convertView.findViewById(R.id.informacoes_description);
            // Populate the data into the template view using the data object
            descricaoInformacao.setText(informacoes.nome);
            setCores(informacoes.nome,descricaoInformacao);

            // Return the completed view to render on screen
            return convertView;
        }




    public void setCores(String nome, TextView descricaoInformacao){

        switch (nome){
            case "Fale com o MackNotas":
                descricaoInformacao.setTextColor(Color.BLUE);
                break;
            default:
                break;

        }

    }


    public static void sendEmail(Context context, String[] recipientList,
                                 String title, String subject) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipientList);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        //body
        String body="";
        try {

            String versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            body = "App version: "+versionName+"\n";
            body += "Phone: "+android.os.Build.MODEL+"\n";
            body += "Android : "+ Build.VERSION.SDK_INT+"\n";
            body += "---------------------------------"+"\n\n";
            body += "Mensagem: "+"\n";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(emailIntent, title));
    }




}

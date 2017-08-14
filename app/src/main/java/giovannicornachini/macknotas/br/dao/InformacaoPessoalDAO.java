package giovannicornachini.macknotas.br.dao;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import giovannicornachini.macknotas.br.Entidades.DesempenhoPessoal;

/**
 * Created by GiovanniCornachini on 05/07/15.
 */
public class InformacaoPessoalDAO {

    public static DesempenhoPessoal getDesempenhoPessoal(String login, String senha, String unidade) {
        DesempenhoPessoal desempenhoPessoal = new DesempenhoPessoal();
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();


        try {
            //Parametros timeout
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 8000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            int timeoutSocket = 8000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);


            DefaultHttpClient client = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost("https://tia-webservice.herokuapp.com/tiaLogin_v2.php");
            StringEntity params =new StringEntity("{\"userTia\":\""+login+"\",\"userPass\":\""+senha+"\""
                    +",\"userUnidade\":\""+unidade+"\",\"tipo\":\"7\"}");
            httpPost.setEntity(params);
            httpPost.setHeader("Contenttype",
                    "application/json");

            HttpResponse execute = null;
            execute = client.execute(httpPost);
            InputStream content = null;
            content = execute.getEntity().getContent();


            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(content));
            String s = "";
            String response = "";
            while ((s = buffer.readLine()) != null)
                response += s;


            JSONObject obj = new JSONObject(response);

            desempenhoPessoal.setMediaGeral((float) obj.getDouble("mediageral"));
            JSONArray semestres = obj.getJSONArray("semestre");
            JSONArray notas = obj.getJSONArray("semestrenotas");

            for(int j = 0; j<semestres.length(); j++){
                labels.add(semestres.getString(j));
                entries.add(new Entry((float) notas.getDouble(j),j));
            }


            desempenhoPessoal.setLabels(labels);
            desempenhoPessoal.setEntries(entries);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


        return desempenhoPessoal;
    }
}

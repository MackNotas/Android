package giovannicornachini.macknotas.br.dao;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.util.List;

import giovannicornachini.macknotas.br.Entidades.Calendario;

/**
 * Created by GiovanniCornachini on 03/06/15.
 */
public class CalendarioDAO {

    List<Calendario> listCalendario;

    public String getResponse(String login, String senha, String unidade){

        String response = "";

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
                    +",\"userUnidade\":\""+unidade+"\",\"tipo\":\"6\"}");
            httpPost.setEntity(params);
            httpPost.setHeader("Contenttype",
                    "application/json");

            HttpResponse execute = null;
            execute = client.execute(httpPost);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(content));
            String s = "";
            response = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }
            //fim obtendo calendario

            Log.d("MackNotas","Fim getCalendario: "+response);

            if(!response.isEmpty()){
                saveJson(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }

    public List<List<Calendario>> getCalendario(String response){
        List<List<Calendario>> totalListcalendario = new ArrayList<>();
        try {
            JSONArray jsonarray = null;

            try {
                jsonarray = new JSONArray(response);
            }catch(Exception ex){
                return totalListcalendario;
            }

            for(int i=0; i<jsonarray.length(); i++){

                JSONArray faltas = jsonarray.getJSONArray(i);
                listCalendario = new ArrayList<>();
                if(faltas.length()>0) {
                    for(int j=0; j<faltas.length();j++){
                        JSONObject obj = faltas.getJSONObject(j);
                        Calendario calendario = new Calendario();
                        calendario.setMateria(obj.getString("materia"));
                        calendario.setDia(obj.getString("dia"));
                        calendario.setData(obj.getString("data"));
                        calendario.setTipoProva(obj.getString("tipo"));
                        calendario.setDiaSemana(obj.getString("diaSemana"));

                        listCalendario.add(calendario);
                    }
                }
                totalListcalendario.add(listCalendario);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return totalListcalendario;
    }


    public void saveJson(final String response){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Calendario");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    if (parseObjects.size() > 0) {
                        ParseObject parseObject = parseObjects.get(parseObjects.size() - 1);
                        parseObject.put("json", response);
                        parseObject.put("user", ParseUser.getCurrentUser());
                        parseObject.pinInBackground();
                    } else {
                        ParseObject parseObject = new ParseObject("Calendario");
                        parseObject.put("json", response);
                        parseObject.put("user", ParseUser.getCurrentUser());
                        parseObject.pinInBackground();
                    }

                } else {

                }

            }
        });
    }

    public String getJson(){
        String response = "";
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Calendario");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.fromLocalDatastore();
        try {
            List<ParseObject> po = query.find();
            try {
                response = po.get(0).getString("json");
            }catch(Exception ex){
                return response;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return response;
    }
}

package giovannicornachini.macknotas.br.dao;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import giovannicornachini.macknotas.br.Entidades.AtvDeferidas;
import giovannicornachini.macknotas.br.Entidades.AtvTotalHoras;

/**
 * Created by GiovanniCornachini on 02/08/15.
 */
public class AtvComplemDAO {

    public List<AtvDeferidas> getAtvComplemDeferidas(String response){
        List<AtvDeferidas> listAtvDeferidas = new ArrayList<>();
        AtvDeferidas atvDeferidas;

        try {

            JSONObject obj = new JSONObject(response);
            JSONArray atvDeferidasArray = obj.getJSONArray("atDeferidas");

            for(int i = 0; i < atvDeferidasArray.length(); i++){
                atvDeferidas = new AtvDeferidas();
                atvDeferidas.setAssunto(atvDeferidasArray.getJSONObject(i).getString("assunto"));
                atvDeferidas.setAnoSemestre(atvDeferidasArray.getJSONObject(i).getString("anoSemestre"));
                atvDeferidas.setData(atvDeferidasArray.getJSONObject(i).getString("data"));
                atvDeferidas.setHoras(atvDeferidasArray.getJSONObject(i).getString("horas"));
                atvDeferidas.setModalidade(atvDeferidasArray.getJSONObject(i).getString("modalidade"));
                atvDeferidas.setTipo(atvDeferidasArray.getJSONObject(i).getString("tipo"));

                listAtvDeferidas.add(atvDeferidas);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listAtvDeferidas;

    }

    public AtvTotalHoras getTotalHoras(String response){
        AtvTotalHoras atvTotalHoras = new AtvTotalHoras();



        try {

            JSONObject obj = new JSONObject(response);
            JSONObject totalHoras = obj.getJSONObject("totalHoras");

            atvTotalHoras.setEnsino(totalHoras.getString("atEnsino"));
            atvTotalHoras.setExcedentes(totalHoras.getString("excedentes"));
            atvTotalHoras.setExtensao(totalHoras.getString("atExtensao"));
            atvTotalHoras.setPesquisa(totalHoras.getString("atPesquisa"));
            atvTotalHoras.setTotal(totalHoras.getString("total"));


        }catch (JSONException e) {
            e.printStackTrace();
        }

        return atvTotalHoras;
    }

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
            StringEntity params = new StringEntity("{\"userTia\":\"" + login + "\",\"userPass\":\"" + senha + "\""
                    + ",\"userUnidade\":\""+unidade+"\",\"tipo\":\"5\"}");
            httpPost.setEntity(params);
            httpPost.setHeader("Contenttype",
                    "application/json");

            HttpResponse execute = null;
            execute = client.execute(httpPost);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(content));
            String s = "";

            while ((s = buffer.readLine()) != null) {
                response += s;
            }

            Log.d("MackNotas", "Atv. Comp. Atualizadas"+response);
        }catch (ClientProtocolException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!response.isEmpty()){
            saveJson(response);
        }else{
            response = getJson();
        }


        return response;
    }


    public void saveJson(final String response){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AtvComplem");
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
                        ParseObject parseObject = new ParseObject("AtvComplem");
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AtvComplem");
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

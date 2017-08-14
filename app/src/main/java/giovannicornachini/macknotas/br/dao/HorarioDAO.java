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

import giovannicornachini.macknotas.br.Entidades.Horario;

/**
 * Created by GiovanniCornachini on 06/06/15.
 */
public class HorarioDAO {


        Horario horarios;

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
                        +",\"userUnidade\":\""+unidade+"\",\"tipo\":\"2\"}");
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


                Log.d("MackNotas", "Pegar Horarios: " + response);





            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if (!response.isEmpty()){
                final String finalResponse = response;
                saveJson(finalResponse);
            }

            return response;
        }


    public Horario getHorario(String response){
        horarios = new Horario();
        try{
            JSONObject obj = new JSONObject(response);
            JSONArray gradeArray = obj.getJSONArray("grade");
            List<String> horas = new ArrayList();
            List<String> materiasList;
            List<List<String>> materiasTotal = new ArrayList<>();

            //horas
            for(int i = 0; i < gradeArray.getJSONObject(0).getJSONArray("horas").length(); i++){
                horas.add(gradeArray.getJSONObject(0).getJSONArray("horas").getString(i));
            }

            //materias
            for(int i = 0; i < gradeArray.length(); i++){
                materiasList = new ArrayList<>();
                for(int j=0; j<gradeArray.getJSONObject(i).getJSONArray("materias").length();j++) {
                    materiasList.add(gradeArray.getJSONObject(i).getJSONArray("materias").getString(j));
                }
                materiasTotal.add(materiasList);
            }

            horarios.setHoras(horas);
            horarios.setMaterias(materiasTotal);

        }catch (Exception ex){

        }

        return horarios;
    }

    public Horario getHorarioVazio(){
        horarios = new Horario();

        final String response;

        try {

            response = "{\"materias\":[[\"Você não tem nenhum horário disponível\"],[\"Você não tem nenhum horário disponível\"],[\"Você não tem nenhum horário disponível\"],[\"Você não tem nenhum horário disponível\"],[\"Você não tem nenhum horário disponível\"],[\"Você não tem nenhum horário disponível\"]],\"horas\":[\"00:00\"]}";
            // = new JSONArray(response);

                JSONObject obj = new JSONObject(response);
                JSONArray jsonarray = obj.getJSONArray("horas");
                try {
                    List<String> horas = new ArrayList();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        horas.add(jsonarray.getString(i));
                    }

                    horarios.setHoras(horas);
                } catch (Exception ex) {

                }

                jsonarray = obj.getJSONArray("materias");
                List<List<String>> materiasTotal = new ArrayList<>();
                List<String> materiasList;
                JSONArray materias;

                for (int i = 0; i < jsonarray.length(); i++) {
                    materias = jsonarray.getJSONArray(i);
                    materiasList = new ArrayList<>();

                    for (int j = 0; j < materias.length(); j++) {
                        materiasList.add(materias.getString(j));
                    }
                    materiasTotal.add(materiasList);

                }
                horarios.setMaterias(materiasTotal);

            }

            catch(JSONException e) {
                e.printStackTrace();
            }


            return horarios;
        }


    public void saveJson(final String response){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Horario");
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
                        ParseObject parseObject = new ParseObject("Horario");
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Horario");
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
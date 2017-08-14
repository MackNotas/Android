package giovannicornachini.macknotas.br.dao;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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

import giovannicornachini.macknotas.br.Entidades.Materia;

/**
 * Created by GiovanniCornachini on 17/04/15.
 */
public class NotasDAO {


    private String response;

    public List<Materia> buscarNotas(String login, String senha, String unidade){
        List<Materia> listMateria = new ArrayList();

        try {
            Log.d("MackNotas", "Iniciando thread");

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
                    +",\"userUnidade\":\""+unidade+"\",\"tipo\":\"1\"}");
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
            //fim obtendo notas

            JSONArray jsonarray = new JSONArray(response);
            JSONArray notasJson;
            Materia materia;
            for(int i=0; i<jsonarray.length(); i++){
                JSONObject obj = jsonarray.getJSONObject(i);

                materia = new Materia();
                materia.setFormula(obj.getString("formulas"));
                materia.setNome(obj.getString("nome").toUpperCase());
                notasJson = obj.getJSONArray("notas");


                List<String> list = new ArrayList<>();
                for(int j=0;j<notasJson.length();j++){
                    list.add(notasJson.getString(j));
                }

                materia.setNotas(list);


                listMateria.add(materia);

            }



            Log.d("MackNotas","Fim Thread: "+response);

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //se recuperar notas, salva em background
        if(listMateria.size()>0){

            salvarNotas();

        }


        return listMateria;
    }


    public void salvarNotas(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Notas");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    List<String> listJsonarray = null;
                    JSONObject object;
                    JSONArray arrayJson= null;
                    final JSONArray arrayJsonFinal= new JSONArray();
                    try {
                        JSONArray jsonarray = new JSONArray(response);


                        for(int i=0; i<jsonarray.length(); i++){
                            JSONObject obj = jsonarray.getJSONObject(i);

                            String nome = obj.getString("nome");
                            //String formulas = obj.getString("formulas");
                            arrayJson = obj.getJSONArray("notas");

                            //substituindo notas por X
                            JSONObject[] jsons = new JSONObject[arrayJson.length()];
                            for (int j = 0; j < jsons.length; j++){
                                if(!arrayJson.get(j).equals(""))
                                    arrayJson.put(j,"X");
                                //Log.d("MackNotas","Substituindo notas: ");

                            }

                            obj = new JSONObject();
                            obj.put("notas",arrayJson);
                            obj.put("nome",nome);


                            arrayJsonFinal.put(obj);

                        }



                        } catch (JSONException e1) {
                        e1.printStackTrace();
                        }

                    //String notaFinal = arrayJsonFinal;
                    if(parseObjects.size()>0) {
                        Log.d("Notas", "Retrieved " + parseObjects.get(parseObjects.size() - 1).get("nota"));
                        Log.d("Notas", "Retrieved SIZE " + parseObjects.size());

                        final ParseObject parseObject = parseObjects.get(parseObjects.size() - 1);


                        if (ParseUser.getCurrentUser().getBoolean("hasPush")) {
                                parseObject.put("notaJson", arrayJsonFinal.toString());
                                parseObject.put("user", ParseUser.getCurrentUser());
                                parseObject.saveInBackground();
                        }



                        ParseQuery<ParseObject> query = ParseQuery.getQuery("NotasPin");
                        query.whereEqualTo("user", ParseUser.getCurrentUser());
                        query.fromLocalDatastore();
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if(e==null){
                                    if(parseObjects.size()>0){
                                    ParseObject parseObject = parseObjects.get(parseObjects.size() - 1);
                                    parseObject.put("nota", response);
                                    parseObject.put("user", ParseUser.getCurrentUser());
                                    parseObject.pinInBackground();
                                    }else{
                                        ParseObject parseObject = new ParseObject("NotasPin");
                                        parseObject.put("nota", response);
                                        parseObject.put("user", ParseUser.getCurrentUser());
                                        parseObject.pinInBackground();

                                    }

                                }else{

                                }

                            }
                        });


                    }else{

                        ParseObject parseObject = new ParseObject("NotasPin");
                        parseObject.put("nota", response);
                        parseObject.put("user", ParseUser.getCurrentUser());
                        parseObject.pinInBackground();


                        //se hasPush, salva no Parse
                        if(ParseUser.getCurrentUser().getBoolean("hasPush")) {
                            parseObject = new ParseObject("Notas");
                            parseObject.put("notaJson", arrayJsonFinal.toString());
                            parseObject.put("user", ParseUser.getCurrentUser());
                            parseObject.pinInBackground();
                            parseObject.saveInBackground();
                        }
                    }

                } else {
                    Log.d("MackNotas","Nenhum item de notas foi encontrado no pin");
                }
            }
        });
    }


}

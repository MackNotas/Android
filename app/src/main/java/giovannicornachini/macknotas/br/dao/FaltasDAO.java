package giovannicornachini.macknotas.br.dao;

import android.util.Log;

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

import giovannicornachini.macknotas.br.Entidades.Faltas;

/**
 * Created by GiovanniCornachini on 08/05/15.
 */
public class FaltasDAO {


    public List<Faltas> getFaltas(final String login, final String senha){

                String response;
                List listFaltas = new ArrayList();

                try {
                    Log.d("MackNotas", "Iniciando thread busca Faltas");

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
                            +",\"userUnidade\":\"001\",\"tipo\":\"3\"}");
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
                    //fim obtendo faltas




                    try {
                        JSONArray jsonarray = new JSONArray(response);
                        Faltas faltas;
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);

                            faltas = new Faltas();
                            faltas.setFaltasAtuais(obj.getString("faltas"));
                            faltas.setFaltasPermitidas(obj.getString("permitido"));
                            faltas.setFaltasPorcentagem(obj.getString("porcentagem"));
                            faltas.setFaltasUltima(obj.getString("ultimaData"));

                            listFaltas.add(faltas);

                        }
                    }catch (JSONException e){
                        JSONObject isInvalid = new JSONObject(response);
                    }


                    Log.d("MackNotas","Fim Thread: "+response);

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return listFaltas;


    }

    public List<Faltas> faltasVazia(int quantMaterias){
        List listFaltas = new ArrayList();
        for(int i = 0; i<quantMaterias; i++){
            listFaltas.add(new Faltas("","","",""));
        }
        return listFaltas;
    }

}

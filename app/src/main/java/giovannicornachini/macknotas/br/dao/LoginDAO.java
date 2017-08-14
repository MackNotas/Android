package giovannicornachini.macknotas.br.dao;

import android.content.Context;
import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import giovannicornachini.macknotas.br.ActMain;


/**
 * Created by GiovanniCornachini on 17/04/15.
 */
public class LoginDAO  {

    public void realizarLogin(final String login, final String senha, final String unidade ,final ActMain actMain){


        Map<String, String> parametrosCloudCode = new HashMap<String, String>();
        parametrosCloudCode.put("userTia", login);
        parametrosCloudCode.put("userPass", senha);
        parametrosCloudCode.put("userUnidade", unidade);
        //verifica senha e login no TIA via webservice
        ParseCloud.callFunctionInBackground("verifyTiaBeforeSignUpWithTiaAndPass", parametrosCloudCode, new FunctionCallback<HashMap>() {


            @Override
            public void done(HashMap hashMap, com.parse.ParseException e) {
                if (e == null) {
                    if((boolean)hashMap.get("login")==true){
                        Log.d("MackNotas", "Login: login no tia OK");

                        //tenta login no PARSE
                        ParseUser.logInInBackground(login, senha, new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, com.parse.ParseException e) {
                                if (parseUser != null) {
                                    try {
                                        //cadastrar channel
                                        ParsePush.subscribeInBackground("t" + ParseUser.getCurrentUser().getUsername().toString());
                                        getPessoalData(login, senha, unidade);
                                    }catch(Exception ex){

                                    }
                                    actMain.setResposta("true");
                                    Log.d("MackNotas", "Sucesso: login parse + tia OK");

                                } else {
                                    Log.d("MackNotas", "Erro: login Tia ok, login Parse ERROR");

                                    //cria novo user e cadastra
                                    ParseUser user = new ParseUser();
                                    user.setUsername(login);
                                    user.setPassword(senha);
                                    user.signUpInBackground(new SignUpCallback() {

                                        @Override
                                        public void done(com.parse.ParseException e) {
                                            if (e == null) {
                                                Log.d("MackNotas", "Cadastro: Cadastrado no Parse com sucesso");

                                                //Criptografa senha
                                                Map<String, String> parametrosCloudCode = new HashMap<String, String>();
                                                parametrosCloudCode.put("passToBeEncrypted", senha);
                                                //encriptografa a senha cloud code
                                                ParseCloud.callFunctionInBackground("encryptPassWithPFUserAtSignUp", parametrosCloudCode, new FunctionCallback<JSONObject>() {

                                                    @Override
                                                    public void done(JSONObject map, com.parse.ParseException e) {
                                                        if (e == null) {
                                                            try {
                                                                ///cadastrar channel
                                                                ParsePush.subscribeInBackground("t" + ParseUser.getCurrentUser().getUsername().toString());
                                                            } catch (Exception ex) {

                                                            }
                                                            actMain.setResposta("true");
                                                            ParseUser user = ParseUser.getCurrentUser();
                                                            user.put("showNota", true);
                                                            user.put("pushOnlyOnce", true);
                                                            user.put("hasPush", false);
                                                            user.put("unidade", unidade);
                                                            user.saveInBackground();
                                                            Log.d("MackNotas", "Fim login com Sucesso!");
                                                        } else {
                                                            actMain.setResposta("false");
                                                            Log.d("MackNotas", "Erro: " + e.toString());

                                                        }
                                                    }
                                                });


                                            } else {
                                                Log.d("MackNotas", "Erro: cadastrarLoginParse" + e.toString());

                                                Log.d("MackNotas", "Mudando senha no Parse...");
                                                Map<String, String> parametrosCloudCode = new HashMap<String, String>();
                                                parametrosCloudCode.put("userTia", login);
                                                parametrosCloudCode.put("userPass", senha);
                                                parametrosCloudCode.put("userUnidade", unidade);
                                                //muda a senha de login
                                                ParseCloud.callFunctionInBackground("updateRegisteredUserPasswordWithTiaAndPass", parametrosCloudCode, new FunctionCallback<String>() {


                                                    @Override
                                                    public void done(String s, com.parse.ParseException e) {
                                                        if (s.equals("true")) {

                                                            //faz login
                                                            ParseUser.logInInBackground(login, senha, new LogInCallback() {

                                                                @Override
                                                                public void done(ParseUser parseUser, com.parse.ParseException e) {
                                                                    if (parseUser != null) {
                                                                        try {
                                                                            ///cadastrar channel
                                                                            ParsePush.subscribeInBackground("t" + ParseUser.getCurrentUser().getUsername().toString());
                                                                        } catch (Exception ex) {

                                                                        }
                                                                        actMain.setResposta("true");
                                                                        Log.d("MackNotas", "Sucesso: login");

                                                                    } else {
                                                                        actMain.setResposta("false");
                                                                        Log.d("MackNotas", "Erro: " + e.toString());
                                                                    }
                                                                }
                                                            });
                                                            actMain.setResposta("true");
                                                            Log.d("MackNotas", "Fim login com Sucesso!");
                                                        }

                                                    }
                                                });


                                            }
                                        }
                                    });






                                }
                            }
                        });





                    }else{
                        actMain.setResposta("false");
                        Log.d("MackNotas", "Login: login no tia não Válido");
                    }

                } else {
                    actMain.setResposta("false");
                    Log.d("MackNotas", "Erro: " + e.toString());
                }
            }
        });

        Log.d("MackNotas", "FIM METODO LOGIN");
    }


    /*
     *Método utilizado para a coleta de dados
     */
    public void getPessoalData(final String login, final String senha, final String unidade){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String nome;
                String curso;
                String response;

                try {

                    if(unidade == null){
                        return;
                    }

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
                            +",\"userUnidade\":\""+ unidade +"\",\"tipo\":\"4\"}");
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

                    JSONObject jsonObject = new JSONObject(response);
                    nome = jsonObject.getString("nomeCompleto");
                    curso = jsonObject.getString("curso");

                    ParseUser parseUser = ParseUser.getCurrentUser();
                    parseUser.setEmail(login+"@mackenzista.com.br");
                    parseUser.put("curso", curso);
                    parseUser.put("nome",nome);
                    parseUser.put("unidade", unidade);
                    parseUser.saveInBackground();


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
            }).start();
    }


    public void writePasswordToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("configMack", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void writeUnidadeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("unidadeMack", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public String readPasswordFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("configMack");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public String readUnidadeFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("unidadeMack");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }






}



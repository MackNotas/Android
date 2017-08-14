package giovannicornachini.macknotas.br.dao;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by GiovanniCornachini on 10/05/15.
 */
public class AjustesDAO {

    String resposta;

    public boolean verificaTIA() {


        int code=0;
        try {
            URL u = new URL("https://www3.mackenzie.com.br/tia/index.php");
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setConnectTimeout(8000);
            huc.setRequestMethod("GET");
            huc.connect();
            code = huc.getResponseCode();
        }catch (IOException e){

        }
        if(code==200){
            return true;
        }else {
            return false;
        }
    }

    public boolean verificaMackNotas(){

        int code=0;
        try {
            URL u = new URL("https://tia-webservice.herokuapp.com/tiaLogin.php");
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setConnectTimeout(8000);
            huc.setRequestMethod("GET");
            huc.connect();
            code = huc.getResponseCode();
        }catch (IOException e){

        }

        if(code==200){
            return true;
        }else {
            return false;
        }
    }


    public boolean verificaPush(){

        int code=0;
        try {
            URL u = new URL("http://tia-pushwebservice.herokuapp.com/tiaPushJob.php");
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setConnectTimeout(8000);
            huc.setRequestMethod("GET");
            huc.connect();
            code = huc.getResponseCode();
        }catch (IOException e){

        }

        if(code==200){
            return true;
        }else {
            return false;
        }
    }

    public boolean hasPush(){
        final ParseUser user = ParseUser.getCurrentUser();
        boolean hasPush = user.getBoolean("hasPush");

        return hasPush;
    }

}

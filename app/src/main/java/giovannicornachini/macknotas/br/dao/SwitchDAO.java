package giovannicornachini.macknotas.br.dao;

import com.parse.ParseUser;

/**
 * Created by GiovanniCornachini on 16/05/15.
 */
public class SwitchDAO {


    public boolean getShowNotaStatus(){
        ParseUser user = ParseUser.getCurrentUser();

        return user.getBoolean("showNota");
    }

    public boolean getPushOnlyOnce(){
        ParseUser user = ParseUser.getCurrentUser();

        return user.getBoolean("pushOnlyOnce");
    }

    public void setShowNotaStatus(boolean status){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("showNota",status);
        user.saveInBackground();

    }

    public void setPushOnlyOnce(boolean status){
        ParseUser user = ParseUser.getCurrentUser();
        user.put("pushOnlyOnce",status);
        user.saveInBackground();
    }
}

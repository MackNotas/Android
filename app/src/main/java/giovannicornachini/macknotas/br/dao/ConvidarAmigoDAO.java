package giovannicornachini.macknotas.br.dao;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GiovanniCornachini on 28/06/15.
 */
public class ConvidarAmigoDAO {

    public int getInvitesDisponiveis() {
        final int[] invites = new int[1];
        invites[0] = -1;
        final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Invite");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> followList, ParseException e) {
                if (e == null) {
                    try {
                        invites[0] = followList.get(0).getInt("invitesDisponiveis");
                    }catch(Exception ex){
                        invites[0] =0;
                        Log.d("MackNotas", "Error: getInvitesDisponiveis()");
                        return;
                    }
                    return;
                } else {
                    invites[0] =0;
                    Log.d("MackNotas", "Error: getInvitesDisponiveis()");
                    return;
                }
            }

        });
        while (invites[0]==-1){
            //wait
        }
        return invites[0];
    }

    public List<String> getUsersInvited(){
        final int[] count = {-1};
        final List<String> invited = new ArrayList<>();

        final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Invite");
        query.whereEqualTo("user", user);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> invitedList, ParseException e) {
                if (e == null) {
                    JSONArray jsonArray;
                    try {
                        jsonArray = invitedList.get(0).getJSONArray("usersInvited");
                    }catch(Exception error){
                        count[0] =0;
                        Log.d("MackNotas", "Error: getUsersInvited. "+error);
                        return;

                    }

                    for(int i =0; i<jsonArray.length();i++){
                        try {
                            String j = jsonArray.getString(0);
                            invited.add(j);
                        } catch (JSONException e1) {
                            //
                        }
                    }

                    count[0] = 0;
                    return;
                } else {
                    count[0] =0;
                    Log.d("MackNotas", "Error: getUsersInvited. "+e);
                    return;
                }
            }

        });
        while (count[0] ==-1){
            //wait
        }

        return invited;
    }
}

package giovannicornachini.macknotas.br;

/**
 * Created by GiovanniCornachini on 12/04/15.
 */

import android.provider.Settings;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import io.fabric.sdk.android.Fabric;
import java.util.List;

public class Application extends android.app.Application{
    public static GoogleAnalytics analytics;
    public static Tracker tracker;


    public void onCreate() {
        try {
            Fabric.with(this, new Crashlytics());
            Crashlytics.getInstance().setDebugMode(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "KEY", "KEY");


        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        if(ParseUser.getCurrentUser()!=null) {
            installation.put("user", ParseUser.getCurrentUser());
            Crashlytics.setUserIdentifier(ParseUser.getCurrentUser().getUsername().toString());

            List<String> subscribedChannels = ParseInstallation.getCurrentInstallation().getList("channels");

            if(subscribedChannels!=null) {

                if (subscribedChannels.size() == 1 && subscribedChannels != null) {
                    if (!subscribedChannels.get(0).equals("t" + ParseUser.getCurrentUser().getUsername())) {
                        ParsePush.unsubscribeInBackground(subscribedChannels.get(0));
                        ParsePush.subscribeInBackground("t" + ParseUser.getCurrentUser().getUsername().toString());
                    }

                } else if (subscribedChannels.size() > 1 && subscribedChannels != null) {
                    if (subscribedChannels.size() > 1) {
                        int j = 0;
                        for (int i = 0; i < subscribedChannels.size(); i++) {
                            if (subscribedChannels.get(i).equals("t" + ParseUser.getCurrentUser().getUsername())) {
                                j++;
                            } else {

                                ParsePush.unsubscribeInBackground(subscribedChannels.get(i));
                            }
                        }
                        if (j == 0) {
                            ParsePush.subscribeInBackground("t" + ParseUser.getCurrentUser().getUsername().toString());
                        }

                    }
                }
            }else{
                ParsePush.subscribeInBackground("t" + ParseUser.getCurrentUser().getUsername().toString());
            }

            ParseUser.enableRevocableSessionInBackground();

        }
        installation.saveInBackground();

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("KEY"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

    }
}
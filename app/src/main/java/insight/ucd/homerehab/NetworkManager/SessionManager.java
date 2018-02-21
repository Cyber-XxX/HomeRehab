package insight.ucd.homerehab.NetworkManager;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;

import insight.ucd.homerehab.R;
import insight.ucd.homerehab.Static.Variables;

/**
 * Created by Cyber-X on 06/02/2018.
 */

public class SessionManager {

    private static SessionManager instance = null;
    private String response = "";
    private String session_id = null;

    private SessionManager(){
    }

    public static SessionManager getInstance(){
        if(instance == null){
            Log.d("Creating_instance", "Creating instance");
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * This method is used to do the login. It call a method from ConnectionManager to do the request
     *
     * @param user_id the user id of the user that wants do the login
     * @param context
     * @return a string with response
     * @throws JSONException
     * @throws IOException
     */
    public String do_login(String user_id, final Context context) throws JSONException, IOException {

        String response = "";
        ConnectionManager conn = new ConnectionManager();
        HashMap<String, String> params = new HashMap<>();
        params.put("id", user_id);
        response = conn.do_get(Variables.url_login, params);
        return response;
    }

    public void setSessionId(String new_session_id){
        session_id = new_session_id;
    }

    public String getSessionId(){
        return session_id;
    }

    public void setResponse(String resp){
        response = resp;
        Log.d("Setting_response", "Response setted " + getResponse());
    }

    public String getResponse(){
        return response;
    }
}

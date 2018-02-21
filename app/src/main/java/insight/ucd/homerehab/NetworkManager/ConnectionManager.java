package insight.ucd.homerehab.NetworkManager;

import android.content.Context;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import insight.ucd.homerehab.R;
import insight.ucd.homerehab.Static.Variables;

/**
 * Created by Cyber-X on 20/02/2018.
 */

public class ConnectionManager {

    private String response;

    /**
     * Method to do a get request
     *
     * @param url_link is the link that reach the server
     * @param list_params parameters' list with necessaries parameters inside a GEt request
     * @return the result of the get request
     * @throws IOException
     */
    public String do_get(String url_link, HashMap<String, String> list_params) throws IOException {

        //Create URL and http connection
        URL url = new URL(this.urlBuild(url_link, list_params));
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            response = readStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
        return response;
    }

    /**
     * This method is used to read the stream come from the net
     *
     * @param is
     * @return a string that represents the result of the stream
     * @throws IOException
     */
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    /**
     * This method is used for build the correct url with parameters intended to Get request
     * and return the url that become builded.
     *
     * @param url the url which needs the params
     * @param list_params an hashmap with necessary parameters
     * @return url builded with params
     */
    public String urlBuild(String url, HashMap<String, String> list_params){
        Iterator it = list_params.entrySet().iterator();
        while(it.hasNext()) {
            HashMap.Entry<String,String> pair = (HashMap.Entry)it.next();
            url = url + pair.getKey() + "=" + pair.getValue() + "&";
        }

        return url.substring(0, url.length()-1);
    }
}

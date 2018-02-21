package insight.ucd.homerehab.Asynctask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import insight.ucd.homerehab.Activity.DeviceListActivity;
import insight.ucd.homerehab.Static.Variables;
import insight.ucd.homerehab.NetworkManager.SessionManager;
import insight.ucd.homerehab.R;

/**
 * Created by Cyber-X on 13/02/2018.
 */

public class LoginAsync extends AsyncTask<Void, Integer, String> {

    private String user_id;
    private Context context;
    private ConstraintLayout progress;
    private TextView txt_alert;
    private SessionManager session = SessionManager.getInstance();
    private String response = "";
    private CheckBox check;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor_prefs;

    public LoginAsync(String user_id, Context context, ConstraintLayout progress, TextView txt_alert, CheckBox check) {
        this.user_id = user_id;
        this.context = context;
        this.progress = progress;
        this.txt_alert = txt_alert;
        this.check = check;
    }

    @Override
    protected void onPreExecute ()
    {
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            JSONObject j_response = new JSONObject(session.do_login(this.user_id, this.context));
            JSONObject jo = (JSONObject) j_response.get("response");
            response = jo.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Response_mine", response+ " <- ");
        return response;
    }



    @Override
    protected void onPostExecute (String response)
    {
        prefs = context.getSharedPreferences(Variables.MYIMP, Context.MODE_PRIVATE);
        editor_prefs = prefs.edit();
        if (response.equals("ok")) {
            if(this.check.isChecked()){
                editor_prefs.putBoolean(Variables.remember_login, true);
                editor_prefs.commit();
            }
            this.progress.setVisibility(View.GONE);
            Intent intent = new Intent(context, DeviceListActivity.class);
            context.startActivity(intent);
        } else if (response.equals("failed")) {
            this.progress.setVisibility(View.GONE);
            txt_alert.setText(context.getResources().getText(R.string.txt_alert_failed));
        } else if (response.equals("Error")) {
            this.progress.setVisibility(View.GONE);
            txt_alert.setText(context.getResources().getText(R.string.txt_alert_error));
        }
    }
}

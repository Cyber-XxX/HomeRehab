package insight.ucd.homerehab.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import insight.ucd.homerehab.Static.Variables;
import insight.ucd.homerehab.Asynctask.LoginAsync;
import insight.ucd.homerehab.R;

/*
* Activity used for the patience authentication
* */
public class LoginActivity extends AppCompatActivity {

    private EditText user_id;
    private Button btn_login;
    private CheckBox remember_me;
    private ConstraintLayout layout_progress;
    private Context context = this;
    private TextView txt_alert;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private static String response = "";
    private static String response_cont = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefs = context.getSharedPreferences(Variables.MYIMP, Context.MODE_PRIVATE);
        editor = prefs.edit();

        if (prefs.getBoolean(Variables.remember_login, false)) {
            Intent intent = new Intent(context, DeviceListActivity.class);
            startActivity(intent);
        }

        user_id = findViewById(R.id.edit_userid);
        user_id.setText("");

        btn_login = findViewById(R.id.btn_login);
        remember_me = findViewById(R.id.chk_remember);
        layout_progress = findViewById(R.id.relative_progress);
        layout_progress.setVisibility(View.GONE);
        txt_alert = findViewById(R.id.txt_alert);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_alert.setText("");
                if(String.valueOf(user_id.getText()).equals("")){
                    txt_alert.setText("Insert an User ID");
                } else if(!String.valueOf(user_id.getText()).equals("")) {
                    layout_progress.setVisibility(View.VISIBLE);
                    LoginAsync login = new LoginAsync(String.valueOf(user_id.getText()), context, layout_progress, txt_alert, remember_me);
                    login.execute();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

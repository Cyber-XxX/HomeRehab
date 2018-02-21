package insight.ucd.homerehab.Activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import insight.ucd.homerehab.Service.ShimmerService;
import insight.ucd.homerehab.R;

public class TwoButtonsActivity extends AppCompatActivity {

    private Button btn_connect;
    private Button btn_start;
    private Button btn_stop;
    private TextView txt_info_conn;
    private ShimmerService mService = new ShimmerService(this);
    private String address= "";
    private String address_conn = "";
    private TextView streminag_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_buttons);
        Intent intent = getIntent();

        if (!isMyServiceRunning()) {
            Intent service_intent = new Intent(this, ShimmerService.class);
            startService(service_intent);
        }

        Intent intent_binder = new Intent(this, ShimmerService.class);
        bindService(intent_binder, shimmerServiceConnection, Context.BIND_AUTO_CREATE);

        //Get info about the bluetooth address from the intent
        address = intent.getStringExtra("device_address");
        address_conn = address.substring(address.length() - 17);

        //Initialize UI elements
        btn_connect = findViewById(R.id.btn_connect);
        btn_start = findViewById(R.id.btn_start);
        btn_stop = findViewById(R.id.btn_stop);
        txt_info_conn = findViewById(R.id.txt_info_connection);
        streminag_response = findViewById(R.id.txt_streaming);

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_info_conn.setText("Wait...");
                mService.connectShimmer(address_conn);

                while(!mService.isShimmerConnected()){
                    txt_info_conn.setText("Connecting to: " + address);
                }
                txt_info_conn.setText("Connected to: " + address);
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_info_conn.setText("Wait for streaming");
                mService.startStreaming();
                while(!mService.isDeviceStreaming()) {

                }
                txt_info_conn.setText("The device " + address + " is streaming.");
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.stopStreaming();
                txt_info_conn.setText("The device " + address + " is not streaming.");
            }
        });
    }

    protected ServiceConnection shimmerServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            ShimmerService.LocalBinder binder = (ShimmerService.LocalBinder) service;
            mService = binder.getService();
        }

        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    protected boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("service.ShimmerService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(shimmerServiceConnection);
    }

    public void setStreamingResponse(ArrayList<Double> list_response){
        for(Double resp : list_response) {
            streminag_response.setText(String.valueOf(resp));
        }
    }
}

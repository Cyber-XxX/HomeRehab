package insight.ucd.homerehab.Handler;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

import insight.ucd.androidshimmerdriver.android_shimmer_driver.connection_shimmer_interface.Shimmer;
import insight.ucd.androidshimmerdriver.android_shimmer_driver.driver.Configuration;
import insight.ucd.androidshimmerdriver.android_shimmer_driver.driver.FormatCluster;
import insight.ucd.androidshimmerdriver.android_shimmer_driver.driver.ObjectCluster;

/**
 * Created by Cyber-X on 30/01/2018.
 */

public class HandlerMessageShimmer extends Handler {

    private Context context;
    private static String mFileName = "shimmerlogexample";


    public HandlerMessageShimmer(Context context){
        this.context = context;

    }

    @Override
    public void handleMessage(Message msg) {
        Log.d("Message:", String.valueOf(msg.what));
        ArrayList<Double> list_clust_result = new ArrayList<>();
        switch (msg.what) { // handlers have a what identifier which is used to identify the type of msg
            case Shimmer.MESSAGE_READ:
                if ((msg.obj instanceof ObjectCluster)) {
                    ObjectCluster objectCluster = (ObjectCluster) msg.obj;

                    double acell_x = setData(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_X, objectCluster);
                    double acell_y = setData(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Y, objectCluster);
                    double acell_z = setData(Configuration.Shimmer3.ObjectClusterSensorName.ACCEL_LN_Z, objectCluster);
                    double gyro_x = setData(Configuration.Shimmer3.ObjectClusterSensorName.GYRO_X, objectCluster);
                    double gyro_y = setData(Configuration.Shimmer3.ObjectClusterSensorName.GYRO_Y, objectCluster);
                    double gyro_z = setData(Configuration.Shimmer3.ObjectClusterSensorName.GYRO_Z, objectCluster);
                    double timestamp = System.currentTimeMillis();

                    Log.d("acell_x:", String.valueOf(acell_x));
                    Log.d("acell_y:", String.valueOf(acell_y));
                    Log.d("acell_z:", String.valueOf(acell_z));
                    Log.d("gyro_x:", String.valueOf(gyro_x));
                    Log.d("gyro_y:", String.valueOf(gyro_y));
                    Log.d("gyro_z:", String.valueOf(gyro_z));
                    ArrayList<Double> data = new ArrayList<>();
                    data.add(acell_x);
                    data.add(acell_y);
                    data.add(acell_z);
                    data.add(gyro_x);
                    data.add(gyro_y);
                    data.add(gyro_z);
                }
                break;

            case Shimmer.MESSAGE_TOAST:
                Log.d("toast", msg.getData().getString(Shimmer.TOAST));
                break;
        }
    }

    private double setData(String name, ObjectCluster objectCluster) {
        Log.d("again", "again");
        Collection<FormatCluster> ofFormats = objectCluster.mPropertyCluster.get(name);  // first retrieve all the possible formats for the current sensor device
        FormatCluster formatCluster = ObjectCluster.returnFormatCluster(ofFormats, "CAL");
        if (formatCluster != null) {
            return formatCluster.mData;
        }
        return 0;
    }
    }


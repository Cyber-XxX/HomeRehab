package insight.ucd.homerehab.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import insight.ucd.androidshimmerdriver.android_shimmer_driver.connection_shimmer_interface.Shimmer;
import insight.ucd.homerehab.Handler.HandlerMessageShimmer;

/**
 * Created by Cyber-X on 05/02/2018.
 */

public class ShimmerService extends Service {

    private final IBinder mBinder = new LocalBinder();
    public static Shimmer shimmerDevice = null;
    private Context context;
    private Handler mHandler = new HandlerMessageShimmer(this.context);

    public ShimmerService() {

    }

    public ShimmerService(Context context){
        this.context = context;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        shimmerDevice.stop();
    }

    public void connectShimmer(String bluetoothAddress) {
        shimmerDevice = new Shimmer(this, mHandler,"RightArm", 102.4, 0, 0, Shimmer.SENSOR_ACCEL| Shimmer.SENSOR_GYRO| Shimmer.SENSOR_MAG, false);
        shimmerDevice.connect(bluetoothAddress,"gerdavax");
    }

    public void disconnectShimmer() {
        if(shimmerDevice != null) {
            shimmerDevice.stop();
        }
    }

    public boolean isShimmerConnected() {
        return shimmerDevice != null && shimmerDevice.getShimmerState() == Shimmer.STATE_CONNECTED;
    }

    public Shimmer getShimmer() {
        return shimmerDevice;
    }

    public void startStreaming() {
        if(shimmerDevice != null) {
            if (shimmerDevice.getShimmerState() == Shimmer.STATE_CONNECTED) {
                Log.d("is in statuStreaming3","yes2");
                shimmerDevice.startStreaming();
                Log.d("is in statuStreaming4",String.valueOf(shimmerDevice.getStreamingStatus()));
            }
        }
    }

    public class LocalBinder extends Binder {
        public ShimmerService getService() {
            return ShimmerService.this;
        }
    }
    public void stopStreaming() {
        if (shimmerDevice != null) {
            shimmerDevice.stopStreaming();
        }
    }

    @SuppressWarnings("unused")
    public boolean isDeviceStreaming() {
        return shimmerDevice != null && shimmerDevice.getStreamingStatus();
    }

    @SuppressWarnings("unused")
    public int getShimmerState() {
        int status = -1;
        if(shimmerDevice != null) {
            status = shimmerDevice.getShimmerState();
        }
        return status;
    }
}

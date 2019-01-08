package pocroianu.cobaltbluelightfilter.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import pocroianu.cobaltbluelightfilter.fragments.AutomatedAdjustActivity;

public class AmbientLightSensorService extends Service implements SensorEventListener {
    SensorManager sensorManager;
    Sensor lightSensor;
    final static String TAG ="tag";
    double brightnessPercent;
    int brightnessValue;



    /**
     *
     */
    public AmbientLightSensorService() {
    }

    /**
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    /**
     * Registering the Sensor Manager
     */
    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(getApplicationContext())){

                // Do stuff here

                sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
                assert sensorManager != null;
                lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

                if (lightSensor != null) {
                    sensorManager.registerListener(
                            this,
                            lightSensor,
                            SensorManager.SENSOR_DELAY_NORMAL);
                    Log.i(TAG, " sensor regstr");
                }}



            else {
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

    }

    /**
     *
     */
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            AutomatedAdjustActivity.lightSensorValue.setText(""+msg.arg1);
        }
    };

    /**
     * This if else is designed in accordance with the formula submitted in Report.
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            Log.i(TAG, "before sensor text chg " + event.values[0]);

            Message msg = Message.obtain();
            msg.arg1= (int) event.values[0];
            handler.sendMessage(msg);

            normalMethod((int)event.values[0]);
        }

    }

    /**
     * Normal method formula used when there is no possibility of dry eye problem.
     * @param ambientLight
     */
    public void normalMethod(int ambientLight){
        if(ambientLight<4064) {
            if(ambientLight==0)
                brightnessPercent=25;
            else
                brightnessPercent = ((9.62 * Math.log(ambientLight) + 20));

            brightnessValue = (int) (2.55 * brightnessPercent);
            Log.i(TAG," "+brightnessValue+" "+brightnessPercent);
            setScreenBrightness(brightnessValue);
            Log.i(TAG,"Normal Method Used");
        }
        else{
            setScreenBrightness(255);
        }

    }

    /**
     *
     * @param brightnessValue
     */
    public void setScreenBrightness(int brightnessValue){

        // Make sure brightness value between 0 to 255
        if(brightnessValue >= 0 && brightnessValue <= 255){
            Settings.System.putInt(
                    this.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS,
                    brightnessValue
            );
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
        lightSensor=null;
        sensorManager =null;
    }
}

package pocroianu.cobaltbluelightfilter.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
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

import pocroianu.cobaltbluelightfilter.activities.MainActivity;
import pocroianu.cobaltbluelightfilter.fragments.AutomatedAdjustFragment;

public class AmbientLightSensorService extends Service implements SensorEventListener {

    SensorManager sensorManager;
    Sensor lightSensor;

    int screenDimPercent;
    int brightnessValue;





    /**
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
            if (Settings.System.canWrite(getApplicationContext())) {

                // Do stuff here
                createSensorManager();

            } else {

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
    private void createSensorManager() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        assert sensorManager != null;
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor != null) {
            sensorManager.registerListener(
                    this,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    /**
     *
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AutomatedAdjustFragment.lightSensorValue.setText("" + msg.arg1 + " lux");
        }
    };

    /**
     * This if else is designed in accordance with the formula submitted in Report.
     *
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            Message msg = Message.obtain();
            msg.arg1 = (int) event.values[0];
            handler.sendMessage(msg);
            ambientLightAlgorithm((int) event.values[0]);
        }
    }

    /**
     * This
     *
     * @param ambientLightValue
     */
    public void ambientLightAlgorithm(int ambientLightValue) {
        if (ambientLightValue < 4064) {
            if (ambientLightValue < 40)
                screenDimPercent = 75;
            else if (ambientLightValue > 40 && ambientLightValue <= 250)
                screenDimPercent = 50;
            else if (ambientLightValue > 250 && ambientLightValue <= 1500)
                screenDimPercent = 25;
            else if (ambientLightValue > 1500)
                screenDimPercent = 0;

            setScreenBrightness(screenDimPercent);

        } else {
            setScreenBrightness(0);
        }

    }

    /**
     * @param screenDimPercent
     */
    public void setScreenBrightness(int screenDimPercent) {

        // Make sure transparency percent value is between 0 to 100
        if (screenDimPercent >= 0 && screenDimPercent < 25) {

            MainActivity.mOverlayView.setBackgroundColor(Color.rgb(25, 0, 0));
            MainActivity.layoutParams.alpha = 0.0F;
        }
        else if (screenDimPercent >=25 && screenDimPercent <50) {
            MainActivity.mOverlayView.setBackgroundColor(Color.rgb(50, 0, 0));
            MainActivity.layoutParams.alpha = 0.25F;
        }
        else if (screenDimPercent >=50 && screenDimPercent <75) {
            MainActivity.mOverlayView.setBackgroundColor(Color.rgb(100, 0, 0));
            MainActivity.layoutParams.alpha = 0.50F;
        }
        else if (screenDimPercent >=75 && screenDimPercent <100) {
            MainActivity.mOverlayView.setBackgroundColor(Color.rgb(150, 0, 0));
            MainActivity.layoutParams.alpha = 0.75F;
        }

        MainActivity.windowManager.updateViewLayout(MainActivity.mOverlayView, MainActivity.layoutParams);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {


        sensorManager.unregisterListener(this);
        lightSensor = null;
        sensorManager = null;

        MainActivity.mOverlayView.setBackgroundColor(Color.rgb(0, 0, 0));
        MainActivity.layoutParams.alpha = 0.0F;
        MainActivity.windowManager.updateViewLayout(MainActivity.mOverlayView, MainActivity.layoutParams);

    }
}

package pocroianu.cobaltbluelightfilter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AutomatedAdjustActivity extends AppCompatActivity {

    private Intent intent;
    public static TextView lightSensorValue;



    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_automated_adjust_activity);


        lightSensorValue = (TextView) findViewById(R.id.lightSensorValue);


        checkPermission();

    }

    /**
     *
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)){
                boolean floatWindowPermission = true;
                startSensorManagerService();

            }else{
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                int FLOAT_WINDOW_REQUEST_CODE = 1;
                startActivityForResult(intent, FLOAT_WINDOW_REQUEST_CODE);
            }
        }
    }


    /**
     * This will start the Sensor Manager Service
     */
    private void startSensorManagerService(){
        Intent intent1 = new Intent(this, SensorService.class);
        startService(intent1);
    }


}

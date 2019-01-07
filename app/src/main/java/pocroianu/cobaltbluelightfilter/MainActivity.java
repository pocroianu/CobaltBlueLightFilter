package pocroianu.cobaltbluelightfilter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Nicolae Pocroianu
 */
public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private SeekBar seekBarRed;
    private SeekBar seekBarGreen;
    private SeekBar seekBarBlue;
    private SeekBar seekBarAlpha;

    private Intent intent;
    private boolean floatWindowPermission = false ;
    private final int FLOAT_WINDOW_REQUEST_CODE = 1;

    public static TextView luxText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBarRed = (SeekBar) findViewById(R.id.seekBarRed);
        seekBarRed.setOnSeekBarChangeListener(this);

        seekBarGreen = (SeekBar) findViewById(R.id.seekBarGreen);
        seekBarGreen.setOnSeekBarChangeListener(this);

        seekBarBlue = (SeekBar) findViewById(R.id.seekBarBlue);
        seekBarBlue.setOnSeekBarChangeListener(this);

        seekBarAlpha = (SeekBar) findViewById(R.id.seekBarAlpha);
        seekBarAlpha.setOnSeekBarChangeListener(this);

        luxText = (TextView) findViewById(R.id.luxView);

        checkPermission();
    }

    /**
     * This is implemented because when using Android API 23 or higher,you have to
     * check for permission
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(this)){
                floatWindowPermission = true;
                startBlueLightFilterService();
                startSensorManagerService();

            }else{
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,FLOAT_WINDOW_REQUEST_CODE);
            }
        }

    }

    /**
     * This will start the BlueLightService
     */
    private void startBlueLightFilterService() {
        intent = new Intent(this,BlueLightFilterService.class);
        startService(intent);
    }

    /**
     * This will start the Sensor Manager Service
     */
    private void startSensorManagerService(){
        Intent intent1 = new Intent(this, SensorService.class);
        startService(intent1);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(floatWindowPermission){


            if(seekBar==seekBarRed)
            {intent.putExtra("seekBarType",StaticValues.redSeekBarType);
            }
            if(seekBar==seekBarGreen)
            {intent.putExtra("seekBarType",StaticValues.greenSeekBarType);
            }
            if(seekBar==seekBarBlue)
            {intent.putExtra("seekBarType",StaticValues.blueSeekBarType);
            }
            if(seekBar==seekBarAlpha)
            {intent.putExtra("seekBarType",StaticValues.alphaSeekBarType);
            }


            intent.putExtra("level",progress);

            startService(intent);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }




    /*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FLOAT_WINDOW_REQUEST_CODE: {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (Settings.canDrawOverlays(this)) {
                        floatWindowPermission = true;
                        startBlueLightFilterService();
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        floatWindowPermission = false;
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
        }
    }

*/
}

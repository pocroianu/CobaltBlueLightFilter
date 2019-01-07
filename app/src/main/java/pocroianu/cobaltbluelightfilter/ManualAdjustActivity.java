package pocroianu.cobaltbluelightfilter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class ManualAdjustActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private SeekBar seekBarRed;
    private SeekBar seekBarGreen;
    private SeekBar seekBarBlue;

    private Intent intent;
    private boolean floatWindowPermission = false ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_manual_adjust_activity);

        seekBarRed = (SeekBar) findViewById(R.id.seekBarRed);
        seekBarRed.setOnSeekBarChangeListener(this);

        seekBarGreen = (SeekBar) findViewById(R.id.seekBarGreen);
        seekBarGreen.setOnSeekBarChangeListener(this);

        seekBarBlue = (SeekBar) findViewById(R.id.seekBarBlue);
        seekBarBlue.setOnSeekBarChangeListener(this);

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

            }else{
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                int FLOAT_WINDOW_REQUEST_CODE = 1;
                startActivityForResult(intent, FLOAT_WINDOW_REQUEST_CODE);
            }
        }
    }

    /**
     * This will start the BlueLightService
     */
    private void startBlueLightFilterService() {
        intent = new Intent(this,BlueLightService.class);
        startService(intent);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(floatWindowPermission){

            if(seekBar==seekBarRed)
            {
                StaticValues.redValue=(int) (255*Math.sqrt(progress *1.0/100));
                intent.putExtra("seekBarType",StaticValues.redSeekBarType);
            }
            if(seekBar==seekBarGreen)
            {
                StaticValues.greenValue=(int) (255*Math.sqrt(progress *1.0/100));
                intent.putExtra("seekBarType",StaticValues.greenSeekBarType);
            }
            if(seekBar==seekBarBlue)
            {
                StaticValues.blueValue=(int) (255*Math.sqrt(progress *1.0/100));
                intent.putExtra("seekBarType",StaticValues.blueSeekBarType);
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
}

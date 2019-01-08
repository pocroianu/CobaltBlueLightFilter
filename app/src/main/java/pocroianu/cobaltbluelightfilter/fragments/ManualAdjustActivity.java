package pocroianu.cobaltbluelightfilter.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import java.util.Objects;

import pocroianu.cobaltbluelightfilter.R;
import pocroianu.cobaltbluelightfilter.services.ManualAdjustService;
import pocroianu.cobaltbluelightfilter.static_.StaticValues;

public class ManualAdjustActivity extends Fragment implements SeekBar.OnSeekBarChangeListener{

    private SeekBar seekBarRed;
    private SeekBar seekBarGreen;
    private SeekBar seekBarBlue;

    private Intent intent;
    private boolean floatWindowPermission = false ;

    View view ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_manual_adjust_activity, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        seekBarRed = (SeekBar) view.findViewById(R.id.seekBarRed);
        seekBarRed.setOnSeekBarChangeListener(this);



        seekBarGreen = (SeekBar) view.findViewById(R.id.seekBarGreen);
        seekBarGreen.setOnSeekBarChangeListener(this);

        seekBarBlue = (SeekBar) view.findViewById(R.id.seekBarBlue);
        seekBarBlue.setOnSeekBarChangeListener(this);


        checkPermission();
    }


    /**
     * This is implemented because when using Android API 23 or higher,you have to
     * check for permission
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(getActivity())){
                floatWindowPermission = true;
                startBlueLightFilterService();

            }else{
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + Objects.requireNonNull(getActivity()).getPackageName()));
                int FLOAT_WINDOW_REQUEST_CODE = 1;
                startActivityForResult(intent, FLOAT_WINDOW_REQUEST_CODE);
            }
        }
    }

    /**
     * This will start the ManualAdjustService
     */
    private void startBlueLightFilterService() {
        intent = new Intent(getActivity(),ManualAdjustService.class);
        view.getContext().startService(intent);
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

            //If this condition is not set,the app may behave strangely.
            Objects.requireNonNull(getActivity()).startService(intent);
        }
    }



    //Unimplemented methods.
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}

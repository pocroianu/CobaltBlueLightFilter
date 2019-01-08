package pocroianu.cobaltbluelightfilter.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import pocroianu.cobaltbluelightfilter.R;
import pocroianu.cobaltbluelightfilter.services.AmbientLightSensorService;

public class AutomatedAdjustActivity extends Fragment {

    private Intent intent;
    public static TextView lightSensorValue;

    View view ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_automated_adjust_activity, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lightSensorValue = (TextView) view.findViewById(R.id.lightSensorValue);
        checkPermission();
    }



    /**
     *
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(Settings.canDrawOverlays(getActivity())){
                boolean floatWindowPermission = true;
                startSensorManagerService();

            }else{
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + Objects.requireNonNull(getActivity()).getPackageName()));
                int FLOAT_WINDOW_REQUEST_CODE = 1;
                startActivityForResult(intent, FLOAT_WINDOW_REQUEST_CODE);
            }
        }
    }


    /**
     * This will start the Sensor Manager Service
     */
    private void startSensorManagerService(){
        Intent intent1 = new Intent(view.getContext(), AmbientLightSensorService.class);
        Objects.requireNonNull(getActivity()).startService(intent1);
    }

}

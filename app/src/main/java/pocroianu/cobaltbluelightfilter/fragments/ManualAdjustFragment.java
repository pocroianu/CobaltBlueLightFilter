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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import java.util.Objects;

import pocroianu.cobaltbluelightfilter.R;
import pocroianu.cobaltbluelightfilter.services.ManualAdjustService;
import pocroianu.cobaltbluelightfilter.static_.StaticValues;

public class ManualAdjustFragment extends Fragment implements SeekBar.OnSeekBarChangeListener {

    private SeekBar seekBarRed;
    private SeekBar seekBarBlue;
    private SeekBar seekBarAlpha;

    private RadioGroup radioGroup;
    private RadioButton radioButton0;
    private RadioButton radioButton25;
    private RadioButton radioButton50;
    private RadioButton radioButton75;


    private Intent intent;
    private boolean floatWindowPermission = false;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_manual_adjust_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        seekBarRed = (SeekBar) view.findViewById(R.id.seekBarRed);
        seekBarRed.setOnSeekBarChangeListener(this);

        seekBarBlue = (SeekBar) view.findViewById(R.id.seekBarBlue);
        seekBarBlue.setOnSeekBarChangeListener(this);

        createRadioButtons();
        checkPermission();
    }

    /**
     *
     */
    private void createRadioButtons() {

        radioGroup = (RadioGroup) view .findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radioButton0:
                        intent.putExtra("radioButtonType", 0);
                        break;
                    case R.id.radioButton25:
                        intent.putExtra("radioButtonType", 25);
                        break;
                    case R.id.radioButton50:
                        intent.putExtra("radioButtonType", 50);
                        break;
                    case R.id.radioButton75:
                        intent.putExtra("radioButtonType", 75);
                        break;
                }

                Objects.requireNonNull(getActivity()).startService(intent);
            }
        });
    }


    /**
     * This is implemented because when using Android API 23 or higher,
     * You have to check for permission
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(getActivity())) {
                floatWindowPermission = true;
                startBlueLightFilterService();

            } else {
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
        intent = new Intent(getActivity(), ManualAdjustService.class);
        view.getContext().startService(intent);
    }

    /**
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (floatWindowPermission) {

            if (seekBar == seekBarRed) {
                StaticValues.redValue = (int) (255 * Math.sqrt(progress * 1.0 / 100));
                intent.putExtra("seekBarType", StaticValues.redSeekBarType);
            }

            if (seekBar == seekBarBlue) {
                StaticValues.blueValue = (int) (255 * Math.sqrt(progress * 1.0 / 100));
                intent.putExtra("seekBarType", StaticValues.blueSeekBarType);
            }


            intent.putExtra("level", progress);

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

package pocroianu.cobaltbluelightfilter.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import pocroianu.cobaltbluelightfilter.R;
import pocroianu.cobaltbluelightfilter.static_.StaticValues;

public class ManualAdjustService extends Service {

    public static final String TAG = "ManualAdjustService";

    private View mOverlayView;

    private int currentLevel = 0;
    private int _seekBarType;


    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;


    /**
     *
     */
    public ManualAdjustService() {
    }

    /**
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }


    /**
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     *
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);

        // An alpha value to apply to this entire window.
        // An alpha of 1.0 means fully opaque and 0.0 means fully transparent
        layoutParams.alpha = 0.1F;

        // When FLAG_DIM_BEHIND is set, this is the amount of dimming to apply.
        // Range is from 1.0 for completely opaque to 0.0 for no dim.
        layoutParams.dimAmount = 0F;

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mOverlayView = inflater.inflate(R.layout.fiter_layout, null);

        windowManager.addView(mOverlayView, layoutParams);
    }


    /**
     *
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentLevel = intent.getIntExtra("level", currentLevel);
        _seekBarType = intent.getIntExtra("seekBarType" , _seekBarType);

        if(_seekBarType==StaticValues.redSeekBarType){

            mOverlayView.setBackgroundColor(Color.rgb((int) (255*Math.sqrt(currentLevel *1.0/100)),StaticValues.greenValue, StaticValues.blueValue));

        }

        if(_seekBarType==StaticValues.greenSeekBarType){
            mOverlayView.setBackgroundColor(Color.rgb(StaticValues.redValue,(int) (255*Math.sqrt(currentLevel *1.0/100)),StaticValues.blueValue ));
        }

        if(_seekBarType==StaticValues.blueSeekBarType){
            mOverlayView.setBackgroundColor(Color.rgb(StaticValues.redValue,StaticValues.greenValue, (int) (255*Math.sqrt(currentLevel *1.0/100))));
        }




        windowManager.updateViewLayout(mOverlayView, layoutParams);

        return START_REDELIVER_INTENT;
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowManager.removeView(mOverlayView);
    }



}

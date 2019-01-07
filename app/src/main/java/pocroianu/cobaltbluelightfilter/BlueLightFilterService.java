package pocroianu.cobaltbluelightfilter;

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

public class BlueLightFilterService extends Service {

    public static final String TAG = "BlueLightFilterService";

    private View mOverlayView;

    private int currentLevel = 0;
    private int _seekBarType;

    private int _redCurrentValue=0;
    private int _greenCurrentValue=0;
    private int _blueCurrentValue=0;


    private WindowManager.LayoutParams params;
    private WindowManager wm;


    /**
     *
     */
    public BlueLightFilterService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate");

        params = new WindowManager.LayoutParams(
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
        params.alpha = 0.3F;

        // When FLAG_DIM_BEHIND is set, this is the amount of dimming to apply.
        // Range is from 1.0 for completely opaque to 0.0 for no dim.
        params.dimAmount = 0F;

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mOverlayView = inflater.inflate(R.layout.fiter_layout, null);

        wm.addView(mOverlayView, params);
    }


    /**
     *
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentLevel = intent.getIntExtra("level", currentLevel);
        _seekBarType = intent.getIntExtra("seekBarType" , _seekBarType);

        if(_seekBarType==StaticValues.redSeekBarType){
            _redCurrentValue=currentLevel;
            mOverlayView.setBackgroundColor(Color.rgb((int) (255-255*Math.sqrt(currentLevel *1.0/100)),_greenCurrentValue, _blueCurrentValue));

        }

        if(_seekBarType==StaticValues.greenSeekBarType){
            _greenCurrentValue=currentLevel;
            mOverlayView.setBackgroundColor(Color.rgb(_redCurrentValue,(int) (255-255*Math.sqrt(currentLevel *1.0/100)),_blueCurrentValue ));
        }

        if(_seekBarType==StaticValues.blueSeekBarType){
            _blueCurrentValue=currentLevel;
            mOverlayView.setBackgroundColor(Color.rgb(_redCurrentValue,_greenCurrentValue, (int) (255-255*Math.sqrt(currentLevel *1.0/100))));
        }

        if(_seekBarType==StaticValues.alphaSeekBarType){
            mOverlayView.setAlpha(currentLevel);
        }


        wm.updateViewLayout(mOverlayView, params);

        return START_REDELIVER_INTENT;
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");

        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        wm.removeView(mOverlayView);
    }



}

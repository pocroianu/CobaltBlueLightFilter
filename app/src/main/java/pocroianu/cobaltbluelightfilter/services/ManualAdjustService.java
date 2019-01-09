package pocroianu.cobaltbluelightfilter.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import pocroianu.cobaltbluelightfilter.activities.MainActivity;
import pocroianu.cobaltbluelightfilter.static_.StaticValues;

public class ManualAdjustService extends Service {

    public static final String TAG = "ManualAdjustService";



    private int currentLevel = 0;
    private int seekBarType;
    private int radioButtonType = 0;

//    private View mOverlayView;
//    private WindowManager.LayoutParams layoutParams;
//    private WindowManager windowManager;


    /**
     *
     */
    public ManualAdjustService() {
    }

    /**
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }


    /**
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

        /*layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_DIM_BEHIND, PixelFormat.TRANSLUCENT);

        // An alpha value to apply to this entire window.
        // An alpha of 1.0 means fully opaque and 0.0 means fully transparent
        layoutParams.alpha = StaticValues.alphaDefaultValue;

        // When FLAG_DIM_BEHIND is set, this is the amount of dimming to apply.
        // Range is from 1.0 for completely opaque to 0.0 for no dim.
        layoutParams.dimAmount = StaticValues.dimDefaultValue;

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mOverlayView = inflater.inflate(R.layout.fiter_layout, null);

        windowManager.addView(mOverlayView, layoutParams);*/
    }


    /**
     *
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        currentLevel = intent.getIntExtra("level", currentLevel);
        seekBarType = intent.getIntExtra("seekBarType", seekBarType);
        radioButtonType = intent.getIntExtra("radioButtonType" , radioButtonType);

        if (seekBarType == StaticValues.redSeekBarType) {
            MainActivity.mOverlayView.setBackgroundColor(Color.rgb((int) (255 * Math.sqrt(currentLevel * 1.0 / 100)), 0, StaticValues.blueValue));
        }
        if (seekBarType == StaticValues.blueSeekBarType) {
            MainActivity.mOverlayView.setBackgroundColor(Color.rgb(StaticValues.redValue, 0, (int) (255 * Math.sqrt(currentLevel * 1.0 / 100))));
        }
        if(radioButtonType == 0 ){
            MainActivity.layoutParams.alpha = 0.0F;
        }
        if(radioButtonType == 25 ){
            MainActivity.layoutParams.alpha = 0.25F;
        }
        if(radioButtonType == 50 ){
            MainActivity.layoutParams.alpha = 0.5F;
        }
        if(radioButtonType == 75 ){
            MainActivity.layoutParams.alpha = 0.75F;
        }

        MainActivity.windowManager.updateViewLayout(MainActivity.mOverlayView, MainActivity.layoutParams);

        return START_REDELIVER_INTENT;
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy");

        /*MainActivity.windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        MainActivity.windowManager.removeView(MainActivity.mOverlayView);*/
    }


}

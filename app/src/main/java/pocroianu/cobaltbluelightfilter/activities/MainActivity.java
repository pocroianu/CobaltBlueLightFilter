package pocroianu.cobaltbluelightfilter.activities;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import pocroianu.cobaltbluelightfilter.R;
import pocroianu.cobaltbluelightfilter.fragments.AutomatedAdjustFragment;
import pocroianu.cobaltbluelightfilter.fragments.HomeFragment;
import pocroianu.cobaltbluelightfilter.fragments.ManualAdjustFragment;
import pocroianu.cobaltbluelightfilter.fragments.SettingsFragment;
import pocroianu.cobaltbluelightfilter.static_.StaticValues;

/**
 * Created by Nicolae Pocroianu
 */
public class MainActivity extends AppCompatActivity {

    public static View mOverlayView;
    public static WindowManager.LayoutParams layoutParams;
    public static WindowManager windowManager;
    public Fragment previousFragment = null;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mactivity);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        createOverlayView();

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    /**
     *
     */
    public void createOverlayView() {
        MainActivity.layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_DIM_BEHIND, PixelFormat.TRANSLUCENT);

        // An alpha value to apply to this entire window.
        // An alpha of 1.0 means fully opaque and 0.0 means fully transparent
        MainActivity.layoutParams.alpha = StaticValues.alphaDefaultValue;

        // When FLAG_DIM_BEHIND is set, this is the amount of dimming to apply.
        // Range is from 1.0 for completely opaque to 0.0 for no dim.
        MainActivity.layoutParams.dimAmount = StaticValues.dimDefaultValue;

        MainActivity.windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        MainActivity.mOverlayView = inflater.inflate(R.layout.fiter_layout, null);

        MainActivity.windowManager.addView(MainActivity.mOverlayView, MainActivity.layoutParams);
    }

    /**
     * Selects the required Fragment.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_manual_adjust:
                            selectedFragment = new ManualAdjustFragment();
                            break;
                        case R.id.nav_automated_adjust:
                            selectedFragment = new AutomatedAdjustFragment();
                            break;

                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                    }

                    getSupportFragmentManager().popBackStack();

                    //Create the fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    //Keep a reference of the fragment
                    previousFragment = selectedFragment;

                    return true;
                }
            };

}


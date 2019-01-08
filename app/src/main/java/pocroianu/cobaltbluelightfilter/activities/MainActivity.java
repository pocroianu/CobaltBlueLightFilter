package pocroianu.cobaltbluelightfilter.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import pocroianu.cobaltbluelightfilter.R;
import pocroianu.cobaltbluelightfilter.fragments.AutomatedAdjustFragment;
import pocroianu.cobaltbluelightfilter.fragments.HomeFragment;
import pocroianu.cobaltbluelightfilter.fragments.ManualAdjustFragment;
import pocroianu.cobaltbluelightfilter.fragments.SettingsFragment;

/**
 * Created by Nicolae Pocroianu
 */
public class MainActivity extends AppCompatActivity {



    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mactivity);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);


        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
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

                    //Create the fragment
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    public void onRadioButtonSelect(View view) {

    }
}


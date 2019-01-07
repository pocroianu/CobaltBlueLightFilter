package pocroianu.cobaltbluelightfilter;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by Nicolae Pocroianu
 */
public class MainActivity extends AppCompatActivity {

    private Button automatedAdjustButton;
    private Button manualAdjustButton;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_activity);

        automatedAdjustButton=(Button) findViewById(R.id.automatedAdjustButton);
        manualAdjustButton=(Button) findViewById(R.id.manualAdjustButton);

        //Sets the click handler for the AutomatedAdjustButton
        automatedAdjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAutomatedAdjustActivity();
            }
        });

        //Sets the click handler for the ManualAdjustButton
        manualAdjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openManualAdjustActivity();
            }
        });
    }

    /**
     * This will open the AutomatedAdjustActivity
     */
    private void openAutomatedAdjustActivity(){

        Intent intent=new Intent(this,AutomatedAdjustActivity.class);
        startActivity(intent);
    }

    /**
     * This will open ManualAdjustActivity
     */
    private void openManualAdjustActivity(){

        Intent intent=new Intent(this,ManualAdjustActivity.class);
        startActivity(intent);
    }


}

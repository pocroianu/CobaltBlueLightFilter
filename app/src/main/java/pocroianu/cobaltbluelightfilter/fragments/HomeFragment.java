package pocroianu.cobaltbluelightfilter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pocroianu.cobaltbluelightfilter.R;


public class HomeFragment extends Fragment {

    private Button automatedAdjustButton;
    private Button manualAdjustButton;
    View view ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_home, container, false);

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     *
     */
    private void showContent(){
        return;
    }
}

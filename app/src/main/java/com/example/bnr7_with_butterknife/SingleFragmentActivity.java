package com.example.bnr7_with_butterknife;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Abstract Class for (OMG!) Activities with single fragment
 * idk what to say rly
 */

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentTransaction fm=getSupportFragmentManager().beginTransaction();
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment==null) {
            fragment = createFragment();
            fm.add(R.id.fragment_container, fragment);
            fm.commit();
        }
    }
}

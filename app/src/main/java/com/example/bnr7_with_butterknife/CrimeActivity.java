package com.example.bnr7_with_butterknife;

import androidx.fragment.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {

    /**
     * Activity that cares {@link CrimeListFragment}
     * when any of crimes from got clicked,the fragment inside sends an intent to {@link CrimePagerActivity}
     */

    @Override
    protected Fragment createFragment() {
        return CrimeListFragment.newInstance();
    }
}
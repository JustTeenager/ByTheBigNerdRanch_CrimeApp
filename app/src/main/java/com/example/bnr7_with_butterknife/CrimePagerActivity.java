package com.example.bnr7_with_butterknife;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Class that uses a ViewPager for Crimes so it handles an UI for each crime
 */

public class CrimePagerActivity extends AppCompatActivity {

    private static final String KEY_TO_CRIME="CrimeKey";

    @BindView(R.id.crimePager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        ButterKnife.bind(this);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return CrimeFragment.newInstance(((Crime)CrimeLab.getCrimeLab(CrimePagerActivity.this).getCrimes().values().toArray()[position]).getId());
                }

                @Override
                public int getCount() {
                    return CrimeLab.getCrimeLab(CrimePagerActivity.this).getCrimes().size();
                }
        });
        //we need to open all the crimes,not the first only
        UUID id= (UUID) getIntent().getSerializableExtra(KEY_TO_CRIME);
        for (int i=0;i<CrimeLab.getCrimeLab(this).getCrimes().size();i++){
            if (id.equals(CrimeLab.getCrimeLab(this).getCrimes().keySet().toArray()[i])){
                mViewPager.setCurrentItem(i);
            }
        }
    }

    public static Intent newIntent(Context context, UUID id){
        Intent intent=new Intent(context,CrimePagerActivity.class);
        intent.putExtra(KEY_TO_CRIME,id);
        return intent;
    }
}
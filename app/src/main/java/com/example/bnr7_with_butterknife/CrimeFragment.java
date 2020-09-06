package com.example.bnr7_with_butterknife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import java.util.Date;
import java.util.UUID;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

/**
 * Fragment that cares all UI of details of Crime
 */

public class CrimeFragment extends Fragment {


    //Key for intents
    private static final String KEY_TO_ARGS="KeyToArgs";
    private static final int KEY_TO_TARGETING_FRAGMENT_DATE=0;
    private static final int KEY_TO_TARGETING_FRAGMENT_TIME=1;

    //Crime that we display
    private Crime mCrime;

    //we need an Unbinder because of Fragments Lifecycle
    private Unbinder unbinder;

    @BindView(R.id.CheckBox) CheckBox mSolvedCheck;
    @BindView(R.id.editText) EditText mTextTitle;
    @BindView(R.id.crime_date_button) Button mDateButton;
    @BindView(R.id.crime_time_button) Button mTimeButton;

    @OnClick(R.id.crime_time_button)
    public void onClickTime(View v){
        TimePickerFragment fragment= TimePickerFragment.newInstance(mCrime.getDate());
        fragment.setTargetFragment(this,KEY_TO_TARGETING_FRAGMENT_TIME);
        fragment.show(getFragmentManager(),null);
    }


    @OnClick(R.id.crime_date_button)
    public void onClickDate(View v){
        /**
         * @see R.bool.isTablet
         * on the small screens we obviously want to start new Activity rather then start a FragmentDialog
         * so it helps us to get knowledge about the screen size
         */
        if (!getResources().getBoolean(R.bool.isTablet)){
            //startActivity
            startActivityForResult(DatePickerActivity.newInstance(getActivity(),mCrime.getDate()),KEY_TO_TARGETING_FRAGMENT_DATE);
        }
        else {
            //start a FragmentDialog
            DatePickerFragment fragment = DatePickerFragment.newInstance(mCrime.getDate());
            fragment.setTargetFragment(this, KEY_TO_TARGETING_FRAGMENT_DATE);
            fragment.show(getFragmentManager(), null);
        }

    }

    /**
     * @see android.widget.RadioGroup.OnCheckedChangeListener
     * Butterknife is about to give all of UI elemnts a listener
     */
    @OnCheckedChanged(R.id.CheckBox)
    public void onCheckedChanged(boolean isChecked){
        mCrime.setSolved(isChecked);
    }

    @OnTextChanged(R.id.editText)
    public void onTextChanged(CharSequence s){
        mCrime.setTitle(s.toString());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete_crime:{
                //delete selected crime and get back to crimeListActivity
                CrimeLab.getCrimeLab(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.crime_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //declaring a toolbar
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        //ButterKnife.bind(this, v);//so important to set it before the init of other Views
        unbinder = ButterKnife.bind(this, v);

        //display info without any redaction
        mSolvedCheck.setChecked(mCrime.isSolved());
        mTextTitle.setText(mCrime.getTitle());
        updateDate();

        return v;
    }

    //sets both date and time
    private void updateDate() {
        mDateButton.setText(DateFormat.format("EEEE, yyyy/MM/dd HH:mm",mCrime.getDate()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //take id of Crime that needed from intent
        super.onCreate(savedInstanceState);
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrimeById((UUID) getArguments().getSerializable(KEY_TO_ARGS));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * {@link DatePickerFragment} and {@link TimePickerFragment} used to call this method
     * To let us know the updates user make
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode!=Activity.RESULT_OK) return;
        switch (requestCode) {
            case KEY_TO_TARGETING_FRAGMENT_DATE:{
                mCrime.setDate((Date) data.getSerializableExtra(DatePickerFragment.KEY_TO_EXTRA_DATE));
                updateDate();
                break;
            }
            case KEY_TO_TARGETING_FRAGMENT_TIME:{
                mCrime.setDate((Date) data.getSerializableExtra(TimePickerFragment.KEY_TO_EXTRA_TIME));
                updateDate();
                break;
            }
        }
    }

    /**
     *
     * @param id shows us which Crime we need to put into fragment
     * @return the fragment to show
     */
    public static Fragment newInstance(UUID id){
        Bundle args=new Bundle();
        args.putSerializable(KEY_TO_ARGS,id);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
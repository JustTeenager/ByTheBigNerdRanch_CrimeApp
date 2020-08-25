package com.example.bnr7_with_butterknife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
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
    private static final int KEY_TO_TARGETING_FRAGMENT=0;

    //Crime that we display
    private Crime mCrime;

    //we need an Unbinder because of Fragments Lifecycle
    private Unbinder unbinder;

    @BindView(R.id.CheckBox) CheckBox mSolvedCheck;
    @BindView(R.id.editText) EditText mTextTitle;
    @BindView(R.id.crime_date_button) Button mDateButton;

    @OnClick(R.id.crime_date_button)
    public void onClick(View v){
        DatePickerFragment fragment=DatePickerFragment.newInstance(mCrime.getDate());
        fragment.setTargetFragment(this,KEY_TO_TARGETING_FRAGMENT);
        fragment.show(getFragmentManager(),null);
    }

    /**
     * see {@link android.widget.RadioGroup.OnCheckedChangeListener}
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        //ButterKnife.bind(this, v);//so important to set it before the init of other Views
        unbinder = ButterKnife.bind(this, v);

        //display info without any redaction
        mSolvedCheck.setChecked(mCrime.isSolved());
        mTextTitle.setText(mCrime.getTitle());
        mDateButton.setText(mCrime.getDate().toString());

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //take id of Crime that needed from intent
        mCrime = CrimeLab.getCrimeLab(getActivity()).getCrimeById((UUID) getArguments().getSerializable(KEY_TO_ARGS));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode != KEY_TO_TARGETING_FRAGMENT) return;
        else if (resultCode!=Activity.RESULT_OK) return;
        else {
            mCrime.setDate((Date) data.getSerializableExtra(DatePickerFragment.KEY_TO_EXTRA_DATE));
            mDateButton.setText(mCrime.getDate().toString());
        }
    }

    //function for cool Fragment create
    public static Fragment newInstance(UUID id){
        Bundle args=new Bundle();
        args.putSerializable(KEY_TO_ARGS,id);
        CrimeFragment fragment=new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
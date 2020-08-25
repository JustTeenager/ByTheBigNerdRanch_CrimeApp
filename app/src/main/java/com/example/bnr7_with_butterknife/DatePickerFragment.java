package com.example.bnr7_with_butterknife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class that helps user to choose the date of Crime (presents at {@link CrimeListFragment})
 * Target fragment is {@link CrimeFragment}
 */

public class DatePickerFragment extends DialogFragment {

    //Key for args
    private static final String KEY_TO_DATE="keyDate";

    //Intent key for onActivityResult(int,int,Intent)
    public static final String KEY_TO_EXTRA_DATE="keyExtraDate";

    //@BindView(R.id.datePicker)
    private DatePicker mDatePicker;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Date date= (Date) getArguments().get(KEY_TO_DATE);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);

        View v= LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date,null);
        mDatePicker=v.findViewById(R.id.datePicker);
        mDatePicker.init(year, month, day,null);

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK,new GregorianCalendar(mDatePicker.getYear(),mDatePicker.getMonth(),mDatePicker.getDayOfMonth()).getTime());
                    }
                })
                .create();
    }

    private void sendResult(int resultCode,Date date){
        if (getTargetFragment()==null) return;
        Intent intent=new Intent();
        intent.putExtra(KEY_TO_EXTRA_DATE,date);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

    public static DatePickerFragment newInstance(Date date){
        Bundle bundle=new Bundle();
        bundle.putSerializable(KEY_TO_DATE,date);
        DatePickerFragment fragment=new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}

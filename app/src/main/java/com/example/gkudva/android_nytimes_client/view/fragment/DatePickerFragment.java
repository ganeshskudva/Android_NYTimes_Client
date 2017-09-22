package com.example.gkudva.android_nytimes_client.view.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by gkudva on 20/09/17.
 */

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener mListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), mListener, year, month, dayOfMonth);
    }
}

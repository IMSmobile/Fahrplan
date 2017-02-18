package io.github.imsmobile.fahrplan.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import org.joda.time.DateTime;

import io.github.imsmobile.fahrplan.model.DepartureArrivalModel;

public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;
    private DepartureArrivalModel model;

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    public void setDepartureArrivalModel(DepartureArrivalModel model) {
        this.model = model;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DateTime selectedDate = model.getSelectedDateTime();
        return new DatePickerDialog(getActivity(), listener, selectedDate.getYear(), selectedDate.getMonthOfYear(), selectedDate.getDayOfMonth());
    }



}

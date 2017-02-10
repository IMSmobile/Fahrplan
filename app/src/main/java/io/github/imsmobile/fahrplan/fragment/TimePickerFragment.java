package io.github.imsmobile.fahrplan.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Locale;

import io.github.imsmobile.fahrplan.R;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {



    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView editText = (TextView) getActivity().findViewById(R.id.label_departure);
        editText.setText(getResources().getString(R.string.label_departure) + " " + String.format(Locale.ENGLISH, "%02d", hourOfDay) + ":" + String.format(Locale.ENGLISH, "%02d", minute));
    }
}

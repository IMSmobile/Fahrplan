package io.github.imsmobile.fahrplan.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Locale;

import io.github.imsmobile.fahrplan.R;
import io.github.imsmobile.fahrplan.fragment.listener.TimeDialogListener;
import io.github.imsmobile.fahrplan.model.DepartureArrivalModel;

public class TimeDialogFragment  extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePicker.OnTimeChangedListener, RadioGroup.OnCheckedChangeListener {

    private TextView dateLabel;
    private TimePicker timePicker;
    private TimeDialogListener timeDialogListener;
    private RadioGroup radioGroup;
    private DepartureArrivalModel model;

    public void setDepartureArrivalModel(DepartureArrivalModel model) {
        this.model = new DepartureArrivalModel(model);
    }

    public void setTimeDialogListener(TimeDialogListener timeDialogListener) {
        this.timeDialogListener = timeDialogListener;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timeDialogListener.timeSelected(model);
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );


        View rootView = getActivity().getLayoutInflater().inflate(R.layout.time_fragment, null);

        radioGroup = (RadioGroup) rootView.findViewById(R.id.radio_group_departure_arrival);
        radioGroup.setOnCheckedChangeListener(this);
        if (model.isDeparture()) {
            ((RadioButton) rootView.findViewById(R.id.radio_departure)).setChecked(true);
        } else {
            ((RadioButton) rootView.findViewById(R.id.radio_arrival)).setChecked(true);
        }

        timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(model.getSelectedDateTime().getHourOfDay());
        timePicker.setCurrentMinute(model.getSelectedDateTime().getMinuteOfHour());
        timePicker.setOnTimeChangedListener(this);
        final FragmentManager childFragmentManager = getChildFragmentManager();


        dateLabel = (TextView) rootView.findViewById(R.id.label_date);
        dateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.setOnDateSetListener(TimeDialogFragment.this);
                fragment.setDepartureArrivalModel(model);
                fragment.show(childFragmentManager, "datePicker");
            }
        });
        setDate();
        builder.setView(rootView);
        return builder.create();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        model.setSelectedDateTime(model.getSelectedDateTime().withYear(year).withMonthOfYear(month).withDayOfMonth(dayOfMonth));
        setDate();
    }

    private void setDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        dateLabel.setText(getResources().getString(R.string.label_date) + " " + dateFormat.format(model.getSelectedDateTime().toDate()));
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        model.setSelectedDateTime(model.getSelectedDateTime().withHourOfDay(hourOfDay).withMinuteOfHour(minute));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        model.setDeparture(checkedId == R.id.radio_departure);
    }
}



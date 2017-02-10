package io.github.imsmobile.fahrplan.ui;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

import io.github.imsmobile.fahrplan.R;

/**
 * Created by sandro on 09.02.2017.
 */
public class ProgressDialogUI extends ProgressDialog {

    public ProgressDialogUI(FragmentActivity activity) {
        super(activity);
        setProgressStyle(ProgressDialog.STYLE_SPINNER);
        setMessage(activity.getResources().getString(R.string.waiting_text));
        setIndeterminate(true);
        setCanceledOnTouchOutside(false);
    }
}

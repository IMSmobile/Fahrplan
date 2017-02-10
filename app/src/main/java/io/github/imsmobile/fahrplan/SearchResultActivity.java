package io.github.imsmobile.fahrplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.imsmobile.fahrplan.task.ConnectionSearchTask;
import io.github.imsmobile.fahrplan.ui.ProgressDialogUI;

public class SearchResultActivity extends AppCompatActivity {

    private TextView textView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String from = intent.getStringExtra(MainActivity.FROM_MESSAGE);
        String to = intent.getStringExtra(MainActivity.TO_MESSAGE);
        textView = new TextView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.content);
        layout.addView(textView);
        startSearch(from, to);
    }

    private void startSearch(String from, String to) {
        new ConnectionSearchTask(this).execute(from, to);
    }

    public void setResult(String text) {
        textView.setText(text);
    }

    public void startProgressDialog() {
        dialog = new ProgressDialogUI(this);
        dialog.show();
    }

    public void stopProcessDialog() {
        dialog.dismiss();
    }
}

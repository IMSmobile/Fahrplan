package io.github.imsmobile.fahrplan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        String from = intent.getStringExtra(MainActivity.FROM_MESSAGE);
        String to = intent.getStringExtra(MainActivity.TO_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText("From: " + from + " To:" + to);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_search_result);
        layout.addView(textView);
    }
}

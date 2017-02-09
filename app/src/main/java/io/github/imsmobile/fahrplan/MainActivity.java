package io.github.imsmobile.fahrplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerSearchButton();
    }

    private void registerSearchButton() {
        Button buttonOne = (Button) findViewById(R.id.btn_search);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                search();
            }
        });
    }

    private void search() {
        Intent intent = new Intent(this, SearchResultActivity.class);
        startActivity(intent);
    }
}

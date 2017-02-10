package io.github.imsmobile.fahrplan;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends BaseActivity{
    public final static String FROM_MESSAGE = "io.github.imsmoble.fahrplan.from";
    public final static String TO_MESSAGE = "io.github.imsmoble.fahrplan.to";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
        registerSearchButton();
    }



    private void registerSearchButton() {
        Button buttonOne = (Button) findViewById(R.id.btn_search);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity();
            }
        });
    }

    private void startActivity() {
        Intent intent = new Intent(this, SearchResultActivity.class);
        EditText fromText = (EditText) findViewById(R.id.input_from);
        String from = fromText.getText().toString();

        EditText toText = (EditText) findViewById(R.id.input_to);
        String to = toText.getText().toString();
        intent.putExtra(FROM_MESSAGE, from);
        intent.putExtra(TO_MESSAGE, to);
        startActivity(intent);
    }
}

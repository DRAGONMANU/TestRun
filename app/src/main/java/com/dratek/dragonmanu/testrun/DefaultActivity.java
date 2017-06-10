package com.dratek.dragonmanu.testrun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DefaultActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        final EditText defaulton       = (EditText) findViewById(R.id.defaulton);
        final EditText defaultoff       = (EditText) findViewById(R.id.defaultoff);

        defaulton.setText(sharedpreferences.getString("defaulton",""));
        defaultoff.setText(sharedpreferences.getString("defaultoff",""));

        final Button setdefaultButton = (Button) findViewById(R.id.setdefault);

        setdefaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendback = new Intent(getApplicationContext(),MainActivity.class);
                sendback.setType("plain/text");
                sendback.putExtra("defaulton",defaulton.getText().toString());
                sendback.putExtra("defaultoff",defaultoff.getText().toString());
                setResult(RESULT_OK, sendback);
                DefaultActivity.super.finish();
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        });
    }
    @Override
    public void onBackPressed() {
        DefaultActivity.super.finish();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
}

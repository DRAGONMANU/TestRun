package com.dratek.dragonmanu.testrun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WifiActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        final EditText ssid       = (EditText) findViewById(R.id.ssid);
        final EditText pass       = (EditText) findViewById(R.id.pass);
        ssid.setText(sharedpreferences.getString("ssid",""));
        pass.setText(sharedpreferences.getString("pass",""));
        final Button setswitch = (Button) findViewById(R.id.setit);

        setswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendback = new Intent(getApplicationContext(),MainActivity.class);
                sendback.setType("plain/text");
                sendback.putExtra("message","wifi"+ssid.getText().toString()+","+pass.getText().toString());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("ssid", ssid.getText().toString());
                editor.putString("pass",pass.getText().toString());
                editor.commit();
                setResult(RESULT_OK, sendback);
                WifiActivity.super.finish();
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        });
    }
}

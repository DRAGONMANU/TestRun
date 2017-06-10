package com.dratek.dragonmanu.testrun;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class IpportActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipport);
        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        final EditText ip = (EditText) findViewById(R.id.ip);
        final EditText port = (EditText) findViewById(R.id.port);
        ip.setText(sharedpreferences.getString("ip",""));
        port.setText(sharedpreferences.getString("port",""));
        final Button setswitch = (Button) findViewById(R.id.button);

        setswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendback = new Intent(getApplicationContext(),MainActivity.class);
                sendback.setType("plain/text");
                sendback.putExtra("message","ippo"+ip.getText().toString()+","+port.getText().toString());
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("ip", ip.getText().toString());
                editor.putString("port",port.getText().toString());
                editor.commit();
                setResult(RESULT_OK, sendback);
                IpportActivity.super.finish();
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        });
    }
}

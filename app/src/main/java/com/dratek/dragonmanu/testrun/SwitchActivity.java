package com.dratek.dragonmanu.testrun;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SwitchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);

        final EditText switchconfig       = (EditText) findViewById(R.id.switchinput);

        final Button setswitch = (Button) findViewById(R.id.switchit);

        setswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendback = new Intent(getApplicationContext(),MainActivity.class);
                sendback.setType("plain/text");
                sendback.putExtra("message","swit"+switchconfig.getText().toString());
                setResult(RESULT_OK, sendback);
                SwitchActivity.super.finish();
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
            }
        });
    }
}

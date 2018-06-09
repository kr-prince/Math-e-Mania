package com.example.prince.math_e_mania;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Button b1, b2, b3, b4;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.playbutton);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.instbutton);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.hofbutton);
        b3.setOnClickListener(this);
        b4 = (Button) findViewById(R.id.settingsbutton);
        b4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.playbutton :
                intent = new Intent(this, GamePlay.class);
                intent.putExtra("PlayGame", 1);
                startActivity(intent);
                break;

            case R.id.instbutton :
                intent = new Intent(this, Instructions.class);
                intent.putExtra("Instructions", 2);
                startActivity(intent);
                break;

            case R.id.hofbutton :
                intent = new Intent(this, HallOfFame.class);
                intent.putExtra("HallOfFame", 3);
                startActivity(intent);
                break;

            case R.id.settingsbutton :
                intent = new Intent(this, Settings.class);
                intent.putExtra("Settings", 4);
                startActivity(intent);
                break;

        }
    }
}

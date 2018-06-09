package com.example.prince.math_e_mania;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by Prince on 27/4/2015.
 */
public class HallOfFame extends ActionBarActivity {

    SharedPreferences sharedpref;
    TextView name, score, speed, nopsolved, date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.halloffame);
        sharedpref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        initialize();
    }

    private void initialize() {

        name = (TextView) findViewById(R.id.name);
        score = (TextView) findViewById(R.id.score);
        speed = (TextView) findViewById(R.id.speed);
        nopsolved = (TextView) findViewById(R.id.nopsolved);
        date = (TextView) findViewById(R.id.date);
        display();
    }

    private void display() {

        if (sharedpref.getString("topname", null) == null)
            name.setText("No Record Found");
        else {
            name.setText(sharedpref.getString("topname", null));
            score.setText("Score - "+sharedpref.getLong("topscore", 0));
            speed.setText("Speed - "+String.format("%.2f",sharedpref.getFloat("topspeed", (float) 0.00))+"s");
            nopsolved.setText("Problems - "+sharedpref.getInt("topnop", 0));
            date.setText(sharedpref.getString("topdate", null));
        }
    }

}

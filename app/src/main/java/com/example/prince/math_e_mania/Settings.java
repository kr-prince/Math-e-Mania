package com.example.prince.math_e_mania;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Prince on 23/4/2015.
 */

public class Settings extends ActionBarActivity {

    RadioGroup level;
    RadioButton selectedlevel, level1, level2, level3;
    Button save;
    SharedPreferences sharedpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        level = (RadioGroup) findViewById(R.id.gamelevel);
        save = (Button) findViewById(R.id.savebutton);

        initialize();
        addListenerOnButton();
    }

    private void initialize() {

        sharedpref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        level1 = (RadioButton) findViewById(R.id.level1);
        level2 = (RadioButton) findViewById(R.id.level2);
        level3 = (RadioButton) findViewById(R.id.level3);

        switch (sharedpref.getInt("level", 2)) {
            case 1 :
                level1.setChecked(true);
                break;
            case 2 :
                level2.setChecked(true);
                break;
            case 3 :
                level3.setChecked(true);
        }
    }

    public void addListenerOnButton() {

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectedlevel = (RadioButton) findViewById(level.getCheckedRadioButtonId());
                SharedPreferences.Editor editor = sharedpref.edit();
                switch (selectedlevel.getId()) {
                    case R.id.level1 :
                        editor.putInt("level", 1);
                        break;

                    case R.id.level2 :
                        editor.putInt("level", 2);
                        break;

                    case R.id.level3 :
                        editor.putInt("level", 3);
                        break;
                }
                if (editor.commit()) {
                    Toast.makeText(Settings.this, "Save Successful",Toast.LENGTH_LONG).show();
                    Settings.this.finish();
                }
                else {
                    Toast.makeText(Settings.this, "Oooops. \nPlease try again",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

package com.example.prince.math_e_mania;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Prince on 16/4/2015.
 */
public class GamePlay extends ActionBarActivity implements View.OnClickListener {

    TextView counter, problem, score, lives;
    ProgressBar mProgressBar;
    Button badd, bsub, bmult, bdiv;
    CountDownTimer mytimer;
    Random rnum;
    boolean sol;
    long scr;
    double totaltime;
    int seconds, chances, ques[], op, temp, x, totalproblems, level;
    SharedPreferences sharedpref;
    String expr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sharedpref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
    }

    private void initialize() {

        mProgressBar=(ProgressBar)findViewById(R.id.progressBar);
        counter = (TextView) findViewById(R.id.counter);
        problem = (TextView) findViewById(R.id.problem);
        lives = (TextView) findViewById(R.id.lives);
        score = (TextView) findViewById(R.id.score);
        badd = (Button) findViewById(R.id.addsign);
        badd.setOnClickListener(this);
        bsub = (Button) findViewById(R.id.subsign);
        bsub.setOnClickListener(this);
        bmult = (Button) findViewById(R.id.multsign);
        bmult.setOnClickListener(this);
        bdiv = (Button) findViewById(R.id.divsign);
        bdiv.setOnClickListener(this);
        chances = 3;  ques = new int[5];
        scr = 0; totalproblems = 0; totaltime = 0.0;
        level = sharedpref.getInt("level", 2);
        if (level == 1) level = 32;
        else if (level == 2) level = 22;
        else level = 12;
        mProgressBar.setMax(level-2);
        mProgressBar.setProgress(level-2);

        startGame();
    }

    private void startGame() {
        setproblem();
        starttimer();
    }

    private void setproblem() {

        rnum = new Random();
        expr = new String();
        op = 0; temp = 0; x = 0;
        sol = false;
        counter.setText(""+(level-2));

        while( x < 4 ) {
            ques[x] = rnum.nextInt(9) + 1;
            if (op == 3) {
                if (temp % ques[x] != 0) {
                    int t = 9;
                    while(temp % t != 0)
                        t--;
                    ques[x] = t;
                }
            }
            expr += Integer.toString(ques[x]);
            calculate();
            if (x < 3) {
                op = rnum.nextInt(4);
                expr += "_";
            }
            ++x;
        }
        ques[4] = temp;
        expr += ( " = " + Integer.toString(temp));
        problem.setText(expr);
        totalproblems += 1;
        x = 1; temp = ques[0];
    }

    private void calculate() {

        if (op == 0)
            temp += ques[x];
        else if (op == 1)
            temp -= ques[x];
        else if (op == 2)
            temp *= ques[x];
        else if (op == 3)
            temp /= ques[x];
    }

    private void starttimer() {

        if (mytimer != null)
            mytimer.cancel();
        mytimer = new CountDownTimer(level*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished/1000) -1;
                counter.setText(String.valueOf(seconds/10) +
                        String.valueOf(seconds%10));
                mProgressBar.setProgress(seconds);

                if (seconds == 0) onFinish();
            }

            @Override
            public void onFinish() {
                mytimer.cancel();
                if (sol) {
                    scr += (32-level+seconds)*10;
                    totaltime += (level-2-seconds);
                    score.setText(""+scr);
                }
                else {
                    --chances;
                    if (chances == 2)
                        lives.setText("**");
                    else if (chances == 1)
                        lives.setText("*");
                    else
                        lives.setText("");
                }
                if (chances > 0)
                    startGame();
                else {
                    endTheGame();
                    checkforhighscore();
                }
            }
        };
        mytimer.start();
    }

    private void endTheGame() {

        if (isFinishing())
            return;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        alertDialogBuilder.setTitle("Your Score");

        // set dialog message
        alertDialogBuilder
                .setMessage("Attempted Problems = "+totalproblems+
                            "\nAverage Solving Speed = "+String.format("%.2f", totaltime/(totalproblems-3))+"s"+
                            "\nScore = "+scr)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity after checking for high score
                        GamePlay.this.finish();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void checkforhighscore() {

        String tname = sharedpref.getString("topname", null);
        long tscore = sharedpref.getLong("topscore", 0);
        float tspeed = sharedpref.getFloat("topspeed", (float) 0.00);
        boolean newtopper = true;

        if (tname != null) {
            if (tscore < scr)
                newtopper = true;
            else if ((tscore == scr)&&(tspeed < (totaltime/(totalproblems-3))))
                newtopper = true;
            else newtopper = false;
        }
        if ((tname == null)||(newtopper)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Record!!");
            builder.setMessage("Enter your first name");
            final EditText inputField = new EditText(this);
            builder.setView(inputField);
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    SharedPreferences.Editor editor = sharedpref.edit();
                    editor.putString("topname", inputField.getText().toString());
                    editor.putLong("topscore", scr);
                    editor.putFloat("topspeed", (float) (totaltime/(totalproblems-3)));
                    editor.putInt("topnop", totalproblems - 3);
                    final Calendar c = Calendar.getInstance();
                    String tdate = (""+c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                    editor.putString("topdate", tdate);
                    editor.commit();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start) {
            initialize();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addsign :
                expr = expr.replaceFirst("_","+");
                temp += ques[x];
                break;

            case R.id.subsign :
                expr = expr.replaceFirst("_","-");
                temp -= ques[x];
                break;

            case R.id.multsign :
                expr = expr.replaceFirst("_","x");
                temp *= ques[x];
                break;

            case R.id.divsign :
                expr = expr.replaceFirst("_","/");
                temp /= ques[x];
                break;
        }
        problem.setText(expr);
        ++x;
        if (x == 4) {
            if (temp == ques[4])
                sol = true;
            mytimer.onFinish();
        }
    }
}

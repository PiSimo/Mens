package org.pisimo.mens;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

public class TestActivity9 extends AppCompatActivity {

    public void goToNextTest(boolean wasRight)  {
        //Create scores file because this is the first test
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity9.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity9.this, "Wrong", Toast.LENGTH_SHORT).show();
                fos.write("0\n".getBytes());
            }
            fos.close();
        }catch (Exception e){
            System.err.print("|'('o')'| Error:"+e);
        }
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test9);

        String[] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

        Date d = new Date();
        Random rand = new Random();

        int currentDay = d.getDay();
        TextView question = (TextView)findViewById(R.id.test9Question);
        RadioButton[] rbs = {(RadioButton)findViewById(R.id.test9opt0),(RadioButton)findViewById(R.id.test9opt1),(RadioButton)findViewById(R.id.test9opt2)};

        //question.setText("");
        int backSteps = rand.nextInt(4)+1;
        int backDay = currentDay - backSteps;
        if(backDay < 0)backDay = 7 + backDay;
        question.setText(backSteps+" days ago it was:");

        final int pickTheOne = rand.nextInt(3);
        rbs[pickTheOne].setText(days[backDay]);
        for(int i = 0;i != 3;i++){
            if(i != pickTheOne){
                String c = "";
                do{
                    c = days[rand.nextInt(7)];
                }while (rbs[0].getText().toString().indexOf(c) != -1 || rbs[1].getText().toString().indexOf(c) != -1 || rbs[2].getText().toString().indexOf(c) != -1);
                rbs[i].setText(c);
            }
        }

        rbs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pickTheOne == 0)goToNextTest(true);
                else goToNextTest(false);
            }
        });
        rbs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pickTheOne == 1)goToNextTest(true);
                else goToNextTest(false);
            }
        });
        rbs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pickTheOne == 2)goToNextTest(true);
                else goToNextTest(false);
            }
        });

    }
}

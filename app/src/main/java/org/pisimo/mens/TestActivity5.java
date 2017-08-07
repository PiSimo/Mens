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

public class TestActivity5 extends AppCompatActivity {

    public void goToNextTest(boolean wasRight)  {
        //Create scores file because this is the first test
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity5.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity5.this, "Wrong", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_test5);

        String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        Date d = new Date();
        Random rand = new Random();

        int currentMonth = d.getMonth();
        int diff = rand.nextInt(4)+1;
        int diffMonth = currentMonth - diff;
        if(diffMonth <0)diffMonth = 12 - diffMonth;

        String titleTxt = diff+" months ago we were in:";
        TextView tv = (TextView)findViewById(R.id.upText);
        tv.setText(titleTxt);

        RadioButton[] rbs = {(RadioButton)findViewById(R.id.radioButton7),(RadioButton)findViewById(R.id.radioButton8),(RadioButton)findViewById(R.id.radioButton9)};
        final int correctOne = rand.nextInt(3);

        rbs[correctOne].setText(months[diffMonth]);


        for(int i = 0;i != 3;i++){

            if(i != correctOne){
                int random = 1000;
                do{
                    random = rand.nextInt(10)+rand.nextInt(3);
                }while(rbs[0].getText().toString().indexOf(months[random]) != -1 || rbs[1].getText().toString().indexOf(months[random]) != -1 || rbs[2].getText().toString().indexOf(months[random]) != -1);
                rbs[i].setText(months[random]);
            }

        }

        //RadioButton events
        rbs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctOne == 0)goToNextTest(true);
                else goToNextTest(false);
            }
        });

        rbs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctOne == 1)goToNextTest(true);
                else goToNextTest(false);
            }
        });

        rbs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctOne == 2)goToNextTest(true);
                else goToNextTest(false);
            }
        });


    }
}

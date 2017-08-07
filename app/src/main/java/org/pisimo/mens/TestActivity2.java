package org.pisimo.mens;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

public class TestActivity2 extends AppCompatActivity {

    public void goToNextTest(boolean wasRight)  {
        //Create scores file because this is the first test
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity2.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity2.this, "Wrong", Toast.LENGTH_SHORT).show();
                fos.write("0\n".getBytes());
            }
            fos.close();
            Intent test2 = new Intent(TestActivity2.this, TestActivity3.class);
            startActivity(test2);
        }catch (Exception e){
            System.err.print("|'('o')'| Error:"+e);
        }
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        Date d = new Date();
        Random rand = new Random();
        int currentYear = d.getYear() +1900;
        int[] confusedYears = new int[3];
        for(int i = 0;i != 3;i++){
            int x = 0;
            for(;;){
                 x = currentYear - (rand.nextInt(10)+1);
                 if(x!=confusedYears[0] && x != confusedYears[1] && x != confusedYears[2])break;
            }
            confusedYears[i] = x;
        }

        final int pick = rand.nextInt(2);
        final int pickOne = confusedYears[pick];

        TextView question = (TextView)findViewById(R.id.textView21);
        question.setText((currentYear - pickOne)+ " years ago we were in the?");


        final RadioButton r1 = (RadioButton)findViewById(R.id.radioButton);
        final RadioButton r2 = (RadioButton)findViewById(R.id.radioButton2);
        final RadioButton r3 = (RadioButton)findViewById(R.id.radioButton3);
        final RadioButton[] rs = {r1,r2,r3};

        for(int i = 0;i != 3;i++){
            rs[i].setText(""+confusedYears[i]);
        }

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pick == 0) {
                    goToNextTest(true);

                }else goToNextTest(false);
            }
        });

        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pick == 1) {
                    goToNextTest(true);

                }else goToNextTest(false);
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pick == 2) {
                    goToNextTest(true);

                }else goToNextTest(false);
            }
        });


    }
}

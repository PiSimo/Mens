package org.pisimo.mens;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestActivity4 extends AppCompatActivity {

    volatile ProgressBar pb = null;
    volatile int sharedI = 0;
    volatile int wrongOne = 0;
    volatile Handler h = new Handler();
    volatile TextView textFormula = null;
    volatile TextView topText  = null;
    volatile RadioButton[] rbs = new RadioButton[3];
    volatile boolean testTime = false;

    public void goToNextTest(boolean wasRight)  {
        //Create scores file because this is the first test
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity4.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity4.this, "Wrong", Toast.LENGTH_SHORT).show();
                fos.write("0\n".getBytes());
            }
            fos.close();
            Intent test2 = new Intent(TestActivity4.this, TestActivity5.class);
            startActivity(test2);
        }catch (Exception e){
            System.err.print("|'('o')'| Error:"+e);
        }
        testTime = false;
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);

        ////
        pb = (ProgressBar)findViewById(R.id.progressBar);
        textFormula = (TextView)findViewById(R.id.formula);
        topText = (TextView)findViewById(R.id.textView27);
        rbs[0] = (RadioButton)findViewById(R.id.radioButton4);
        rbs[1] = (RadioButton)findViewById(R.id.radioButton5);
        rbs[2] = (RadioButton)findViewById(R.id.radioButton6);

        Random rand = new Random();

        //Chemical array
        String[] elements = {"S","Ti","Mo","Ne","Os","Hg","Br","Xe","Si","Rb","Tc","Mn","Ar","Hf","Ta","Fr","Co"};
        List<String> list = new ArrayList<>();
        for(int i=1;i < elements.length;i++){
            list.add(elements[i]);
        }
        Collections.shuffle(list);

        //Display a random (FAKE) compound
        String compound = "";
        int len = 4+rand.nextInt(3);
        for(int i =0;i != len;i++){
            compound += list.get(i)+" ";
        }


        textFormula.setText(compound);

        //Set up radioButtons
        //set text
        wrongOne = rand.nextInt(3);
        int tmp = 60;
        for(int i = 0;i != 3;i++){
            if(wrongOne == i)rbs[i].setText(list.get(len+1));
            else{
                int pick;
                do{
                    pick = rand.nextInt(len);
                }while(pick == tmp);
                rbs[i].setText(list.get(pick));
                tmp = pick;
            }
        }
        //set listener
        rbs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (wrongOne == 0) goToNextTest(true);
                    else goToNextTest(false);
                }
            }
        });
        rbs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (wrongOne == 1) goToNextTest(true);
                    else goToNextTest(false);
                }
            }
        });
        rbs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (wrongOne == 2) goToNextTest(true);
                    else goToNextTest(false);
                }
            }
        });



        //Time and question thread:
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 7;i != -1;i--){
                    sharedI = i;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            if(Build.VERSION.SDK_INT > 24) {
                                pb.setProgress(sharedI * 100 / 7,true);
                            }
                            else pb.setProgress(sharedI * 100 / 7);

                            //Removed due to compatibility issues
                            // pb.setProgress((int)(sharedI*100)/7,true);
                        }
                    }) ;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        topText.setText("Which of those elements where not in the formula?");
                        textFormula.setVisibility(View.INVISIBLE);
                        pb.setVisibility(View.INVISIBLE);
                        for(RadioButton r:rbs){
                            r.setVisibility(View.VISIBLE);
                        }
                    }
                });

                testTime = true;
            }
        }).start();
    }
}

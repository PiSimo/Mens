package org.pisimo.mens;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestActivity8 extends AppCompatActivity {

    volatile TextView[] optList = new TextView[5];
    volatile String[] options = new String[5];
    volatile Handler h = new Handler();
    volatile boolean testTime = false;
    volatile TextView topText = null;
    volatile ProgressBar pb = null;
    volatile EditText answ = null;
    volatile Button btn = null;
    volatile String item = "";
    volatile int nItem = -1;
    volatile int sharedI = 0;

    public void goToNextTest(boolean wasRight)  {
        //Create scores file because this is the first test
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity8.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity8.this, "Wrong", Toast.LENGTH_SHORT).show();
                fos.write("0\n".getBytes());
            }
            fos.close();
            Intent test2 = new Intent(TestActivity8.this, TestActivity9.class);
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
        setContentView(R.layout.activity_test8);
        Random rand = new Random();

        //Setting UI elements
        optList[0] = (TextView)findViewById(R.id.test8opt0);
        optList[1] = (TextView)findViewById(R.id.test8opt1);
        optList[2] = (TextView)findViewById(R.id.test8opt2);
        optList[3] = (TextView)findViewById(R.id.test8opt3);
        optList[4] = (TextView)findViewById(R.id.test8opt4);

        pb = (ProgressBar)findViewById(R.id.test8pb);
        topText = (TextView)findViewById(R.id.test8topText);
        btn = (Button)findViewById(R.id.test8btn1);
        answ = (EditText)findViewById(R.id.test8editTxt);

        //set button listener
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime){
                    String answer = answ.getText().toString();
                    if(answer.indexOf(String.valueOf(nItem)) == -1)goToNextTest(false);
                    else goToNextTest(true);
                }
            }
        });

        //Items
        final String[] items = {"Milk bottles","Eggs","Bread loaves","Tomatoes","Cheeses","Steakes","Flour Packages","Cakes","Onions","Garlics","Mushrooms","Pasta Packages","Juices"};
        List<String> itemsList = new ArrayList<>();
        for(int i = 0;i != items.length;i++)itemsList.add(items[i]);

        //Shuffle items
        Collections.shuffle(itemsList);


        for(int i = 0;i != 5;i++){
            options[i] = itemsList.get(i);
        }

        int randomPick = rand.nextInt(options.length);
        item = options[randomPick];

        for(int i = 0;i != 5;i++){
            int quant = rand.nextInt(3)+1;
            optList[i].setText("->   "+quant+" x "+options[i]);
            if(randomPick == i)nItem = quant;
        }



        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 9;i != -1;i--){
                    sharedI = i;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            if(Build.VERSION.SDK_INT > 24) {

                                pb.setProgress(sharedI * 100 / 9, true);
                            }else pb.setProgress(sharedI*100/9);
                            //Removed due to compatibility issues
                            // pb.setProgress((int)(sharedI*100)/9,true);
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        System.err.println("AIAIAI :"+e);
                    }
                }

                //Make anything magically be invisible
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        for(TextView tv :optList){
                            tv.setVisibility(View.INVISIBLE);
                        }
                        topText.setText("How many "+item+" you have to buy?");
                        btn.setVisibility(View.VISIBLE);
                        answ.setVisibility(View.VISIBLE);
                        testTime = true;

                    }
                });
            }
        }).start();
    }
}

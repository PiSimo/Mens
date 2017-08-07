package org.pisimo.mens;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestActivity6 extends AppCompatActivity {

    volatile ProgressBar pb = null;
    volatile ImageView imgv = null;
    volatile TextView  txtv = null;
    volatile ImageView[] opts = new ImageView[3];
    volatile Handler h = new Handler();

    volatile boolean comingFromTestActivity7 = false;
    volatile int sharedI = 0;
    volatile int randomOne = -1;
    volatile boolean testTime = false;


    public void goToNextTest(boolean wasRight)  {
        //Create scores file because this is the first test
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity6.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity6.this, "Wrong", Toast.LENGTH_SHORT).show();
                fos.write("0\n".getBytes());
            }
            fos.close();
            Intent test2 = new Intent(TestActivity6.this, TestActivity7.class);
            if(!comingFromTestActivity7)startActivity(test2);
        }catch (Exception e){
            System.err.print("|'('o')'| Error:"+e);
        }
        testTime = false;
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test6);
        Random rand = new Random();
        AssetManager am = getAssets();

        //n-1 are the displayed icons; nth icon is the intruder
        int n =8;

        //Icons position
        int[]posXs = {270,60,130,400,520,270,480};
        int[] posYs = {50,250,500,510,350,300,170};

        //Recycling this test
        int countLines = 0;
        try{
            FileInputStream fis = openFileInput("scores.txt");
            int c = 0;
            while((c = fis.read()) != -1){
                if((char)c == '\n')countLines++;
            }
        }catch (Exception e){
            System.err.println(e);
        }
        if(countLines >8) {
            comingFromTestActivity7 = true;
            TextView testN = (TextView) findViewById(R.id.textView29);
            testN.setText("Test 10/11");
            n = 6;
            posXs[3] = 450;
            posYs[3] = 500;
            posXs[4] = 500;
            posYs[4] = 250;

        }

        txtv = (TextView)findViewById(R.id.textView30);
        pb = (ProgressBar)findViewById(R.id.progressBar2);
        opts[0] = (ImageView)findViewById(R.id.test6Opt1);
        opts[1] = (ImageView)findViewById(R.id.test6Opt2);
        opts[2] = (ImageView)findViewById(R.id.test6Opt3);


        //Settin' options event
        opts[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (randomOne == 0)goToNextTest(true);
                    else goToNextTest(false);
                }
            }
        });
        opts[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (randomOne == 1)goToNextTest(true);
                    else goToNextTest(false);
                }
            }
        });
        opts[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (randomOne == 2)goToNextTest(true);
                    else goToNextTest(false);
                }
            }
        });
        //Loading icons
        List<String> list = new ArrayList<>();
        for(int i=1;i < 35;i++){
            list.add(i+".png");
        }
        Collections.shuffle(list); //Random every time different

        //7 icons for grup 8th intruder
        String[] icons = new String[n];
        for(int i = 0;i != n;i++){
            icons[i] = list.get(i);
        }

        //Set image to the invisible options
        randomOne = rand.nextInt(3);
        int randSomething = rand.nextInt(n-1);
        for(int i =  0;i != 3;i++){
            if(i == randomOne){

                try {
                    InputStream f  = am.open("icons/"+icons[n-1]);
                    Bitmap bt = BitmapFactory.decodeStream(f);
                    opts[i].setImageBitmap(bt);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                try {
                    InputStream f  = am.open("icons/"+icons[randSomething]);
                    Bitmap bt = BitmapFactory.decodeStream(f);
                    opts[i].setImageBitmap(bt);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(randSomething == n-2)randSomething -= 3;
                else randSomething += 1;
            }

        }

        imgv = (ImageView)findViewById(R.id.imageView2);
        Bitmap b = Bitmap.createBitmap(700,700, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(350,350,350,p);


        for(int i = 0;i != n-1;i++){
            try {
                InputStream f = am.open("icons/"+icons[i]);
                Bitmap exp = BitmapFactory.decodeStream(f);
                c.drawBitmap(exp, posXs[i],posYs[i], p);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        imgv.setImageBitmap(b);

        //Count 7s and turn activty to question
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = 8;i != -1;i--){
                    sharedI = i;
                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            if(Build.VERSION.SDK_INT > 24) {

                                pb.setProgress(sharedI * 100 / 8,true);
                            }
                            else pb.setProgress(sharedI*100/8);
                            //Removed due to compatibility issues
                            // pb.setProgress((int)(sharedI*100)/8,true);
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        System.err.println("Aiaiaia un error:"+e);
                    }
                }

                //Make things disappear(magic) and ask question:
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        pb.setVisibility(View.INVISIBLE);
                        imgv.setVisibility(View.INVISIBLE);
                        txtv.setText("Find the intruder.");
                        for(int i = 0;i != 3;i++){
                            opts[i].setVisibility(View.VISIBLE);
                        }
                        testTime = true;
                    }
                });
            }
        }).start();
    }
}

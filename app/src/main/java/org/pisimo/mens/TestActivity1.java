package org.pisimo.mens;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class TestActivity1 extends AppCompatActivity {

    volatile Handler  handler = new Handler();
    volatile ProgressBar pb = null;
    volatile TextView    remaningTime = null;
    volatile TextView    topText = null;
    volatile ImageView   askIcon = null;

    volatile String askIconName = "";
    volatile int sharedI = 0;
    volatile int rightClass = 0;
    volatile boolean testTime = false;


    // https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public void goToNextTest(boolean wasRight)  {
        //Create scores file because this is the first test
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity1.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity1.this, "Wrong", Toast.LENGTH_SHORT).show();
                fos.write("0\n".getBytes());
            }
            fos.close();
            Intent test2 = new Intent(TestActivity1.this, TestActivity2.class);
            startActivity(test2);
        }catch (Exception e){
        }
        testTime = false;
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);

        try{
            FileOutputStream fos = openFileOutput("scores.txt",MODE_PRIVATE);
            String initialTime = System.currentTimeMillis()+"\n";              //Write into scores file when the test began
            fos.write(initialTime.getBytes());
            fos.close();
        }catch (Exception e){
            finish();
        }

        pb = (ProgressBar)findViewById(R.id.cucuBar);
        remaningTime = (TextView)findViewById(R.id.textView22);
        topText = (TextView)findViewById(R.id.textView16);
        askIcon = (ImageView)findViewById(R.id.askIcon);
        ImageView[] imgViews = {(ImageView)findViewById(R.id.imageView7),(ImageView)findViewById(R.id.imageView8),(ImageView)findViewById(R.id.imageView10)};


        //Click listener on each group (are disabled till testime gets true)
        imgViews[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime){
                    if(rightClass == 0){goToNextTest(true);}
                    else goToNextTest(false);
                }
            }
        });
        imgViews[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime){
                    if(rightClass == 1){goToNextTest(true);}
                    else goToNextTest(false);
                }
            }
        });
        imgViews[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime){
                    if(rightClass == 2){goToNextTest(true);}
                    else goToNextTest(false);
                }
            }
        });

        //Shuffle iconNames to have always different icon groups
        Random rand = new Random();
        List<String> list = new ArrayList<>();
        for(int i=1;i < 35;i++){
            list.add(i+".png");
        }
        Collections.shuffle(list);

        //5 icons for each group(3*5),the last icon will be asked
        String[] iconNames = new String[12];
        for(int i = 0;i != 12;i++){
            iconNames[i] = list.get(i);
        }

        //icon for question and his belonging class
        int pick = rand.nextInt(12);
        askIconName = iconNames[pick];
        rightClass = pick /4;

        //ImageViews:
        for(int i = 0;i != 3;i++){
            ImageView imgView = imgViews[i];
            Bitmap b = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setStyle(Paint.Style.FILL);
            c.drawCircle(300,300,300,p);  //Background circle
            imgView.setImageBitmap(b);

            AssetManager am = getAssets(); //Load icons from asset

            //Icons position:
            int[] posXs = {250,60,250,440};
            int[] posYs = {130,270,450,270};

            for(int t = 0;t != 4;t++) {
                try {
                    InputStream f = am.open("icons/"+iconNames[(i*4)+t]);
                    Drawable d = Drawable.createFromStream(f, null);
                    Bitmap bmp = drawableToBitmap(d);
                    c.drawBitmap(bmp, posXs[t], posYs[t], p);
                } catch (IOException e) {

                    Toast.makeText(TestActivity1.this
                            , ""+e, Toast.LENGTH_SHORT).show();
                }
            }
            imgView.setImageBitmap(b);
        }


        //Time-Keeper and action after 10s Thread:
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Give 10s to look at the icons
                for(int i = 10;i !=-1;i--) {
                    sharedI = i;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(Build.VERSION.SDK_INT > 24){
                                    pb.setProgress(sharedI *10,true);

                                }
                                else pb.setProgress(sharedI *10);


                            }catch (Exception e){
                            }
                            remaningTime.setText(sharedI+"s");          //Decrement text with time
                        }
                    });
                    try {
                        Thread.sleep(1000); //Total :10s
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }


                //Make groups become the buttons
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        AssetManager am = getAssets();

                        //Set test-time to true so that if a group gets pressed it will be considered as the answer
                        testTime = true;

                        //(lazy) implementation
                        ImageView[] imgvs = {(ImageView)findViewById(R.id.imageView7),(ImageView)findViewById(R.id.imageView8),(ImageView)findViewById(R.id.imageView10)};
                        TextView[]  txtvs = {(TextView)findViewById(R.id.textView17),(TextView)findViewById(R.id.textView19),(TextView)findViewById(R.id.textView20)};
                        String[] numbers = {"two.png","one.png","three.png"};
                        for(int i = 0;i != 3;i++){
                            ImageView imgv = imgvs[i];
                            TextView txtv = txtvs[i];
                            txtv.setVisibility(View.INVISIBLE);
                            //TOADD overwrite buttons with x color and if they are pressed KABBOM
                            Bitmap b = Bitmap.createBitmap(600,600, Bitmap.Config.ARGB_8888);
                            Canvas c = new Canvas(b);
                            Paint p = new Paint();
                            p.setColor(getResources().getColor(R.color.orangio));
                            p.setStyle(Paint.Style.FILL);
                            c.drawCircle(300,300,200,p);  //Background circle

                            //Draw The numbers inside the circle
                            InputStream f = null;
                            try {
                                f = am.open("icons/"+numbers[i]);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Drawable d = Drawable.createFromStream(f, null);
                            Bitmap bmp = drawableToBitmap(d);
                            c.drawBitmap(bmp,220,210, p);

                            imgv.setImageBitmap(b);

                        }
                        //Time reminder invisible
                        pb.setVisibility(View.INVISIBLE);
                        remaningTime.setVisibility(View.INVISIBLE);

                        topText.setText("In which group was the icon?");
                        try {
                            askIcon.setImageDrawable(Drawable.createFromStream(am.open("icons/"+askIconName),null));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


            }
        }).start();


    }



}






package org.pisimo.mens;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class TestActivity11 extends AppCompatActivity {

    volatile TextView painterName = null;
    volatile TextView question = null;
    volatile TextView testTxt = null;

    volatile RadioButton[] rbs = new RadioButton[3];

    volatile ProgressBar pb = null;

    volatile ImageView imgv = null;

    volatile Handler h = new Handler();

    //vars
    volatile boolean testTime = true;
    volatile int rightOne = 0;
    volatile int sharedI = 0;

    public void thisIsTheEnd(boolean wasRight){
        try {
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_APPEND);
            if (wasRight) {
                Toast.makeText(TestActivity11.this, "Correct", Toast.LENGTH_SHORT).show();
                fos.write("1\n".getBytes());
            } else {
                Toast.makeText(TestActivity11.this, "Wrong", Toast.LENGTH_SHORT).show();
                fos.write("0\n".getBytes());
            }
            String endTime = System.currentTimeMillis()+"";
            fos.write(endTime.getBytes());
            fos.close();

        }catch (Exception e){
            System.err.print("|'('o')'| Error:"+e);
        }
        testTime = false;


        //Adding settings file to scores (age,gender)
        try{
            FileInputStream  fis = openFileInput("settings.txt");
            String content = "";
            int c = 0;
            while((c = fis.read()) != -1){
                content += String.valueOf((char)c);
            }
            fis.close();
            fis = openFileInput("scores.txt");

            c = 0;
            while((c = fis.read()) != -1){
                content += String.valueOf((char)c);
            }
            fis.close();


            try{
                FileOutputStream fos = openFileOutput("scores.txt",MODE_PRIVATE);
                fos.write(content.getBytes());
                fos.close();
            }catch (Exception e){
                System.out.println(""+e);
            }

           //TO REMOVE IN THE FUTURE, data collection:
           /* BufferedWriter bw = new BufferedWriter( new FileWriter(new File("/storage/emulated/0/Download/"+System.currentTimeMillis()+".txt")));
            bw.write(content);
            bw.close();*/

        }catch (Exception e){
            System.out.println(""+e);
        }
        startActivity(new Intent(TestActivity11.this,ScoreActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test11);
        //UTILS
        AssetManager am = getAssets();
        Random rand = new Random();

        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);

        //UI components
        pb = (ProgressBar)findViewById(R.id.testActivity11pb0);
        testTxt = (TextView)findViewById(R.id.textView33);
        imgv = (ImageView)findViewById(R.id.testActivity11img0);
        question = (TextView)findViewById(R.id.testActivity11txt0);
        painterName = (TextView)findViewById(R.id.testActivity11txt1);
        rbs[0] = (RadioButton)findViewById(R.id.test11opt0);
        rbs[1] = (RadioButton)findViewById(R.id.test11opt1);
        rbs[2] = (RadioButton)findViewById(R.id.test11opt2);

        String[] imgList = {"magritte.jpg","starry.jpg","turner.jpg","venus.jpg","wave.jpg"};
        String[] painters = {"René Magritte","Vincent van Gogh","J.M.W. Turner","Sandro Botticelli","Katsushika Hokusai"};

        int pick = rand.nextInt(imgList.length);

        Bitmap b = Bitmap.createBitmap(260,190, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        Paint p = new Paint();

        try {
            InputStream f = am.open("paintings/"+imgList[pick]);
            Bitmap exp = BitmapFactory.decodeStream(f);
            c.drawBitmap(exp, 1, 1, p);
            painterName.setText(painters[pick]);
        } catch (IOException e) {
            System.err.println(e);
        }

        imgv.setImageBitmap(b);


        //Setting options (invisible)
        rightOne = rand.nextInt(3);
        rbs[rightOne].setText(painters[pick]);
        for(int i =0;i != 3;i++){
            if(i != rightOne){
                String x = "";
                do{
                    x = painters[rand.nextInt(imgList.length)];
                }while(x == rbs[0].getText() || x == rbs[1].getText() || x == rbs[2].getText());
                rbs[i].setText(x);
            }
        }

        //Setting click listener
        rbs[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (rightOne == 0) thisIsTheEnd(true);
                    else thisIsTheEnd(false);
                }
            }
        });
        rbs[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (rightOne == 1) thisIsTheEnd(true);
                    else thisIsTheEnd(false);
                }
            }
        });
        rbs[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(testTime) {
                    if (rightOne == 2) thisIsTheEnd(true);
                    else thisIsTheEnd(false);
                }
            }
        });



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
                            }else pb.setProgress(sharedI*100/7);
                            //Removed due to compatibility issues
                            // pb.setProgress((int)(sharedI*100)/7,true);
                        }
                    });
                    try{
                        Thread.sleep(1000);
                    }catch (Exception e){
                        System.err.println("AIAIAI :"+e);
                    }
                }

                Intent nextTest = new Intent(TestActivity11.this,TestActivity6.class);
                startActivity(nextTest);
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        question.setText("Who was the painter?");
                        pb.setVisibility(View.GONE);
                        testTxt.setVisibility(View.VISIBLE);
                        painterName.setVisibility(View.GONE);


                        for(int i = 0;i != 3;i++){
                            rbs[i].setVisibility(View.VISIBLE);
                        }

                        testTime = true;
                    }
                });


            }
        }).start();
    }
}

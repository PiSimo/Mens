package org.pisimo.mens;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import java.io.*;



public class ScoreActivity extends AppCompatActivity {


   public String readFile(String path){
        String cnt = "";
        try{
            FileInputStream fis = openFileInput(path);
            int c = 0;
            while((c = fis.read()) != -1)cnt += String.valueOf((char)c);
            fis.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return cnt;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_score);
            final TextView scoresTxt = (TextView)findViewById(R.id.scoresTxt);
            Button btn = (Button)findViewById(R.id.button3);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        FileOutputStream fos = openFileOutput("scores_history.txt",MODE_PRIVATE);
                        fos.write("".getBytes());
                        fos.close();
                        scoresTxt.setText("");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            //Test score reading :
            String content = readFile("scores.txt");

            //If the test was ended then calculating score and adding to scores_history
            if(content.split("\n").length > 14){
                //Each result + times+gender+age:
                String[] splitted = content.split("\n");
                long testTime = 0;
                int age = 0;
                int totalScore = 0;
                boolean isMale = false;
                double finalScore = 0.0;
                float[] scores = new float[11];


                int t = 0; //test appartenence
                //get age,gender,testime,scores:
                for(int i = 0;i != splitted.length;i++){
                    if(i == 0)     age = Integer.valueOf(splitted[i]);
                    else if(i == 1)isMale = (splitted[i].indexOf("FE") == -1);
                    else if(i == 2)testTime = Long.valueOf(splitted[i]);
                    else if(i == splitted.length-1)testTime -= Long.valueOf(splitted[i]);
                    else{
                        scores[t] = Float.valueOf(splitted[i]);
                        totalScore += scores[t];
                        t++;
                    }
                }

                //calculate testime:
                testTime = Math.abs(testTime) / 1000;
                Toast.makeText(this,"Test Took :"+testTime,Toast.LENGTH_LONG).show();


                //prepare NEURAL NETWORK /*gradient descent*/ input
                double[] input = new double[14];
                input[0] = age / 100;
                input[1] = (isMale) ? 0:1;
                input[2] = testTime / 1000;
                for(int i = 3;i != 14;i++)input[i] = scores[i-3];



                //Switched to nn
                try {
                    //Load net weights from assets/weights
                    AssetManager manager = getAssets();
                    Neural net = new Neural(14,new int[]{14,20,10,1},new int[]{Layer.ACTIVATION_RECTIFIED_LINEAR_UNIT, Layer.ACTIVATION_RECTIFIED_LINEAR_UNIT,Layer.ACTIVATION_RECTIFIED_LINEAR_UNIT, Layer.ACTIVATION_SIGMOID} );
                    net.loadFromFile(manager.open("weights"));
                    //System.out.println("Input:"+Arrays.toString(input));

                    // net outpu:>
                    finalScore =  (net.calculate(input)[0]) * 100.0;
                    finalScore = (int)finalScore;
                    //System.out.println("Output"+ Arrays.toString(net.calculate(input)));

                    // finalScore = (int)finalScore;
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //Read scores_history if fileNotFound will be created after
                String historyContent = readFile("scores_history.txt");
                String[] history = historyContent.split("\n");

                //Add test score to file
                try {
                    FileOutputStream fos = openFileOutput("scores_history.txt",MODE_PRIVATE);
                    /*
                     * File(scores_history.txt) format is : date.toString(),age,gender,testTime,scoresSum,neural-network output\n
                     */

                    String addCurrentResults = new Date().toString()+","+age+","+isMale+","+testTime+","+totalScore+","+finalScore+"\n";
                    fos.write(addCurrentResults.getBytes());

                    //rewrite scores' history
                    if(history[0] != ""){
                        for(String line:history){
                            line=line+"\n";
                            fos.write(line.getBytes());
                        }
                    }
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //Overwrite scores.txt to prevent unwanted adding to scores_history
                try{
                    FileOutputStream fos = openFileOutput("scores.txt",MODE_PRIVATE);
                    fos.write("".getBytes());
                    fos.close();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else {
                System.out.println("Scores file not complete or empty!");
            }

            /*
             * GUI:
             */
            //Loading scores_history to TextView

            //Reading history
            String[] elements = readFile("scores_history.txt").split("\n");
            String scoresTxtContent = "";
            for(int i = 0;i != elements.length;i++){
                String element = elements[i];
                if(element != ""){
                    String[] csvValues = element.split(",");
                    element = ""+csvValues[0].substring(4,8)+" "+csvValues[0].substring(8,10)+" "+csvValues[0].substring(11,16)+"\n"; //format time
                    element += csvValues[1]+" ";
                    element += (Boolean.valueOf(csvValues[2])) ? "MALE ":"FEMALE "; //age gender
                    element += "Right Answers: "+csvValues[4]+"/11 ";
                    element += "Memory: "+csvValues[5]+"%\n\n";
                }
                scoresTxtContent += element;
            }
            scoresTxt.setText(scoresTxtContent);
    }
}

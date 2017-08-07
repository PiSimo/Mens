package org.pisimo.mens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent settingsAct = new Intent(this,SettingsActivity.class);
        final Intent aboutAct = new Intent(this,AboutActivity.class);
        final Intent testAct = new Intent(this,TestActivity1.class);
        final Intent scoresAct = new Intent(this,ScoreActivity.class);


        //Settings File
        FileInputStream settingsFile = null;
        try {
            settingsFile = openFileInput("settings.txt");
            settingsFile.close();
        } catch (FileNotFoundException e) {

            startActivity(settingsAct);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Button EVENTS:
        //ABOUT
        Button about = (Button)findViewById(R.id.buttonAbout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(aboutAct);
            }
        });

        //SETTINGS
        Button settings = (Button)findViewById(R.id.buttonSettings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(settingsAct);
            }
        });

        //TEST
        Button test = (Button)findViewById(R.id.buttonTest);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(testAct); }
        });

        //SCORES
        Button scores = (Button)findViewById(R.id.buttonScores);
        scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(scoresAct);
            }
        });
    }
}

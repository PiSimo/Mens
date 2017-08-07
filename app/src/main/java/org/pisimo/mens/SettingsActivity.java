package org.pisimo.mens;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SettingsActivity extends AppCompatActivity {

    String selectedGender = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final String[] gender = {"FEMALE","MALE"};

        Spinner spin = (Spinner)findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGender = gender[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter<String> spin_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        spin.setAdapter(spin_adapter);

        String setAge = "";
        int setSpinner = 1;
        try{
            FileInputStream fis = openFileInput("settings.txt");
            int i = 0;
            String content = "";
            while((i = fis.read()) != -1){
                content += String.valueOf((char)i);
            }
            setAge = content.substring(0,content.indexOf("\n"));
            if(content.indexOf("FEMALE") != -1){
                setSpinner = 0;
            }



        }catch (Exception e){


        }
        final EditText age = (EditText)findViewById(R.id.editText);
        final Switch dataCollection = (Switch)findViewById(R.id.switch1);
        age.setText(setAge);
        spin.setSelection(setSpinner);


        Button saveButton = (Button)findViewById(R.id.button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText age = (EditText)findViewById(R.id.editText);
                String enteredAge = age.getText().toString();
                final Switch dataCollection = (Switch)findViewById(R.id.switch1);

                //No implementatin yet
                //boolean collect = dataCollection.isChecked();

                if(enteredAge.isEmpty()) {
                    Toast.makeText(SettingsActivity.this,"You have to enter your age", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        FileOutputStream fos = openFileOutput("settings.txt", Context.MODE_PRIVATE);
                        String write = enteredAge+"\n"+selectedGender+"\n";
                        /*TODO:
                        if(collect){
                            write += "COLLECT\n";
                        }*/
                        fos.write(write.getBytes());
                        fos.close();
                        Toast.makeText(SettingsActivity.this,"Settings changed!",Toast.LENGTH_LONG).show();
                        finish();
                    }catch (Exception e){
                        Toast.makeText(SettingsActivity.this,"Error Occured",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}

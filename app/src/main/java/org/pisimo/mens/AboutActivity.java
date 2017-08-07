package org.pisimo.mens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView t2 = (TextView) findViewById(R.id.textViewL);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }
}

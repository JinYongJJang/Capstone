package com.example.cy.cody_;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Intent ClosetIntent;
    TextView Main_Login_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, loadingActivity.class);
        startActivity(intent);
        Button buttonCloset = (Button) findViewById(R.id.Closet);
        buttonCloset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closetintent = new Intent(MainActivity.this,ClosetActivity.class);
                startActivity(closetintent);
            }
        });
        Main_Login_Button = (TextView) findViewById(R.id.Main_Login_button);
        Main_Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
            }
        });
    }
}

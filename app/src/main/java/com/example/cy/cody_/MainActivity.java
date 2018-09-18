package com.example.cy.cody_;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Intent ClosetIntent;
    TextView Main_Login_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View view = getWindow().getDecorView();
        Intent intent = new Intent(this, loadingActivity.class);
        startActivity(intent);



        ImageButton buttonCloset = (ImageButton) findViewById(R.id.Closet);
        buttonCloset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent closetintent = new Intent(MainActivity.this,ClosetActivity.class);
                startActivity(closetintent);
            }
        });
        ImageButton buttonCody = (ImageButton) findViewById(R.id.Fast_Cody);
        buttonCody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent codyintent = new Intent(MainActivity.this,Fast_codyActivity.class);
                startActivity(codyintent);
            }
        });
        ImageButton buttonpro = (ImageButton) findViewById(R.id.How_Cloth);
        buttonpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prointent = new Intent(MainActivity.this,How_clothActivity.class);
                startActivity(prointent);
            }
        });
        ImageButton buttonuser = (ImageButton) findViewById(R.id.Expert);
        buttonuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userintent = new Intent(MainActivity.this,ExpertActivity.class);
                startActivity(userintent);
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

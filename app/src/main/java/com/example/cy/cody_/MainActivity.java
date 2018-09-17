package com.example.cy.cody_;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Intent ClosetIntent;

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
    }
}

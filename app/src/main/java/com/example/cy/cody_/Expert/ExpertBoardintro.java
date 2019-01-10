package com.example.cy.cody_.Expert;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.JsonRequest;
import com.example.cy.cody_.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExpertBoardintro extends AppCompatActivity {

    TextView Expert_Show_Name;
    Button Expert_Show_Button;
    TextView Expert_Show_Title;
    ImageView Expert_ImageView;
    TextView Expert_Show_Content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_boardintro);

        Expert_Show_Name = findViewById(R.id.Expert_Show_Name);
        Expert_Show_Button = findViewById(R.id.Expert_Show_Button);
        Expert_ImageView = findViewById(R.id.Expert_ImageView);
        Expert_Show_Title = findViewById(R.id.Expert_Show_Title);
        Expert_Show_Content = findViewById(R.id.Expert_Show_Content);

        Intent GetIntent = getIntent();
        int ID = GetIntent.getExtras().getInt("ID");  // 클릭했을 때 ID값 받아온다

        final JSONObject json = new JSONObject();
        try {
            json.put("ID", ID);  // ID를 json으로 묶어서 volley를 이용해 전송 준비
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String Name;
                String Title;
                String Content;
                String FileName;
                Log.v("JIN_response", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        Log.v("JIN_asd", "123");
                        Name = jsonResponse.getString("Name");
                        Title = jsonResponse.getString("Title");
                        Content = jsonResponse.getString("Content");


                        Expert_Show_Name.setText(Name);
                        Expert_Show_Title.setText(Title);
                        Expert_Show_Content.setText(Content);

                        byte[] Decode_IMG = Base64.decode(jsonResponse.getString("FileName"), Base64.DEFAULT);
                        Bitmap Decode_Byte = BitmapFactory.decodeByteArray(Decode_IMG, 0, Decode_IMG.length);
                        Expert_ImageView.setImageBitmap(Decode_Byte);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ExpertBoardintro.this);
                        builder.setMessage("보내기 실패")
                                .create()
                                .show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        JsonRequest request = new JsonRequest(json, "http://113.198.229.173/Expert_Boardintro.php", responseListener);
        RequestQueue queue = Volley.newRequestQueue(ExpertBoardintro.this);
        queue.add(request);
    }
}


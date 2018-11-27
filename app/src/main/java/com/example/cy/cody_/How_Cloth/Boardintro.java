package com.example.cy.cody_.How_Cloth;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Adapter;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Boardintro extends AppCompatActivity {

    TextView HC_Show_Name;
    Button HC_Show_Button;
    TextView HC_Show_Title;
    TextView HC_Show_Content;

    ByteArrayInputStream ina;

    HC_PageAdapter adapter;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardintro);

        HC_Show_Name = findViewById(R.id.HC_Show_Name);
        HC_Show_Button = findViewById(R.id.HC_Show_Button);
        HC_Show_Title = findViewById(R.id.HC_Show_Title);
        HC_Show_Content = findViewById(R.id.HC_Show_Content);

        Intent GetIntent = getIntent();
        int ID = GetIntent.getExtras().getInt("ID");  // How_Cloth Table의 ID값

        Log.v("JIN_ID", String.valueOf(ID));

        final JSONObject json = new JSONObject();
        try {
            json.put("ID", ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.v("JIN_response", response);
                try{
                    int ID;
                    String Title;
                    String Content;
                    int Favorite;
                    String Date;
                    String Email;
                    String Name;
                    double Temperature;
                    String Weather;

                    JSONObject jsonResponse = new JSONObject(response);
                    Log.v("JIN_JSON", jsonResponse.toString());
                    JSONArray jsonArray = new JSONArray(jsonResponse.getString("response"));
                    Log.v("JIN_ARRRRRRR", jsonArray.toString());

                    for(int i=0; i <jsonArray.length(); i++){

                        JSONObject dataJsonObj = jsonArray.getJSONObject(i);

                        JSONArray Cody_ID_JSON = new JSONArray(dataJsonObj.getString("Cody_ID"));  // json 내의 Cody_ID 배열을 발견

                        boolean success = dataJsonObj.getBoolean("success");

                        if(success){
                            boolean send = dataJsonObj.getBoolean("send");
                            if(send){  // Cody 에서 가져온거
                                ID = dataJsonObj.getInt("ID");
                                Title = dataJsonObj.getString("Title");
                                Content = dataJsonObj.getString("Content");
                                Favorite = dataJsonObj.getInt("Favorite");
                                Date = dataJsonObj.getString("Date");
                                Email = dataJsonObj.getString("Email");
                                Name = dataJsonObj.getString("Name");
                                Temperature = dataJsonObj.getDouble("Temperature");
                                Weather = dataJsonObj.getString("Weather");
                            }
                            else{   // Galley_pic 에서 가져온거
                                ID = dataJsonObj.getInt("ID");
                                Title = dataJsonObj.getString("Title");
                                Content = dataJsonObj.getString("Content");
                                Favorite = dataJsonObj.getInt("Favorite");
                                Date = dataJsonObj.getString("Date");
                                Email = dataJsonObj.getString("Email");
                                Name = dataJsonObj.getString("Name");
                            }


                            HC_Show_Name.setText(Name);
                            HC_Show_Title.setText(Title);
                            HC_Show_Content.setText(Content);

                            viewPager = (ViewPager)findViewById(R.id.view);
                            adapter = new HC_PageAdapter(Boardintro.this, Cody_ID_JSON);
                            viewPager.setAdapter(adapter);


                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(Boardintro.this);
                            builder.setMessage("보내기 실패")
                                    .create()
                                    .show();
                        }

                    }

                }
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        };
        JsonRequest request = new JsonRequest(json, "http://113.198.229.173/HC_ItemView.php", responseListener);
        RequestQueue queue = Volley.newRequestQueue(Boardintro.this);
        queue.add(request);
    }
}

package com.example.cy.cody_.Login;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.JsonRequest;
import com.example.cy.cody_.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText EmailText;
    EditText passwordText;
    EditText nameText;

    String Email;
    String Name;
    String Password;

    /******************************** 꼭 고쳐 ********************************/
    String User_Picture = "RegisterActivity.java.를 고치세요";  // 유저의 프로필 사진을 넣을 예정
    String User_Bool = "0"; // 0은 일반인, 1은 전문가
    /************************************************************************/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EmailText = (EditText) findViewById(R.id.EmailText);
        nameText = (EditText) findViewById(R.id.nameText);
        passwordText = (EditText) findViewById(R.id.passwordText);


        Button registerButton = (Button) findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Email = EmailText.getText().toString();
                Name = nameText.getText().toString();
                Password = passwordText.getText().toString();

                JSONObject json = new JSONObject();
                try{
                    json.put("Email", Email);
                    json.put("Name", Name);
                    json.put("Password", Password);
                    json.put("User_Picture", User_Picture);
                    json.put("User_Bool", User_Bool);
                    // 모든 값들을 json object로 생성
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            // php로 받은 응답을 Json Object로 생성
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {  // 성공 시
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else {  // 실패  시

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                JsonRequest registerRequest = new JsonRequest(json, "http://113.198.229.173/Register.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}

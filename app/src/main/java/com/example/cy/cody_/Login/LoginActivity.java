package com.example.cy.cody_.Login;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.JsonRequest;
import com.example.cy.cody_.MainActivity;
import com.example.cy.cody_.R;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText emailText;
    EditText passwordText;

    TextView joinButton;

    Button loginButton;

    String Email;
    String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        joinButton = (TextView) findViewById(R.id.join_button);
        //final TextView findIDButton = (TextView) findViewById(R.id.findID_button);
        loginButton = (Button) findViewById(R.id.login_button);

        joinButton.setOnClickListener(new View.OnClickListener(){  // 회원가입 버튼 클릭

            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerIntent);
            }
        });


//        findIDButton.setOnClickListener(new View.OnClickListener() {  // 아이디 찾기 버튼 클릭
//            @Override
//            public void onClick(View view) {
//                Intent registerIntent = new Intent(getApplicationContext(), EmailpwfindActivity.class);
//                startActivity(registerIntent);
//            }
//        });



        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Email = emailText.getText().toString();
                Password = passwordText.getText().toString();

                JSONObject json = new JSONObject();
                try{
                    json.put("Email", Email);
                    json.put("Password", Password);
                }
                catch(JSONException e){
                    e.printStackTrace();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.v("JIN_LOGIN", response);
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                String Email = jsonResponse.getString("Email");
                                String Name = jsonResponse.getString("Name");

                                Intent intent = new Intent();
                                intent.putExtra("Email", Email);
                                intent.putExtra("Name", Name);
                                //intent.putExtra("Password", Password);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("로그인 실패")
                                        .setNegativeButton("다시시도", null)
                                        .create()
                                        .show();
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                JsonRequest loginRequest = new JsonRequest(json, "http://113.198.229.173/Login.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }
}

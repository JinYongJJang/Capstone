package com.example.cy.cody_.How_Cloth;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.JsonRequest;
import com.example.cy.cody_.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SubActivity extends AppCompatActivity {

    String Email;
    String Name;
    String Title;
    String Content;
    String Picture;

    EditText Title_Edit;
    Button Get_Picture;
    EditText Content_Edit;
    Button SaveButton;

    final int GALLERY_CODE = 1300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        /*************** How_clothActivity 에서 넘어온 값 저장 *******************/
        Intent GetIntent = getIntent();
        Email = GetIntent.getExtras().getString("Email");
        Name = GetIntent.getExtras().getString("Name");
        /***********************************************************************/


        TextView Name_TextView = findViewById(R.id.textview);
        Name_TextView.setText(Name);  // 로그인 되어 있는 상태로 실행되기 때문에 유저 이름으로 변경가능


        Title_Edit = findViewById(R.id.Title_Edit);
        Content_Edit = findViewById(R.id.Content_Edit);


        SaveButton=(Button)findViewById(R.id.usersaveboa);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Save_shows();
            }
        });


        Get_Picture = (Button)findViewById(R.id.usergetpic);
        Get_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Get_Picture_show();
            }
        });
    }

    void Save_shows(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" 오늘 뭐 입지?");
        builder.setMessage("작성하신 게시물을 저장하시겠습니까? ");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Title = Title_Edit.getText().toString();
                Content = Content_Edit.getText().toString();
                Picture = "SubActivity 고쳐야해";
                // Picutre 해야함

                JSONObject json = new JSONObject();
                try {
                    json.put("Title", Title);
                    json.put("Content", Content);
                    json.put("Picture", Picture);
                    json.put("Email", Email);
                    //Log.v("JIN", json.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            if(success){
                                finish();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(SubActivity.this);
                                builder.setMessage("보내기 실패")
                                        .create()
                                        .show();
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                };

                JsonRequest request = new JsonRequest(json, "http://113.198.229.173/How_Cloth.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(SubActivity.this);
                queue.add(request);
            }
        });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }


    void Get_Picture_show()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" 오늘 뭐 입지?");
        builder.setMessage("사진 선택을 위해 카메라 액세스 허용 하시겠습니까? ");  // 사진 선택에는 권한이 필요없습니다 고객님
        builder.setPositiveButton("승인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"승인을 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("차단", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"차단을 선택했습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

}
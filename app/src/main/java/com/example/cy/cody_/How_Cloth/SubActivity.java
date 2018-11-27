package com.example.cy.cody_.How_Cloth;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.JsonRequest;
import com.example.cy.cody_.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubActivity extends AppCompatActivity {

    String Email;
    String Name;
    String Title;
    String Content;
    String Cody_ID = null;
    byte[] decodedString_IMG;
    Bitmap decodeByte_IMG;

    ImageView UserImageVIew;
    EditText Title_Edit;
    Button Get_Picture;
    EditText Content_Edit;
    Button SaveButton;

    final int GALLERY_CODE = 1300;
    final int LOAD_CODY_DATABASE = 1301;


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
        UserImageVIew = findViewById(R.id.userimageview);


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
                // Picutre 해야함

                JSONObject json = new JSONObject();
                try {
                    json.put("Title", Title);
                    json.put("Content", Content);
                    json.put("Cody_ID", Cody_ID);
                    json.put("Email", Email);
                    Log.v("JIN_JSONSTRING", json.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Log.v("JIN_Res", response);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" 오늘 뭐 입지?");

        CharSequence info[] = new CharSequence[] {"갤러리", "사진찍기", "불러오기"};
        builder.setItems(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        Toast.makeText(getApplicationContext(),"갤러리.",Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), "사진",Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Intent PictureSelect_intent =  new Intent(SubActivity.this, PictureSelect.class);
                        PictureSelect_intent.putExtra("Email", Email);  // 이메일값 넘겨줘서 다음 인텐트에서 DB 조인
                        startActivityForResult(PictureSelect_intent, LOAD_CODY_DATABASE);
                        break;
                }
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            switch(requestCode){
                case LOAD_CODY_DATABASE:
                    String Recv_Cody_ID = data.getStringExtra("Click_Position");   // 선택된 코디의 Cody_ID 값을 받는다
                    Log.v("JIN_data_position", data.getStringExtra("Click_Position"));
                    JSONObject json = new JSONObject();
                    try {
                        json.put("Cody_ID", Recv_Cody_ID);
                        Cody_ID = Recv_Cody_ID;  // 저장 할 때 Cody_ID를 넣기 위해 사용

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray jsonArray = new JSONArray(jsonResponse.getString("response"));
                                Log.v("JIN_ARRAY", jsonArray.toString());

                                JSONObject dataJsonObj = jsonArray.getJSONObject(0);
                                String Top_file = dataJsonObj.getString("Top_file");

                                decodedString_IMG = Base64.decode(Top_file, Base64.DEFAULT);
                                decodeByte_IMG = BitmapFactory.decodeByteArray(decodedString_IMG, 0, decodedString_IMG.length);
                                UserImageVIew.setImageBitmap(decodeByte_IMG);
                            }
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    };

                    JsonRequest request = new JsonRequest(json, "http://113.198.229.173/Load_Cody_Database.php", responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SubActivity.this);
                    queue.add(request);

                    break;
            }
        }
    }
}
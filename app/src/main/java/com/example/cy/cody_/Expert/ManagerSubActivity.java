package com.example.cy.cody_.Expert;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ManagerSubActivity extends AppCompatActivity {
    private final int GALLERY_CODE=1112;

    String Email;
    String Name;
    String Title;
    String Content;
    String Picture;
    Bitmap bit = null;
    EditText Title_Edit;
    Button Get_Picture;
    EditText Content_Edit;
    Button SaveButton;
    ImageView CodyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_sub);

        /*************** How_clothActivity 에서 넘어온 값 저장 *******************/
        Intent GetIntent = getIntent();
        Email = GetIntent.getExtras().getString("Email");
        Name = GetIntent.getExtras().getString("Name");
        /***********************************************************************/

        TextView Name_TextView = findViewById(R.id.textview);
        Name_TextView.setText(Name);

        Title_Edit = findViewById(R.id.Title_Edit);
        Content_Edit = findViewById(R.id.Content_Edit);
        SaveButton = findViewById(R.id.managersaveboa);
        CodyImage = findViewById(R.id.managerimageview);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save_show();
                Log.e("writing test - ","check ");

            }
        });
        Get_Picture = findViewById(R.id.manager_get_pic);
        Get_Picture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_CODE); // 갤러리 호출



            }
        });

    }

    void Save_show(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(" 오늘 뭐 입지?");
        builder.setMessage("작성하신 글을 게시하시겠습니까? ");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Title = Title_Edit.getText().toString();
                Content = Content_Edit.getText().toString();


                Picture = getStringFromBitmap(bit);


                JSONObject json = new JSONObject();
                try {
                    json.put("Title", Title);
                    json.put("Content", Content);
                    json.put("Picture", Picture);
                    json.put("Email", Email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("writing test - ",json.toString());
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("JIN", response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean succeses = jsonResponse.getBoolean("success");
                            if(succeses){
                                Toast.makeText(ManagerSubActivity.this,"게시글 작성하였습니다.",Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerSubActivity.this);
                                builder.setMessage("게시글이 작성에 실패하셨습니다.")
                                        .setPositiveButton("다시시도",null)
                                        .setNegativeButton("취소",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                JsonRequest request = new JsonRequest(json, "http://113.198.229.173/Expert.php", responseListener);
                RequestQueue queue = Volley.newRequestQueue(ManagerSubActivity.this);
                queue.add(request);
            }
        });
        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_CODE:

                    String imagePath = getRealPathFromURI(data.getData()); // path 경로
                    Log.e("CY",imagePath);

                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환

                    CodyImage.setImageBitmap(bitmap);//이미지 뷰에 비트맵 넣기
                    bit = bitmap;
                    break;
                default:
                    break;
            }

        }
    }



    // 이미지의 절대 경로 가져오는 함수
    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

    /** 이미지 json 변환 **/
    private String getStringFromBitmap(Bitmap pictures){
        String encodedImage = null;
        ByteArrayOutputStream BitmapString = new ByteArrayOutputStream();
        pictures.compress(Bitmap.CompressFormat.JPEG , 100,BitmapString);
        byte[] b = BitmapString.toByteArray();
        encodedImage = Base64.encodeToString(b,Base64.DEFAULT);
        return encodedImage;
    }
}



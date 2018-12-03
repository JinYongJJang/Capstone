package com.example.cy.cody_.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cy.cody_.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserinfoActivity extends AppCompatActivity{
    private static final String TAG = UserinfoActivity.class.getSimpleName();
    private TextView Name, Email, Password;
    private Button btn_logout,btn_photo_upload;
    SessionManager sessionManager;
    String getEmail;
    private static String URL_READ = "http://113.198.229.173/read_detail.php";
    private static String URL_EDIT = "http://113.198.229.173/edit_detail.php";
    //private static String URL_UPLOAD = "http://113.198.229.173/upload.php";
    private Menu action;
    private Bitmap bitmap;
    CircleImageView profile_image;
    Toolbar myToolbar;
    byte[] decodedString_User_Picture;
    Bitmap decodeByte_User_Picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


//        Intent GetIntent = getIntent();
//        getEmail = GetIntent.getExtras().getString("Email");  // 수정이니까 이미 email이 있다


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();



        HashMap<String, String> user = sessionManager.SessiongetUserDetail();
        getEmail = user.get(sessionManager.EMAIL);


        Name = findViewById(R.id.Name);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        btn_logout = findViewById(R.id.btn_logout);
        btn_photo_upload = findViewById(R.id.btn_photo);
        profile_image = findViewById(R.id.profile_image);

        getUserDetail();


        /****************  수정 막기************************/
        Name.setFocusableInTouchMode(false);
        Email.setFocusableInTouchMode(false);
        Password.setFocusableInTouchMode(false);
        btn_photo_upload.setEnabled(false);
        btn_photo_upload.setClickable(false);

        Name.setFocusable(false);
        Email.setFocusable(false);
        Password.setFocusable(false);
        /**************************************************/


        btn_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });


    }


    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.v("JIN",response.toString());
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("read");

                            if(success.equals("1")){
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strName = object.getString("Name").trim();
                                    String strEmail = object.getString("Email").trim();
                                    String strPassword = object.getString("Password").trim();
                                    String strUser_Picture = object.getString("image");

                                    decodedString_User_Picture= Base64.decode(strUser_Picture, Base64.DEFAULT);
                                    decodeByte_User_Picture = BitmapFactory.decodeByteArray(decodedString_User_Picture, 0, decodedString_User_Picture.length);
                                    profile_image.setImageBitmap(decodeByte_User_Picture);

                                    bitmap = decodeByte_User_Picture;

                                    Name.setText(strName);
                                    Email.setText(strEmail);
                                    Password.setText(strPassword);
                                }
                            }
                        }catch (JSONException e){
                            Log.v("JIN", "error2");
                            e.printStackTrace();
//                            progressDialog.dismiss();
                            Toast.makeText(UserinfoActivity.this,"Error Reading Detail"+e.toString(),Toast.LENGTH_SHORT).show();

                        }

                    }
                },null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Email",getEmail);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    /********************************  툴바의 메뉴 만들기  **************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_action,menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_edit:
                Name.setFocusableInTouchMode(true);
                Email.setFocusableInTouchMode(true);
                Password.setFocusableInTouchMode(true);
                btn_photo_upload.setEnabled(true);
                btn_photo_upload.setClickable(true);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(Name,InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);
                return true;

            case R.id.menu_save:
                SaveEditDetail();
                action.findItem(R.id.menu_edit).setVisible(true);
                action.findItem(R.id.menu_save).setVisible(false);

                Name.setFocusableInTouchMode(false);
                Email.setFocusableInTouchMode(false);
                Password.setFocusableInTouchMode(false);

                Name.setFocusable(false);
                Email.setFocusable(false);
                Password.setFocusable(false);

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**************************************************************************************************************/


    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() !=null){
            Uri filePath = data.getData();
            try{
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                profile_image.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodeImage;
    }


    private void SaveEditDetail() {

        final String Name = this.Name.getText().toString().trim();
        final String Email = this.Email.getText().toString().trim();
        final String Password = this.Password.getText().toString().trim();
        final String User_Picture = getStringImage(bitmap);   // 이미지를 base인코딩해서 스트링값으로 가져온다
        //final String Email = getEmail;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("JIN_res",response);
                        progressDialog.dismiss();

                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(UserinfoActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                                sessionManager.createSession(Name, Email, Password);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(UserinfoActivity.this,"Error"+e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, null)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("Name",Name);
                params.put("Email",Email);
                params.put("Password",Password);
                params.put("User_Picture", User_Picture);
                //params.put("id",id);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}




package com.example.cy.cody_;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cy.cody_.Closet.ClosetActivity;
import com.example.cy.cody_.How_Cloth.Item;
import com.example.cy.cody_.How_Cloth.RecyclerAdapter;
import com.example.cy.cody_.Login.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Fast_codyActivity extends AppCompatActivity {
    private ImageView Top_Cody,Bottom_Cody,Outer_Cody;
    private ArrayList clothes_images;
    private ArrayList <File>Top_list;
    private ArrayList <File>Bottom_list;
    private ArrayList <File>Outer_list;
    private Button restart_cody;
    private Button select_cody;

    String Temp;
    int top_num;
    int bottom_num;
    int outer_num;
    SessionManager sessionManager;

    String Email;

    String Top_file;
    String Top_R;
    String Top_G;
    String Top_B;

    String Bottom_file;
    String Bottom_R;
    String Bottom_G;
    String Bottom_B;

    String Outer_file;
    String Outer_R;
    String Outer_G;
    String Outer_B;

    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_cody);



        Intent GetIntent = getIntent();
        Temp = GetIntent.getExtras().getString("Temp");
        Log.v("JIN_temp", Temp);

        sessionManager = new SessionManager(this);
        /********************* 변경) 로그인 되어있을때 *********************/
        HashMap<String, String> user = sessionManager.SessiongetUserDetail();
        Email = user.get(sessionManager.EMAIL);
        /*********************************************************************/

        Top_Cody = (ImageView)findViewById(R.id.top_cody);
        Bottom_Cody = (ImageView)findViewById(R.id.bottom_cody);
        Outer_Cody = (ImageView)findViewById(R.id.outer_cody);
        restart_cody = (Button)findViewById(R.id.cody_restart);
        select_cody = (Button)findViewById(R.id.cody_select);
        clothes_images= new ArrayList<>();

        String rootSD = Environment.getExternalStorageDirectory().toString();
        file = new File(rootSD+"/Pictures");
        File[] list = file.listFiles();// SD 카드 전체 파일을 다 불러 오는 친구들
        Top_list = new ArrayList<File>();
        Bottom_list = new ArrayList<File>();
        Outer_list = new ArrayList<File>();


        for(int i=0; i<list.length;i++){
            if(list[i].getName().substring((list[i].getName().length()-8),(list[i].getName().length()-4)).equals("_Top")){
                Top_list.add(list[i]);
            };
            if(list[i].getName().substring((list[i].getName().length()-11),(list[i].getName().length()-4)).equals("_Bottom")){
                Bottom_list.add(list[i]);
            };
            if(list[i].getName().substring((list[i].getName().length()-10),(list[i].getName().length()-4)).equals("_Outer")){
                Outer_list.add(list[i]);
            };
        }
        Random top_random_f = new Random();
        Random outer_random_f = new Random();
        Random bottom_random_f = new Random();

        top_num = top_random_f.nextInt(Top_list.size());
        bottom_num = bottom_random_f.nextInt(Bottom_list.size());
        outer_num = outer_random_f.nextInt(Outer_list.size());

        Top_Cody.setImageBitmap((BitmapFactory.decodeFile(Top_list.get(top_num).getAbsolutePath())));
        Log.v("JIN_TOP", Top_list.get(top_num).getName());
        Bottom_Cody.setImageBitmap((BitmapFactory.decodeFile(Bottom_list.get(bottom_num).getAbsolutePath())));
        Outer_Cody.setImageBitmap((BitmapFactory.decodeFile(Outer_list.get(outer_num).getAbsolutePath())));





        //Recommend_Cloth();

        select_cody.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Toast.makeText(Fast_codyActivity.this,"코디가 선택되었습니다.",Toast.LENGTH_LONG).show();
                finish();
            }
        });

        restart_cody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random top_random = new Random();
                Random outer_random = new Random();
                Random bottom_random = new Random();

                top_num = top_random.nextInt(Top_list.size());
                int bottom_num = bottom_random.nextInt(Bottom_list.size());
                int outer_num = outer_random.nextInt(Outer_list.size());

                top_num = top_random.nextInt(Top_list.size());
                bottom_num = bottom_random.nextInt(Bottom_list.size());
                outer_num = outer_random.nextInt(Outer_list.size());

                Top_Cody.setImageBitmap((BitmapFactory.decodeFile(Top_list.get(top_num).getAbsolutePath())));
                Bottom_Cody.setImageBitmap((BitmapFactory.decodeFile(Bottom_list.get(bottom_num).getAbsolutePath())));
                Outer_Cody.setImageBitmap((BitmapFactory.decodeFile(Outer_list.get(outer_num).getAbsolutePath())));
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    private void Recommend_Cloth(){

        new AsyncTask<String, String, String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {

                final HttpURLConnection urlConnection;
                try{
                    /**********************    json 전송    ***********************/
                    JSONObject json = new JSONObject();
//                    json.put("FileName", Top_list.get(top_num).getName());  // 랜덤으로 뽑은 파일 이름을 준다
                    json.put("Email", Email);

                    Log.v("JIN_jsonPUT", json.toString());

                    URL url  = new URL("http://113.198.229.173/Recommend_Cloth.php");
                    urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("POST");  // 요청방식 설정
                    urlConnection.setDoOutput(true);  // 서버로 응답을 보내겠다.
                    urlConnection.setDoInput(true); // 서버로부터 응답을 받겠다.
                    urlConnection.setConnectTimeout(5000);  // 응답 대기시간 설정

                    OutputStream outputStream = urlConnection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    writer.write("user=" + json.toString());  // volley 처럼 hashmap을 이용하니까 이상하게 나왔음  ex) { "{user" = "{ "Page_Num" : "3" }} "
                    writer.flush();

                    if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK){
                        Log.v("JIN_err", "나 연결 안됨");
                        return null;
                    }
                    /***************************************************************/

                    urlConnection.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;


                    while( (line = bufferedReader.readLine()) != null){
                        if( stringBuilder.length() > 0){
                            stringBuilder.append("\n");
                        }
                        stringBuilder.append(line);
                        Log.v("JIN_line", line.toString());
                    }
//
//                    JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
//                    JSONArray Top_jsonArray = new JSONArray(jsonResponse.getString("Top_response"));
//                    JSONArray Bottom_jsonArray = new JSONArray(jsonResponse.getString("bot_response"));
//                    JSONArray Outer_jsonArray = new JSONArray(jsonResponse.getString("Outer_response"));
//
//
//                    for(int i=0; i<Top_jsonArray.length(); i++) {  // 랜덤으로 뽑힌 top 한놈을 변수에 저장
//                        JSONObject Top_Data = Top_jsonArray.getJSONObject(i);
//                        Top_file  = Top_Data.getString("Top_file");
//                        Top_R = Top_Data.getString("R");
//                        Top_G = Top_Data.getString("G");
//                        Top_B = Top_Data.getString("B");
//                    }
//
//                    for(int i=0; i<Bottom_jsonArray.length(); i++) {  // 랜덤으로 뽑힌 top 한놈을 변수에 저장
//                        JSONObject Bottom_Data = Bottom_jsonArray.getJSONObject(i);
//                        Bottom_file  = Bottom_Data.getString("Bottom_file");
//                        Bottom_R = Bottom_Data.getString("R");
//                        Bottom_G = Bottom_Data.getString("G");
//                        Bottom_B = Bottom_Data.getString("B");
//                    }

//
//                    Log.v("JIN_TOP", Top_jsonArray.toString());

                    writer.close();
                    outputStream.close();
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                catch(IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


            }
        }.execute();

    }
}
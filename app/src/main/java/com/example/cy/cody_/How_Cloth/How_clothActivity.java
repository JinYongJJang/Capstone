package com.example.cy.cody_.How_Cloth;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cy.cody_.Login.LoginActivity;
import com.example.cy.cody_.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class How_clothActivity extends AppCompatActivity {
    static final int ITEM_SIZE = 4;
    static final int REQUEST_LOGIN = 1301;
    int Page_Num = 1;

    String Email = null;
    String Name;
    static List<Item> items = new ArrayList<>();


    static RecyclerView recyclerView;
    static RecyclerView.Adapter Adapter;
    static RecyclerView.LayoutManager layoutManager;

    TextView Num_1;
    TextView Num_2;
    TextView Num_3;
    TextView Num_4;
    TextView Num_5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_cloth);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);

        CallAsyncTask();  // 초기에 한번 그림 보여주기 위해 바로 실행

        Num_1 = findViewById(R.id.Num_1);
        Num_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 1;
                Toast.makeText(getApplicationContext(),"1번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });

        Num_2 = findViewById(R.id.Num_2);
        Num_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 2;
                Toast.makeText(getApplicationContext(),"2번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });

        Num_3 = findViewById(R.id.Num_3);
        Num_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 3;
                Toast.makeText(getApplicationContext(),"3번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });

        Num_4 = findViewById(R.id.Num_4);
        Num_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 4;
                Toast.makeText(getApplicationContext(),"4번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });
        Num_5 = findViewById(R.id.Num_5);
        Num_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 5;
                Toast.makeText(getApplicationContext(),"5번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });


        /**********************    메인에서 로그인이 되어있을때 값을 받아옴    *******************/
        Intent GetIntent = getIntent();
        Email = GetIntent.getExtras().getString("Email");
        Name = GetIntent.getExtras().getString("Name");
        /*************************************************************************************/

//        Log.v("JIN", Email);
//        Intent intent=new Intent(this,SubActivity.class);
//        startActivity(intent);
//        finish();




    }
    public void onClick(View view){  // 글 작성 버튼

        if (Email != null) {  // 로그인이 되어 있을경우
            Intent intent = new Intent(this,SubActivity.class);
            intent.putExtra("Email", Email);
            intent.putExtra("Name", Name);
            startActivity(intent);
        }
        else {  // 로그인이 안된 상태일 경우 로그인을 시키고 작성가능하게 한다.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(" 오늘 뭐 입지?");
            builder.setMessage("로그인이 필요합니다, 로그인 하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent LoginIntent = new Intent(How_clothActivity.this, LoginActivity.class);  // 로그인페이지로 이동시킨후
                            startActivityForResult(LoginIntent, REQUEST_LOGIN);  // onActivityResult 로 이동
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_LOGIN:
                    Email = data.getStringExtra("Email");  // 경고창으로 로그인페이지에 이동 후 받은 결과값을 저장
                    Name = data.getStringExtra("Name");
                    break;
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void CallAsyncTask(){

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
                     json.put("Page_Num", String.valueOf(Page_Num));

                     Log.v("JIN", json.toString());

                     URL url  = new URL("http://113.198.229.173/How_Cloth_List.php");
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

                     JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
                     JSONArray jsonArray = new JSONArray(jsonResponse.getString("response"));

                     items.clear();  // add 하면 뒤에 계속 들어가니까 버튼을 누를때 마다 초기화 시켰다.

                     for(int i=0; i<jsonArray.length(); i++) {
                         JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                         items.add(new Item(
                                 dataJsonObj.getInt("ID"),
                                 dataJsonObj.getString("Title"),
                                 dataJsonObj.getString("Cody_ID"),
                                 dataJsonObj.getString("Name"),
                                 dataJsonObj.getString("User_Picture")
                         ));
                     }
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

                 recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_how_cloth));
                 recyclerView.setLayoutManager( new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL) );

             }
         }.execute();

    }
}


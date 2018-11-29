package com.example.cy.cody_.Expert;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cy.cody_.Expert.ManagerSubActivity;
import com.example.cy.cody_.Login.LoginActivity;
import com.example.cy.cody_.Expert.Item;
import com.example.cy.cody_.Expert.RecyclerAdapter;
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

public class ExpertActivity extends AppCompatActivity {
    static final int REQUEST_LOGIN = 1105;
    static List<Item> items = new ArrayList<>();
    static RecyclerView recyclerView;
    static RecyclerView.Adapter Adapter;
    static RecyclerView.LayoutManager layoutManager;

    int Page_Num = 1;

    TextView Num_1;
    TextView Num_2;
    TextView Num_3;
    TextView Num_4;
    TextView Num_5;

    String Email = null;
    String Name = null;
    static final int ITEM_SIZE = 4;

    Button btn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        btn = (Button)findViewById(R.id.recyclerviewbutton1_chang);

        /**********************    메인에서 로그인이 되어있을때 값을 받아옴    *******************/
        Intent GetIntent = getIntent();
        Email = GetIntent.getExtras().getString("Email");
        Name = GetIntent.getExtras().getString("Name");
        /*****************************************************************************************/
        Log.e(Name,"Name");
        if (!Name.equals("vv")){
            btn.setVisibility(View.INVISIBLE);
        }else{
            btn.setVisibility(View.VISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview2_chang);
        recyclerView.setHasFixedSize(true);

        CallAsyncTask();  // 초기에 한번 그림 보여주기 위해 바로 실행

        Num_1 = findViewById(R.id.Num_1_chang);
        Num_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 1;
                Toast.makeText(getApplicationContext(),"1번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });

        Num_2 = findViewById(R.id.Num_2_chang);
        Num_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 2;
                Toast.makeText(getApplicationContext(),"2번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });

        Num_3 = findViewById(R.id.Num_3_chang);
        Num_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 3;
                Toast.makeText(getApplicationContext(),"3번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });

        Num_4 = findViewById(R.id.Num_4_chang);
        Num_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 4;
                Toast.makeText(getApplicationContext(),"4번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });
        Num_5 = findViewById(R.id.Num_5_chang);
        Num_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Page_Num = 5;
                Toast.makeText(getApplicationContext(),"5번",Toast.LENGTH_SHORT).show();
                CallAsyncTask();
            }
        });

    }
    public void onClick2(View view){
        Intent intent = new Intent(this,ManagerSubActivity.class);
        intent.putExtra("Email", Email);
        intent.putExtra("Name", Name);
        startActivity(intent);
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

                    URL url  = new URL("http://113.198.229.173/Expert_List.php");
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
                        Log.e("CY",line);
                    }
                    Log.e("CY_1",stringBuilder.toString());
                    JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
                    JSONArray jsonArray = new JSONArray(jsonResponse.getString("response"));
                    Log.e("CY_2",jsonArray.toString());
                    items.clear();  // add 하면 뒤에 계속 들어가니까 버튼을 누를때 마다 초기화 시켰다.

                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                        items.add(new com.example.cy.cody_.Expert.Item(
                                dataJsonObj.getInt("ID"),
                                dataJsonObj.getString("Title"),
                                dataJsonObj.getString("Expert_pic"),
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

                recyclerView.setAdapter(new com.example.cy.cody_.Expert.RecyclerAdapter(getApplicationContext(), items, R.layout.activity_expert));
                recyclerView.setLayoutManager( new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL) );

            }
        }.execute();

    }

}

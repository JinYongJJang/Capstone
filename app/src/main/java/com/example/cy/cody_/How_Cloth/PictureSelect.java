package com.example.cy.cody_.How_Cloth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class PictureSelect extends AppCompatActivity {

    String Email;
    ListView listview;
    String Click_Position = null;

    ArrayList<ListViewItem_HC> data ;
    ListViewAdapter_HC Myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_select);

        Intent GetIntent = getIntent();
        Email = GetIntent.getExtras().getString("Email");

        listview = (ListView) findViewById(R.id.ListView_HC);

        //포문으로 json array length 까지 data.add(~~)
        CallAsyncTask_PictureSelect();   // AsyncTask 실행

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {  // 리스틑뷰 클릭시
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Click_Position = String.valueOf(data.get(position).getID());  // 리스트뷰가 보여주는 ID를 저장한다.
            }
        });

        Button Select_button_HC = findViewById(R.id.Select_Button_HC);
        Select_button_HC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Return_Intent = new Intent();
                Return_Intent.putExtra("Click_Position", Click_Position);
                setResult(RESULT_OK, Return_Intent);
                finish();
            }
        });
        
    }

    @SuppressLint("StaticFieldLeak")
    private void CallAsyncTask_PictureSelect() {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... strings) {
                final HttpURLConnection urlConnection_HC;
                try{
                    /*****************************                전송                 *********************************/
                    JSONObject json = new JSONObject();
                    json.put("Email", Email);

                    URL url = new URL("http://113.198.229.173/PictureSelect_HC.php");
                    urlConnection_HC = (HttpURLConnection) url.openConnection();

                    urlConnection_HC.setRequestMethod("POST");  // 요청방식 설정
                    urlConnection_HC.setDoOutput(true);  // 서버로 응답을 보내겠다.
                    urlConnection_HC.setDoInput(true); // 서버로부터 응답을 받겠다.
                    urlConnection_HC.setConnectTimeout(5000);  // 응답 대기시간 설정

                    OutputStream OutputStream_HC = urlConnection_HC.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(OutputStream_HC, "UTF-8"));

                    writer.write("user=" + json.toString());
                    writer.flush();
                    /****************************************************************************************************/

                    urlConnection_HC.connect();
                    BufferedReader bufferedReader_HC = new BufferedReader(new InputStreamReader(urlConnection_HC.getInputStream(), "UTF-8"));
                    StringBuilder stringBuilder_HC = new StringBuilder();
                    String line = null;
                    while( (line = bufferedReader_HC.readLine()) != null){
                        if( stringBuilder_HC.length() > 0){
                            stringBuilder_HC.append("\n");
                        }
                        stringBuilder_HC.append(line);
                    }


                    data = new ArrayList<ListViewItem_HC>();

                    JSONObject jsonResponse = new JSONObject(stringBuilder_HC.toString());
                    JSONArray jsonArray = new JSONArray(jsonResponse.getString("response"));


                    for(int i=0; i<jsonArray.length(); i++) {
                        JSONObject dataJsonObj = jsonArray.getJSONObject(i);
                        data.add(new ListViewItem_HC(
                                dataJsonObj.getInt("ID"),
                                dataJsonObj.getString("Top_file"),
                                dataJsonObj.getString("Bottom_file"),
                                dataJsonObj.getString("Coat_file")
                        ));
                    }
                    writer.close();



                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                catch (IOException e){
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

                Myadapter = new ListViewAdapter_HC(getApplicationContext(), R.layout.list_item_hc, data);
                listview.setAdapter(Myadapter);
            }
        }.execute();
    }

}

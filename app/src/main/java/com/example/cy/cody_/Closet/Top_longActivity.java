package com.example.cy.cody_.Closet;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.cy.cody_.Login.UserinfoActivity;
import com.example.cy.cody_.R;

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

public class  Top_longActivity extends AppCompatActivity{
    private DrawerLayout mDrawerLayout; // 서랍 레이아웃
    private RecyclerView mRecyclerView;
    private ListViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private File file;
    private ArrayList<String> Top_list;
    String Email;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_long);

        /**********************    메인에서 로그인이 되어있을때 값을 받아옴    *******************/
        Intent GetIntent = getIntent();
        Email = GetIntent.getExtras().getString("Email");
        /*************************************************************************************/

        gridView = (GridView) findViewById(R.id.gridview1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); // 뒤로가기 버튼
        actionBar.setDisplayHomeAsUpEnabled(true); // 네비게이션 메뉴를 사용하기 위한 코드

        mDrawerLayout = findViewById(R.id.drawer_layout); // 네비게이션 메뉴 사용 객체

        NavigationView navigationView = findViewById(R.id.navigation_view);
        // 네비게이션 메뉴바 구동
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_attachment:
                        Intent MyInfo = new Intent(Top_longActivity.this, UserinfoActivity.class);
                        startActivity(MyInfo);
                        break;
                    case R.id.nav_sub_menu_item01:
                        Toast.makeText(Top_longActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_sub_menu_item02:
                        Toast.makeText(Top_longActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_sub_menu_item03:
                        Toast.makeText(Top_longActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_sub_menu_item04:
                        Toast.makeText(Top_longActivity.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                        break;
                }
                return true;
            }
        });


        AsyncTask_Top();






//        String rootSD = Environment.getExternalStorageDirectory().toString();
//        file = new File(rootSD+"/Pictures");
//        File[] list = file.listFiles();// SD 카드 전체 파일을 다 불러 오는 친구들
//
//        for(int i=0; i<list.length;i++){
//            if(list[i].getName().substring( (list[i].getName().length()-8), (list[i].getName().length()-4) ).equals("_top")){  // 맨 뒷글자 비교
//                Top_list.add(list[i]);
//            }
//        }



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(Top_longActivity.this, ""+position,Toast.LENGTH_SHORT).show();
            }
        });
    }








    // 보통 ListView는 통신을 통해 가져온 데이터를 보여줍니다.
    // arrResId, titles, contents를 서버에서 가져온 데이터라고 생각하시면 됩니다.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // 메뉴키를 눌렀을때
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }// 메뉴키를 눌렀을때 해당 아이디에 있는 메뉴를 객체화 시킨다.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 메뉴 아이템이 클릭되었을때 발동
        int id = item.getItemId(); // 아이템의 아이디를 얻는다.
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START); // 햄버거 메뉴를 눌렀을때
                return true;
            case R.id.action_settings: // 점 3개를 눌렀을때
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressLint("StaticFieldLeak")
    private void AsyncTask_Top() {
        new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                Top_list= new ArrayList<>();
            }

            @Override
            protected String doInBackground(String... strings) {
                final HttpURLConnection urlConnection_HC;
                try{
                    /*****************************                전송                 *********************************/
                    JSONObject json = new JSONObject();
                    json.put("Email", Email);

                    URL url = new URL("http://113.198.229.173/Top.php");
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
                    BufferedReader bufferedReader_Top = new BufferedReader(new InputStreamReader(urlConnection_HC.getInputStream(), "UTF-8"));
                    StringBuilder stringBuilder_Top = new StringBuilder();
                    String line = null;
                    while( (line = bufferedReader_Top.readLine()) != null){
                        if( stringBuilder_Top.length() > 0){
                            stringBuilder_Top.append("\n");
                        }
                        stringBuilder_Top.append(line);
                        Log.v("JIN", line);
                    }

                    JSONObject jsonResponse = new JSONObject(stringBuilder_Top.toString());
                    Log.v("JIN_res", jsonResponse.toString());
                    JSONArray jsonArray = new JSONArray(jsonResponse.getString("response"));
                    Log.v("JIN_ARR", String.valueOf( jsonArray.toString() ) );

                    for(int i=0; i < jsonArray.length(); i++){
                        JSONObject DataJsonObject = jsonArray.getJSONObject(i);
                        Top_list.add( DataJsonObject.getString("Top_file") );

                    }

                    Log.v("JIN_TOP", Top_list.toString());


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

                gridView.setAdapter( new ImageAdapter(Top_longActivity.this, Top_list, "Top") );

            }
        }.execute();
    }
}

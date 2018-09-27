package com.example.cy.cody_;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class How_clothActivity extends AppCompatActivity {
    final int ITEM_SIZE = 4;
    RecyclerView recyclerView;
    RecyclerView.Adapter Adapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_cloth);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManager);

        List<Item> items = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];
        item[0] = new Item(R.drawable.a, " 제 옷 좀 봐주세요.");
        item[1] = new Item(R.drawable.jw, "  내 혓바닥이 제일 길지");
        item[2] = new Item(R.drawable.jy, "  코뿔소 닮은 사람 나와");
        item[3] = new Item(R.drawable.cy, "  똥싸러 인천 간 똥창");

        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_how_cloth));
//        Intent intent=new Intent(this,SubActivity.class);
//        startActivity(intent);
//        finish();
    }
    public void onClick(View view){
        Intent intent=new Intent(this,SubActivity.class);
        startActivity(intent);
    }


}


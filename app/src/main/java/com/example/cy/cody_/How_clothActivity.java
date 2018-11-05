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
        item[0] = new Item(R.drawable.cloth_01, "옷 색깔 어때요?.");
        item[1] = new Item(R.drawable.cloth_02, "이 아우터 어디꺼죠?");
        item[2] = new Item(R.drawable.cloth_03, "소개팅 코디 괜춘?");
        item[3] = new Item(R.drawable.cloth_04, "이 옷은 어때요?");

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
    void count(View view){

    }

}


package com.example.cy.cody_;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Fast_codyActivity extends AppCompatActivity {
    private ImageView Top_Cody,Bottom_Cody,Outer_Cody;
    private ArrayList clothes_images;
    private ArrayList <File>Top_list;
    private ArrayList <File>Bottom_list;
    private ArrayList <File>Outer_list;
    private Button restart_cody;

    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_cody);
        Top_Cody = (ImageView)findViewById(R.id.top_cody);
        Bottom_Cody = (ImageView)findViewById(R.id.bottom_cody);
        Outer_Cody = (ImageView)findViewById(R.id.outer_cody);
        restart_cody = (Button)findViewById(R.id.cody_restart);
        clothes_images= new ArrayList<>();

        String rootSD = Environment.getExternalStorageDirectory().toString();
        file = new File(rootSD+"/Pictures");
        File[] list = file.listFiles();// SD 카드 전체 파일을 다 불러 오는 친구들
        Top_list = new ArrayList<File>();
        Bottom_list = new ArrayList<File>();
        Outer_list = new ArrayList<File>();
        for(int i=0; i<list.length;i++){
            if(list[i].getName().substring((list[i].getName().length()-8),(list[i].getName().length()-4)).equals("_top")){
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

        int top_num = top_random_f.nextInt(Top_list.size());
        int bottom_num = outer_random_f.nextInt(Bottom_list.size());
        int outer_num = bottom_random_f.nextInt(Outer_list.size());

        Top_Cody.setImageBitmap((BitmapFactory.decodeFile(Top_list.get(top_num).getAbsolutePath())));
        Bottom_Cody.setImageBitmap((BitmapFactory.decodeFile(Bottom_list.get(bottom_num).getAbsolutePath())));
        Outer_Cody.setImageBitmap((BitmapFactory.decodeFile(Outer_list.get(outer_num).getAbsolutePath())));

        restart_cody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random top_random = new Random();
                Random outer_random = new Random();
                Random bottom_random = new Random();

                int top_num = top_random.nextInt(Top_list.size());
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
}
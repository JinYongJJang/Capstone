package com.example.cy.cody_.Closet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private String Check;
    private ArrayList<File> Top_bitmap = new ArrayList<File>();
    private ArrayList<File> Outer_bitmap = new ArrayList<File>();
    private ArrayList<File> Bottom_bitmap = new ArrayList<File>();


    public ImageAdapter(Context c,ArrayList<File> uri,String Ch){
        mContext = c;
        if(Ch.equals("Top")) {
            Check = Ch;
            for (File a : uri) {
                Top_bitmap.add(a);
            }
        }
        else if(Ch.equals("Outer")) {
            Check = Ch;
            for (File a : uri) {
                Outer_bitmap.add(a);
            }
        }
        else if(Ch.equals("Bottom")) {
            Check = Ch;
            for (File a : uri) {
                Bottom_bitmap.add(a);
            }
        }
    }

    @Override
    public int getCount() {
        if(Check.equals("Top"))
            return Top_bitmap.size();
        else if(Check.equals("Outer"))
            return Outer_bitmap.size();
        else if(Check.equals("Bottom"))
            return Bottom_bitmap.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null){
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(600,600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8,8,8,8);
        }
        else{
            imageView = (ImageView) convertView;
        }
        //Log.e("User bitmap", bitmap.get(position).toString());
        if(Check.equals("Top"))
            imageView.setImageBitmap((BitmapFactory.decodeFile(Top_bitmap.get(position).getAbsolutePath())));
        else if(Check.equals("Outer"))
            imageView.setImageBitmap((BitmapFactory.decodeFile(Outer_bitmap.get(position).getAbsolutePath())));
        else if(Check.equals("Bottom"))
            imageView.setImageBitmap((BitmapFactory.decodeFile(Bottom_bitmap.get(position).getAbsolutePath())));
        return imageView;
    }

}
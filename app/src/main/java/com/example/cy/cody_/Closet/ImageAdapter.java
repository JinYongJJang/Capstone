package com.example.cy.cody_.Closet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
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
    private ArrayList<String> Top_bitmap = new ArrayList();
    private ArrayList<String> Outer_bitmap = new ArrayList();
    private ArrayList<String> Bottom_bitmap = new ArrayList();

    byte[] decodedString_Top;
    byte[] decodedString_Bottom;
    byte[] decodedString_Outer;


    public ImageAdapter(Context c, ArrayList<String> string, String Ch){
        mContext = c;
        if(Ch.equals("Top")) {
            Check = Ch;
            for (String a : string) {
                Top_bitmap.add(a);
            }
        }
        else if(Ch.equals("Bottom")) {
            Check = Ch;
            for (String a : string) {
                Bottom_bitmap.add(a);
            }
        }
        else if(Ch.equals("Outer")) {
            Check = Ch;
            for (String a : string) {
                Outer_bitmap.add(a);
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
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(600, 600));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        //Log.e("User bitmap", bitmap.get(position).toString());
        if (Check.equals("Top")) {
            decodedString_Top = Base64.decode(Top_bitmap.get(position), Base64.DEFAULT);
            Bitmap decodeByte_Top = BitmapFactory.decodeByteArray(decodedString_Top, 0, decodedString_Top.length);
            imageView.setImageBitmap(decodeByte_Top);
        }

        else if ( Check.equals("Bottom") ){
            decodedString_Bottom = Base64.decode(Bottom_bitmap.get(position), Base64.DEFAULT);
            Bitmap decodeByte_Bottom = BitmapFactory.decodeByteArray(decodedString_Bottom, 0, decodedString_Bottom.length);
            imageView.setImageBitmap(decodeByte_Bottom);
        }
        else if(Check.equals("Outer")){
            decodedString_Outer = Base64.decode(Outer_bitmap.get(position), Base64.DEFAULT);
            Bitmap decodeByte_Outer = BitmapFactory.decodeByteArray(decodedString_Outer, 0, decodedString_Outer.length);
            imageView.setImageBitmap(decodeByte_Outer);
        }

        return imageView;
    }
}
package com.example.cy.cody_.How_Cloth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.cy.cody_.R;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter_HC extends BaseAdapter {

    private Context context;
    private LayoutInflater Inflater;
    private ArrayList<ListViewItem_HC> Data;
    private int Layout;

    public ListViewAdapter_HC(Context context, int layout, ArrayList<ListViewItem_HC> data){
        this.context = context;
        Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Data = data;
        Layout = layout;
    }

    @Override
    public int getCount() {
        return Data.size();
    }

    @Override
    public Object getItem(int i) {
        return Data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = Inflater.inflate(this.Layout, parent, false);
        }

        ImageView Top = convertView.findViewById(R.id.Top_Image);
        ImageView Bottom = convertView.findViewById(R.id.Bottom_Image);
        ImageView Coat = convertView.findViewById(R.id.Coat_Image);

        byte[] decodedString_Top = Base64.decode(Data.get(postion).getTop(), Base64.DEFAULT);
        Bitmap decodeByte_Top = BitmapFactory.decodeByteArray(decodedString_Top, 0, decodedString_Top.length);
        Top.setImageBitmap(decodeByte_Top);

        byte[] decodedString_Bottom = Base64.decode(Data.get(postion).getBottom(), Base64.DEFAULT);
        Bitmap decodeByte_Bottom = BitmapFactory.decodeByteArray(decodedString_Bottom, 0, decodedString_Bottom.length);
        Bottom.setImageBitmap(decodeByte_Bottom);

        byte[] decodedString_Coat = Base64.decode(Data.get(postion).getCoat(), Base64.DEFAULT);
        Bitmap decodeByte_Coat = BitmapFactory.decodeByteArray(decodedString_Coat, 0, decodedString_Coat.length);
        Coat.setImageBitmap(decodeByte_Coat);

        return convertView;
    }
}

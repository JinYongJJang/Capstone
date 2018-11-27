package com.example.cy.cody_.How_Cloth;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cy.cody_.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HC_PageAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;
    ArrayList<byte[]> images = new ArrayList<>();

    public HC_PageAdapter(Context context, JSONArray Cody_ID_JSON){
        this.context = context;
        for(int i=0; i<Cody_ID_JSON.length(); i++){
            try{
                JSONObject json = Cody_ID_JSON.getJSONObject(i);
                images.add(Base64.decode(json.getString("Picture"), Base64.DEFAULT));
            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((View)o);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView Image_Slider = (ImageView)v.findViewById(R.id.Image_Slider);
        Bitmap decodeByte_Coat = BitmapFactory.decodeByteArray(images.get(position), 0, images.get(position).length);
        Image_Slider.setImageBitmap(decodeByte_Coat);
        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.invalidate();
    }
}

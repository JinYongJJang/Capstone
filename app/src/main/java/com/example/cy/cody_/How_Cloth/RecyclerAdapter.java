package com.example.cy.cody_.How_Cloth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.cy.cody_.R;

import java.util.List;
public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<Item> items;
    int item_layout;
    byte[] decodedString;
    Bitmap decodeByte;

    public RecyclerAdapter(Context context, List<Item> items, int item_layout) {

        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = items.get(position);

        //Drawable drawable = ContextCompat.getDrawable(context, item.getImage());

        holder.User_Name.setText(item.getUser_Name()); // User -> User_Name
        holder.User_Picture.setBackgroundColor(200);  // User -> User_Picture
        //holder.image.setBackground(drawable);

        decodedString = Base64.decode(item.getImage(), Base64.DEFAULT);
        decodeByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.image.setImageBitmap(decodeByte); // How_Cloth -> Cody_ID

        holder.title.setText(item.getTitle());  // How_Cloth -> Title

        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Boardintro.class);
                intent.putExtra("ID",item.getID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView User_Picture;
        TextView User_Name;
        ImageView image;
        TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            User_Picture = (ImageView) itemView.findViewById(R.id.User_Picture);
            User_Name = (TextView) itemView.findViewById(R.id.User_Name);
            image = (ImageView) itemView.findViewById(R.id.image);
            title = (TextView) itemView.findViewById(R.id.title);
            cardview = (CardView) itemView.findViewById(R.id.cardview);
        }
    }
}

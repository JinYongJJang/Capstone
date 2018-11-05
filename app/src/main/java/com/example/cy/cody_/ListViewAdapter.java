package com.example.cy.cody_;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private ArrayList<ClosetActivity.item> mDataset; //MainActivity에 item class를 정의해 놓았음
    Context context;
    public int test;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 사용될 항목들 선언
        public TextView mName;
        public ImageView mPhoto;
        public CardView cv;
        public View view;

        public ViewHolder(View v) {
            super(v);

            mName = (TextView) v.findViewById(R.id.info_text);
            mPhoto = (ImageView)v.findViewById(R.id.iv_photo);
            cv = (CardView)v.findViewById(R.id.card_view);
        }
    }

    // 생성자 - 넘어 오는 데이터파입에 유의해야 한다.
    public ListViewAdapter(ArrayList<ClosetActivity.item> myDataset) {
        mDataset = myDataset;
    }


    //뷰홀더
    // Create new views (invoked by the layout manager)
    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        context = v.getContext();
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mName.setText(mDataset.get(position).getName());
        holder.mPhoto.setImageResource(mDataset.get(position).getPhoto());
        final int pos = position;

        holder.cv.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(holder.getAdapterPosition() == 0){
                    Toast.makeText(v.getContext(),"상의",Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context,Top_longActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                else if(holder.getAdapterPosition() == 1){
                    Toast.makeText(v.getContext(),"하의",Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context,Bottom_longActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
                else if(holder.getAdapterPosition() == 2){
                    Toast.makeText(v.getContext(),"아우터",Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context,OuterActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}



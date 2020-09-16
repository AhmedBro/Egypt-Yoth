package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.NewModel;
import com.ahmedesam.egyptyouth.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNews extends RecyclerView.Adapter<AllNews.holder> {
    ArrayList<NewModel> mItems;
    Context mContext;


    public AllNews(ArrayList<NewModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_news_shap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        holder.mNewHeader.setText(mItems.get(position).getmHeader());
        Glide.with(mContext).load(mItems.get(position).getmImage()).into(holder.mNewImage);
        holder.mNew.setText(mItems.get(position).getmNew());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class holder extends RecyclerView.ViewHolder {

        ImageView mNewImage;

        TextView mNewHeader;

        TextView mNew;
        public holder(@NonNull View itemView) {
            super(itemView);
            mNewHeader = itemView.findViewById(R.id.mNewHeader);
            mNewImage= itemView.findViewById(R.id.mNewImage);
            mNew = itemView.findViewById(R.id.mNew);
        }

    }
}

package com.ahmedesam.egyptyouth.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.NewModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Ui.fragments.ShowNewFragment;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InternationalAndNationalAdapter extends RecyclerView.Adapter<InternationalAndNationalAdapter.holder> {
    ArrayList<NewModel> mItems;
    Context mContext;

    public InternationalAndNationalAdapter(ArrayList<NewModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.international_news_shap, parent, false);
        holder mHolder = new holder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.mNewHeader.setText(mItems.get(position).getmHeader());
        Glide.with(mContext).load(mItems.get(position).getmImage()).into(holder.mNewImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowNewFragment mShowNewFragment = new ShowNewFragment(mItems.get(position).getmPostID());


                mShowNewFragment.show(((AppCompatActivity) mContext).getSupportFragmentManager() , "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class holder extends RecyclerView.ViewHolder {
        ImageView mNewImage;
        TextView mNewHeader;

        public holder(@NonNull View itemView) {
            super(itemView);
            mNewHeader = itemView.findViewById(R.id.mNewHeader);
            mNewImage= itemView.findViewById(R.id.mNewImage);


        }
    }
}

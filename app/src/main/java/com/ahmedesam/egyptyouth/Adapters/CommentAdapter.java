package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.CommentModel;
import com.ahmedesam.egyptyouth.R;
import com.bumptech.glide.Glide;
import com.devs.readmoreoption.ReadMoreOption;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Holder> {
    ArrayList<CommentModel> mItem;
    Context mContext;
    private ReadMoreOption readMoreOption;

    public CommentAdapter() {
    }

    public CommentAdapter(ArrayList<CommentModel> mItem, Context mContext) {
        this.mItem = mItem;
        this.mContext = mContext;
        readMoreOption = new ReadMoreOption.Builder(mContext).moreLabelColor(Color.BLACK)
                .lessLabelColor(Color.BLACK)
                .build();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_shap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        readMoreOption.addReadMoreTo(holder.mComment, mItem.get(position).getmText());
        holder.mUserName.setText(mItem.get(position).getmUserName());
        Glide.with(mContext).load(mItem.get(position).getmUserImage()).into(holder.mUserImage);
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.mUserImage)
        CircleImageView mUserImage;
        @BindView(R.id.mUserName)
        TextView mUserName;
        @BindView(R.id.mComment)
        TextView mComment;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

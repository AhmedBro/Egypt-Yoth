package com.ahmedesam.egyptyouth.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.PostModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.ahmedesam.egyptyouth.Ui.Activities.CommentsActivity;
import com.ahmedesam.egyptyouth.Ui.Activities.UserInfo;
import com.ahmedesam.egyptyouth.Ui.fragments.LikesFragment;
import com.brouding.doubletaplikeview.DoubleTapLikeView;
import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.devs.readmoreoption.ReadMoreOption;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import needle.Needle;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.holder> {
    ArrayList<PostModel> mItems;
    Context mContext;

    static Boolean PlayVideo = true;
    ShardPrefrances mShardPrefrances;


    private ReadMoreOption readMoreOption;

    public PostsAdapter(Boolean playVideo) {
        PlayVideo = playVideo;
    }

    public PostsAdapter() {
    }

    public PostsAdapter(ArrayList<PostModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
        readMoreOption = new ReadMoreOption.Builder(mContext).moreLabelColor(Color.WHITE)
                .lessLabelColor(Color.WHITE)
                .build();
        mShardPrefrances = new ShardPrefrances(mContext);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_shap, parent, false));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        if (!mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID).equals(mItems.get(position).getmUserId())){
            holder.mChoose.setVisibility(View.GONE);
        }


        FirebaseFirestore mDatabaseReference = FirebaseFirestore.getInstance();
        mDatabaseReference.collection("Posts").document(mItems.get(position).getmPostID()).collection("User Liked").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        if (document.getData().get("mId").toString().equals(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID))) {
                            holder.likeButton.setChecked(true);
                            break;
                        }
                    }
                }

            }
        });

        holder.mCommentsNumber.setText(mItems.get(position).getmCommentsNumber() + " " + "Comment");

        holder.mUserName.setText(mItems.get(position).getmUserName());

        if (Integer.parseInt(mItems.get(position).getmLikeNumber()) < 0) {
            holder.mLikeNumber.setText("0" + " " + "Likes");
        } else {
            holder.mLikeNumber.setText(mItems.get(position).getmLikeNumber() + " " + "Likes");
        }
        Glide.with(mContext).load(mItems.get(position).getmUserImage()).into(holder.mUserImage);
        readMoreOption.addReadMoreTo(holder.mPostText, mItems.get(position).getmPost());
        if (mItems.get(position).getmImage().isEmpty()) {
            holder.mPostImage.setVisibility(View.GONE);
        }

        if (mItems.get(position).getmVideo().isEmpty()) {
            holder.mPostVideo.setVisibility(View.GONE);
        }

        if (!mItems.get(position).getmImage().isEmpty()) {
            holder.mPostImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(mItems.get(position).getmImage()).into(holder.mPostImage);
        }

        if (!mItems.get(position).getmVideo().isEmpty()) {
            holder.mPostVideo.setVisibility(View.VISIBLE);
            holder.mPostVideo.setVideoURI(Uri.parse(mItems.get(position).getmVideo()));
            holder.DoubleTap.setVisibility(View.GONE);
            holder.mPostVideo.setOnCompletionListener(new OnCompletionListener() {
                @Override
                public void onCompletion() {
                    holder.mPostVideo.restart();
                }
            });
        }

        final double[] doubleClickLastTime = {0L};
        holder.mPostVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (System.currentTimeMillis() - doubleClickLastTime[0] < 300) {
                    doubleClickLastTime[0] = 0;
                    holder.likeButton.setChecked(true);
                } else {
                    doubleClickLastTime[0] = System.currentTimeMillis();
                }
            }
        });

        holder.DoubleTap.setOnTapListener(new DoubleTapLikeView.OnTapListener() {
            @Override
            public void onDoubleTap(View view) {
                if (!holder.likeButton.isChecked()) {
                    holder.likeButton.setChecked(true);
                    holder.likeButton.playAnimation();
                }
            }

            @Override
            public void onTap() {

            }
        });

        holder.mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, UserInfo.class);
                mIntent.putExtra("ID", mItems.get(position).getmUserId());
                mContext.startActivity(mIntent);
            }
        });

        holder.mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, UserInfo.class);
                mIntent.putExtra("ID", mItems.get(position).getmUserId());
                mContext.startActivity(mIntent);
            }
        });
        holder.likeButton.setEventListener(new SparkEventListener() {
            HashMap<String, Object> map = new HashMap<String, Object>();
            HashMap<String, Object> map2 = new HashMap<String, Object>();


            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (!buttonState) {

                    map.put("mLikeNumber", String.valueOf(Integer.parseInt(mItems.get(position).getmLikeNumber()) - 1));
                    mDatabaseReference.collection("Posts").document(mItems.get(position).getmPostID()).update(map);
                    mDatabaseReference.collection("Posts").document(mItems.get(position).getmPostID()).collection("User Liked").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).delete();

                }
            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {

                if (buttonState) {
                    map.put("mLikeNumber", String.valueOf(Integer.parseInt(mItems.get(position).getmLikeNumber()) + 1));
                    FirebaseFirestore mDatabaseReference = FirebaseFirestore.getInstance();
                    mDatabaseReference.collection("Posts").document(mItems.get(position).getmPostID()).update(map);
                    map2.put("mId", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
                    map2.put("mUserName", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME));
                    map2.put("mUserImage", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_IMAGE));
                    mDatabaseReference.collection("Posts").document(mItems.get(position).getmPostID()).collection("User Liked").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).set(map2);


                }
            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });

        holder.mAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CommentsActivity.class);
                CommentsActivity mCommentsActivity = new CommentsActivity(mItems.get(position).getmPostID());
                mContext.startActivity(mIntent);
            }
        });
        holder.mCommentsNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, CommentsActivity.class);
                CommentsActivity mCommentsActivity = new CommentsActivity(mItems.get(position).getmPostID());
                mContext.startActivity(mIntent);
            }
        });

        holder.mLikeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikesFragment mCommentsFragment = new LikesFragment(mItems.get(position).getmPostID());
                mCommentsFragment.show(((FragmentActivity) mContext).getSupportFragmentManager(), "");
            }
        });

        holder.mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mContext, holder.mChoose);
                //inflating menu from xml resource
                popup.inflate(R.menu.delete);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mDelete:
                                FirebaseFirestore.getInstance().collection("Posts").document(mItems.get(position).getmPostID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class holder extends RecyclerView.ViewHolder {
        @BindView(R.id.mUserImage)
        CircleImageView mUserImage;
        @BindView(R.id.mUserName)
        TextView mUserName;
        @BindView(R.id.mPostImage)
        RoundedImageView mPostImage;
        @BindView(R.id.mPostVideo)
        VideoView mPostVideo;
        @BindView(R.id.like_button)
        SparkButton likeButton;
        @BindView(R.id.mAddComment)
        ImageView mAddComment;
        @BindView(R.id.mPostText)
        TextView mPostText;
        @BindView(R.id.DoubleTap)
        DoubleTapLikeView DoubleTap;

        @BindView(R.id.mLikeNumber)
        TextView mLikeNumber;
        @BindView(R.id.mCommentsNumber)
        TextView mCommentsNumber;
        @BindView(R.id.mParent)
        ConstraintLayout mParent;
        @BindView(R.id.mChoose)
        ImageView mChoose;

        public holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (mShardPrefrances.IsDark()) {


                mParent.setBackground(mContext.getResources().getDrawable(R.color.white));
                mLikeNumber.setTextColor(mContext.getResources().getColor(R.color.black));
                mUserName.setTextColor(mContext.getResources().getColor(R.color.black));
                mPostText.setTextColor(mContext.getResources().getColor(R.color.black));
                mCommentsNumber.setTextColor(mContext.getResources().getColor(R.color.black));
                mAddComment.setColorFilter(mContext.getResources().getColor(R.color.black));
                likeButton.setInactiveImage(R.drawable.heart);

            } else {


                mParent.setBackground(mContext.getResources().getDrawable(R.color.black));
                mLikeNumber.setTextColor(mContext.getResources().getColor(R.color.white));
                mUserName.setTextColor(mContext.getResources().getColor(R.color.white));
                mCommentsNumber.setTextColor(mContext.getResources().getColor(R.color.white));
                mPostText.setTextColor(mContext.getResources().getColor(R.color.white));
                mAddComment.setColorFilter(mContext.getResources().getColor(R.color.white));
                likeButton.setInactiveImage(R.drawable.white);

            }
        }

    }


}

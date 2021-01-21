package com.ahmedesam.egyptyouth.Ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Service.TouchImageView;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowFullImages extends AppCompatActivity {
    static String Url;
    @BindView(R.id.mChoose)
    ImageView mChoose;
    @BindView(R.id.mShare)
    ImageView mShare;
    ShardPrefrances mShardPrefrances;
    static String mImageId;
    static String mFrom;

    public ShowFullImages(String url, String mFrom, String mImageId) {
        ShowFullImages.mFrom = mFrom;
        Url = url;
        this.mImageId = mImageId;
    }

    public ShowFullImages() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_images);
        ButterKnife.bind(this);
        TouchImageView mRoundedImageView = findViewById(R.id.iv_zoom);
        Glide.with(this).load(Url).into(mRoundedImageView);
        mShardPrefrances = new ShardPrefrances(this);
        if (!mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID).equals(mFrom)) {
            mChoose.setVisibility(View.GONE);
        }

    }

    @OnClick({R.id.mChoose, R.id.mShare})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mChoose:
                PopupMenu popup = new PopupMenu(this, mChoose);
                //inflating menu from xml resource
                popup.inflate(R.menu.delete);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mDelete:
                                FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).collection("Images").document(mImageId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                        Toast.makeText(ShowFullImages.this, "Deleted", Toast.LENGTH_SHORT).show();
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

                break;
            case R.id.mShare:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, Url);

                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "مشاركة بواسطة .."));
                break;
        }
    }
}
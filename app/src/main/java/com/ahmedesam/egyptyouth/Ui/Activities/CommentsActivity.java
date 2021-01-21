package com.ahmedesam.egyptyouth.Ui.Activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.CommentAdapter;
import com.ahmedesam.egyptyouth.Models.CommentModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsActivity extends AppCompatActivity {
    @BindView(R.id.Comments)
    RecyclerView Comments;
    @BindView(R.id.mUserComment)
    EditText mUserComment;


    static String Id;
    FirebaseFirestore mDatabaseReference;
    DatabaseReference databaseReference;
    CommentModel model;
    ShardPrefrances mShardPrefrances;
    @BindView(R.id.mUserImage)
    CircleImageView mUserImage;
    ArrayList<CommentModel> mComments;
    CommentModel mCommentModel;
    CommentAdapter mCommentAdapter;
    MediaPlayer mediaPlayer;


    @BindView(R.id.mFinish)
    ImageView mFinish;
    @BindView(R.id.mSend)
    ImageView mSend;

    public CommentsActivity(String id) {
        Id = id;
    }

    public CommentsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        mDatabaseReference = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mShardPrefrances = new ShardPrefrances(this);
        Glide.with(this).load(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_IMAGE)).into(mUserImage);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        Comments.setLayoutManager(manager);
        mCommentAdapter = new CommentAdapter();
        GetAllComment();
    }


    private void GetAllComment() {
        mComments = new ArrayList<>();
        mDatabaseReference.collection("Posts").document(Id).collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mComments.clear();
                for (QueryDocumentSnapshot document : value) {
                    mCommentModel = new CommentModel(document.getData().get("mText").toString(),
                            document.getData().get("mUserName").toString(),
                            document.getData().get("mUserImage").toString(),
                            document.getData().get("mId").toString(),
                            document.getData().get("mUserId").toString());

                    mComments.add(mCommentModel);
                }
                mCommentAdapter = new CommentAdapter(mComments, CommentsActivity.this);
                Comments.setAdapter(mCommentAdapter);

                SetCommentNumber(String.valueOf(mComments.size()));
            }
        });

    }

    private void SetCommentNumber(String valueOf) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("mCommentsNumber", valueOf);
        mDatabaseReference.collection("Posts").document(Id).update(map);
    }


    private void SendComment() {
        if (!mUserComment.getText().toString().isEmpty()) {
            String id = databaseReference.push().getKey();
            model = new CommentModel(mUserComment.getText().toString(), mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME), mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_IMAGE), id, mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
            mDatabaseReference.collection("Posts").document(Id).collection("Comments").document(id).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    mUserComment.setText("");
                    Toast.makeText(CommentsActivity.this, R.string.add_Comment, Toast.LENGTH_SHORT).show();

                    mediaPlayer = MediaPlayer.create(CommentsActivity.this, R.raw.comment);
                    mediaPlayer.start();
                }
            });


        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCommentAdapter.notifyDataSetChanged();

    }

    @OnClick({R.id.mFinish, R.id.mSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mFinish:
                finish();
                break;
            case R.id.mSend:
                SendComment();
                break;
        }
    }
}
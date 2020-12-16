package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.CommentAdapter;
import com.ahmedesam.egyptyouth.Models.CommentModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class CommentsFragment extends BottomSheetDialogFragment {
    View view;
    Unbinder unbinder;
    @BindView(R.id.Comments)
    RecyclerView Comments;
    @BindView(R.id.mUserComment)
    EditText mUserComment;
    @BindView(R.id.mSend)
    ImageView mSend;

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

    public CommentsFragment() {
        // Required empty public constructor
    }

    public CommentsFragment(String id) {
        Id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_comments, container, false);
        unbinder = ButterKnife.bind(this, view);
        mDatabaseReference = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mShardPrefrances = new ShardPrefrances(getContext());
        Glide.with(getContext()).load(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_IMAGE)).into(mUserImage);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        Comments.setLayoutManager(manager);
        mCommentAdapter = new CommentAdapter();
        GetAllComment();
        return view;
    }

    private void GetAllComment() {
        mComments = new ArrayList<>();
        mDatabaseReference.collection("Posts").document(Id).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        mCommentModel = new CommentModel(document.getData().get("mText").toString(),
                                document.getData().get("mUserName").toString(),
                                document.getData().get("mUserImage").toString(),
                                document.getData().get("mId").toString(),
                                document.getData().get("mUserId").toString());

                        mComments.add(mCommentModel);
                    }
                    mCommentAdapter = new CommentAdapter(mComments, getActivity());
                    Comments.setAdapter(mCommentAdapter);
                }
            }
        });

    }

    @OnClick(R.id.mSend)
    public void onViewClicked() {
        SendComment();
        Log.e("Clllcl", "Clllcikcmlc");
    }

    private void SendComment() {
        if (!mUserComment.getText().toString().isEmpty()) {
            String id = databaseReference.push().getKey();
            model = new CommentModel(mUserComment.getText().toString(), mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME), mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_IMAGE), id, mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
            mDatabaseReference.collection("Posts").document(Id).collection("Comments").document(id).set(model);
            mUserComment.setText("");
            dismiss();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCommentAdapter.notifyDataSetChanged();
    }
}
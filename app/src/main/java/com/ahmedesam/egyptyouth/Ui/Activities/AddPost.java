package com.ahmedesam.egyptyouth.Ui.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.Models.PostModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddPost extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 1;
    private final int PICK_VIDEO_REQUEST = 2;

    @BindView(R.id.mEnd)
    ImageView mEnd;
    @BindView(R.id.mPublish)
    ImageView mPublish;
    @BindView(R.id.mCaption)
    EditText mCaption;
    @BindView(R.id.mPostImage)
    RoundedImageView mPostImage;
    @BindView(R.id.mPostVideo)
    VideoView mPostVideo;
    @BindView(R.id.mUploadImage)
    Button mUploadImage;
    @BindView(R.id.mUploadVideo)
    Button mUploadVideo;
    FirebaseStorage storage;
    StorageReference storageReference;
    static Uri filePath = null;
    @BindView(R.id.Buttons)
    LinearLayout Buttons;
    ShardPrefrances mShardPrefrances;
    static String UTI = "";
    DatabaseReference databaseReference;
    private FirebaseFirestore mDatabase;
    ImageModel Image;
    static String Type = "";
    PostModel mPostModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);

        mShardPrefrances = new ShardPrefrances(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mDatabase = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Image = new ImageModel();
        mPostModel = new PostModel();
    }


    @OnClick({R.id.mEnd, R.id.mPublish, R.id.mUploadImage, R.id.mUploadVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mEnd:
                finish();
                break;
            case R.id.mPublish:
                BublishPost();
                break;
            case R.id.mUploadImage:
                SelectImage();
                break;
            case R.id.mUploadVideo:
                SelectVideo();
                break;
        }

    }

    private void BublishPost() {

        if (mCaption.getText().toString().isEmpty() && String.valueOf(filePath).isEmpty()) {
            Snackbar.make(findViewById(android.R.id.content), "Please Type Caption Or Choose Image", Snackbar.LENGTH_LONG).show();
        } else {
            if (Type.equals("Image")) {
                uploadUserImage();

            } else if (Type.equals("Video")) {
                UpLoadUserVideo();

            }

        }
    }

    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void SelectVideo() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            Glide.with(this).load(String.valueOf(filePath)).into(mPostImage);
            mPostImage.setVisibility(View.VISIBLE);
            Buttons.setVisibility(View.GONE);
            Type = "Image";

        } else if (requestCode == PICK_VIDEO_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            mPostVideo.setVideoURI(filePath);
            mPostVideo.setVisibility(View.VISIBLE);
            Buttons.setVisibility(View.GONE);
            Type = "Video";
        }
    }

    void UpLoadUserVideo() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Sharing...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference.child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME)).child("Posts Videos")
                    .child(UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();


                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downUri = task.getResult();
                                                UTI = downUri.toString();
                                                PostWithVideo(UTI);


                                            } else {
                                                Toast.makeText(AddPost.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    Log.e("Uriiiiiiiiiiiii", UTI);


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddPost.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }

    private void uploadUserImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(AddPost.this);
            progressDialog.setTitle("Sharing...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference.child(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_FNAME))).child("PostsImage")
                    .child(UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();


                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downUri = task.getResult();
                                                UTI = downUri.toString();
                                                PostWithImage(UTI);

                                            } else {
                                                Toast.makeText(AddPost.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    Log.e("Uriiiiiiiiiiiii", UTI);


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddPost.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }

    }

    void PostWithImage(String Uri) {
        String id = databaseReference.push().getKey();
        mPostModel = new PostModel(mCaption.getText().toString(), Uri, "0", id, mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID), "", mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_FNAME), mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_IMAGE));
        mDatabase.collection("Posts").document(id).set(mPostModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(findViewById(android.R.id.content), "You area Shared a Post ", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content), "Some Thing Went Wrong Try again ", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    void PostWithVideo(String Uri) {
        String id = databaseReference.push().getKey();
        mPostModel = new PostModel(mCaption.getText().toString(), "", "0", id, mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID), Uri, mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_FNAME), mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_IMAGE));
        mDatabase.collection("Posts").document(id).set(mPostModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(findViewById(android.R.id.content), "You area Shared a Post ", Snackbar.LENGTH_LONG).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(findViewById(android.R.id.content), "Some Thing Went Wrong Try again ", Snackbar.LENGTH_LONG).show();

            }
        });
    }
}
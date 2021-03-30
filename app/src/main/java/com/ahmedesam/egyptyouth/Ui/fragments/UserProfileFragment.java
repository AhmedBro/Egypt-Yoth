package com.ahmedesam.egyptyouth.Ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.ImageAdapter;
import com.ahmedesam.egyptyouth.Adapters.PostsAdapter;
import com.ahmedesam.egyptyouth.Adapters.VideoUserAdapter;
import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.Models.PostModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.ahmedesam.egyptyouth.Ui.Activities.splash;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends Fragment {
    View view;
    Unbinder mUnbinder;
    @BindView(R.id.UserImage)
    CircleImageView UserImage;

    @BindView(R.id.mUserName)
    TextView mUserName;
    @BindView(R.id.mUserAddress)
    TextView mUserAddress;
    @BindView(R.id.mUserSkills)
    TextView mUserSkills;

    @BindView(R.id.mFollowNumber)
    TextView mFollowNumber;
    @BindView(R.id.Images)
    RecyclerView Images;
    @BindView(R.id.Videos)
    RecyclerView Videos;
    DatabaseReference databaseReference;
    ArrayList<ImageModel> mImages;
    ArrayList<ImageModel> mVideos;
    VideoUserAdapter mVideoUserAdapter;
    ImageAdapter mImageAdapter;
    ShardPrefrances mShardPrefrances;
    @BindView(R.id.mUserAge)
    TextView mUserAge;

    @BindView(R.id.mEditInformation)
    Button mEditInformation;
    @BindView(R.id.mUploadImage)
    Button mUploadImage;
    @BindView(R.id.mUploadVideo)
    Button mUploadVideo;

    Activity mActivity;
    ArrayList<PostModel> mPosts;
    @BindView(R.id.LogOut)
    Button LogOut;
    @BindView(R.id.mUserId)
    TextView mUserId;
    @BindView(R.id.mPosts)
    RecyclerView mUserPosts;
    @BindView(R.id.mYear)
    TextView mYear;
    @BindView(R.id.mParent)
    ConstraintLayout mParent;
    //    @BindView(R.id.mFollowNumber)
//    TextView mFollowNumber;
    private FirebaseFirestore mDatabase;
    private Uri filePath;
    ImageModel Image;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private final int PICK_VIDEO_REQUEST = 222;
    private final int PICK_IMAGE_REQUEST2 = 2;
    FirebaseFirestore mDatabaseReference;
    PostsAdapter mPostsAdapter;
    userModel model;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    PostModel mPostModel;

    static String UTI = "";


    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseFirestore.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mImageAdapter = new ImageAdapter();
        mVideoUserAdapter = new VideoUserAdapter();
        mDatabaseReference = FirebaseFirestore.getInstance();

        Image = new ImageModel();
        mImages = new ArrayList<>();
        mVideos = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mUserPosts.setLayoutManager(manager);
        InstItems();

        SetData();

        LoadUserImages();

        LoadUserVideos();

        GetPosts();

        if (mShardPrefrances.IsDark()) {


            mParent.setBackground(getResources().getDrawable(R.color.white));
            mUploadImage.setBackground(getResources().getDrawable(R.drawable.edit_photo_button_light));
            mUploadImage.setTextColor(getResources().getColor(R.color.white));
            mEditInformation.setBackground(getResources().getDrawable(R.drawable.edit_photo_button_light));
            mEditInformation.setTextColor(getResources().getColor(R.color.white));
            mUploadVideo.setBackground(getResources().getDrawable(R.drawable.edit_photo_button_light));
            mUploadVideo.setTextColor(getResources().getColor(R.color.white));
            LogOut.setBackground(getResources().getDrawable(R.drawable.edit_photo_button_light));
            LogOut.setTextColor(getResources().getColor(R.color.white));
            mUserAddress.setTextColor(getResources().getColor(R.color.black));
            mUserAge.setTextColor(getResources().getColor(R.color.black));
            mUserName.setTextColor(getResources().getColor(R.color.black));
            mUserSkills.setTextColor(getResources().getColor(R.color.black));
            mYear.setTextColor(getResources().getColor(R.color.black));

        } else {


            mParent.setBackground(getResources().getDrawable(R.color.black));
            mUploadImage.setBackground(getResources().getDrawable(R.drawable.edit_photo_button));
            mUploadImage.setTextColor(getResources().getColor(R.color.white));
            mEditInformation.setBackground(getResources().getDrawable(R.drawable.edit_photo_button));
            mEditInformation.setTextColor(getResources().getColor(R.color.white));
            mUploadVideo.setBackground(getResources().getDrawable(R.drawable.edit_photo_button));
            mUploadVideo.setTextColor(getResources().getColor(R.color.white));
            LogOut.setBackground(getResources().getDrawable(R.drawable.edit_photo_button));
            LogOut.setTextColor(getResources().getColor(R.color.white));
            mUserAddress.setTextColor(getResources().getColor(R.color.white));
            mUserAge.setTextColor(getResources().getColor(R.color.white));
            mUserName.setTextColor(getResources().getColor(R.color.white));
            mUserSkills.setTextColor(getResources().getColor(R.color.white));
            mYear.setTextColor(getResources().getColor(R.color.white));

        }


        return view;
    }

    private void GetPosts() {
        mPosts = new ArrayList<>();
        mDatabaseReference.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mPosts.clear();
                for (QueryDocumentSnapshot document : value) {
                    mPostModel = new PostModel((String.valueOf(document.getData().get("mPost"))),
                            String.valueOf(document.getData().get("mImage")),
                            String.valueOf(document.getData().get("mLikeNumber")),
                            String.valueOf(document.getData().get("mPostID")),
                            String.valueOf(document.getData().get("mUserId")),
                            String.valueOf(document.getData().get("mVideo")),
                            String.valueOf(document.getData().get("mUserName")),
                            String.valueOf(document.getData().get("mUserImage")),
                            String.valueOf(document.getData().get("mCommentsNumber"))
                    );

                    if (String.valueOf(document.getData().get("mUserId")).equals(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID))) {
                        mPosts.add(mPostModel);
                    }

                }
                Collections.sort(mPosts, new Comparator<PostModel>() {
                    @Override
                    public int compare(PostModel o1, PostModel o2) {
                        return o2.getmLikeNumber().compareTo(o1.getmLikeNumber());
                    }
                });
                try {
                    mPostsAdapter = new PostsAdapter(mPosts, getContext());
                    mUserPosts.setAdapter(mPostsAdapter);
                } catch (Exception e) {

                }


            }

        });
    }

    private void LoadUserImages() {
        mDatabase.collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).collection("Images").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<String, String> map = new HashMap<>();

                mImages.clone();
                for (QueryDocumentSnapshot document : value) {
                    map = (Map<String, String>) document.getData().get("Image");

                    Image = new ImageModel(map.get("url"), map.get("id") , map.get("mUserId"));


                    mImages.add(Image);
                }
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                Images.setLayoutManager(manager);
                mImageAdapter = new ImageAdapter(mImages, getActivity());
                Images.setAdapter(mImageAdapter);
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    private void LoadUserVideos() {
        mDatabase.collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).collection("Videos").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<String, String> map = new HashMap<>();
                mVideos.clear();
                for (QueryDocumentSnapshot document : value) {
                    map = (Map<String, String>) document.getData().get("Video");

                    Image = new ImageModel(map.get("url"), map.get("id") , map.get("mUserId"));


                    mVideos.add(Image);
                }
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                Videos.setLayoutManager(manager);
                mVideoUserAdapter = new VideoUserAdapter(mVideos, getActivity(), "MyInfo");
                Videos.setAdapter(mVideoUserAdapter);

            }
        });

    }

    //----------------------------------------------------------------------------------------------
    private void InstItems() {
        mShardPrefrances = new ShardPrefrances(getActivity());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    //----------------------------------------------------------------------------------------------
    private void SetData() {
        model = new userModel();
        mDatabase.collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                try {
                    model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(), document.getData().get("mAge").toString(), document.getData().get("mDescription").toString(), document.getData().get("mPhone").toString(), document.getData().get("mLikeNumber").toString());

                } catch (Exception e) {
                    model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(), document.getData().get("mLikeNumber").toString());

                }
                mUserName.setText(model.getmName());
                mUserAge.setText(model.getmAge());
                mUserAddress.setText(model.getmAddress());
                mUserSkills.setText(model.getmDescription());
                Glide.with(Objects.requireNonNull(getContext())).load(model.getmImage()).into(UserImage);
                mFollowNumber.setText(model.getmLikeNumber());
                mUserId.setText(model.getmId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
    //----------------------------------------------------------------------------------------------

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

    //----------------------------------------------------------------------------------------------
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

    //----------------------------------------------------------------------------------------------
    private void SelectUserImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST2);
    }
    //----------------------------------------------------------------------------------------------

    // Override onActivityResult method
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            uploadImage();

        } else if (requestCode == PICK_IMAGE_REQUEST2
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            uploadUserImage();
        } else if (requestCode == PICK_VIDEO_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            filePath = data.getData();
            UpLoadUserVideo();
        }
    }

    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
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
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downUri = task.getResult();
                                                UTI = downUri.toString();

                                                UpDateImage();
                                                mShardPrefrances.EditImage(UTI);
                                                Glide.with(getActivity()).load(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_IMAGE)).into(UserImage);
                                            } else {
                                                Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
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
                                    .makeText(getActivity(),
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
    //----------------------------------------------------------------------------------------------

    private void uploadUserImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference.child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME)).child("Images")
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
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downUri = task.getResult();
                                                UTI = downUri.toString();

                                                UploadImage();
                                            } else {
                                                Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
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
                                    .makeText(getActivity(),
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
    //----------------------------------------------------------------------------------------------

    private void UploadImage() {
        HashMap<String, Object> Map = new HashMap<>();

        String id = databaseReference.push().getKey();
        Image = new ImageModel(UTI, id , mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
        Map.put("Image", Image);
        mDatabase.collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).collection("Images").document(id).set(Map);
    }

    //----------------------------------------------------------------------------------------------
    private void UploadVideo() {

        HashMap<String, Object> Map = new HashMap<>();

        String id = databaseReference.push().getKey();
        Image = new ImageModel(UTI, id , mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
        Map.put("Video", Image);
        mDatabase.collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).collection("Videos").document(id).set(Map);
    }

    //----------------------------------------------------------------------------------------------

    private void UpDateImage() {
        HashMap<String, Object> Map = new HashMap<>();
        Map.put("mImage", UTI);
        mDatabase.collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).update(Map);
    }
    //----------------------------------------------------------------------------------------------

    @OnClick({R.id.UserImage, R.id.mEditInformation, R.id.mUploadImage, R.id.mUploadVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.UserImage:

                SelectImage();
                break;
            case R.id.mEditInformation:
                EditInformationFragment mEditInformationFragment = new EditInformationFragment();
                mEditInformationFragment.show(getFragmentManager(), "");
                break;
            case R.id.mUploadImage:
                SelectUserImage();
                break;
            case R.id.mUploadVideo:
                SelectVideo();
                break;
        }
    }
    //----------------------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        SetData();
        mVideoUserAdapter.notifyDataSetChanged();
        mImageAdapter.notifyDataSetChanged();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = getActivity();
    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    //----------------------------------------------------------------------------------------------
    private void doAction() {
        if (mActivity == null) {
            return;
        }
    }

    //----------------------------------------------------------------------------------------------
    void UpLoadUserVideo() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            progressDialog.setCancelable(false);

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference.child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME)).child("Video")
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
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                Uri downUri = task.getResult();
                                                UTI = downUri.toString();

                                                UploadVideo();
                                            } else {
                                                Toast.makeText(getActivity(), "" + task.getException(), Toast.LENGTH_SHORT).show();
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
                                    .makeText(getActivity(),
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

    @OnClick(R.id.LogOut)
    public void onViewClicked() {
        mShardPrefrances.logoutUser();
        Intent mIntent = new Intent(getActivity(), splash.class);
        startActivity(mIntent);

    }

    @Override
    public void onPause() {

        super.onPause();
        mVideoUserAdapter = new VideoUserAdapter(false);
    }
}
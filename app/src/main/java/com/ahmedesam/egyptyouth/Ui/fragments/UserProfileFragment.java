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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.ImageAdapter;
import com.ahmedesam.egyptyouth.Adapters.VideoUserAdapter;
import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.Zoom.OnActivityStateChanged;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
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
    //    @BindView(R.id.mPostsNumber)
//    TextView mPostsNumber;
//    @BindView(R.id.mFollowersNumber)
//    TextView mFollowersNumber;
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
    @BindView(R.id.mEditPhoto)
    Button mEditPhoto;
    @BindView(R.id.mEditInformation)
    Button mEditInformation;
    @BindView(R.id.mUploadImage)
    Button mUploadImage;
    @BindView(R.id.mUploadVideo)
    Button mUploadVideo;

    Activity mActivity;

    @BindView(R.id.LogOut)
    Button LogOut;
    private FirebaseFirestore mDatabase;
    private Uri filePath;
    ImageModel Image;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private final int PICK_VIDEO_REQUEST = 222;
    private final int PICK_IMAGE_REQUEST2 = 2;

    userModel model;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    OnActivityStateChanged onActivityStateChanged = null;

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
        onActivityStateChanged  = mVideoUserAdapter.onActivityStateChanged;
        Image = new ImageModel();
        mImages = new ArrayList<>();
        mVideos = new ArrayList<>();
        InstItems();

        SetData();

        LoadUserImages();

        LoadUserVideos();
        return view;
    }

    private void LoadUserImages() {
        mDatabase.collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).collection("Images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, String> map = new HashMap<>();
                if (task.isSuccessful()) {
                    mImages.clone();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = (Map<String, String>) document.getData().get("Image");

                        Image = new ImageModel(map.get("url"), map.get("id"));


                        mImages.add(Image);
                    }
                    RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
                    Images.setLayoutManager(manager);
                    mImageAdapter = new ImageAdapter(mImages, getActivity());
                    Images.setAdapter(mImageAdapter);
                } else {
                    Log.e("Faild To Load Images", Objects.requireNonNull(task.getException().getMessage()));
                }
            }
        });


    }

    //----------------------------------------------------------------------------------------------
    private void LoadUserVideos() {
        mDatabase.collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).collection("Videos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, String> map = new HashMap<>();
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = (Map<String, String>) document.getData().get("Video");

                        Image = new ImageModel(map.get("url"), map.get("id"));


                        mVideos.add(Image);
                    }
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                    Videos.setLayoutManager(manager);
                    mVideoUserAdapter = new VideoUserAdapter(mVideos, getActivity());
                    Videos.setAdapter(mVideoUserAdapter);
                }
                else {
                    Log.e("Faild To Load Videos" , task.getException().getMessage());
                }
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
                    model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(), document.getData().get("mAge").toString(), document.getData().get("mDescription").toString(), document.getData().get("mAddress").toString(), document.getData().get("mLikeNumber").toString());

                } catch (Exception e) {
                    model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(), document.getData().get("mLikeNumber").toString());

                }
                mUserName.setText(model.getmName());
                mUserAge.setText(model.getmAge());
                mUserAddress.setText(model.getmAddress());
                mUserSkills.setText(model.getmDescription());
                Glide.with(getActivity()).load(model.getmImage()).into(UserImage);
                mFollowNumber.setText(model.getmLikeNumber());
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
        Image = new ImageModel(UTI, id);
        Map.put("Image", Image);
        mDatabase.collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).collection("Images").add(Map);
    }

    //----------------------------------------------------------------------------------------------
    private void UploadVideo() {

        HashMap<String, Object> Map = new HashMap<>();

        String id = databaseReference.push().getKey();
        Image = new ImageModel(UTI, id);
        Map.put("Video", Image);
        mDatabase.collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).collection("Videos").add(Map);
    }

    //----------------------------------------------------------------------------------------------

    private void UpDateImage() {
        HashMap<String, Object> Map = new HashMap<>();
        Map.put("mImage", UTI);
        mDatabase.collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).update(Map);
    }
    //----------------------------------------------------------------------------------------------

    @OnClick({R.id.mEditPhoto, R.id.mEditInformation, R.id.mUploadImage, R.id.mUploadVideo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mEditPhoto:

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
        if(onActivityStateChanged != null)
            onActivityStateChanged.onPaused();
        super.onPause();
    }
}
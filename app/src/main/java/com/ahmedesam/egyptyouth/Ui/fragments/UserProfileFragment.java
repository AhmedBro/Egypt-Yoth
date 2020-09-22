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
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
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
    @BindView(R.id.mFollowersNumber)
    TextView mFollowersNumber;
    @BindView(R.id.mFollowNumber)
    TextView mFollowNumber;
    @BindView(R.id.Images)
    RecyclerView Images;
    @BindView(R.id.Videos)
    RecyclerView Videos;

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
    private DatabaseReference mDatabase;
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
        mDatabase = FirebaseDatabase.getInstance().getReference();

        InstItems();

        SetData();

        LoadUserImages();

        LoadUserVideos();
        return view;
    }

    private void LoadUserImages() {
        mDatabase.child("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).child("Images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mImages = new ArrayList<>();
                Image = new ImageModel();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Image = data.getValue(ImageModel.class);
                    mImages.add(Image);

                }
                Log.e("ImageArray", mImages + "");
                RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
                Images.setLayoutManager(manager);
                mImageAdapter = new ImageAdapter(mImages, getActivity());
                Images.setAdapter(mImageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------
    private void LoadUserVideos() {
        mDatabase.child("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).child("Videos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mVideos = new ArrayList<>();
                Image = new ImageModel();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Image = data.getValue(ImageModel.class);
                    mVideos.add(Image);

                }
                Log.e("Videos", mVideos + "");
                RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity() , RecyclerView.VERTICAL , false);
                Videos.setLayoutManager(manager);
                mVideoUserAdapter = new VideoUserAdapter(mVideos, getActivity());
                Videos.setAdapter(mVideoUserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        mDatabase.child("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model = snapshot.getValue(userModel.class);
                mUserName.setText(model.getmName());
                mUserAge.setText(model.getmDate());
                mUserAddress.setText(model.getmAddress());
                mUserSkills.setText(model.getmDescription());
                try {
                    Glide.with(mActivity).load(model.getmImage()).into(UserImage);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        String id = mDatabase.push().getKey();
        Image = new ImageModel(UTI, id);
        Map.put(id, Image);
        mDatabase.child("Users").child(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).child("Images").updateChildren(Map);
    }

    //----------------------------------------------------------------------------------------------
    private void UploadVideo() {
        HashMap<String, Object> Map = new HashMap<>();
        String id = mDatabase.push().getKey();
        Image = new ImageModel(UTI, id);
        Map.put(id, Image);
        mDatabase.child("Users").child(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).child("Videos").updateChildren(Map);
    }

    //----------------------------------------------------------------------------------------------

    private void UpDateImage() {
        HashMap<String, Object> Map = new HashMap<>();
        Map.put("mImage", UTI);
        mDatabase.child("Users").child(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).updateChildren(Map);
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
        LoadUserImages();
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
}
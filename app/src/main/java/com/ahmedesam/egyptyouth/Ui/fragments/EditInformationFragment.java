package com.ahmedesam.egyptyouth.Ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class EditInformationFragment extends BottomSheetDialogFragment {
    View view;
    @BindView(R.id.mUserName)
    EditText mUserName;
    @BindView(R.id.mUserDescription)
    EditText mUserDescription;
    @BindView(R.id.mUserAge)
    EditText mUserAge;
    @BindView(R.id.mSave)
    Button mSave;
    @BindView(R.id.mUserAddress)
    EditText mUserAddress;
    @BindView(R.id.UserImage)
    CircleImageView UserImage;
    private Uri filePath;
    private FirebaseFirestore mDatabase;
    ShardPrefrances mShardPrefrances;
    Unbinder mUnbinder;
    Activity mActivity;
    static String Id;
    StorageReference storageReference;
    FirebaseStorage storage;
    userModel model;
    static String UTI = "";
    private final int PICK_IMAGE_REQUEST = 22;
    public EditInformationFragment(String id) {
        Id = id;
    }

    public EditInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.edit_profile, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        storageReference = storage.getReference();
        mShardPrefrances = new ShardPrefrances(getContext());
        mActivity = getActivity();
        SetData();
        Validate();
        UserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });
        return view;
    }

    private boolean Validate() {
        if (mUserAge.getText().toString().isEmpty()) {
            Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.enter_your_age, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mUserDescription.getText().toString().isEmpty()) {
            Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.Enter_Your_Description, Snackbar.LENGTH_LONG).show();

            return false;
        } else if (mUserName.getText().toString().isEmpty()) {
            Snackbar.make(mActivity.findViewById(android.R.id.content), R.string.enter_your_name, Snackbar.LENGTH_LONG).show();

            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.mSave)
    public void onViewClicked() {
        if (Validate()) {
            HashMap<String, Object> Map = new HashMap<>();
            Map.put("mName", mUserName.getText().toString());
            Map.put("mAge", mUserAge.getText().toString());
            Map.put("mDescription", mUserDescription.getText().toString());
            Map.put("mAddress", mUserAddress.getText().toString());
            mShardPrefrances.EditAge(mUserAge.getText().toString());
            mShardPrefrances.EditAddress(mUserAddress.getText().toString());
            mShardPrefrances.EditDescription(mUserDescription.getText().toString());
            mShardPrefrances.EditName(mUserName.getText().toString());
            mDatabase.collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).update(Map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(mActivity, "Your Profile Is Updated", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mActivity, "Error Please Try Again Later", Toast.LENGTH_SHORT).show();
                            Log.e("Update Profile Error", e.getMessage());
                        }
                    });
            Log.e("ID", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));

            dismiss();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentManager fragmentManager4 = getActivity().getSupportFragmentManager();
        fragmentManager4.beginTransaction().replace(R.id.replace, new UserProfileFragment()).commit();
    }

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
                mUserDescription.setText(model.getmDescription());
                Glide.with(getActivity()).load(model.getmImage()).into(UserImage);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


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

    private void UpDateImage() {
        HashMap<String, Object> Map = new HashMap<>();
        Map.put("mImage", UTI);
        mDatabase.collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).update(Map);
    }
}
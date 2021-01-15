package com.ahmedesam.egyptyouth.Ui.Activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.MediaAdapter;
import com.ahmedesam.egyptyouth.Adapters.chatAdapter;
import com.ahmedesam.egyptyouth.Models.Contact;
import com.ahmedesam.egyptyouth.Models.ModelChat;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {
    private static final int RESULT_LOAD_IMG = 1;
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    TextView mName, mStatus;
    ImageButton mImageButton;
    ImageView mImageView;
    Intent mIntent;
    FirebaseAuth mFirebaseAuth;
    public static String mMyId, mHisId;
    EditText mMassage;
    FirebaseFirestore mFirebaseDatabase;
    FirebaseFirestore mDatabaseReference;
    @BindView(R.id.mUserImage)
    CircleImageView mUserImage;
    private RecyclerView.Adapter mMediaAdapter;
    int PICK_IMAGE_INTENT = 1;
    ArrayList<String> mediaUriList = new ArrayList<>();
    static String UTI = "";
    //-to Check Seen
    ValueEventListener SeenListener;
    DatabaseReference mUserRefForSeen;

    ArrayList<ModelChat> mChat;
    chatAdapter mChatAdapter;
    MediaPlayer mediaPlayer;
    static boolean send = false;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "AIzaSyAa33zPueQk5T6uzT4Q8O0wvLb8nQXexd8";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    public ChatActivity() {

    }

    static boolean ChatExist = false;

    boolean notify = false;
    @BindView(R.id.mSendImage)
    ImageButton mSendImage;
    @BindView(R.id.MediaRec)
    RecyclerView mMedia;
    StorageReference storageReference;
    static ArrayList<Uri> mMediaPath;
    static ArrayList<String> mMediaDownload;
    static String mMessageId;
    ShardPrefrances mShardPrefrances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mIntent = getIntent();
        mChat = new ArrayList<>();
        mMediaPath = new ArrayList<>();
        mMediaDownload = new ArrayList<>();
        storageReference = FirebaseStorage.getInstance().getReference();
        mShardPrefrances = new ShardPrefrances(this);
        mMyId = mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID);
        mHisId = mIntent.getStringExtra("Id");


        Instviews();
        initializeMedia();
    }

    private void Instviews() {


        mToolbar = findViewById(R.id.mToolBar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("");
        //----------------------------------------------------------------------------------------------
        mRecyclerView = findViewById(R.id.ChatRec);
        mName = findViewById(R.id.mName);


        mStatus = findViewById(R.id.mStatus);
        mImageView = findViewById(R.id.mUserImage);
        mImageButton = findViewById(R.id.mSend);
        mMassage = findViewById(R.id.mEditForMessage);

        mName.setText(mIntent.getStringExtra("Name"));
        Glide.with(this).load(mIntent.getStringExtra("Image")).into(mUserImage);
        //---------------------------------------------------------------------------------------------
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseFirestore.getInstance();


        //----------------------------------------------------------------------------------------------

        mFirebaseDatabase.collection("Users").document(mHisId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String Name = "" + value.getData().get("mName");

                String mStatus1 = "" + value.getData().get("mStatus");

                String mTyping = "" + value.getData().get("mTyping");

                if (mTyping.equalsIgnoreCase(mMyId)) {
                    mStatus.setText(R.string.typing);
                } else {
                    if (mStatus1.equalsIgnoreCase("Online")) {
                        mStatus.setText(mStatus1);
                    } else {

                        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                        try {
                            cal.setTimeInMillis(Long.parseLong(mStatus1));
                            String Date = DateFormat.format("hh:mm aa", cal).toString();
                            mStatus.setText("Last Seen at: " + Date);
                        } catch (Exception e) {

                        }

                    }

                }
            }
        });
        //----------------------------------------------------------------------------------------------
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                notify = true;
                String aMessage = mMassage.getText().toString().trim();

                if (TextUtils.isEmpty(aMessage) && mMediaPath.size() == 0) {
                    Toast.makeText(ChatActivity.this, "Cannot Send Empty Message", Toast.LENGTH_SHORT).show();

                } else {

                    for (int i = 0; i < mMediaPath.size(); i++) {
                        uploadImage(mMediaPath.get(i));

                    }
                    sendMessage(aMessage);
                    MakeChat();

                    mMassage.setText("");


                }
            }


        });


        //-Rec For Messamge


        //-----------------------------------------
        mMassage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    CheckTyping("");
                } else {
                    CheckTyping(mHisId);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        ReadMessage();
        SeenMessage();


        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom < oldBottom) {
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mRecyclerView.smoothScrollToPosition(
                                        mRecyclerView.getAdapter().getItemCount() - 1);
                            } catch (Exception w) {

                            }

                        }
                    }, 100);
                }
            }
        });
    }

    private void SeenMessage() {
        mUserRefForSeen = FirebaseDatabase.getInstance().getReference().child("Users").child(mMyId).child("Chats").child(mHisId).child("Messages");
        SeenListener = mUserRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelChat modelChat = ds.getValue(ModelChat.class);
                    if (modelChat.getmSender().equals(mHisId) && modelChat.getmReceiver().equals(mMyId)) {
                        HashMap<String, Object> IsSeen = new HashMap<>();
                        IsSeen.put("mIsSeen", true);
                        ds.getRef().updateChildren(IsSeen);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mUserRefForSeen = FirebaseDatabase.getInstance().getReference().child("Users").child(mHisId).child("Chats").child(mMyId).child("Messages");
        SeenListener = mUserRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelChat modelChat = ds.getValue(ModelChat.class);
                    if (modelChat.getmSender().equals(mHisId) && modelChat.getmReceiver().equals(mMyId)) {
                        HashMap<String, Object> IsSeen = new HashMap<>();
                        IsSeen.put("mIsSeen", true);
                        ds.getRef().updateChildren(IsSeen);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ReadMessage() {

        CollectionReference db = FirebaseFirestore.getInstance().collection("Users").document(mMyId).collection("Chats").document(mHisId).collection("Messages");
        db.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mChat.clear();
                for (QueryDocumentSnapshot doc : value) {
                    ModelChat modelChat = new ModelChat(String.valueOf(doc.get("mMessage")), String.valueOf(doc.get("mSender")), String.valueOf(doc.get("mReceiver")), String.valueOf(doc.get("mTime")), String.valueOf(doc.get("mMessage")), Boolean.parseBoolean(String.valueOf(doc.get("mIsSeen"))));

                    if (modelChat.getmSender().equals(mMyId) && modelChat.getmReceiver().equals(mHisId) ||
                            modelChat.getmSender().equals(mHisId) && modelChat.getmReceiver().equals(mMyId)) {
                        mChat.add(modelChat);

                    }
                }
                Collections.sort(mChat, new Comparator<ModelChat>() {
                    @Override
                    public int compare(ModelChat o1, ModelChat o2) {
                        return o1.getmTime().compareTo(o2.getmTime());
                    }
                });

                mChatAdapter = new chatAdapter(ChatActivity.this, mChat, mIntent.getStringExtra("Image"));
                mRecyclerView.setAdapter(mChatAdapter);
                RecyclerView.LayoutManager manager = new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false);
                manager.scrollToPosition(mChat.size() - 1);
                mRecyclerView.setLayoutManager(manager);
                mChatAdapter.notifyDataSetChanged();

            }
        });
//        db.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mChat.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ModelChat modelChat = ds.getValue(ModelChat.class);
//
//                    if (modelChat.getmSender().equals(mMyId) && modelChat.getmReceiver().equals(mHisId) ||
//                            modelChat.getmSender().equals(mHisId) && modelChat.getmReceiver().equals(mMyId)) {
//                        mChat.add(modelChat);
//
//                    }
//
//                    Collections.sort(mChat, new Comparator<ModelChat>() {
//                        @Override
//                        public int compare(ModelChat o1, ModelChat o2) {
//                            return o1.getmTime().compareTo(o2.getmTime());
//                        }
//                    });
//
//                    mChatAdapter = new chatAdapter(ChatActivity.this, mChat, mIntent.getStringExtra("Image"));
//                    mRecyclerView.setAdapter(mChatAdapter);
//                    RecyclerView.LayoutManager manager = new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false);
//                    manager.scrollToPosition(mChat.size() - 1);
//                    mRecyclerView.setLayoutManager(manager);
//                    mChatAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    private void sendMessage(final String aMessage) {
        mMessageId = FirebaseDatabase.getInstance().getReference().push().getKey();
        String aTime = String.valueOf(System.currentTimeMillis());
        FirebaseFirestore mDatabaseReference = FirebaseFirestore.getInstance();
        HashMap<String, Object> map = new HashMap<>();
        map.put("mSender", mMyId);
        map.put("mReceiver", mHisId);
        map.put("mMessage", aMessage);
        map.put("mTime", aTime);
        map.put("mIsSeen", false);
        map.put("mId", mMessageId);

        mDatabaseReference.collection("Users").document(mMyId).collection("Chats").document(mHisId).collection("Messages").document(mMessageId).set(map);
        mDatabaseReference.collection("Users").document(mHisId).collection("Chats").document(mMyId).collection("Messages").document(mMessageId).set(map);


        HashMap<String, Object> map2 = new HashMap<>();

        map2.put("mTime", aTime);
        map2.put("mLastMessage", aMessage);
        mDatabaseReference.collection("Users").document(mHisId).collection("Chats").document(mMyId).update(map2);
        mDatabaseReference.collection("Users").document(mMyId).collection("Chats").document(mHisId).update(map2);

        mMediaAdapter.notifyDataSetChanged();
        mediaPlayer = MediaPlayer.create(this, R.raw.send);
        mediaPlayer.start();
    }

    private void Checkstatus(String mStatus) {

        DocumentReference mDatabaseReference = FirebaseFirestore.getInstance().collection("Users").document(mMyId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("mStatus", mStatus);
        mDatabaseReference.update(map);


    }

    private void CheckTyping(String mTyping) {

        DocumentReference mDatabaseReference = FirebaseFirestore.getInstance().collection("Users").document(mMyId);

        HashMap<String, Object> map = new HashMap<>();
        map.put("mTyping", mTyping);
        mDatabaseReference.update(map);

    }


    @Override
    protected void onStart() {
        super.onStart();
        Checkstatus("Online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        String mTime = String.valueOf(System.currentTimeMillis());

        Checkstatus(mTime);
        mUserRefForSeen.removeEventListener(SeenListener);

    }

    @Override
    protected void onResume() {

        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        String mTime = String.valueOf(System.currentTimeMillis());

        Checkstatus(mTime);
    }

    void MakeChat() {
        CollectionReference UserDb = FirebaseFirestore.getInstance().collection("Users").document(mMyId).collection("Chats");
        Query query = UserDb.whereEqualTo("mId", mHisId);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot d : value) {
                    if (d.getData().get("mId").equals(mHisId)) {
                        ChatExist = true;
                    }
                }
            }
        });

        if (!ChatExist) {
            HashMap<String, Object> map2 = new HashMap();
            HashMap<String, Object> map3 = new HashMap();
            HashMap<String, Object> map = new HashMap();
            map.put("mImage", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_IMAGE));
            map.put("mName", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME));
            map.put("mId", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
            map.put("mTyping", "");
            map.put("mStatus", "Offline");
            map.put("mTime", "");
            map.put("mLastMessage", "");
            FirebaseFirestore.getInstance().collection("Users").document(mMyId).collection("Chats").document(mHisId).update("mName", mIntent.getStringExtra("Name"));

            map2.put("mImage", getIntent().getStringExtra("Image"));
            map2.put("mId", mHisId);
            map2.put("mName", getIntent().getStringExtra("Name"));
            map2.put("mTime", "");
            map2.put("mLastMessage", "");
            FirebaseFirestore.getInstance().collection("Users").document(mMyId).collection("Chats").document(mHisId).set(map2);
            FirebaseFirestore.getInstance().collection("Users").document(mHisId).collection("Chats").document(mMyId).set(map);


        }
    }


    @OnClick(R.id.mSendImage)
    public void onViewClicked() {
        openGallery();
    }


    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture(s)"), PICK_IMAGE_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_INTENT) {
                if (data.getClipData() == null) {
                    mediaUriList.add(data.getData().toString());
                    mMediaPath.add(data.getData());

                } else {
                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                        mediaUriList.add(data.getClipData().getItemAt(i).getUri().toString());
                        mMediaPath.add(data.getClipData().getItemAt(i).getUri());
                    }
                }

                mMediaAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initializeMedia() {
        mediaUriList = new ArrayList<>();

        mMedia.setNestedScrollingEnabled(false);
        mMedia.setHasFixedSize(false);
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager mMediaLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.HORIZONTAL, false);
        mMedia.setLayoutManager(mMediaLayoutManager);
        mMediaAdapter = new MediaAdapter(getApplicationContext(), mediaUriList);
        mMedia.setAdapter(mMediaAdapter);
    }


    private void uploadImage(Uri filePath) {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref
                    = storageReference
                    .child(
                            "Media/"
                                    + UUID.randomUUID().toString());

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
                                            .makeText(ChatActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {

                                                UpToMedia(task.getResult().toString());


                                                Toast.makeText(ChatActivity.this, UTI, Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(ChatActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
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
                                    .makeText(ChatActivity.this,
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


    void UpToMedia(String Url) {
        Log.e("MediaaaaaToooo", Url);
        Log.e("MediaaaaaToooo", mMessageId);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mMyId).child("Chats").child(mHisId).child("Messages").child(mMessageId).child("mMedia").push().setValue(Url);
        FirebaseDatabase.getInstance().getReference().child("Users").child(mHisId).child("Chats").child(mMyId).child("Messages").child(mMessageId).child("mMedia").push().setValue(Url);
    }


    void sendNoti() {
        try {
            String jsonResponse;

            URL url = new URL("https://onesignal.com/api/v1/notifications");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);

            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", "Basic NGEwMGZmMjItY2NkNy0xMWUzLTk5ZDUtMDAwYzI5NDBlNjJj");
            con.setRequestMethod("POST");

            String strJsonBody = "{"
                    + "\"app_id\": \"66b4a76c-6fff-433d-b831-e29c9fc8b0ea\","
                    + "\"filters\": [{\"field\": \"tag\", \"key\": \"Id\", \"relation\": \">\", \"value\": \"" + "ahmed.esamffff@gmail.com" + "\"},{\"operator\": \"OR\"},{\"field\": \"amount_spent\", \"relation\": \">\",\"value\": \"0\"}],"
                    + "\"data\": {\"foo\": \"bar\"},"
                    + "\"contents\": {\"en\": \"English Message\"}"
                    + "}";


            System.out.println("strJsonBody:\n" + strJsonBody);

            byte[] sendBytes = strJsonBody.getBytes("UTF-8");
            con.setFixedLengthStreamingMode(sendBytes.length);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(sendBytes);

            int httpResponse = con.getResponseCode();
            System.out.println("httpResponse: " + httpResponse);

            if (httpResponse >= HttpURLConnection.HTTP_OK
                    && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            } else {
                Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                scanner.close();
            }
            System.out.println("jsonResponse:\n" + jsonResponse);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}

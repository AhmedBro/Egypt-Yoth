package com.ahmedesam.egyptyouth.Ui.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogIn extends AppCompatActivity {
    private int RC_SIGN_IN = 1000;

    @BindView(R.id.fb_login_button)
    com.facebook.login.widget.LoginButton fbLoginButton;
    @BindView(R.id.facebook_btn)
    AppCompatButton facebookBtn;
    @BindView(R.id.google_sign_in_button)
    AppCompatButton googleSignInButton;
    private CallbackManager callbackManager;
    userModel muUserModel;
    FirebaseFirestore database;
    DatabaseReference myRef;
    ShardPrefrances mShardPrefrances;
    GoogleSignInOptions gso;

    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        mShardPrefrances = new ShardPrefrances(this);
        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();
        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLoginButton.performClick();
                fbLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
                Log.e("hereeeeeeee", "out");

                fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        loadFacebookData(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.e("hereeeeeeee", "cancel: ");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.e("hereeeeeeee", exception.getMessage());
                    }
                });
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        showHashKey();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                handleSignInResult(task , account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }

        }

    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if (currentAccessToken != null) {
                loadFacebookData(currentAccessToken);
            } else {
                Toast.makeText(LogIn.this, "user logged out", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void loadFacebookData(AccessToken newAccessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(newAccessToken.getToken());
        mAuth.signInWithCredential(credential);


        GraphRequest graphRequest = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {

                    String firstName = object.getString("first_name");
                    String lastName = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    String birthday = object.optString("user_birthday");
                    Log.e("dd", "https://graph.facebook.com/" + id + "/picture?type=large");

                    FirebaseUser user = mAuth.getCurrentUser();


                    mShardPrefrances.createLoginSession(false, id, firstName + " " + lastName, email, "https://graph.facebook.com/" + id + "/picture?type=large", birthday);
                    muUserModel = new userModel(firstName + lastName, id, email, "https://graph.facebook.com/" + id + "/picture?type=large", birthday);
                    creatNode(muUserModel);
                    OneSignal.sendTag("User_Id" , mAuth.getCurrentUser().getEmail());
                    Intent mIntent = new Intent(LogIn.this, InsertYourInfo.class);
                    startActivity(mIntent);
                    finish();
                } catch (JSONException e) {
                    Log.e("Ereor Face", e.getMessage());
                }
            }
        });

        Bundle bundle1 = new Bundle();
        bundle1.putString("fields", "first_name,last_name,email,id");
        graphRequest.setParameters(bundle1);
        graphRequest.executeAsync();
    }

    public void showHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.ahmedesam.egyptyouth",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
                //  Toast.makeText(getApplicationContext(),sign,     Toast.LENGTH_LONG).show();
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    void creatNode(userModel muUserModel) {
        database = FirebaseFirestore.getInstance();
        HashMap<String, Object> map = new HashMap<>();
        map.put("mId", muUserModel.getmId().toUpperCase());
        map.put("mName", muUserModel.getmName().toUpperCase());
        map.put("mMail", muUserModel.getmMail().toUpperCase());
        map.put("mLikeNumber", "0");
        map.put("mImage", muUserModel.getmImage());
        database.collection("Users").document(muUserModel.getmId()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("LogInSuccess", "Success");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("LogInFailed", e.getMessage());
            }
        });
    }

    private void checkLogout() {
        LoginManager.getInstance().logOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask , String Token) {


        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(Token, null);
            mAuth.signInWithCredential(credential);

            mShardPrefrances.createLoginSession(false, completedTask.getResult().getId(), completedTask.getResult().getDisplayName(), completedTask.getResult().getEmail(), String.valueOf(completedTask.getResult().getPhotoUrl()), "");
            muUserModel = new userModel(completedTask.getResult().getDisplayName(), completedTask.getResult().getId(), completedTask.getResult().getEmail(), String.valueOf(completedTask.getResult().getPhotoUrl()), "");
            creatNode(muUserModel);
            OneSignal.sendTag("User_Id" , mAuth.getCurrentUser().getEmail());
            Intent mIntent = new Intent(LogIn.this, InsertYourInfo.class);
            startActivity(mIntent);
            finish();
        } catch (Exception e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("Google Handle", e.getMessage());

        }
    }

    private void signIn() {
        Log.e("Goolge sign in", "sssssss");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onStart() {
        super.onStart();
//        FirebaseUser user = auth.getCurrentUser();
        //      updateUI(user);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        checkLogout();


    }


}

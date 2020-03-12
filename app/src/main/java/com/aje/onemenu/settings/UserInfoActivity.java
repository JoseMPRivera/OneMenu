package com.aje.onemenu.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aje.onemenu.R;
import com.aje.onemenu.activities.FoodPreference;
import com.aje.onemenu.activities.MainActivity;
import com.aje.onemenu.activities.ProfileActivity;
import com.aje.onemenu.activities.RestaurantsMenu;
import com.aje.onemenu.classes.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class UserInfoActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {

    private ImageView profile_image;
    private TextView name;
    private TextView email;
    private TextView id;
    private Button signOutButton;
    private FirebaseFirestore db;
    private User currentUser;
    private DocumentReference usersInfo;
    private GoogleSignInAccount account;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        id = findViewById(R.id.id);
        signOutButton = findViewById(R.id.signOutButton);

        signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess())
                            gotoMainActivity();
                        else
                            Toast.makeText(getApplicationContext(), "Log Out Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void gotoMainActivity() {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        fileList();

    }

    private void setUserInfo(GoogleSignInAccount account){

        currentUser = new User();

        currentUser.setEmail(account.getEmail());
        currentUser.setId(account.getId());
        currentUser.setName(account.getDisplayName());

        name.setText(account.getDisplayName());
        email.setText(account.getEmail());
        id.setText(account.getId());
        Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profile_image);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()) {

          account = result.getSignInAccount();

          db = FirebaseFirestore.getInstance();
          usersInfo = db.collection("users").document(account.getId());
          usersInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("UserInfoActivity", "Document exists!");
                        } else {
                            setUserInfo(account);
                            Log.d("UserInfoActivity", "Document does not exist!");

                            usersInfo.set(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    Log.d("successful", "We pushed to db");
                                    if(task.isSuccessful()){
                                        String test = "Account created";
                                        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "Error, fail to connect to database", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    } else {
                        Log.d("UserInfoActivity", "Failed with: ", task.getException());
                    }
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Error sign in with Google account", Toast.LENGTH_SHORT).show();
            gotoMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opt = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if(opt.isDone()) {
            GoogleSignInResult result = opt.get();
            handleSignInResult(result);
        }

        else {
            opt.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);

                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

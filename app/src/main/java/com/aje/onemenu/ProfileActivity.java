package com.aje.onemenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private ImageView profile_image;
    private TextView name;
    private TextView email;
    private TextView id;
    private Button signOutButton;
    private Button EdgarButton;
    private Button AlexButton;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

//    https://www.youtube.com/watch?v=uPg1ydmnzpk
//    17:30

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        id = findViewById(R.id.id);
        signOutButton = findViewById(R.id.signOutButton);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

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
                            Toast.makeText(ProfileActivity.this, "Log Out Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        //Restaurants_menu Button
        EdgarButton = findViewById(R.id.edgarButton);
        EdgarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, RestaurantsMenu.class);
                startActivity(intent);
            }
        });

        AlexButton = findViewById(R.id.alexButton);
        AlexButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FoodPreference.class);
                startActivity(intent);
            }
        });
    }

    private void gotoMainActivity() {

        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        fileList();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            name.setText(account.getDisplayName());
            email.setText(account.getEmail());
            id.setText(account.getId());

            Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profile_image);
        }
        else {
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
}
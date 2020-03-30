package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aje.onemenu.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class FoodDescription extends AppCompatActivity {

    private ImageView foodPhoto;
    private String foodName;
    private String foodDescription;
    private String foodIngredients;
    private float foodRating;
    private RelativeLayout reviewsLayout;
    private RelativeLayout addReviewLayout;
    private Button addPhotoButton;
    private Button submitReviewButton;
    private ImageView imageView;
    private static int RESULT_LOAD_IMAGE = 1;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private String userID;
    private String restaurantID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);

        userID = getIntent().getStringExtra("userId");
        restaurantID = getIntent().getStringExtra("restaurantId");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reviewsLayout = findViewById(R.id.reviews_layout);
        addReviewLayout = findViewById(R.id.add_review_layout);
        addPhotoButton = findViewById(R.id.choose_image_button);
        submitReviewButton = findViewById(R.id.submit_review_button);
        imageView = findViewById(R.id.food_image_view);

        addReviewLayout.setVisibility(View.GONE);
        reviewsLayout.setVisibility(View.VISIBLE);

        RatingBar rate = findViewById(R.id.ratingBar);
        rate.setOnRatingBarChangeListener( new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                foodRating = v;
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoodDescription.this, "" + foodRating, Toast.LENGTH_SHORT).show();

                uploadImage();
            }
        });

    }

    private void setReviewLayoutInvisible() {
        if (reviewsLayout.getVisibility() == View.VISIBLE && addReviewLayout.getVisibility() == View.GONE) {
            reviewsLayout.setVisibility(View.GONE);
            addReviewLayout.setVisibility(View.VISIBLE);
        }
    }
    private void setReviewLayoutVisible() {
        if (reviewsLayout.getVisibility() == View.GONE && addReviewLayout.getVisibility() == View.VISIBLE) {
            reviewsLayout.setVisibility(View.VISIBLE);
            addReviewLayout.setVisibility(View.GONE);
        }
    }

//            Picasso.get().load(selectedImage).placeholder(R.mipmap.ic_launcher).into(imageView);
//            Picasso.get().load(selectedImage).into(imageView);

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();

            imageView.setImageURI(selectedImageUri); //Picasso.get().load(selectedImage).into(imageView);
        }
    }

    public void uploadImage(){

        if(selectedImageUri != null && !selectedImageUri.equals(Uri.EMPTY)){

            Log.d("PATH test", selectedImageUri.getPath());
            Log.d("LASTPATH test", selectedImageUri.getLastPathSegment());

            if(userID.equals(null)) userID = "";
            if(restaurantID.equals(null)) restaurantID = "";

            storageReference = storageReference.child("reviews/" + restaurantID + "/" + userID + ".jpg");
            UploadTask uploadTask = storageReference.putFile(selectedImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("IMAGE PATH", taskSnapshot.getMetadata().getPath());
                }
            });
        } else {

            Log.d("No URI", "Image not selected");
        }
    }
}

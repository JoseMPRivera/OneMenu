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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.aje.onemenu.R;
import com.aje.onemenu.reviews.CustomAdapterReview;
import com.aje.onemenu.reviews.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodDescription extends AppCompatActivity {

    private EditText textReview;
    private float foodRating;
    private RelativeLayout reviewsLayout;
    private RelativeLayout addReviewLayout;
    private Button addPhotoButton;
    private Button submitReviewButton;
    private ImageView imageView;
    private static int RESULT_LOAD_IMAGE = 1;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private String userID;
    private String restaurantID;
    private String imageReviewPath;
    private ArrayList<String> reviewArray;
    private ArrayList<String> urlArray;
    private HashMap<String, StorageReference> hashMapImages;
    private ListView listViewReview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);

        Log.d("FOODDESCRIPTION", "On create loaded");

        db = FirebaseFirestore.getInstance();
        userID = getIntent().getStringExtra("userId");
        restaurantID = getIntent().getStringExtra("restaurantId");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reviewsLayout = findViewById(R.id.reviews_layout);
        addReviewLayout = findViewById(R.id.add_review_layout);
        addPhotoButton = findViewById(R.id.choose_image_button);
        submitReviewButton = findViewById(R.id.submit_review_button);
        imageView = findViewById(R.id.food_image_view);
        textReview = findViewById(R.id.get_text_review);
        listViewReview = findViewById(R.id.list_view_reviews);

        reviewArray = new ArrayList<>();
        urlArray = new ArrayList<>();


        Log.d("FOODDESCRIPTION", "variables asigned");

//        setReviewLayoutVisible();

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
                setReviewLayoutInvisible();
            }
        });

        Button addReviewB = findViewById(R.id.add_review_button);

        addReviewB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReviewLayoutVisible();
            }
        });

    }

    private void updateReviewList(){

//        String rest[] = restaurantID.split("/");
//        String restaurantName = rest[0];
//        String mealName = rest[rest.length - 1];

//        db.collection("reviews").document(restaurantName).collection(mealName).get()
        final ArrayList<StorageReference> storageList = new ArrayList<>();

        storageReference = storageReference.child("reviews/" + restaurantID);

        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {

                    storageList.add(item);
                    hashMapImages.put(item.getName(), item);
                }
            }
        });

        String path[] = restaurantID.split("/");

        final ArrayList<Review> list = new ArrayList<>();

        db.collection("reviews").document(path[0]).collection(path[1]).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot d: queryDocumentSnapshots){
                            list.add(d.toObject(Review.class));
                        }
                    }
                });

        updateUI(list, storageList);


    }

    private void updateUI(ArrayList<Review> list, ArrayList<StorageReference> sList){

        listViewReview.setAdapter(new CustomAdapterReview(FoodDescription.this, list, sList));
    }


    private void setReviewLayoutInvisible() {
            reviewsLayout.setVisibility(View.INVISIBLE);
            addReviewLayout.setVisibility(View.VISIBLE);
    }
    private void setReviewLayoutVisible() {
            reviewsLayout.setVisibility(View.VISIBLE);
            addReviewLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();

            imageView.setImageURI(selectedImageUri); //Picasso.get().load(selectedImage).into(imageView);
        }
    }

    private void uploadImage(){

        final Review reviewInfo = new Review();
        reviewInfo.setRating(foodRating);
        reviewInfo.setReview(textReview.getText().toString());

        if(selectedImageUri != null && !selectedImageUri.equals(Uri.EMPTY)){

            Log.d("PATH test", selectedImageUri.getPath());
            Log.d("LASTPATH test", selectedImageUri.getLastPathSegment());

            if(userID.equals(null) || restaurantID.equals(null)) {
                userID = "error";
                restaurantID = "error";
            }

            String reviewPathFINAL= "reviews/" + restaurantID;

            storageReference = storageReference.child( reviewPathFINAL+ "/" + userID + ".jpg");
            UploadTask uploadTask = storageReference.putFile(selectedImageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageReviewPath = taskSnapshot.getMetadata().getPath();
                    reviewInfo.setUrl(imageReviewPath);
                    Log.d("IMAGE PATH", imageReviewPath);

                    String restID[] = restaurantID.split("/");

                    db.collection("reviews").document(restID[0]).collection(restID[1]).document(userID)
                            .set(reviewInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("REVIEW", "We pushed the review to db");
                            if(task.isSuccessful()){
                                String test = "Review Added";
                                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("REVIEW", "There was an error with the completition of the review");
//                    Toast.makeText(getApplicationContext(), "Error, fail to connect to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else {

            String restID[] = restaurantID.split("/");

            db.collection("reviews").document(restID[0]).collection(restID[1]).document(userID)
                    .set(reviewInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("REVIEW", "We pushed the review to db");
                    if(task.isSuccessful()){
                        String test = "Review Added";
                        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("REVIEW", "There was an error with the completition of the review");
//                    Toast.makeText(getApplicationContext(), "Error, fail to connect to database", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}


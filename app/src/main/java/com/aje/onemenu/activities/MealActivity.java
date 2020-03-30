package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Meal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MealActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference mealInformationReference;
    private Meal mealInformation;
    private Button reviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        db = FirebaseFirestore.getInstance();
        final String path = getIntent().getStringExtra("path");
        final String userID = getIntent().getStringExtra("userId");
        mealInformationReference = db.document(path);


        reviewButton = findViewById(R.id.review_button);

        mealInformationReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

//                    Log.d("Document Meal Description", task.getResult().toObject(Meal.class).getDescription());

                    if( task.getResult().exists()){
                        mealInformation = task.getResult().toObject(Meal.class);
                        Log.d("Document Meal Description", mealInformation.getDescription());
                    }
                }
                else {
                    Log.d("Connection Failed", "We were unable to get the document Meal from db");
                }
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MealActivity.this, FoodDescription.class);

                String restId[] = path.split("/");

                intent.putExtra("userId", userID);
                intent.putExtra("restaurantId", restId[1] + "/" + restId[restId.length - 1]);
                startActivity(intent);

            }
        });


    }
}

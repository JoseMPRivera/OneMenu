package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Meal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;


public class MealActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference mealInformationReference;
    private Meal mealInformation;
    private Button reviewButton;
    private TextView textViewName;
    private TextView textViewDescription;
    private TextView textViewMeat;
    private TextView textViewVegetable;
    private TextView textViewExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        db = FirebaseFirestore.getInstance();
        final String path = getIntent().getStringExtra("path");
        mealInformationReference = db.document(path);


        reviewButton = findViewById(R.id.review_button);

        mealInformationReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    mealInformation = documentSnapshot.toObject(Meal.class);
                    textViewName=findViewById(R.id.item_name);
                    textViewName.setText(mealInformation.getName());
                    textViewDescription=findViewById(R.id.item_description);
                    textViewDescription.setText("Description\n" + mealInformation.getDescription());
                    textViewMeat=findViewById(R.id.item_meat);
                    textViewMeat.setText("Meat\n"+Arrays.toString(mealInformation.getMeat().toArray()));
                    textViewVegetable=findViewById(R.id.item_vegetable);
                    textViewVegetable.setText("Vegetables\n"+Arrays.toString(mealInformation.getVegetable().toArray()));
                    textViewExtras=findViewById(R.id.item_extras);
                    textViewExtras.setText("Extras\n"+Arrays.toString(mealInformation.getMeat().toArray()));
                }else {
                    Log.d("Connection Failed", "We were unable to get the document Meal from db");
                }
            }
        });

//        mealInformationReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//
////                    Log.d("Document Meal Description", task.getResult().toObject(Meal.class).getDescription());
//
//                    if( task.getResult().exists()){
//                        mealInformation = task.getResult().toObject(Meal.class);
//                        textViewName=findViewById(R.id.item_name);
//                        textViewName.setText(mealInformation.getName());
//                        textViewDescription=findViewById(R.id.item_description);
//                        textViewDescription.setText("Description\n" + mealInformation.getDescription());
//                        textViewMeat=findViewById(R.id.item_meat);
//                        textViewMeat.setText("Meat\n"+Arrays.toString(mealInformation.getMeat().toArray()));
//                        textViewVegetable=findViewById(R.id.item_vegetable);
//                        textViewVegetable.setText("Vegetables\n"+Arrays.toString(mealInformation.getVegetable().toArray()));
//                        textViewExtras=findViewById(R.id.item_extras);
//                        textViewExtras.setText("Extras\n"+Arrays.toString(mealInformation.getMeat().toArray()));
//
//                    }
//                }
//                else {
//                    Log.d("Connection Failed", "We were unable to get the document Meal from db");
//                }
//            }
//        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MealActivity.this, FoodDescription.class);

                String restId[] = path.split("/");

//                Log.d("ID NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", restId[1]);
//                Log.d("MEAL NOOOOOOOOOOOOOOOOOOOOOOOOOOOO", restId[restId.length - 1]);

                intent.putExtra("restaurantId", restId[1] + "/" + restId[restId.length - 1]);
                startActivity(intent);

            }
        });


    }
}

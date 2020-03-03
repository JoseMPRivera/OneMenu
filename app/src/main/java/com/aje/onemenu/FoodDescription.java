package com.aje.onemenu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

public class FoodDescription extends AppCompatActivity {

    private ImageView foodPhoto;
    private String foodName;
    private String foodDescription;
    private String foodIngredients;
    private float foodRating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);

        RatingBar rate = findViewById(R.id.ratingBar);
        rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                foodRating = v;
            }
        });


        Button submit = findViewById(R.id.food_ingredients_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoodDescription.this, "" + foodRating, Toast.LENGTH_SHORT).show();
            }
        });

    }
}

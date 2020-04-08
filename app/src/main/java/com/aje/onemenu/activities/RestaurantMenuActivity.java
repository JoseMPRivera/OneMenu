package com.aje.onemenu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aje.onemenu.R;
import com.aje.onemenu.classes.FoodItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class RestaurantMenuActivity extends AppCompatActivity {

    private ArrayList<FoodItem> recommendedList = new ArrayList<>();
    private ArrayList<FoodItem> mealList = new ArrayList<>();
    private ArrayList<FoodItem> beverageList = new ArrayList<>();
    private ArrayList<FoodItem> appetizerList = new ArrayList<>();
    private ArrayList<FoodItem> dessertList = new ArrayList<>();
    private ArrayList<FoodItem> tryList = new ArrayList<>();
    private FirebaseFirestore db;
    private String restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_food_menu);

       // ListView menuLayout = findViewById(R.id.list_view);
        final LinearLayout recommendedLayout = findViewById(R.id.recommended_list);
        final LinearLayout mealLayout = findViewById(R.id.main_list);
        final LinearLayout beverageLayout = findViewById(R.id.beverage_list);
        final LinearLayout dessertLayout = findViewById(R.id.dessert_list);
        final LinearLayout appetizerLayout = findViewById(R.id.appetizer_list);
        final LinearLayout tryLayout = findViewById(R.id.try_list);

        //gather database
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);

        restaurant = getIntent().getStringExtra("restaurant");
        if(restaurant == null) restaurant = " ";

        populate(restaurant, "appetizers", recommendedLayout, recommendedList);
        populate(restaurant, "meal", mealLayout, mealList);
        populate(restaurant, "appetizers", appetizerLayout, appetizerList);
        populate(restaurant, "dessert", dessertLayout, dessertList);
        populate(restaurant, "drink", beverageLayout, beverageList);
        populate(restaurant, "try", tryLayout, tryList);

        HorizontalScrollView sv = findViewById(R.id.meal_scroller);
        sv.scrollTo(0, sv.getBottom());
    }

    //populate arraylists
    public void populate(final String restaurant, final String foodGroup, final LinearLayout layout, final ArrayList<FoodItem> foodList){

        db.collection("restaurants").document(restaurant).collection(restaurant+"_menu").whereEqualTo("type", foodGroup).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot d: list){
                                FoodItem p = d.toObject(FoodItem.class);
                                p.setPath( "restaurants" + "/" + restaurant + "/" + restaurant+"_menu" +"/"+ d.getId());
                                //menuList.add(p);
                                foodList.add(p);
                            }
                            update(foodList, layout, foodGroup);
                        }
                        else{
                            Log.d("RestaurantMenuActivity", "There no such menu in the database" );
                        }
                    }
                });
    }
    //display whatever is on the arraylist
    //change appetizers to recommended
    public void update(ArrayList<FoodItem> list ,LinearLayout layout, String foodGroup){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;
        int height = size.y;

        for(final FoodItem a: list){

            final LinearLayout mealitem = new LinearLayout(this);
            mealitem.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams mealitemParams;
            if(foodGroup.contains("appetizers")){
                mealitemParams = new LinearLayout.LayoutParams((int)(width*2/3), LinearLayout.LayoutParams.MATCH_PARENT);
            } else {
                mealitemParams = new LinearLayout.LayoutParams((int) (width * 2 / 5), LinearLayout.LayoutParams.MATCH_PARENT);
            }
            mealitemParams.setMargins(10,0,10,0);
            mealitem.setLayoutParams(mealitemParams);
            mealitem.setBackgroundColor(Color.WHITE);

            //name
            if(!(foodGroup.contains("appetizers"))){
                TextView name = new TextView(this);
                name.setText(a.getName());
                name.setTextSize(13);
                mealitem.addView(name);
            }

            //second layout
            LinearLayout nameAndDescription = new LinearLayout(this);
            if(foodGroup.contains("meal") || foodGroup.contains("appetizers")){
                nameAndDescription.setOrientation(LinearLayout.VERTICAL);
            } else {
                nameAndDescription.setOrientation(LinearLayout.HORIZONTAL);
            }
            mealitem.addView(nameAndDescription);
//            nameAndDescription.setBackgroundColor(Color.GRAY);

            //picture
            ImageView image = new ImageView(this);
            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_restaurant_black_24dp);
            image.setImageDrawable(myDrawable);
            if(foodGroup.contains("meal")){
                image.setLayoutParams(new LinearLayout.LayoutParams((int)(width*2/5),(int)(width*2/8) ));
            }
            else if(foodGroup.contains("appetizers")) {
                image.setLayoutParams(new LinearLayout.LayoutParams((int) (width *2/3 ), (int) (width/4)));

            } else {
                image.setLayoutParams(new LinearLayout.LayoutParams((int) (width / 5), (int) (width / 5)));
            }
            nameAndDescription.addView(image);

            //description
            TextView description  = new TextView(this);
            if(foodGroup.contains("appetizers")){
                description.setText(a.getName());
                description.setText(a.getName());
                description.setTextSize(13);
            }else {
                description.setText(a.getDescription());
                description.setTextSize(7);
            }
            nameAndDescription.addView(description);
            mealitem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(RestaurantMenuActivity.this, a.getRestaurantMealPath(), Toast.LENGTH_SHORT).show();
                    Log.d("RESTAURANT MENU ACTIVITY", a.getRestaurantMealPath());

                    if(!a.getRestaurantMealPath().isEmpty()){

                        Log.d("TEST", a.getRestaurantMealPath());

                        Intent intent = new Intent(RestaurantMenuActivity.this, MealActivity.class);
                        intent.putExtra("path", a.getRestaurantMealPath());
                        startActivity(intent);

                    }

                    else {

                        Log.d("Document Snapshop", "Error while loading restaurant from database");
                        finish();
                    }


                }
            });
            layout.addView(mealitem);
        }
    }
}

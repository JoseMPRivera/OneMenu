package com.aje.onemenu.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Restaurant;
import com.aje.onemenu.classes.User;
import com.aje.onemenu.classes.UserId;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantsList extends AppCompatActivity {

    private ListView listView;
    private FirebaseFirestore db;
    private ArrayList<String> restaurantNames = new ArrayList<>();
    private ArrayList<String> restaurantDescriptions = new ArrayList<>();
    private String userID;
    private User user;
    private ArrayList<User> listOfUsers = new ArrayList<>();
    private ArrayList<String> userRecommendation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_restaurants_list);
        userID = UserId.getInstance().getUserId();
//        Toast.makeText(this, ""+userID, Toast.LENGTH_SHORT).show();
//        db.collection("users").whereEqualTo("id", userID).get()

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);

        db.collection("users").whereEqualTo("id", userID).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    int counter = 0;
                    for (DocumentSnapshot d : list) {

//                        Toast.makeText(RestaurantsList.this, "add user", Toast.LENGTH_SHORT).show();
                        User a = d.toObject(User.class);
                        userRecommendation = a.getProtein();
                        listOfUsers.add(a);
//                        Toast.makeText(RestaurantsList.this, ""+ listOfUsers.size(), Toast.LENGTH_SHORT).show();

//                        Toast.makeText(RestaurantsList.this, ""+a.getId()+" " +a.getName()+counter++, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
//        if(listOfUsers.isEmpty()) Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();



//        Toast.makeText(RestaurantsList.this, ""+user.getId()+" " +user.getName(), Toast.LENGTH_SHORT).show();

//        Toast.makeText(this, ""+user.toString(), Toast.LENGTH_SHORT).show();

        final RelativeLayout rl = findViewById(R.id.list_Rlayout);
        final TextView nullHandler = findViewById(R.id.no_result);
        nullHandler.setVisibility(View.GONE);



        Log.d("SUCCESS",userID+ "     Database    var     created===============================");

        //need to add .whereequalto("cuisine", cuisine)
        db.collection("restaurants").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){

                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: list){

                        Restaurant p = d.toObject(Restaurant.class);
                        restaurantNames.add(p.getName());
                        restaurantDescriptions.add(p.getDescription());
                    }

                    updateList();
                }

            }
        });

        SearchView simpleSearchView = findViewById(R.id.simpleSearchView);
        simpleSearchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {

                        nullHandler.setVisibility(View.GONE);
                        restaurantNames = new ArrayList<>();
                        restaurantDescriptions = new ArrayList<>();

                        db.collection("restaurants").whereGreaterThanOrEqualTo("name", s.toLowerCase())
                                .whereLessThanOrEqualTo("name", s.toLowerCase()+"\uF7FF").get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        if(!queryDocumentSnapshots.isEmpty()){
                                            nullHandler.setVisibility(View.GONE);
                                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                                            for(DocumentSnapshot d: list){

                                                Restaurant p = d.toObject(Restaurant.class);
                                                restaurantNames.add(p.getName());
                                                restaurantDescriptions.add(p.getDescription());
                                            }

                                            updateList();
                                        }
                                        else{
                                            restaurantNames = new ArrayList<>();
                                            restaurantDescriptions = new ArrayList<>();
                                            updateList();
                                            nullHandler.setVisibility(View.VISIBLE);
                                            //nullHandler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                                        }
                                    }
                                });
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                }
        );

    }

    private void updateList(){
        final ArrayList<Integer> listPhoto = new ArrayList<>();

        for (int i = 0; i < restaurantNames.size(); ++i) {
            listPhoto.add(R.drawable.ic_restaurant_black_24dp);
        }

        List<HashMap<String, String>> aList = new ArrayList<>();
        for (int i = 0; i < restaurantNames.size(); ++i) {

            HashMap<String, String> hm = new HashMap<>();
            hm.put("Name", restaurantNames.get(i));
            hm.put("Description", restaurantDescriptions.get(i));
            hm.put("Image", Integer.toString(listPhoto.get(i)));
            aList.add(hm);
        }

        String[] from = {"Name","Description","Image"};
        int[] to = {R.id.titleRestaurant, R.id.descRestaurant,R.id.iconRestaurant};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.fragment_restaurant_info, from,to);
        listView = findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                HashMap<String, String> item = (HashMap<String, String>) listView .getItemAtPosition(i);
                String restaurant = (String)item.values().toArray()[2];


//                Toast.makeText(RestaurantsList.this, sdi+"", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RestaurantsList.this,RestaurantMenuActivity.class);
                intent.putExtra("restaurant", restaurant);
                intent.putExtra("id", userID);
                intent.putStringArrayListExtra("userRecommendation", userRecommendation);
                startActivity(intent);
            }
        });

        listView.setAdapter(simpleAdapter);
    }

}
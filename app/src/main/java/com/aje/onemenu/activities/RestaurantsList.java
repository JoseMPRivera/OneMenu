package com.aje.onemenu.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantsList extends AppCompatActivity  {

    private ListView listView;
    private FirebaseFirestore db;
    private ArrayList<String> restaurantNames = new ArrayList<String>();
    private ArrayList<String> restaurantDescriptions = new ArrayList<String>();
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_restaurants_list);


        SearchView simpleSearchView = findViewById(R.id.simpleSearchView);
        simpleSearchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        final RelativeLayout rl = findViewById(R.id.list_Rlayout);
//                        listView.removeAllViews();
                        restaurantNames = new ArrayList<String>();
                        restaurantDescriptions = new ArrayList<String>();
                        query = s;
                        db.collection("restaurants").whereGreaterThanOrEqualTo("name", query).get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        rl.removeAllViews();
                                        if(!queryDocumentSnapshots.isEmpty()){
                                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                                            for(DocumentSnapshot d: list){

                                                Restaurant p = d.toObject(Restaurant.class);
                                                restaurantNames.add(p.getName());
                                                restaurantDescriptions.add(p.getDescription());
                                            }

                                            updateList(query);
                                        }
                                        else{
                                            restaurantNames = new ArrayList<String>();
                                            restaurantDescriptions = new ArrayList<String>();
                                            updateList(query);
                                            TextView nullHandler = new TextView(RestaurantsList.this);
                                            nullHandler.setText("No Result");
                                            rl.addView(nullHandler);
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
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);

        Log.d("fail","     Database    var     created===============================");


    }

    private void updateList(String q){
        final ArrayList<Integer> listPhoto = new ArrayList<Integer>();
        listView = (ListView)findViewById(R.id.list_view);
//        listView.setAdapter(null);

        for (int i = 0; i < restaurantNames.size(); ++i) {
            listPhoto.add(R.drawable.ic_restaurant_black_24dp);
        }

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

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

        listView.setAdapter(simpleAdapter);
    }
}

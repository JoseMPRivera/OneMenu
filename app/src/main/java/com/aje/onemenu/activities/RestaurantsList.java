package com.aje.onemenu.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Restaurant;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantsList extends Activity {

    private ListView listView;
    private FirebaseFirestore db;
    private final ArrayList<String> restaurantNames = new ArrayList<String>();
    private final ArrayList<String> restaurantDescriptions = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);

        Log.d("fail","     Database    var     created===============================");

        db.collection("restaurants").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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
                        else{
                            Log.d("fail", "fail");
                        }
                    }
                });
    }

    private void updateList(){
        final ArrayList<Integer> listPhoto = new ArrayList<Integer>();

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
        listView = (ListView)findViewById(R.id.list_view);

        listView.setAdapter(simpleAdapter);
    }
}

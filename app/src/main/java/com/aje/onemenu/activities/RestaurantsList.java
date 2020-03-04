package com.aje.onemenu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.aje.onemenu.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
/*
    Search for restaurants
    Display List of restaurants
 */
public class RestaurantsList extends AppCompatActivity {

    //change to arraylist<restaurant>
    private ArrayList<String> rList = new ArrayList<>();
    public RestaurantsList(){

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        addRestaurant("In 'n Out");
        addRestaurant("Burger King");
        addRestaurant("McDonald's");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);

        LinearLayout ll = findViewById(R.id.main);

        SearchView sv = new SearchView(this);
        ll.addView(sv);
        ListView lv = new ListView(this);
        ll.addView(lv);
        LinearLayout ll_list = displayList(rList);
        ll.addView(ll_list);


    }
    private void addRestaurant(String r){
        //rList.add(Restaurant(r));
        rList.add(r);

    }
    private LinearLayout displayList(ArrayList<String> list){
        LinearLayout ll1 = new LinearLayout(this);
        ll1.setOrientation(LinearLayout.VERTICAL);
        for(String x:list){
            LinearLayout ll2 = new LinearLayout(this);
            TextView tv = new TextView(this);
            tv.setText(x);
            ll2.addView(tv);
            ll1.addView(ll2);
        }
        return ll1;
    }
}

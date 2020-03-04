package com.aje.onemenu.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aje.onemenu.R;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class FoodPreference extends AppCompatActivity {

    private ArrayList<String> meats = new ArrayList<>();
    private ArrayList<String> veggie = new ArrayList<>();
    private ArrayList<String> misc = new ArrayList<>();
    private ArrayList<String> pmeats = new ArrayList<>();
    private ArrayList<String> pveggie = new ArrayList<>();
    private ArrayList<String> pmisc = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMeat();
        addVeggie();
        addMisc();
        final LinearLayout showMeat = showPreference(meats, pmeats);
        final LinearLayout showVeggie = showPreference(veggie, pveggie);
        final LinearLayout showMisc = showPreference(misc, pmisc);

        final LinearLayout preferenceScreen = new LinearLayout(this);
        preferenceScreen.setOrientation(LinearLayout.VERTICAL);
            final TabLayout tl = new TabLayout(this);
            preferenceScreen.addView(tl);
            final RelativeLayout rl = new RelativeLayout(this);
            preferenceScreen.addView(rl);
                rl.addView(showMeat);
                rl.addView(showVeggie);
                rl.addView(showMisc);

        showVeggie.setVisibility(View.GONE);
        showMisc.setVisibility(View.GONE);

            final LinearLayout ll2 = new LinearLayout(this);
            ll2.setOrientation(LinearLayout.VERTICAL);
            preferenceScreen.addView(ll2);
                LinearLayout ll3 = showPreferred(pmeats, "meat");
                ll2.setOrientation(LinearLayout.VERTICAL);
                ll2.addView(ll3);

            tl.addTab(tl.newTab().setText("Protein"));
            tl.addTab(tl.newTab().setText("Vegetable"));
            tl.addTab(tl.newTab().setText("Extra"));
            tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tl.getSelectedTabPosition()){
                        case 0:
                            showVeggie.setVisibility(View.GONE);
                            showMeat.setVisibility(View.VISIBLE);
                            showMisc.setVisibility(View.GONE);
                            break;
                        case 1:
                            showMeat.setVisibility(View.GONE);
                            showVeggie.setVisibility(View.VISIBLE);
                            showMisc.setVisibility(View.GONE);
                            break;
                        case 2:
                            showMisc.setVisibility(View.VISIBLE);
                            showVeggie.setVisibility(View.GONE);
                            showMeat.setVisibility(View.GONE);
                    }
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

        Button next = new Button(this);
        next.setText(R.string.submit);
        preferenceScreen.addView(next);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                preferenceScreen.removeView(ll2);
                ll2.removeAllViews();
                LinearLayout list = ListOfPreferred();
                ll2.addView(list);
                preferenceScreen.addView(ll2);


            }
        });

        Button fd = new Button(this);
        fd.setText("FoodDescription");
        preferenceScreen.addView(fd);
        fd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodPreference.this, FoodDescription.class);
                startActivity(intent);
            }
        });
        Button rList = new Button(this);
        rList.setText("List of Restaurants");
        preferenceScreen.addView(rList);
        rList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodPreference.this, RestaurantsList.class);
                startActivity(intent);
            }
        });





        this.setContentView(preferenceScreen);
    }
    private void addMeat(){
        meats.add("pork");
        meats.add("chicken");
        meats.add("beef");
        meats.add("fish");
        meats.add("crab");
        meats.add("tofu");
        meats.add("shrimp");
        meats.add("duck");
        meats.add("lamb");
        meats.add("goat");
        meats.add("lobster");
        meats.add("bison");
        meats.add("frog");
        meats.add("turkey");
        meats.add("eggs");
        meats.add("deer");

    }
    private void addVeggie(){
        veggie.add("lettuce");
        veggie.add("onion");
        veggie.add("potato");
        veggie.add("tomato");

    }
    private void addMisc(){
        misc.add("ketchup");
        misc.add("mustard");
        misc.add("bun");
        misc.add("mayonnaise");

    }

    private LinearLayout showPreference(ArrayList<String> ingredients, final ArrayList<String> pIngredients){

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < ingredients.size(); i++) {
            final CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(ingredients.get(i));

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        pIngredients.add(cb.getText().toString());
                    }
                    else{
                        pIngredients.remove(cb.getText().toString());
                        Context context = getApplicationContext();
                        CharSequence text = "unselected " + cb.getText();
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            });
            ll.addView(cb);
        }

        return ll;
    }
    private LinearLayout showPreferred(ArrayList<String> list, String name){
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        //LinearLayout row = new LinearLayout(this);

        for(int j = 0; j < list.size(); j++ ){
            if(j == 0) {
                TextView group  = new TextView(this);
                group.setText(new StringBuilder().append(name).append(R.string.colon).toString());
                group.setMaxLines(1);
                ll.addView(group);
                TextView tv = new TextView(this);
                tv.setText(list.get(j));
                tv.setMaxLines(1);
                ll.addView(tv);
            }else{
                TextView tv = new TextView(this);
                tv.setText(new StringBuilder().append(R.string.comma).append(list.get(j)).toString());
                ll.addView(tv);
            }

        }
        return ll;
    }
    private LinearLayout ListOfPreferred(){
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        LinearLayout ll6 = showPreferred(pmeats,"meat");
        LinearLayout ll7 = showPreferred(pveggie,"vegetables");
        LinearLayout ll8 = showPreferred(pmisc, "extras");
        ll6.setOrientation(LinearLayout.VERTICAL);
        ll7.setOrientation(LinearLayout.VERTICAL);
        ll8.setOrientation(LinearLayout.VERTICAL);
        list.addView(ll6);
        list.addView(ll7);
        list.addView(ll8);
        return list;
    }


}